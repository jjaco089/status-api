package com.jerry.status;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class StatusApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatusApplication.class, args);
	}

}
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "Statuses")
class Statuses {
	@Id
	int iden;
	@Column (name = "stat")
	String stat;
}
@Repository
interface StatDao extends CrudRepository<Statuses, Integer> {
	@Query(value = "FROM Statuses ORDER BY iden DESC LIMIT 2")
	Statuses[] findLatest();
}
@Service
class StatServices {
	private final StatDao statDao;
	@Autowired
	public StatServices(StatDao statDao) {
		this.statDao = statDao;
	}
	public Statuses[] findLatest() {
        return statDao.findLatest();
	}
}
@RestController
@CrossOrigin
class StatServlet {
	private final StatServices statServices;
	@Autowired
	public StatServlet(StatServices statServices) {
		this.statServices = statServices;
	}
	@GetMapping("/")
	public ResponseEntity<Statuses[]> findLatest() {
		return new ResponseEntity<>(statServices.findLatest(), HttpStatus.OK);
	}
}
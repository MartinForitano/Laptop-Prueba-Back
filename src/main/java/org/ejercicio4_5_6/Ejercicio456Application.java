package org.ejercicio4_5_6;

import java.time.LocalDate;

import org.ejercicio4_5_6.entities.Laptop;
import org.ejercicio4_5_6.repositories.LaptopRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

// https://springdoc.org/v2/#getting-started --> Documentacion para la version 3.0 de Spring Boot
// https://www.youtube.com/watch?v=-SzKqwgPTyk --> Video introductorio
// Para chequear la documentacion -> localhost:8080/documentacion
// https://www.youtube.com/watch?v=PLJVQo3SNLw
// https://www.baeldung.com/spring-rest-openapi-documentation --> Tutorial
// https://www.youtube.com/watch?v=1Vmi_5ZsyqE
// https://www.baeldung.com/spring-swagger-hiding-endpoints

@SpringBootApplication
public class Ejercicio456Application {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Ejercicio456Application.class, args);
		LaptopRepository lRepository = (LaptopRepository) context.getBean(LaptopRepository.class);

		// insertar un par de laptops
		Laptop l = new Laptop(null, "Toshiba", "L845", LocalDate.of(2009, 10, 15));
		lRepository.save(l);
		// insertar otra laptop
		l = new Laptop(null, "Lenovo", "K890", LocalDate.of(2015, 4, 25));
		lRepository.save(l);
	}

}

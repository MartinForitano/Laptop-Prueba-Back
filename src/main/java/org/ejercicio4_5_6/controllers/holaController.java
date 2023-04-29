package org.ejercicio4_5_6.controllers;

import java.util.List;
import java.util.Optional;

import org.ejercicio4_5_6.entities.Laptop;
import org.ejercicio4_5_6.repositories.LaptopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
@RestController
public class holaController {

	private LaptopRepository lRepository;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${app.mensaje}")
	private String mensaje;
	
	public holaController(LaptopRepository lRepository) {
		super();
		this.lRepository = lRepository;
	}

	@Hidden
	@GetMapping("/mensaje")
	public ResponseEntity<String> mostrarMensaje() {
		return ResponseEntity.ok(mensaje);
	}

	@Operation(summary = "Listado de laptops", description = "Obtener una lista general de laptops", tags = "GET")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Laptop encontrado") })
	@GetMapping("/laptops")
	public List<Laptop> listarLaptops() {
		return lRepository.findAll();
	}

	@Operation(summary = "Alta de laptop", description = "Alta de un modelo de laptop", tags = "POST")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Laptop cargada"),
			@ApiResponse(responseCode = "400", description = "No debe ingresar un ID") })
	@PostMapping("/laptops")
	public ResponseEntity<Laptop> createLaptop(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la laptop a registrar") @RequestBody Laptop laptop) {
		if (laptop.getId() == null) {
			return ResponseEntity.ok(lRepository.save(laptop));
		} else {
			log.warn("Trying to create a laptop with Id");
			return ResponseEntity.badRequest().build();
		}
	}

	@Operation(summary = "Busqueda por ID", description = "Obtener una laptop por su ID", tags = "GET")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Laptop encontrado"),
			@ApiResponse(responseCode = "404", description = "Laptop no existente"),
			@ApiResponse(responseCode = "400", description = "ID invalido") })

	@GetMapping("/laptops/{id}")

	public ResponseEntity<Laptop> findLaptop(
			@Parameter(description = "ID de la laptop a buscar") @PathVariable Long id) {
		Optional<Laptop> L = lRepository.findById(id);
		if (L.isPresent()) {
			return ResponseEntity.ok(L.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Actualizar datos", description = "Actualizar datos de una laptop usando su ID", tags = "PUT")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Datos actualizados"),
			@ApiResponse(responseCode = "404", description = "Laptop no existente"),
			@ApiResponse(responseCode = "400", description = "ID invalido") })
	@PutMapping("/laptops")
	public ResponseEntity<Laptop> updateLaptop(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la laptop a actualizar") @RequestBody Laptop laptop) {
		if (laptop.getId() != null) {
			Optional<Laptop> L = lRepository.findById(laptop.getId());
			if (L.isPresent()) {
				L.get().setFechaFabricacion(laptop.getFechaFabricacion());
				L.get().setMarca(laptop.getMarca());
				L.get().setModelo(laptop.getModelo());
				lRepository.save(L.get());
				return ResponseEntity.ok(L.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			log.warn("Trying update without Id");
			return ResponseEntity.badRequest().build();
		}
	}

	@Operation(summary = "Baja de laptop", description = "Borrar datos de una laptop usando su ID", tags = "DELETE")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Laptop borrada"),
			@ApiResponse(responseCode = "404", description = "Laptop no existente"),
			@ApiResponse(responseCode = "400", description = "ID invalido") })
	@DeleteMapping("/laptops/{id}")
	public ResponseEntity<Laptop> deleteLaptop(
			@Parameter(description = "ID de la laptop a borrar") @PathVariable Long id) {
		if (id != null) {
			Optional<Laptop> L = lRepository.findById(id);
			if (L.isPresent()) {
				lRepository.delete(L.get());
				return ResponseEntity.ok(L.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@Operation(summary = "Baja total", description = "Borrar toda la base de datos", tags = "DELETE")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Base de datos borrada"),
			@ApiResponse(responseCode = "404", description = "No existen laptops"), })
	@DeleteMapping("/laptops")
	public ResponseEntity<Laptop> deleteAllLaptops() {
		List<Laptop> laptopsList = lRepository.findAll();
		if (!laptopsList.isEmpty()) {
			lRepository.deleteAll();
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Bean
	public OpenAPI openApiConfiguration() {
		return new OpenAPI().info(new Info().title("Laptop Api").version("1.0")
				.description("Laptop API documentation using Open APi y Spring Doc")
				.termsOfService("http:/swagger.io/terms/")
				.license(new License().name("MIT / Apache 2.0").url("http://springdoc.org"))
				.contact(new Contact().name("Martin Tomas Foritano").email("martin.foritano.11@gmail.com")
						.url("https://www.linkedin.com/in/martin-tomas-foritano-609303153/")));
	}

}

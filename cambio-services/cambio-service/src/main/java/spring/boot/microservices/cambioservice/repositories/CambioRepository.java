package spring.boot.microservices.cambioservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.boot.microservices.cambioservice.entities.Cambio;

public interface CambioRepository extends JpaRepository<Cambio, Long>{
	
	Cambio findByFromAndTo(String from, String to);
}

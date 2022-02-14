package spring.boot.microservices.cambioservice.resources;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import spring.boot.microservices.cambioservice.entities.Cambio;
import spring.boot.microservices.cambioservice.repositories.CambioRepository;

@Tag(name = "Cambio service API")
@RequestMapping("cambio-service")
@RestController
public class CambioResource {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private CambioRepository repository;
	
	@Operation(summary = "Get cambio from currency")
	@GetMapping(value = "/{amount}/{from}/{to}")
	public Cambio getCambio(@PathVariable("amount") BigDecimal amount, 
			@PathVariable("from") String from,
			@PathVariable("to") String to) {
		
		var cambio = repository.findByFromAndTo(from, to);
		if(cambio == null) throw new RuntimeException("Currency Unsupported");
		
		var port = env.getProperty("local.server.port");
		
		BigDecimal conversionFactor = cambio.getConversionFactor();
		BigDecimal convertedValue = conversionFactor.multiply(amount);
		cambio.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING));
		cambio.setEnviroment(port);
		
		return cambio;
	}
}

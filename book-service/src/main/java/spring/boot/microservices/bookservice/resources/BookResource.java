package spring.boot.microservices.bookservice.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import spring.boot.microservices.bookservice.entities.Book;
import spring.boot.microservices.bookservice.proxy.CambioProxy;
import spring.boot.microservices.bookservice.repositories.BookRepository;

@Tag(name = "Book endpoint")
@RestController
@RequestMapping("/book-service")
public class BookResource {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private BookRepository repository;
	
	@Autowired
	private CambioProxy proxy;
	
	@Operation(summary = "FInd a specific book by your ID")
	@GetMapping(value = "/{id}/{currency}")
	//@Retry(name="default", fallbackMethod = "responseRequest")
	//@CircuitBreaker(name="default", fallbackMethod = "responseRequest")
	public Book findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
		var book = repository.getById(id);
		if(book == null) throw new RuntimeException("Book not found");
		
		var cambio = proxy.getCambio(book.getPrice(), "USD", currency);
		
		var port = env.getProperty("local.server.port");
		
		book.setEnvironment("Book port: " + port + " FEIGN" + "Cambio port: " + cambio.getEnviroment());
		book.setPrice(cambio.getConvertedValue());
		return book;
	}
}

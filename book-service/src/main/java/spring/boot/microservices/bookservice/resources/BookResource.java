package spring.boot.microservices.bookservice.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import spring.boot.microservices.bookservice.proxy.CambioProxy;
import spring.boot.microservices.bookservice.repositories.BookRepository;

@RestController
@RequestMapping("/book-service")
public class BookResource {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private BookRepository repository;
	
	@Autowired
	private CambioProxy proxy;
	
	private static Logger logger = LoggerFactory.getLogger(BookResource.class);
	
	/*@GetMapping(value = "/{id}/{currency}")
	@Retry(name="default", fallbackMethod = "responseRequest")
	@CircuitBreaker(name="default", fallbackMethod = "responseRequest")
	public Book findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
		var book = repository.getById(id);
		if(book == null) throw new RuntimeException("Book not found");
		
		var cambio = proxy.getCambio(book.getPrice(), "USD", currency);
		
		var port = env.getProperty("local.server.port");
		
		book.setEnvironment("Book port: " + port + " FEIGN" + "Cambio port: " + cambio.getEnviroment());
		book.setPrice(cambio.getConvertedValue());
		return book;
	}*/
	
	
	
	//@GetMapping(value = "/{id}/{currency}")
	//@Retry(name="default", fallbackMethod = "responseRequest")
	//@CircuitBreaker(name="default", fallbackMethod = "responseRequest")
	/*public Book findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
		var book = repository.getById(id);
		if(book == null) throw new RuntimeException("Book not found");
		
		HashMap<String, String> params = new HashMap<>();
		params.put("amount", book.getPrice().toString());
		params.put("from", "USD");
		params.put("to", currency);
		
		var response = new RestTemplate()
				.getForEntity("http://localhost:8003/cambio-servce/"
		+ "{amount}/{from}/{to}", Cambio.class, params);
		var cambio = response.getBody();
		
		var port = env.getProperty("local.server.port");
		
		book.setEnvironment(port);
		book.setPrice(cambio.getConvertedValue());
		return book;
	}*/
	
	@GetMapping("/foo-bar")
	//@CircuitBreaker(name="default", fallbackMethod = "responseRequest")
	@RateLimiter(name = "default")
	public String fooBar() {
		logger.info("Request method");
		//var response = new RestTemplate().getForEntity("http://localhost:8080/foorbar", String.class);
		//return response.getBody();
		return "Foo-Bar!!";
	}
	
	public String responseRequest(Exception ex) {
		return "URL not found";
	}
}

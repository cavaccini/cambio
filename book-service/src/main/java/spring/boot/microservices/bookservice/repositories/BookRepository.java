package spring.boot.microservices.bookservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.boot.microservices.bookservice.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

}

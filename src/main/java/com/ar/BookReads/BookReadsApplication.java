package com.ar.BookReads;

import com.ar.BookReads.principal.Principal;
import com.ar.BookReads.repository.AutorRepository;
import com.ar.BookReads.repository.LibroRepository;
import com.ar.BookReads.service.ApiService;
import com.ar.BookReads.service.AutorService;
import com.ar.BookReads.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookReadsApplication implements CommandLineRunner {

	private final Principal principal;

	public BookReadsApplication(Principal principal) {
		this.principal = principal;
	}

	public static void main(String[] args) {
		SpringApplication.run(BookReadsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.showMenu();
	}
}

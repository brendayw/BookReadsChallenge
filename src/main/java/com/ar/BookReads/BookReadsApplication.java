package com.ar.BookReads;

import com.ar.BookReads.principal.Principal;
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
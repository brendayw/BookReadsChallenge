package com.ar.BookReads.dto;

import com.ar.BookReads.model.Libro;

import java.util.List;

public record AutorDTO(
    Long id,
    String nombre,
    Integer fechaNacimiento,
    Integer fechaFallecimiento,
    List<Libro>libros ) {
}
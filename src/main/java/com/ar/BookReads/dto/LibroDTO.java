package com.ar.BookReads.dto;

import com.ar.BookReads.model.Autor;

public record LibroDTO(
        Long id,
        String titulo,
        String idioma,
        Double numeroDescargas,
        Autor autor) {
}
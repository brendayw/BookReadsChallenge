package com.ar.BookReads.service;

import com.ar.BookReads.dto.LibroDTO;
import com.ar.BookReads.model.Libro;
import com.ar.BookReads.model.exceptions.LibroExistenteException;
import com.ar.BookReads.model.exceptions.LibroNoEncontradoException;
import com.ar.BookReads.repository.AutorRepository;
import com.ar.BookReads.repository.LibroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroService {

    @Autowired private LibroRepository repository;
    @Autowired private AutorRepository autorRepository;
    @Autowired private ApiService libroApiService;

    //obtener por titulo y guardarlo
    public Libro buscarYGuardarLibro(String titulo) throws LibroExistenteException, LibroNoEncontradoException {
        //busca el libro en la bdd
        Optional<Libro> libroLocal = repository.findByTituloContainingIgnoreCase(titulo);
        if (libroLocal.isPresent()) {
            throw new LibroExistenteException("El libro ya existe en la base de datos local");
        }
        //busca el libro en la api
        try {
            Optional<Libro> libroApi = libroApiService.buscarLibroEnApi(titulo);
            if (libroApi.isEmpty()) {
                throw new LibroNoEncontradoException("No se encontró el libro en la API externa");
            }
            Libro libro = libroApi.get();
            // Si el autor del libro no tiene ID (no está persistido)
            if (libro.getAutor() != null && libro.getAutor().getId() == null) {
                autorRepository.save(libro.getAutor());
            }
            return repository.save(libro);
        } catch (Exception e) {
            throw new LibroNoEncontradoException("Error al procesar el libro: " + e.getMessage());
        }
    }

    //obtener guardadas
    public List<LibroDTO> obtenerTodosLosLibros() {
        return convierteDatos(repository.findAll());
    }
    //obtener por autor

    //obtener autores vivos

    //obtener

    //metodo para convertir datos
    public List<LibroDTO> convierteDatos(List<Libro> libros) {
        return libros.stream()
                .map(l -> new LibroDTO(l.getId(), l.getTitulo(), l.getIdioma(), l.getNumeroDescargas(), l.getAutor()))
                .collect(Collectors.toList());
    }

}

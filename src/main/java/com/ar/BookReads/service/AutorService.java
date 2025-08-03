package com.ar.BookReads.service;

import com.ar.BookReads.dto.AutorDTO;
import com.ar.BookReads.model.Autor;
import com.ar.BookReads.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorService {
    @Autowired private AutorRepository repository;
    @Autowired private ApiService apiService;

    //buscar autores registrados
    public List<AutorDTO> obtenerTodosLosAutores() {
        return convierteDatos(repository.findAllWithLibros());
    }

    //buscar autores vivos
    public List<AutorDTO> obtenerAutoresVivos(int year) {
        List<Autor> autores = repository.findAutoresVivosConLibros(year);
        return convierteDatos(autores);
    }

    //metodos
    public List<AutorDTO> convierteDatos(List<Autor> autores) {
        return autores.stream()
                .map(a -> new AutorDTO(a.getId(), a.getNombre(), a.getFechaNacimiento(), a.getFechaFallecimiento(), a.getLibros()))
                        .collect(Collectors.toList());
    }
}
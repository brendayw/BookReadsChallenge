package com.ar.BookReads.service;

import com.ar.BookReads.model.*;
import com.ar.BookReads.service.api.ConsumoAPI;
import com.ar.BookReads.service.api.ConvierteDatos;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApiService {
    private static final String API_URL = "https://gutendex.com/books/";
    private final ConsumoAPI consumoApi;
    private final ConvierteDatos conversor;

    public ApiService(ConsumoAPI consumoApi, ConvierteDatos conversor) {
        this.consumoApi = consumoApi;
        this.conversor = conversor;
    }

    public Optional<Libro> buscarLibroEnApi(String titulo) {
        try {
            String url = API_URL +"?search="+titulo.replace(" ", "+");
            String json = consumoApi.obtenerDatos(url);
            Datos datos = conversor.obtenerDatos(json, Datos.class);

            return datos.resultado().stream()
                    .filter(l -> l.titulo().toLowerCase().contains(titulo.toLowerCase()))
                    .findFirst()
                    .map(this::convertirDatosLibroALibro);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar libro en API: " + e.getMessage(), e);
        }
    }

    private Libro convertirDatosLibroALibro(DatosLibro datosLibro) {
        Libro libro = new Libro();
        libro.setTitulo(datosLibro.titulo());

        // Manejo de autores mejorado
        if (!datosLibro.autor().isEmpty()) {
            DatosAutor datosAutor = datosLibro.autor().get(0);
            Autor autor = new Autor(datosAutor);
            libro.setAutor(autor);
        } else {
            Autor autorDesconocido = new Autor();
            autorDesconocido.setNombre("Desconocido");
            libro.setAutor(autorDesconocido);
        }

        libro.setIdioma(datosLibro.idioma().stream().findFirst().orElse(""));
        libro.setNumeroDescargas(datosLibro.numeroDescargas());
        return libro;
    }
}

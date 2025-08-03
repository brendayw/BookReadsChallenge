package com.ar.BookReads.principal;

import com.ar.BookReads.dto.AutorDTO;
import com.ar.BookReads.dto.LibroDTO;
import com.ar.BookReads.model.DatosLibro;
import com.ar.BookReads.model.Libro;
import com.ar.BookReads.service.ApiService;
import com.ar.BookReads.service.AutorService;
import com.ar.BookReads.service.LibroService;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {
    private Scanner input = new Scanner(System.in);
    private LibroService libroService;
    private AutorService autorService;
    private ApiService apiService;


    public Principal(LibroService libroService, AutorService autorService, ApiService apiService) {
        this.libroService = libroService;
        this.autorService = autorService;
        this.apiService = apiService;
    }

    public void showMenu() {
        var opcion = -1;
        while (opcion !=0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = input.nextInt();
            input.nextLine();

            switch (opcion) {
                case 1:
                    busarLibroYGuardarEnBdd();
                    break;
                case 2:
                    obtenerLibrosRegistrados();
                    break;
                case 3:
                    obtenerAutoresRegistrados();
                    break;
                case 4:
                    obtenerAutoresVivos();
                    break;
//                case 5:
//                    obtenerLibrosPorIdioma();
//                    break;
                case 0:
                    System.out.println("Cerrando la app...");
                default:
                    System.out.println("Opción no valida!");
            }

        }
    }

    //buscar libro por nombre
    private void busarLibroYGuardarEnBdd() {
        System.out.print("Ingrese el título del libro a buscar: ");
        var tituloLibro = input.nextLine();

        try {
            Libro libro = libroService.buscarYGuardarLibro(tituloLibro);
            LibroDTO libroDTO = new LibroDTO(
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getIdioma(),
                    libro.getNumeroDescargas(),
                    libro.getAutor()
            );
            System.out.println(formaterLibro(libroDTO));
        } catch (Exception e) {
            System.out.println("Ocurrio un error en al buscar o guardar el libro " + e.getMessage());
        }
    }

    private void obtenerLibrosRegistrados() {
        try {
            List<LibroDTO> libros = libroService.obtenerTodosLosLibros();
            if (libros.isEmpty()) {
                System.out.println("No hay libros registrados.");
                return;
            }
            libros.forEach(libro -> System.out.println(formaterLibro(libro)));
        } catch (Exception e) {
            System.out.println("Error al obtener libros: " + e.getMessage());
        }
    }

    private void obtenerAutoresRegistrados() {
        try {
            List<AutorDTO> autores = autorService.obtenerTodosLosAutores();
            if (autores.isEmpty()) {
                System.out.println("No hay autores registrados.");
                return;
            }
            autores.forEach(autor -> System.out.println(formaterAutor(autor)));
        } catch (Exception e) {
            System.out.println("Error al obtener autores: " + e.getMessage());
        }
    }

    private void obtenerAutoresVivos() {
        try {
            System.out.print("Ingrese el año para verificar autores vivos: ");
            int year = input.nextInt();
            input.nextLine();

            List<AutorDTO> autoresVivos = autorService.obtenerAutoresVivos(year);
            if (autoresVivos.isEmpty()) {
                System.out.println("No hay autores vivos registrados para el año " + year);
                return;
            }
            autoresVivos.forEach(autor -> System.out.println(formaterAutor(autor)));
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un número válido para el año");
            input.nextLine();
        } catch (Exception e) {
            System.out.println("Error al obtener autores vivos: " + e.getMessage());
        }
    }

    private void obtenerLibrosPorIdioma() {
    }

    //metodo que formatea libro
    private String formaterLibro(LibroDTO libro) {
        return String.format(
                """
                ------------------------------
                Título: %s
                Autor: %s
                Idioma: %s
                Descargas: %,.0f
                ------------------------------
                """,
                libro.titulo(),
                libro.autor() != null ? libro.autor().getNombre() : "Desconocido",
                libro.idioma(),
                libro.numeroDescargas()
        );
    }

    //metodo para formatear autor:
    public String formaterAutor(AutorDTO autor) {
        return String.format(
                """
                 ------------------------------
                 Nombre: %s
                 Fecha de nacimiento: %s
                 Fecha de fallecimiento %s
                 Libros escritos: %s
                 ------------------------------
                 """,
                autor.nombre(),
                autor.fechaNacimiento(),
                autor.fechaFallecimiento(),
                autor.libros().stream().map(l -> l.getTitulo()).collect(Collectors.toList())
        );
    }
}

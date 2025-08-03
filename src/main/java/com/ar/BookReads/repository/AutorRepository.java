package com.ar.BookReads.repository;

import com.ar.BookReads.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombreIgnoreCase(String nombre);

    @Query("SELECT DISTINCT a FROM Autor a LEFT JOIN FETCH a.libros")
    List<Autor> findAllWithLibros();

    @Query("SELECT DISTINCT a FROM Autor a LEFT JOIN FETCH a.libros " + "WHERE (:year >= a.fechaNacimiento AND " +
            "(a.fechaFallecimiento IS NULL OR :year <= a.fechaFallecimiento))")
    List<Autor> findAutoresVivosConLibros(@Param("year") int year);

}

package com.jhampier.forohub.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    @Query("SELECT t FROM Topico t JOIN FETCH t.autor JOIN FETCH t.curso WHERE t.id = :id")
    Optional<Topico> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT t FROM Topico t WHERE t.curso.id = :cursoId")
    List<Topico> findByCursoId(@Param("cursoId") Long cursoId);

    @Query("SELECT t FROM Topico t WHERE t.autor.id = :autorId")
    List<Topico> findByAutorId(@Param("autorId") Long autorId);

    @Query("SELECT t FROM Topico t WHERE t.status = :status")
    Page<Topico> findByStatus(@Param("status") Status status, Pageable pageable);

    boolean existsByTituloAndMensaje(String titulo, String mensaje);
}
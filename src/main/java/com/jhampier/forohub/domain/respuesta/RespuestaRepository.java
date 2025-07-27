package com.jhampier.forohub.domain.respuesta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {

    @Query("SELECT r FROM Respuesta r JOIN FETCH r.autor JOIN FETCH r.topico WHERE r.id = :id")
    Optional<Respuesta> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT r FROM Respuesta r WHERE r.topico.id = :topicoId ORDER BY r.fechaCreacion ASC")
    List<Respuesta> findByTopicoIdOrderByFechaCreacion(@Param("topicoId") Long topicoId);

    @Query("SELECT r FROM Respuesta r WHERE r.autor.id = :autorId")
    List<Respuesta> findByAutorId(@Param("autorId") Long autorId);

    @Query("SELECT r FROM Respuesta r WHERE r.solucion = true AND r.topico.id = :topicoId")
    List<Respuesta> findSolucionesByTopicoId(@Param("topicoId") Long topicoId);
}
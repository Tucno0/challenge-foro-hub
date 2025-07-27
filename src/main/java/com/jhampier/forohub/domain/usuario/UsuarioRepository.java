package com.jhampier.forohub.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u JOIN FETCH u.perfiles WHERE u.correoElectronico = :correoElectronico")
    Optional<Usuario> findByCorreoElectronico(@Param("correoElectronico") String correoElectronico);

    boolean existsByCorreoElectronico(String correoElectronico);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.perfiles WHERE u.id = :id")
    Optional<Usuario> findByIdWithPerfiles(@Param("id") Long id);
}
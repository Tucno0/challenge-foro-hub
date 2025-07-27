package com.jhampier.forohub.domain.usuario;

import java.util.Set;

public record DatosRespuestaUsuario(
        Long id,
        String nombre,
        String correoElectronico,
        Set<String> perfiles
) {
    public DatosRespuestaUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreoElectronico(),
                usuario.getPerfiles() != null ? usuario.getPerfiles().stream()
                        .map(perfil -> perfil.getNombre())
                        .collect(java.util.stream.Collectors.toSet()) : Set.of()
        );
    }
}
package com.jhampier.forohub.domain.perfil;

public record DatosRespuestaPerfil(
        Long id,
        String nombre
) {
    public DatosRespuestaPerfil(Perfil perfil) {
        this(perfil.getId(), perfil.getNombre());
    }
}
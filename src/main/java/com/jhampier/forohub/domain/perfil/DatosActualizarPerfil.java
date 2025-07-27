package com.jhampier.forohub.domain.perfil;

import jakarta.validation.constraints.Size;

public record DatosActualizarPerfil(
        @Size(max = 100, message = "El nombre del perfil no puede tener más de 100 caracteres")
        String nombre
) {
}
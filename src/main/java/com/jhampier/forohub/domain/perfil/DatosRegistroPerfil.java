package com.jhampier.forohub.domain.perfil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DatosRegistroPerfil(
        @NotBlank(message = "El nombre del perfil es obligatorio")
        @Size(max = 100, message = "El nombre del perfil no puede tener más de 100 caracteres")
        String nombre
) {
}
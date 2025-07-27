package com.jhampier.forohub.domain.curso;

import jakarta.validation.constraints.Size;

public record DatosActualizarCurso(
        @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
        String nombre,

        @Size(max = 100, message = "La categoría no puede tener más de 100 caracteres")
        String categoria) {
}
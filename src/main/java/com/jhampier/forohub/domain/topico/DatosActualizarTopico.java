package com.jhampier.forohub.domain.topico;

import jakarta.validation.constraints.Size;

public record DatosActualizarTopico(
        @Size(max = 200, message = "El título no puede tener más de 200 caracteres") String titulo,
        String mensaje,

        Status status,
        Long autorId,
        Long cursoId
) {
}
package com.jhampier.forohub.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DatosRegistroTopico(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 200, message = "El título no puede tener más de 200 caracteres")
        String titulo,

        @NotBlank(message = "El mensaje es obligatorio")
        String mensaje,

        @NotNull(message = "El status es obligatorio")
        Status status,

        @NotNull(message = "El autor es obligatorio")
        Long autorId,

        @NotNull(message = "El curso es obligatorio")
        Long cursoId
) {
}
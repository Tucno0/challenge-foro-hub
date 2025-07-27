package com.jhampier.forohub.domain.respuesta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroRespuesta(
        @NotBlank(message = "El mensaje es obligatorio") String mensaje,

        boolean solucion,

        @NotNull(message = "El autor es obligatorio") Long autorId,

        @NotNull(message = "El tópico es obligatorio") Long topicoId) {
}
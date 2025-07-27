package com.jhampier.forohub.domain.respuesta;

public record DatosActualizarRespuesta(
        String mensaje,
        Boolean solucion,
        Long autorId,
        Long topicoId
) {
}
package com.jhampier.forohub.domain.respuesta;

import java.time.LocalDateTime;

public record DatosRespuestaRespuesta(
        Long id,
        String mensaje,
        LocalDateTime fechaCreacion,
        boolean solucion,
        String autorNombre,
        String topicoTitulo
) {
    public DatosRespuestaRespuesta(Respuesta respuesta) {
        this(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.isSolucion(),
                respuesta.getAutor() != null ? respuesta.getAutor().getNombre() : null,
                respuesta.getTopico() != null ? respuesta.getTopico().getTitulo() : null
        );
    }
}
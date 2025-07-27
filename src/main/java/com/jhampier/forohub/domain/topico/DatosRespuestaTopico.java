package com.jhampier.forohub.domain.topico;

import java.time.LocalDateTime;

public record DatosRespuestaTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        Status status,
        String autorNombre,
        String cursoNombre
) {
    public DatosRespuestaTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor() != null ? topico.getAutor().getNombre() : null,
                topico.getCurso() != null ? topico.getCurso().getNombre() : null
        );
    }
}
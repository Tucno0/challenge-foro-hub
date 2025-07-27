package com.jhampier.forohub.domain.respuesta;

import com.jhampier.forohub.domain.topico.Topico;
import com.jhampier.forohub.domain.topico.TopicoRepository;
import com.jhampier.forohub.domain.usuario.Usuario;
import com.jhampier.forohub.domain.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RespuestaService {

    private final RespuestaRepository respuestaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TopicoRepository topicoRepository;

    public RespuestaService(RespuestaRepository respuestaRepository,
                            UsuarioRepository usuarioRepository,
                            TopicoRepository topicoRepository) {
        this.respuestaRepository = respuestaRepository;
        this.usuarioRepository = usuarioRepository;
        this.topicoRepository = topicoRepository;
    }

    @Transactional
    public DatosRespuestaRespuesta registrarRespuesta(DatosRegistroRespuesta datos) {
        // Obtener autor y tópico
        Usuario autor = usuarioRepository.findById(datos.autorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Topico topico = topicoRepository.findById(datos.topicoId())
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado"));

        Respuesta respuesta = new Respuesta(
                null,
                datos.mensaje(),
                null, // fechaCreacion se asigna automáticamente
                datos.solucion(),
                autor,
                topico);

        respuesta = respuestaRepository.save(respuesta);
        return new DatosRespuestaRespuesta(respuesta);
    }

    public Page<DatosRespuestaRespuesta> listarRespuestas(Pageable pageable) {
        return respuestaRepository.findAll(pageable)
                .map(DatosRespuestaRespuesta::new);
    }

    public DatosRespuestaRespuesta obtenerRespuesta(Long id) {
        Respuesta respuesta = respuestaRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Respuesta no encontrada"));
        return new DatosRespuestaRespuesta(respuesta);
    }

    public List<DatosRespuestaRespuesta> listarRespuestasPorTopico(Long topicoId) {
        return respuestaRepository.findByTopicoIdOrderByFechaCreacion(topicoId)
                .stream()
                .map(DatosRespuestaRespuesta::new)
                .toList();
    }

    public List<DatosRespuestaRespuesta> listarRespuestasPorAutor(Long autorId) {
        return respuestaRepository.findByAutorId(autorId)
                .stream()
                .map(DatosRespuestaRespuesta::new)
                .toList();
    }

    public List<DatosRespuestaRespuesta> listarSolucionesPorTopico(Long topicoId) {
        return respuestaRepository.findSolucionesByTopicoId(topicoId)
                .stream()
                .map(DatosRespuestaRespuesta::new)
                .toList();
    }

    @Transactional
    public DatosRespuestaRespuesta actualizarRespuesta(Long id, DatosActualizarRespuesta datos) {
        Respuesta respuesta = respuestaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Respuesta no encontrada"));

        // Obtener autor y tópico si se están actualizando
        Usuario autor = datos.autorId() != null
                ? usuarioRepository.findById(datos.autorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"))
                : respuesta.getAutor();

        Topico topico = datos.topicoId() != null
                ? topicoRepository.findById(datos.topicoId())
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado"))
                : respuesta.getTopico();

        Respuesta respuestaActualizada = new Respuesta(
                respuesta.getId(),
                datos.mensaje() != null ? datos.mensaje() : respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                datos.solucion() != null ? datos.solucion() : respuesta.isSolucion(),
                autor,
                topico);

        respuestaActualizada = respuestaRepository.save(respuestaActualizada);
        return new DatosRespuestaRespuesta(respuestaActualizada);
    }

    @Transactional
    public void eliminarRespuesta(Long id) {
        if (!respuestaRepository.existsById(id)) {
            throw new EntityNotFoundException("Respuesta no encontrada");
        }
        respuestaRepository.deleteById(id);
    }
}
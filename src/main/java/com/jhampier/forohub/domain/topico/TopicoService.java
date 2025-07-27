package com.jhampier.forohub.domain.topico;

import com.jhampier.forohub.domain.curso.Curso;
import com.jhampier.forohub.domain.curso.CursoRepository;
import com.jhampier.forohub.domain.usuario.Usuario;
import com.jhampier.forohub.domain.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public TopicoService(TopicoRepository topicoRepository,
                         UsuarioRepository usuarioRepository,
                         CursoRepository cursoRepository) {
        this.topicoRepository = topicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public DatosRespuestaTopico registrarTopico(DatosRegistroTopico datos) {
        // Verificar duplicados
        if (topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            throw new IllegalArgumentException("Ya existe un tópico con el mismo título y mensaje");
        }

        // Obtener autor y curso
        Usuario autor = usuarioRepository.findById(datos.autorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Curso curso = cursoRepository.findById(datos.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        Topico topico = new Topico(
                null,
                datos.titulo(),
                datos.mensaje(),
                null, // fechaCreacion se asigna automáticamente
                datos.status(),
                autor,
                curso);

        topico = topicoRepository.save(topico);
        return new DatosRespuestaTopico(topico);
    }

    public Page<DatosRespuestaTopico> listarTopicos(Pageable pageable) {
        return topicoRepository.findAll(pageable)
                .map(DatosRespuestaTopico::new);
    }

    public DatosRespuestaTopico obtenerTopico(Long id) {
        Topico topico = topicoRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado"));
        return new DatosRespuestaTopico(topico);
    }

    public List<DatosRespuestaTopico> listarTopicosPorCurso(Long cursoId) {
        return topicoRepository.findByCursoId(cursoId)
                .stream()
                .map(DatosRespuestaTopico::new)
                .toList();
    }

    public List<DatosRespuestaTopico> listarTopicosPorAutor(Long autorId) {
        return topicoRepository.findByAutorId(autorId)
                .stream()
                .map(DatosRespuestaTopico::new)
                .toList();
    }

    public Page<DatosRespuestaTopico> listarTopicosPorStatus(Status status, Pageable pageable) {
        return topicoRepository.findByStatus(status, pageable)
                .map(DatosRespuestaTopico::new);
    }

    @Transactional
    public DatosRespuestaTopico actualizarTopico(Long id, DatosActualizarTopico datos) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado"));

        // Obtener autor y curso si se están actualizando
        Usuario autor = datos.autorId() != null
                ? usuarioRepository.findById(datos.autorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"))
                : topico.getAutor();

        Curso curso = datos.cursoId() != null
                ? cursoRepository.findById(datos.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"))
                : topico.getCurso();

        Topico topicoActualizado = new Topico(
                topico.getId(),
                datos.titulo() != null ? datos.titulo() : topico.getTitulo(),
                datos.mensaje() != null ? datos.mensaje() : topico.getMensaje(),
                topico.getFechaCreacion(),
                datos.status() != null ? datos.status() : topico.getStatus(),
                autor,
                curso);

        topicoActualizado = topicoRepository.save(topicoActualizado);
        return new DatosRespuestaTopico(topicoActualizado);
    }

    @Transactional
    public void eliminarTopico(Long id) {
        if (!topicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Tópico no encontrado");
        }
        topicoRepository.deleteById(id);
    }
}
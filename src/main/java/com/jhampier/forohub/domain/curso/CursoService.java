package com.jhampier.forohub.domain.curso;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public DatosRespuestaCurso registrarCurso(DatosRegistroCurso datos) {
        Curso curso = new Curso(null, datos.nombre(), datos.categoria());
        curso = cursoRepository.save(curso);
        return new DatosRespuestaCurso(curso);
    }

    public Page<DatosRespuestaCurso> listarCursos(Pageable pageable) {
        return cursoRepository.findAll(pageable)
                .map(DatosRespuestaCurso::new);
    }

    public DatosRespuestaCurso obtenerCurso(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));
        return new DatosRespuestaCurso(curso);
    }

    @Transactional
    public DatosRespuestaCurso actualizarCurso(Long id, DatosActualizarCurso datos) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        Curso cursoActualizado = new Curso(
                curso.getId(),
                datos.nombre() != null ? datos.nombre() : curso.getNombre(),
                datos.categoria() != null ? datos.categoria() : curso.getCategoria());

        cursoActualizado = cursoRepository.save(cursoActualizado);
        return new DatosRespuestaCurso(cursoActualizado);
    }

    @Transactional
    public void eliminarCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new EntityNotFoundException("Curso no encontrado");
        }
        cursoRepository.deleteById(id);
    }
}
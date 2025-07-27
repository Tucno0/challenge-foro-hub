package com.jhampier.forohub.controllers;

import com.jhampier.forohub.domain.curso.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaCurso> registrarCurso(
            @RequestBody @Valid DatosRegistroCurso datos,
            UriComponentsBuilder uriBuilder) {

        DatosRespuestaCurso cursoCreado = cursoService.registrarCurso(datos);
        URI url = uriBuilder.path("/cursos/{id}").buildAndExpand(cursoCreado.id()).toUri();
        return ResponseEntity.created(url).body(cursoCreado);
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaCurso>> listarCursos(
            @PageableDefault(size = 10) Pageable pageable) {

        Page<DatosRespuestaCurso> cursos = cursoService.listarCursos(pageable);
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaCurso> obtenerCurso(@PathVariable Long id) {
        DatosRespuestaCurso curso = cursoService.obtenerCurso(id);
        return ResponseEntity.ok(curso);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaCurso> actualizarCurso(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarCurso datos) {

        DatosRespuestaCurso cursoActualizado = cursoService.actualizarCurso(id, datos);
        return ResponseEntity.ok(cursoActualizado);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        cursoService.eliminarCurso(id);
        return ResponseEntity.noContent().build();
    }
}

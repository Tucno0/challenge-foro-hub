package com.jhampier.forohub.controllers;

import com.jhampier.forohub.domain.perfil.*;
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
@RequestMapping("/perfiles")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaPerfil> registrarPerfil(
            @RequestBody @Valid DatosRegistroPerfil datos,
            UriComponentsBuilder uriBuilder) {

        DatosRespuestaPerfil perfilCreado = perfilService.registrarPerfil(datos);
        URI url = uriBuilder.path("/perfiles/{id}").buildAndExpand(perfilCreado.id()).toUri();
        return ResponseEntity.created(url).body(perfilCreado);
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaPerfil>> listarPerfiles(
            @PageableDefault(size = 10) Pageable pageable) {

        Page<DatosRespuestaPerfil> perfiles = perfilService.listarPerfiles(pageable);
        return ResponseEntity.ok(perfiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaPerfil> obtenerPerfil(@PathVariable Long id) {
        DatosRespuestaPerfil perfil = perfilService.obtenerPerfil(id);
        return ResponseEntity.ok(perfil);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaPerfil> actualizarPerfil(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarPerfil datos) {

        DatosRespuestaPerfil perfilActualizado = perfilService.actualizarPerfil(id, datos);
        return ResponseEntity.ok(perfilActualizado);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarPerfil(@PathVariable Long id) {
        perfilService.eliminarPerfil(id);
        return ResponseEntity.noContent().build();
    }
}
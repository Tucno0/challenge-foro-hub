package com.jhampier.forohub.controllers;

import com.jhampier.forohub.domain.respuesta.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/respuestas")
public class RespuestaController {

    private final RespuestaService respuestaService;

    public RespuestaController(RespuestaService respuestaService) {
        this.respuestaService = respuestaService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaRespuesta> registrarRespuesta(
            @RequestBody @Valid DatosRegistroRespuesta datos,
            UriComponentsBuilder uriBuilder) {

        DatosRespuestaRespuesta respuestaCreada = respuestaService.registrarRespuesta(datos);
        URI url = uriBuilder.path("/respuestas/{id}").buildAndExpand(respuestaCreada.id()).toUri();
        return ResponseEntity.created(url).body(respuestaCreada);
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaRespuesta>> listarRespuestas(
            @PageableDefault(size = 10) Pageable pageable) {

        Page<DatosRespuestaRespuesta> respuestas = respuestaService.listarRespuestas(pageable);
        return ResponseEntity.ok(respuestas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaRespuesta> obtenerRespuesta(@PathVariable Long id) {
        DatosRespuestaRespuesta respuesta = respuestaService.obtenerRespuesta(id);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/topico/{topicoId}")
    public ResponseEntity<List<DatosRespuestaRespuesta>> listarRespuestasPorTopico(@PathVariable Long topicoId) {
        List<DatosRespuestaRespuesta> respuestas = respuestaService.listarRespuestasPorTopico(topicoId);
        return ResponseEntity.ok(respuestas);
    }

    @GetMapping("/autor/{autorId}")
    public ResponseEntity<List<DatosRespuestaRespuesta>> listarRespuestasPorAutor(@PathVariable Long autorId) {
        List<DatosRespuestaRespuesta> respuestas = respuestaService.listarRespuestasPorAutor(autorId);
        return ResponseEntity.ok(respuestas);
    }

    @GetMapping("/topico/{topicoId}/soluciones")
    public ResponseEntity<List<DatosRespuestaRespuesta>> listarSolucionesPorTopico(@PathVariable Long topicoId) {
        List<DatosRespuestaRespuesta> soluciones = respuestaService.listarSolucionesPorTopico(topicoId);
        return ResponseEntity.ok(soluciones);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaRespuesta> actualizarRespuesta(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarRespuesta datos) {

        DatosRespuestaRespuesta respuestaActualizada = respuestaService.actualizarRespuesta(id, datos);
        return ResponseEntity.ok(respuestaActualizada);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarRespuesta(@PathVariable Long id) {
        respuestaService.eliminarRespuesta(id);
        return ResponseEntity.noContent().build();
    }
}
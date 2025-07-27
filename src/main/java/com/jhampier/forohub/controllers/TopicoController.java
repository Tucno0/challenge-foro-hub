package com.jhampier.forohub.controllers;

import com.jhampier.forohub.domain.topico.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/topicos")
@Tag(name = "Tópicos", description = "Gestión de tópicos del foro")
@SecurityRequirement(name = "bearer-jwt")
public class TopicoController {

    private final TopicoService topicoService;

    public TopicoController(TopicoService topicoService) {
        this.topicoService = topicoService;
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Crear nuevo tópico", description = "Crea un nuevo tópico en el foro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tópico creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DatosRespuestaTopico.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(
            @RequestBody @Valid DatosRegistroTopico datos,
            UriComponentsBuilder uriBuilder) {

        DatosRespuestaTopico topicoCreado = topicoService.registrarTopico(datos);
        URI url = uriBuilder.path("/topicos/{id}").buildAndExpand(topicoCreado.id()).toUri();
        return ResponseEntity.created(url).body(topicoCreado);
    }

    @GetMapping
    @Operation(summary = "Listar tópicos", description = "Obtiene una lista paginada de todos los tópicos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tópicos obtenida exitosamente", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    public ResponseEntity<Page<DatosRespuestaTopico>> listarTopicos(
            @Parameter(description = "Parámetros de paginación") @PageableDefault(size = 10, sort = {
                    "fechaCreacion" }) Pageable pageable) {

        Page<DatosRespuestaTopico> topicos = topicoService.listarTopicos(pageable);
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tópico por ID", description = "Obtiene los detalles de un tópico específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tópico encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DatosRespuestaTopico.class))),
            @ApiResponse(responseCode = "404", description = "Tópico no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    public ResponseEntity<DatosRespuestaTopico> obtenerTopico(
            @Parameter(description = "ID del tópico", required = true) @PathVariable Long id) {
        DatosRespuestaTopico topico = topicoService.obtenerTopico(id);
        return ResponseEntity.ok(topico);
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<DatosRespuestaTopico>> listarTopicosPorCurso(@PathVariable Long cursoId) {
        List<DatosRespuestaTopico> topicos = topicoService.listarTopicosPorCurso(cursoId);
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/autor/{autorId}")
    public ResponseEntity<List<DatosRespuestaTopico>> listarTopicosPorAutor(@PathVariable Long autorId) {
        List<DatosRespuestaTopico> topicos = topicoService.listarTopicosPorAutor(autorId);
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<DatosRespuestaTopico>> listarTopicosPorStatus(
            @PathVariable Status status,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<DatosRespuestaTopico> topicos = topicoService.listarTopicosPorStatus(status, pageable);
        return ResponseEntity.ok(topicos);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarTopico datos) {

        DatosRespuestaTopico topicoActualizado = topicoService.actualizarTopico(id, datos);
        return ResponseEntity.ok(topicoActualizado);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarTopico(@PathVariable Long id) {
        topicoService.eliminarTopico(id);
        return ResponseEntity.noContent().build();
    }
}

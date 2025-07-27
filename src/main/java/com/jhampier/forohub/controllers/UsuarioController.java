package com.jhampier.forohub.controllers;

import com.jhampier.forohub.domain.usuario.*;
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

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DatosRespuestaUsuario.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "El email ya está registrado", content = @Content)
    })
    public ResponseEntity<DatosRespuestaUsuario> registrarUsuario(
            @RequestBody @Valid DatosRegistroUsuario datos,
            UriComponentsBuilder uriBuilder) {

        DatosRespuestaUsuario usuarioCreado = usuarioService.registrarUsuario(datos);
        URI url = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuarioCreado.id()).toUri();
        return ResponseEntity.created(url).body(usuarioCreado);
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaUsuario>> listarUsuarios(
            @PageableDefault(size = 10) Pageable pageable) {

        Page<DatosRespuestaUsuario> usuarios = usuarioService.listarUsuarios(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaUsuario> obtenerUsuario(@PathVariable Long id) {
        DatosRespuestaUsuario usuario = usuarioService.obtenerUsuario(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaUsuario> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarUsuario datos) {

        DatosRespuestaUsuario usuarioActualizado = usuarioService.actualizarUsuario(id, datos);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
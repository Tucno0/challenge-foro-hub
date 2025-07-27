package com.jhampier.forohub.controllers;

import com.jhampier.forohub.domain.usuario.Usuario;
import com.jhampier.forohub.infra.security.DatosJWTToken;
import com.jhampier.forohub.infra.security.DatosLogin;
import com.jhampier.forohub.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Operaciones de autenticación y autorización")
public class AuthenticationController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenService tokenService;

  @PostMapping("/login")
  @Operation(summary = "Iniciar sesión", description = "Autentica al usuario con email y contraseña, devolviendo un token JWT")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DatosJWTToken.class))),
      @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
  })
  public ResponseEntity<?> autenticarUsuario(@RequestBody @Valid DatosLogin datosLogin) {
    var authToken = new UsernamePasswordAuthenticationToken(
        datosLogin.correoElectronico(),
        datosLogin.contrasena());

    var authentication = authenticationManager.authenticate(authToken);
    var usuario = (Usuario) authentication.getPrincipal();
    var jwtToken = tokenService.generateToken(usuario);

    return ResponseEntity.ok(new DatosJWTToken(jwtToken));
  }

  @GetMapping("/me")
  @Operation(summary = "Obtener usuario actual", description = "Devuelve la información del usuario autenticado")
  @SecurityRequirement(name = "bearer-jwt")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Información del usuario obtenida exitosamente", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "401", description = "Token no válido o expirado", content = @Content)
  })
  public ResponseEntity<?> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();

      if (principal instanceof Usuario usuario) {
        return ResponseEntity.ok(Map.of(
            "usuario", usuario.getNombre(),
            "email", usuario.getCorreoElectronico(),
            "authorities", authentication.getAuthorities(),
            "authenticated", authentication.isAuthenticated()));
      }
    }

    return ResponseEntity.ok(Map.of(
        "message", "No authenticated user",
        "authentication", authentication != null ? authentication.getClass().getSimpleName() : "null"));
  }
}
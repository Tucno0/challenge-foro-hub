package com.jhampier.forohub.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class TratadorDeErrores {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> tratarError404(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<DatosErrorValidacion>> tratarError400(MethodArgumentNotValidException ex) {
    var errores = ex.getFieldErrors().stream()
        .map(DatosErrorValidacion::new)
        .toList();
    return ResponseEntity.badRequest().body(errores);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> tratarErrorArgumento(IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> tratarErrorCredenciales() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<String> tratarUsuarioNoEncontrado() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> tratarError500(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Error interno del servidor: " + ex.getMessage());
  }

  private record DatosErrorValidacion(String campo, String error) {
    public DatosErrorValidacion(FieldError error) {
      this(error.getField(), error.getDefaultMessage());
    }
  }
}
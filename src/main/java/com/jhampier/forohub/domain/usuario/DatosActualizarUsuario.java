package com.jhampier.forohub.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record DatosActualizarUsuario(
        @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
        String nombre,

        @Email(message = "El correo electrónico debe tener un formato válido")
        @Size(max = 150, message = "El correo electrónico no puede tener más de 150 caracteres")
        String correoElectronico,

        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String contrasena,

        Set<Long> perfilesIds) {
}
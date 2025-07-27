package com.jhampier.forohub.infra.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DatosLogin(
        @NotBlank @Email String correoElectronico,

        @NotBlank String contrasena) {
}
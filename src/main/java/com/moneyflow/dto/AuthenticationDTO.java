package com.moneyflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationDTO(
        @Size(min = 3, max = 15, message = "Campo usuário deve ter entre 3 e 15 caracteres")
        @NotBlank(message = "Campo usuário é requerido")
        String username,
        @Size(min = 8, message = "Campo senha deve ter mínimo de 8 caracteres")
        @NotBlank(message = "Campo senha requerido")
        String password) {
}

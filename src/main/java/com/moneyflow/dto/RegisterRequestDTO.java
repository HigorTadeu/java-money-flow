package com.moneyflow.dto;

import com.moneyflow.entity.enuns.UserRole;
import com.moneyflow.entity.enuns.UserStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(@NotBlank(message="Campo requerido")
                                 @Size(max = 100, message = "Campo name deve ter no máximo 100 caracteres")
                                 String name,
                                 @NotBlank(message = "Campo requerido")
                                 @Size(max = 50, message = "Campo login deve ter no máximo 50 caractéres")
                                 String login,
                                 @NotBlank(message = "Campo requerido")
                                 @Size(max = 100, message = "Campo email deve ter no máximo 100 caractéres")
                                 String email,
                                 @NotBlank(message = "Campo requerido")
                                 String password,
                                 @NotBlank(message = "Campo requerido")
                                 UserRole role,
                                 @NotBlank(message = "Campo requerido")
                                 UserStatus status
) { }

package com.moneyflow.dto;

import com.moneyflow.entity.enuns.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}

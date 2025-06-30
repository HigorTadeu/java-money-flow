package com.moneyflow.dto;

import com.moneyflow.entity.enuns.UserRole;
import com.moneyflow.entity.enuns.UserStatus;

public record RegisterDTO(String name, String login, String email, String password, UserRole role, UserStatus status) {
}

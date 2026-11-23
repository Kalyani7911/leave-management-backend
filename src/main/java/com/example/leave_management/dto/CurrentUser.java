package com.example.leave_management.dto;

import org.springframework.security.oauth2.jwt.Jwt;

import com.example.leave_management.entity.Role;

public record CurrentUser(Long id, Role role) {

    public static CurrentUser from(Jwt jwt) {
        return new CurrentUser(
                Long.valueOf(jwt.getSubject()),
                Role.valueOf(jwt.getClaimAsString("role")));
    }

    public boolean isHr() {
        return role == Role.HR;
    }
}
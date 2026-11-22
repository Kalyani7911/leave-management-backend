package com.example.leave_management.dto;

import com.example.leave_management.entity.Role;

public record LoginResponse(
        String token,
        Long employeeId,
        String name,
        Role role,
        String department) {
}
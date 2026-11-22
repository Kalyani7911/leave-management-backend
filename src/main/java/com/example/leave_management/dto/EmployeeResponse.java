package com.example.leave_management.dto;

import com.example.leave_management.entity.Role;

public record EmployeeResponse(
        Long id,
        String name,
        String email,
        Role role,
        String department) {
}
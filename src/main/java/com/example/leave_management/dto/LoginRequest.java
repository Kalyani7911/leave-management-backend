package com.example.leave_management.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

                @NotBlank String employeeCode,
                @NotBlank String password) {
}
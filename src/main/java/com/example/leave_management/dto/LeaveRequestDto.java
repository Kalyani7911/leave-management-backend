package com.example.leave_management.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LeaveRequestDto(
                @NotNull Long employeeId,
                @NotNull Long leaveTypeId,
                @NotNull LocalDate fromDate,
                @NotNull LocalDate toDate,
                @NotBlank @Size(max = 500) String reason) {
}
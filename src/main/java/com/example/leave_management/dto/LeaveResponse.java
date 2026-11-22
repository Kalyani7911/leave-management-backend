package com.example.leave_management.dto;

import java.time.LocalDate;

import com.example.leave_management.entity.LeaveStatus;

public record LeaveResponse(
        Long id,
        String employeeName,
        String leaveType,
        LocalDate fromDate,
        LocalDate toDate,
        long days,
        String reason,
        LeaveStatus status) {
}
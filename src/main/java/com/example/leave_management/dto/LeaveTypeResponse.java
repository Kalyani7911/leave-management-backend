package com.example.leave_management.dto;

public record LeaveTypeResponse(
        Long id,
        String name,
        int yearlyQuota) {
}
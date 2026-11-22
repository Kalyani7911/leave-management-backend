package com.example.leave_management.dto;

public record BalanceResponse(
        String leaveType,
        int quota,
        long used,
        long remaining) {
}
package com.example.leave_management.dto;

public record BalanceResponse(
                String leaveType,
                Integer quota,
                long used,
                Long remaining) {
}
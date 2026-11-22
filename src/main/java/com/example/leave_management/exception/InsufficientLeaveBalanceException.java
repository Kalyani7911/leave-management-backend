package com.example.leave_management.exception;

public class InsufficientLeaveBalanceException extends BusinessRuleException {

    public InsufficientLeaveBalanceException(String message) {
        super(message);
    }
}
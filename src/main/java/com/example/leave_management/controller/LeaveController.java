package com.example.leave_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.leave_management.dto.BalanceResponse;
import com.example.leave_management.dto.LeaveRequestDto;
import com.example.leave_management.dto.LeaveResponse;
import com.example.leave_management.entity.LeaveStatus;
import com.example.leave_management.service.LeaveService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeaveResponse apply(@Valid @RequestBody LeaveRequestDto dto) {
        return service.apply(dto);
    }

    @GetMapping("/employee/{id}")
    public List<LeaveResponse> mine(@PathVariable Long id) {
        return service.forEmployee(id);
    }

    @GetMapping("/pending")
    public List<LeaveResponse> pending(@RequestParam Long managerId) {
        return service.pendingForManager(managerId);
    }

    @GetMapping("/balance/{employeeId}")
    public List<BalanceResponse> balance(@PathVariable Long employeeId) {
        return service.balance(employeeId);
    }

    @PutMapping("/{id}/approve")
    public LeaveResponse approve(@PathVariable Long id) {
        return service.decide(id, LeaveStatus.APPROVED);
    }

    @PutMapping("/{id}/reject")
    public LeaveResponse reject(@PathVariable Long id) {
        return service.decide(id, LeaveStatus.REJECTED);
    }
}
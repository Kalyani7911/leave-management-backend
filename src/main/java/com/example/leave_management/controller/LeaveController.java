package com.example.leave_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
import com.example.leave_management.dto.CurrentUser;
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
    public LeaveResponse apply(@Valid @RequestBody LeaveRequestDto dto,
            @AuthenticationPrincipal Jwt jwt) {
        return service.apply(dto, CurrentUser.from(jwt));
    }

    @GetMapping("/employee/{id}")
    public List<LeaveResponse> mine(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return service.forEmployee(id, CurrentUser.from(jwt));
    }

    @GetMapping("/pending")
    public List<LeaveResponse> pending(@RequestParam Long managerId,
            @AuthenticationPrincipal Jwt jwt) {
        return service.pendingForManager(managerId, CurrentUser.from(jwt));
    }

    @GetMapping("/balance/{employeeId}")
    public List<BalanceResponse> balance(@PathVariable Long employeeId,
            @AuthenticationPrincipal Jwt jwt) {
        return service.balance(employeeId, CurrentUser.from(jwt));
    }

    @PutMapping("/{id}/approve")
    public LeaveResponse approve(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return service.decide(id, LeaveStatus.APPROVED, CurrentUser.from(jwt));
    }

    @PutMapping("/{id}/reject")
    public LeaveResponse reject(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return service.decide(id, LeaveStatus.REJECTED, CurrentUser.from(jwt));
    }
}
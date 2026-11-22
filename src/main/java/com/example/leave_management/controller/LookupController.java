package com.example.leave_management.controller;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.leave_management.dto.EmployeeResponse;
import com.example.leave_management.dto.LeaveTypeResponse;
import com.example.leave_management.repository.EmployeeRepository;
import com.example.leave_management.repository.LeaveTypeRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LookupController {

    private final EmployeeRepository employeeRepo;
    private final LeaveTypeRepository typeRepo;

    @GetMapping("/employees")
    public List<EmployeeResponse> employees() {
        return employeeRepo.findAll().stream()
                .map(e -> new EmployeeResponse(e.getId(), e.getName(), e.getEmail(),
                        e.getRole(), e.getDepartment().getName()))
                .toList();
    }

    @GetMapping("/leave-types")
    public List<LeaveTypeResponse> leaveTypes() {
        return typeRepo.findAll().stream()
                .map(t -> new LeaveTypeResponse(t.getId(), t.getName(), t.getYearlyQuota()))
                .toList();
    }
}
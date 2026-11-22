package com.example.leave_management.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.leave_management.dto.BalanceResponse;
import com.example.leave_management.dto.LeaveRequestDto;
import com.example.leave_management.dto.LeaveResponse;
import com.example.leave_management.entity.Employee;
import com.example.leave_management.entity.LeaveRequest;
import com.example.leave_management.entity.LeaveStatus;
import com.example.leave_management.entity.LeaveType;
import com.example.leave_management.exception.BusinessRuleException;
import com.example.leave_management.exception.InsufficientLeaveBalanceException;
import com.example.leave_management.exception.ResourceNotFoundException;
import com.example.leave_management.repository.EmployeeRepository;
import com.example.leave_management.repository.LeaveRequestRepository;
import com.example.leave_management.repository.LeaveTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LeaveService {

    private final LeaveRequestRepository leaveRepo;
    private final EmployeeRepository employeeRepo;
    private final LeaveTypeRepository typeRepo;

    @Transactional
    public LeaveResponse apply(LeaveRequestDto dto) {
        if (dto.toDate().isBefore(dto.fromDate())) {
            throw new BusinessRuleException("To date cannot be before from date");
        }

        Employee employee = employeeRepo.findById(dto.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        LeaveType type = typeRepo.findById(dto.leaveTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found"));

        boolean overlaps = leaveRepo.existsOverlap(employee.getId(), dto.fromDate(), dto.toDate(),
                List.of(LeaveStatus.PENDING, LeaveStatus.APPROVED));
        if (overlaps) {
            throw new BusinessRuleException("You already have leave on these dates");
        }

        long days = ChronoUnit.DAYS.between(dto.fromDate(), dto.toDate()) + 1;
        long used = leaveRepo.usedDays(employee.getId(), type.getId(), dto.fromDate().getYear());
        if (used + days > type.getYearlyQuota()) {
            throw new InsufficientLeaveBalanceException("Only " + (type.getYearlyQuota() - used)
                    + " day(s) of " + type.getName() + " leave left");
        }

        LeaveRequest request = new LeaveRequest();
        request.setEmployee(employee);
        request.setLeaveType(type);
        request.setFromDate(dto.fromDate());
        request.setToDate(dto.toDate());
        request.setReason(dto.reason());
        return toResponse(leaveRepo.save(request));
    }

    public List<LeaveResponse> forEmployee(Long employeeId) {
        return leaveRepo.findByEmployeeIdOrderByFromDateDesc(employeeId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<LeaveResponse> pendingForManager(Long managerId) {
        return leaveRepo.findByEmployeeManagerIdAndStatus(managerId, LeaveStatus.PENDING).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public LeaveResponse decide(Long id, LeaveStatus newStatus) {
        LeaveRequest request = leaveRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new BusinessRuleException("Only pending requests can be approved or rejected");
        }
        request.setStatus(newStatus);
        return toResponse(request);
    }

    public List<BalanceResponse> balance(Long employeeId) {
        int year = LocalDate.now().getYear();
        return typeRepo.findAll().stream()
                .map(type -> {
                    long used = leaveRepo.usedDays(employeeId, type.getId(), year);
                    return new BalanceResponse(type.getName(), type.getYearlyQuota(),
                            used, type.getYearlyQuota() - used);
                })
                .toList();
    }

    private LeaveResponse toResponse(LeaveRequest l) {
        long days = ChronoUnit.DAYS.between(l.getFromDate(), l.getToDate()) + 1;
        return new LeaveResponse(l.getId(), l.getEmployee().getName(), l.getLeaveType().getName(),
                l.getFromDate(), l.getToDate(), days, l.getReason(), l.getStatus());
    }
}
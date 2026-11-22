package com.example.leave_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.leave_management.entity.LeaveType;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {
}
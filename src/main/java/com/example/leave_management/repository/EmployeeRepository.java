package com.example.leave_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.leave_management.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
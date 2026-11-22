package com.example.leave_management.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.leave_management.entity.LeaveRequest;
import com.example.leave_management.entity.LeaveStatus;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    // all leave requests of one employee, newest first
    List<LeaveRequest> findByEmployeeIdOrderByFromDateDesc(Long employeeId);

    // requests with a given status for everyone who reports to this manager
    List<LeaveRequest> findByEmployeeManagerIdAndStatus(Long managerId, LeaveStatus status);

    // true if the employee already has leave that overlaps these dates
    @Query("select count(l) > 0 from LeaveRequest l "
            + "where l.employee.id = :empId "
            + "and l.status in :statuses "
            + "and l.fromDate <= :toDate "
            + "and l.toDate >= :fromDate")
    boolean existsOverlap(@Param("empId") Long empId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("statuses") List<LeaveStatus> statuses);

    // days already taken or waiting for approval, for one leave type in one year
    @Query(value = "select coalesce(sum(to_date - from_date + 1), 0) from leave_requests "
            + "where employee_id = :empId and leave_type_id = :typeId "
            + "and status in ('APPROVED', 'PENDING') "
            + "and extract(year from from_date) = :year", nativeQuery = true)
    long usedDays(@Param("empId") Long empId,
            @Param("typeId") Long typeId,
            @Param("year") int year);
}
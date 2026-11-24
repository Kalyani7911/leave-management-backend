-- Useful queries for the Leave Desk database (PostgreSQL)
-- Run them in pgAdmin or psql against the leave_db database.

-- 1. JOIN: every leave request with employee, department and leave type
SELECT lr.id, e.name AS employee, d.name AS department, lt.name AS leave_type,
       lr.from_date, lr.to_date, lr.status
FROM leave_requests lr
JOIN employees e   ON e.id = lr.employee_id
JOIN departments d ON d.id = e.department_id
JOIN leave_types lt ON lt.id = lr.leave_type_id
ORDER BY lr.from_date;

-- 2. SELF JOIN: each employee with the name of their manager
SELECT e.name AS employee, m.name AS manager
FROM employees e
LEFT JOIN employees m ON m.id = e.manager_id
ORDER BY e.id;

-- 3. Pending requests for one manager's team (manager id 2)
SELECT lr.id, e.name AS employee, lt.name AS leave_type, lr.from_date, lr.to_date
FROM leave_requests lr
JOIN employees e ON e.id = lr.employee_id
JOIN leave_types lt ON lt.id = lr.leave_type_id
WHERE e.manager_id = 2 AND lr.status = 'PENDING';

-- 4. AGGREGATE: leave balance per employee and leave type for this year
--    (days taken or waiting for approval, counting both start and end dates)
SELECT e.name, lt.name AS leave_type, lt.yearly_quota,
       COALESCE(SUM(lr.to_date - lr.from_date + 1), 0) AS days_used,
       lt.yearly_quota - COALESCE(SUM(lr.to_date - lr.from_date + 1), 0) AS days_left
FROM employees e
CROSS JOIN leave_types lt
LEFT JOIN leave_requests lr
       ON lr.employee_id = e.id AND lr.leave_type_id = lt.id
      AND lr.status IN ('APPROVED', 'PENDING')
      AND EXTRACT(YEAR FROM lr.from_date) = EXTRACT(YEAR FROM CURRENT_DATE)
GROUP BY e.name, lt.name, lt.yearly_quota
ORDER BY e.name, lt.name;

-- 5. OVERLAP CHECK: does employee 4 already have leave between these dates?
--    Two ranges overlap when each one starts before the other ends.
SELECT COUNT(*) > 0 AS has_overlap
FROM leave_requests
WHERE employee_id = 4
  AND status IN ('PENDING', 'APPROVED')
  AND from_date <= DATE '2027-01-12'
  AND to_date   >= DATE '2027-01-10';

-- 6. GROUP BY with HAVING: departments with more than one leave request
SELECT d.name AS department, COUNT(lr.id) AS requests
FROM departments d
JOIN employees e ON e.department_id = d.id
JOIN leave_requests lr ON lr.employee_id = e.id
GROUP BY d.name
HAVING COUNT(lr.id) > 1
ORDER BY requests DESC;

-- 7. INSERT: add a new leave type
INSERT INTO leave_types (name, yearly_quota) VALUES ('Maternity', 90);

-- 8. UPDATE: change the yearly quota of a leave type
UPDATE leave_types SET yearly_quota = 15 WHERE name = 'Casual';

-- 9. UPDATE: approve a pending request
UPDATE leave_requests SET status = 'APPROVED'
WHERE id = 1 AND status = 'PENDING';

-- 10. DELETE: remove a rejected request and the leave type added above
DELETE FROM leave_requests WHERE status = 'REJECTED' AND id = 4;
DELETE FROM leave_types WHERE name = 'Maternity';
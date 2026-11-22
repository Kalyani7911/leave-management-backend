-- Sample data for development and demos

INSERT INTO departments (name) VALUES
 ('Engineering'), ('Human Resources'), ('Sales'), ('Finance');

INSERT INTO leave_types (name, yearly_quota) VALUES
 ('Casual', 12), ('Sick', 10), ('Paid', 18);

-- HR and managers first (no manager_id), then their team members
INSERT INTO employees (name, email, role, department_id, manager_id) VALUES
 ('Anita Deshmukh', 'anita.deshmukh@example.com', 'HR',       2, NULL),
 ('Rahul Joshi',    'rahul.joshi@example.com',    'MANAGER',  1, NULL),
 ('Meera Kulkarni', 'meera.kulkarni@example.com', 'MANAGER',  3, NULL);

INSERT INTO employees (name, email, role, department_id, manager_id) VALUES
 ('Pravin Patil',   'pravin.patil@example.com',   'EMPLOYEE', 1, 2),
 ('Sneha More',     'sneha.more@example.com',     'EMPLOYEE', 1, 2),
 ('Amit Shinde',    'amit.shinde@example.com',    'EMPLOYEE', 1, 2),
 ('Kavita Pawar',   'kavita.pawar@example.com',   'EMPLOYEE', 3, 3),
 ('Suresh Gaikwad', 'suresh.gaikwad@example.com', 'EMPLOYEE', 4, 1);

INSERT INTO leave_requests (employee_id, leave_type_id, from_date, to_date, reason, status) VALUES
 (4, 1, '2026-10-05', '2026-10-06', 'Family function',   'APPROVED'),
 (4, 2, '2026-11-10', '2026-11-11', 'Fever',             'APPROVED'),
 (5, 3, '2026-12-22', '2026-12-31', 'Year-end vacation', 'PENDING'),
 (6, 1, '2026-11-25', '2026-11-25', 'Personal work',     'REJECTED'),
 (7, 2, '2026-11-18', '2026-11-19', 'Medical checkup',   'APPROVED'),
 (8, 1, '2026-12-01', '2026-12-02', 'Travel',            'PENDING');

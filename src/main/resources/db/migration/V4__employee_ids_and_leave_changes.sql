-- 1. Unique employee ID used to sign in (EMP001, EMP002, ...)
ALTER TABLE employees ADD COLUMN employee_code VARCHAR(20);
UPDATE employees SET employee_code = 'EMP' || LPAD(id::text, 3, '0');
ALTER TABLE employees ALTER COLUMN employee_code SET NOT NULL;
ALTER TABLE employees ADD CONSTRAINT uq_employees_employee_code UNIQUE (employee_code);

-- 2. Government holidays replace paid leave: 10 days a year
UPDATE leave_types SET name = 'Government Holiday', yearly_quota = 10 WHERE name = 'Paid';

-- 3. Sick leave is 5 days a year
UPDATE leave_types SET yearly_quota = 5 WHERE name = 'Sick';

-- 4. Loss of pay has no yearly limit (NULL means unlimited)
ALTER TABLE leave_types ALTER COLUMN yearly_quota DROP NOT NULL;
INSERT INTO leave_types (name, yearly_quota) VALUES ('Loss of Pay', NULL);

-- 5. A reason is now required for every request
UPDATE leave_requests SET reason = 'Not specified' WHERE reason IS NULL OR reason = '';
ALTER TABLE leave_requests ALTER COLUMN reason SET NOT NULL;
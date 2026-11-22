ALTER TABLE employees ADD COLUMN password_hash VARCHAR(100);

-- demo password for every employee is: Leave@123
UPDATE employees
SET password_hash = '$2b$10$v.sQJkJGuMHqdt6benvOHe80mfw6pjZkPQWg9tSuK5UYIG2ilE.26';

ALTER TABLE employees ALTER COLUMN password_hash SET NOT NULL;
-- Employee & Leave Management: Version 1 schema (PostgreSQL)
-- Flyway-style name so it can drop straight into src/main/resources/db/migration

CREATE TABLE departments (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE employees (
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(150) NOT NULL,
    email          VARCHAR(150) NOT NULL UNIQUE,
    role           VARCHAR(20)  NOT NULL DEFAULT 'EMPLOYEE'
                   CHECK (role IN ('EMPLOYEE', 'MANAGER', 'HR')),
    department_id  BIGINT NOT NULL REFERENCES departments(id),
    manager_id     BIGINT REFERENCES employees(id),   -- self-reference
    created_at     TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP NOT NULL DEFAULT NOW(),
    CHECK (manager_id IS NULL OR manager_id <> id)
);

CREATE TABLE leave_types (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(50) NOT NULL UNIQUE,
    yearly_quota  INT NOT NULL CHECK (yearly_quota >= 0),
    created_at    TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE leave_requests (
    id             BIGSERIAL PRIMARY KEY,
    employee_id    BIGINT NOT NULL REFERENCES employees(id),
    leave_type_id  BIGINT NOT NULL REFERENCES leave_types(id),
    from_date      DATE NOT NULL,
    to_date        DATE NOT NULL,
    reason         VARCHAR(500),
    status         VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                   CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED')),
    created_at     TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP NOT NULL DEFAULT NOW(),
    CHECK (to_date >= from_date)
);

CREATE INDEX idx_employees_department  ON employees(department_id);
CREATE INDEX idx_employees_manager     ON employees(manager_id);
CREATE INDEX idx_leave_emp_dates       ON leave_requests(employee_id, from_date, to_date);
CREATE INDEX idx_leave_status          ON leave_requests(status);

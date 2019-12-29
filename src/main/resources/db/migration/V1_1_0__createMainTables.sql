CREATE TABLE IF NOT EXISTS enrolment
(
    id                SERIAL PRIMARY KEY,
    offering_id       INTEGER NOT NULL,
    student_id        INTEGER NOT NULL,
    assigned_staff_id INTEGER NOT NULL,
    is_active         BOOLEAN NOT NULL DEFAULT TRUE,
    inserted_at       TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION updated_at_now()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER auto_updated_at
    BEFORE UPDATE
    ON enrolment
    FOR EACH ROW
EXECUTE PROCEDURE updated_at_now();
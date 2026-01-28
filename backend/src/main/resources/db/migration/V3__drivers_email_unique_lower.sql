-- If V1 created a UNIQUE constraint on email, remove the constraint (not the index)
ALTER TABLE drivers DROP CONSTRAINT IF EXISTS drivers_email_key;

-- Optional cleanup if some other index name exists (safe)
DROP INDEX IF EXISTS idx_drivers_email_lower;

-- Enforce case-insensitive uniqueness
CREATE UNIQUE INDEX idx_drivers_email_lower ON drivers (lower(email));
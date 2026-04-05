ALTER TABLE destinations
    ALTER COLUMN approved SET DEFAULT true;

-- Speed up user search: approved = true AND country_name ILIKE %term%
-- (matches Spring Data findByApprovedTrueAndCountryNameContainingIgnoreCase)
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX IF NOT EXISTS idx_destinations_approved_country_trgm
    ON destinations USING gin (country_name gin_trgm_ops)
    WHERE approved = true;

-- Supports listing approved destinations (pagination / sort on id)
CREATE INDEX IF NOT EXISTS idx_destinations_approved_true_id
    ON destinations (id)
    WHERE approved = true;

-- Bootstrap admin (password: Admin@123456 — change after first login in production).
-- Skips if this email already exists.
INSERT INTO users (username, password, email, role, enabled, created_at, updated_at)
SELECT 'travel_admin',
       '$2b$10$wmbO5zaDFR2BeRFzJT8wOu1Rc8ea6ULqmpyzA6WFmK3Y8odpdgT46',
       'admin@traveldestinationplanner.local',
       'ADMIN',
       true,
       CURRENT_TIMESTAMP,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM users u WHERE u.email = 'admin@traveldestinationplanner.local'
);


INSERT INTO users (username, password, email, role, enabled, created_at, updated_at)
SELECT 'travel_user',
       '$2b$10$wmbO5zaDFR2BeRFzJT8wOu1Rc8ea6ULqmpyzA6WFmK3Y8odpdgT46',
       'user@traveldestinationplanner.local',
       'USER',
       true,
       CURRENT_TIMESTAMP,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM users u WHERE u.email = 'user@traveldestinationplanner.local'
);
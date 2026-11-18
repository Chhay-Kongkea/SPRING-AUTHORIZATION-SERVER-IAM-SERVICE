-- Assignment rows are mutable until revoked, so concurrent updates must be detected.
-- IF NOT EXISTS keeps this migration safe for databases created before version 004.
ALTER TABLE user_roles ADD COLUMN IF NOT EXISTS version BIGINT NOT NULL DEFAULT 0;
ALTER TABLE role_permissions ADD COLUMN IF NOT EXISTS version BIGINT NOT NULL DEFAULT 0;
ALTER TABLE user_permissions ADD COLUMN IF NOT EXISTS version BIGINT NOT NULL DEFAULT 0;

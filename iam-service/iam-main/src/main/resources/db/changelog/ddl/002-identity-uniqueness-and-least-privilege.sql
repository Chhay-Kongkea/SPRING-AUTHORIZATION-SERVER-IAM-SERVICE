-- 1) An unlinked (soft-deleted) identity must not block linking the same provider account again,
--    and a user may have only one ACTIVE identity per provider (domain rule B.6).
ALTER TABLE identities DROP CONSTRAINT uk_identities_provider_subject;
CREATE UNIQUE INDEX uk_identities_active_provider_subject
    ON identities(provider, provider_subject) WHERE is_deleted = FALSE;
CREATE UNIQUE INDEX uk_identities_active_user_provider
    ON identities(user_id, provider) WHERE is_deleted = FALSE;

-- 2) Least privilege: every registered user holds USER, so USER must not be able to list all users.
--    The assignment is revoked (never deleted) to keep the audit trail.
UPDATE role_permissions
   SET revoked_at = CURRENT_TIMESTAMP,
       revoked_by = 'system',
       reason     = 'Least privilege: ordinary users must not list every account'
 WHERE revoked_at IS NULL
   AND role_id = (SELECT id FROM roles WHERE name = 'USER')
   AND permission_id = (SELECT id FROM permissions WHERE name = 'user:read');

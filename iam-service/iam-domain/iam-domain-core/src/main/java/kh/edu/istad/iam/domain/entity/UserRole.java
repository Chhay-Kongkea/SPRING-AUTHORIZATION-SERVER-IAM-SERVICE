
package kh.edu.istad.iam.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.iam.domain.valueobject.RoleId;

import java.time.Instant;

public class UserRole extends BaseEntity<Long> {

    private final RoleId roleId;

    private final Instant assignedAt;
    private final String assignedBy;

    private final Instant expiresAt;

    private Instant revokedAt;
    private String revokedBy;

    private String reason;

    public UserRole(
            Long id,
            RoleId roleId,
            String assignedBy,
            Instant expiresAt,
            String reason) {

        super(id);

        if (roleId == null) {
            throw new IllegalArgumentException("Role ID cannot be null");
        }

        this.roleId = roleId;
        this.assignedAt = Instant.now();
        this.assignedBy = requireText(assignedBy, "Assigned by");
        this.expiresAt = expiresAt;
        this.reason = reason;
    }

    public static UserRole reconstitute(
            Long id,
            RoleId roleId,
            Instant assignedAt,
            String assignedBy,
            Instant expiresAt,
            Instant revokedAt,
            String revokedBy,
            String reason) {

        if (id == null) {
            throw new IllegalArgumentException("Assignment ID cannot be null");
        }

        if (assignedAt == null) {
            throw new IllegalArgumentException("Assigned at cannot be null");
        }

        UserRole userRole = new UserRole(
                id,
                roleId,
                assignedBy,
                expiresAt,
                reason
        );

        userRole.setRevocationState(revokedAt, revokedBy);

        return new UserRole(
                id,
                roleId,
                assignedAt,
                assignedBy,
                expiresAt,
                revokedAt,
                revokedBy,
                reason
        );
    }

    private UserRole(
            Long id,
            RoleId roleId,
            Instant assignedAt,
            String assignedBy,
            Instant expiresAt,
            Instant revokedAt,
            String revokedBy,
            String reason) {

        super(id);

        if (roleId == null) {
            throw new IllegalArgumentException("Role ID cannot be null");
        }

        this.roleId = roleId;
        this.assignedAt = assignedAt;
        this.assignedBy = requireText(assignedBy, "Assigned by");
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;
        this.revokedBy = revokedBy;
        this.reason = reason;
    }

    public void revoke(String actor, String reason) {

        if (isRevoked()) {
            throw new IllegalStateException(
                    "Role assignment is already revoked"
            );
        }

        this.revokedBy = requireText(actor, "Revoked by");
        this.reason = requireText(reason, "Reason");
        this.revokedAt = Instant.now();
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public boolean isExpired() {
        return expiresAt != null && !expiresAt.isAfter(Instant.now());
    }

    public boolean isActive() {
        return !isRevoked() && !isExpired();
    }

    public RoleId getRoleId() {
        return roleId;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public String getRevokedBy() {
        return revokedBy;
    }

    public String getReason() {
        return reason;
    }

    private void setRevocationState(Instant revokedAt, String revokedBy) {
        if (revokedAt == null && revokedBy != null) {
            throw new IllegalArgumentException(
                    "Revoked by must be null when assignment is not revoked"
            );
        }

        if (revokedAt != null && (revokedBy == null || revokedBy.isBlank())) {
            throw new IllegalArgumentException(
                    "Revoked by is required for a revoked assignment"
            );
        }

        this.revokedAt = revokedAt;
        this.revokedBy = revokedBy == null ? null : revokedBy.trim();
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }

        return value.trim();
    }
}


package kh.edu.istad.iam.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.iam.domain.valueobject.PermissionEffect;
import kh.edu.istad.iam.domain.valueobject.PermissionId;

import java.time.Instant;

public class UserPermission extends BaseEntity<Long> {

    private final PermissionId permissionId;
    private final PermissionEffect effect;

    private final Instant assignedAt;
    private final String assignedBy;

    private final Instant expiresAt;

    private Instant revokedAt;
    private String revokedBy;

    private String reason;

    public UserPermission(
            Long id,
            PermissionId permissionId,
            PermissionEffect effect,
            String assignedBy,
            Instant expiresAt,
            String reason) {

        super(id);

        if (permissionId == null || effect == null) {
            throw new IllegalArgumentException(
                    "Permission ID and effect are required"
            );
        }

        this.permissionId = permissionId;
        this.effect = effect;
        this.assignedAt = Instant.now();
        this.assignedBy = requireText(assignedBy, "Assigned by");
        this.expiresAt = expiresAt;
        this.reason = reason;
    }

    public static UserPermission reconstitute(
            Long id,
            PermissionId permissionId,
            PermissionEffect effect,
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

        return new UserPermission(
                id,
                permissionId,
                effect,
                assignedAt,
                assignedBy,
                expiresAt,
                revokedAt,
                revokedBy,
                reason
        );
    }

    private UserPermission(
            Long id,
            PermissionId permissionId,
            PermissionEffect effect,
            Instant assignedAt,
            String assignedBy,
            Instant expiresAt,
            Instant revokedAt,
            String revokedBy,
            String reason) {

        super(id);

        if (permissionId == null || effect == null) {
            throw new IllegalArgumentException(
                    "Permission ID and effect are required"
            );
        }

        this.permissionId = permissionId;
        this.effect = effect;
        this.assignedAt = assignedAt;
        this.assignedBy = requireText(assignedBy, "Assigned by");
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;

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

        this.revokedBy = revokedBy == null ? null : revokedBy.trim();
        this.reason = reason;
    }

    public void revoke(String actor, String reason) {

        if (isRevoked()) {
            throw new IllegalStateException(
                    "Permission assignment is already revoked"
            );
        }

        this.revokedBy = requireText(actor, "Revoked by");
        this.reason = requireReason(reason);
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

    public PermissionId getPermissionId() {
        return permissionId;
    }

    public PermissionEffect getEffect() {
        return effect;
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

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }

        return value.trim();
    }

    private static String requireReason(String reason) {
        String value = requireText(reason, "Reason");

        if (value.length() > 255) {
            throw new IllegalArgumentException(
                    "Reason cannot exceed 255 characters"
            );
        }

        return value;
    }
}

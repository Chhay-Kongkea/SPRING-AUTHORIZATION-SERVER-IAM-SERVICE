
package kh.edu.istad.iam.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.iam.domain.valueobject.PermissionId;

import java.time.Instant;
import java.util.Objects;

public class RolePermission extends BaseEntity<Long> {

    private final PermissionId permissionId;
    private final Instant assignedAt;
    private final String assignedBy;
    private final Instant expiresAt;

    private Instant revokedAt;
    private String revokedBy;
    private String reason;

    public RolePermission(
            Long id,
            PermissionId permissionId,
            String assignedBy,
            Instant expiresAt,
            String reason
    ) {
        this(
                id,
                permissionId,
                Instant.now(),
                assignedBy,
                expiresAt,
                null,
                null,
                reason
        );
    }

    private RolePermission(
            Long id,
            PermissionId permissionId,
            Instant assignedAt,
            String assignedBy,
            Instant expiresAt,
            Instant revokedAt,
            String revokedBy,
            String reason
    ) {
        super(id);

        this.permissionId = Objects.requireNonNull(
                permissionId,
                "Permission ID is required"
        );
        this.assignedAt = Objects.requireNonNull(
                assignedAt,
                "Assigned at is required"
        );

        if (assignedBy == null || assignedBy.isBlank()) {
            throw new IllegalArgumentException("Assigned by is required");
        }

        this.assignedBy = assignedBy;
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;
        this.revokedBy = revokedBy;
        this.reason = reason;
    }

    public static RolePermission reconstitute(
            Long id,
            PermissionId permissionId,
            Instant assignedAt,
            String assignedBy,
            Instant expiresAt,
            Instant revokedAt,
            String revokedBy,
            String reason
    ) {
        Objects.requireNonNull(id, "Role permission ID is required");

        return new RolePermission(
                id,
                permissionId,
                assignedAt,
                assignedBy,
                expiresAt,
                revokedAt,
                revokedBy,
                reason
        );
    }

    public void revoke(String actor) {
        if (revokedAt != null) {
            throw new IllegalStateException(
                    "Permission assignment is already revoked"
            );
        }

        if (actor == null || actor.isBlank()) {
            throw new IllegalArgumentException("Actor is required");
        }

        this.revokedAt = Instant.now();
        this.revokedBy = actor;
    }

    public boolean isActive() {
        return revokedAt == null
                && (expiresAt == null || expiresAt.isAfter(Instant.now()));
    }

    public PermissionId getPermissionId() {
        return permissionId;
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
}

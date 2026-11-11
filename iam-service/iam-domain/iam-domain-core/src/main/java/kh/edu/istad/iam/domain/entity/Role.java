
package kh.edu.istad.iam.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.iam.domain.valueobject.PermissionId;
import kh.edu.istad.iam.domain.valueobject.RoleId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Role extends AggregateRoot<RoleId> {

    private String name;
    private String description;

    private boolean system;
    private boolean active;
    private boolean deleted;

    private Instant deletedAt;
    private String deletedBy;

    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    private long version;

    private final List<RolePermission> permissions = new ArrayList<>();

    public Role(
            RoleId id,
            String name,
            String description,
            boolean system,
            String createdBy
    ) {
        super(id);

        this.name = requireText(name, "Role name").toUpperCase();
        this.description = description;
        this.system = system;
        this.active = true;
        this.deleted = false;

        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.createdBy = requireText(createdBy, "Created by");
        this.updatedBy = createdBy;
    }

    private Role(
            RoleId id,
            String name,
            String description,
            boolean system,
            boolean active,
            boolean deleted,
            Instant deletedAt,
            String deletedBy,
            Instant createdAt,
            String createdBy,
            Instant updatedAt,
            String updatedBy,
            long version,
            List<RolePermission> permissions
    ) {
        super(id);

        this.name = requireText(name, "Role name").toUpperCase();
        this.description = description;
        this.system = system;
        this.active = active;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
        this.createdAt = Objects.requireNonNull(
                createdAt,
                "Created at is required"
        );
        this.createdBy = requireText(createdBy, "Created by");
        this.updatedAt = Objects.requireNonNull(
                updatedAt,
                "Updated at is required"
        );
        this.updatedBy = requireText(updatedBy, "Updated by");
        this.version = version;

        if (version < 0) {
            throw new IllegalArgumentException("Version cannot be negative");
        }

        if (deleted && deletedAt == null) {
            throw new IllegalArgumentException(
                    "Deleted at is required for deleted role"
            );
        }

        if (deleted && (deletedBy == null || deletedBy.isBlank())) {
            throw new IllegalArgumentException(
                    "Deleted by is required for deleted role"
            );
        }

        if (permissions != null) {
            this.permissions.addAll(permissions);
        }
    }

    public static Role reconstitute(
            RoleId id,
            String name,
            String description,
            boolean system,
            boolean active,
            boolean deleted,
            Instant deletedAt,
            String deletedBy,
            Instant createdAt,
            String createdBy,
            Instant updatedAt,
            String updatedBy,
            long version,
            List<RolePermission> permissions
    ) {
        Objects.requireNonNull(id, "Role ID is required");

        return new Role(
                id,
                name,
                description,
                system,
                active,
                deleted,
                deletedAt,
                deletedBy,
                createdAt,
                createdBy,
                updatedAt,
                updatedBy,
                version,
                permissions
        );
    }

    public void assignPermission(
            PermissionId permissionId,
            String actor,
            Instant expiresAt,
            String reason
    ) {
        ensureNotDeleted();

        boolean alreadyAssigned = permissions.stream()
                .anyMatch(assignment ->
                        assignment.getPermissionId().equals(permissionId)
                                && assignment.isActive()
                );

        if (alreadyAssigned) {
            throw new IllegalStateException(
                    "Role already has this active permission"
            );
        }

        RolePermission rolePermission = new RolePermission(
                null,
                permissionId,
                actor,
                expiresAt,
                reason
        );

        permissions.add(rolePermission);
        touch(actor);
    }

    public void revokePermission(
            PermissionId permissionId,
            String actor
    ) {
        ensureNotDeleted();

        RolePermission rolePermission = permissions.stream()
                .filter(assignment ->
                        assignment.getPermissionId().equals(permissionId)
                                && assignment.isActive()
                )
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Active permission assignment not found"
                ));

        rolePermission.revoke(actor);
        touch(actor);
    }

    public void updateDetails(
            String name,
            String description,
            String actor
    ) {
        ensureModifiable();

        this.name = requireText(name, "Role name").toUpperCase();
        this.description = description;

        touch(actor);
    }

    public void activate(String actor) {
        ensureNotDeleted();
        this.active = true;
        touch(actor);
    }

    public void deactivate(String actor) {
        ensureNotDeleted();
        this.active = false;
        touch(actor);
    }

    public void softDelete(String actor) {
        ensureModifiable();

        this.deleted = true;
        this.deletedAt = Instant.now();
        this.deletedBy = requireText(actor, "Deleted by");

        touch(actor);
    }

    public List<RolePermission> getPermissions() {
        return List.copyOf(permissions);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSystem() {
        return system;
    }

    public boolean isActive() {
        return active && !deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public long getVersion() {
        return version;
    }

    private void ensureModifiable() {
        ensureNotDeleted();

        if (system) {
            throw new IllegalStateException(
                    "System role cannot be modified"
            );
        }
    }

    private void ensureNotDeleted() {
        if (deleted) {
            throw new IllegalStateException(
                    "Deleted role cannot be modified"
            );
        }
    }

    private void touch(String actor) {
        this.updatedAt = Instant.now();
        this.updatedBy = requireText(actor, "Updated by");
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    field + " cannot be blank"
            );
        }

        return value.trim();
    }
}

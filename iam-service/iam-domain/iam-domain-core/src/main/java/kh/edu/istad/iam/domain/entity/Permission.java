
package kh.edu.istad.iam.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.iam.domain.valueobject.PermissionId;

import java.time.Instant;
import java.util.Objects;

public class Permission extends AggregateRoot<PermissionId> {

    private String name;
    private String resource;
    private String action;
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

    public Permission(
            PermissionId id,
            String resource,
            String action,
            String description,
            boolean system,
            String createdBy
    ) {
        super(id);

        this.resource = requireText(resource, "Resource").toLowerCase();
        this.action = requireText(action, "Action").toLowerCase();
        this.name = this.resource + ":" + this.action;
        this.description = description;

        this.system = system;
        this.active = true;
        this.deleted = false;

        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.createdBy = requireText(createdBy, "Created by");
        this.updatedBy = createdBy;
    }

    private Permission(
            PermissionId id,
            String name,
            String resource,
            String action,
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
            long version
    ) {
        super(id);

        this.resource = requireText(resource, "Resource").toLowerCase();
        this.action = requireText(action, "Action").toLowerCase();
        this.name = requireText(name, "Permission name").toLowerCase();
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

        if (version < 0) {
            throw new IllegalArgumentException("Version cannot be negative");
        }

        this.version = version;

        if (deleted && deletedAt == null) {
            throw new IllegalArgumentException(
                    "Deleted at is required for deleted permission"
            );
        }

        if (deleted && (deletedBy == null || deletedBy.isBlank())) {
            throw new IllegalArgumentException(
                    "Deleted by is required for deleted permission"
            );
        }
    }

    public static Permission reconstitute(
            PermissionId id,
            String name,
            String resource,
            String action,
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
            long version
    ) {
        Objects.requireNonNull(id, "Permission ID is required");

        return new Permission(
                id,
                name,
                resource,
                action,
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
                version
        );
    }

    public void updateDetails(
            String resource,
            String action,
            String description,
            String actor
    ) {
        ensureModifiable();

        this.resource = requireText(resource, "Resource").toLowerCase();
        this.action = requireText(action, "Action").toLowerCase();
        this.name = this.resource + ":" + this.action;
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

    public String getName() {
        return name;
    }

    public String getResource() {
        return resource;
    }

    public String getAction() {
        return action;
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
                    "System permission cannot be modified"
            );
        }
    }

    private void ensureNotDeleted() {
        if (deleted) {
            throw new IllegalStateException(
                    "Deleted permission cannot be modified"
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

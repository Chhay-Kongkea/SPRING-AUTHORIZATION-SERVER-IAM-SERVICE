
package kh.edu.istad.iam.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.iam.domain.valueobject.IdentityId;
import kh.edu.istad.iam.domain.valueobject.IdentityProvider;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

public class Identity extends AggregateRoot<IdentityId> {

    private final UserId userId;
    private final IdentityProvider provider;
    private final String providerSubject;

    private String email;
    private boolean emailVerified;

    private String displayName;
    private String username;
    private String pictureUrl;

    private Map<String, Object> attributes;

    private Instant linkedAt;
    private Instant lastLoginAt;

    private boolean deleted;
    private Instant deletedAt;
    private String deletedBy;

    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    private long version;

    public Identity(
            IdentityId id,
            UserId userId,
            IdentityProvider provider,
            String providerSubject,
            String email,
            boolean emailVerified,
            String displayName,
            String username,
            String pictureUrl,
            Map<String, Object> attributes,
            String createdBy
    ) {
        super(id);

        this.userId = Objects.requireNonNull(userId, "User ID is required");
        this.provider = Objects.requireNonNull(provider, "Provider is required");
        this.providerSubject = requireText(
                providerSubject,
                "Provider subject"
        );

        this.email = email;
        this.emailVerified = emailVerified;
        this.displayName = displayName;
        this.username = username;
        this.pictureUrl = pictureUrl;
        this.attributes = attributes == null ? Map.of() : Map.copyOf(attributes);

        this.linkedAt = Instant.now();
        this.deleted = false;

        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.createdBy = requireText(createdBy, "Created by");
        this.updatedBy = createdBy;
    }

    private Identity(
            IdentityId id,
            UserId userId,
            IdentityProvider provider,
            String providerSubject,
            String email,
            boolean emailVerified,
            String displayName,
            String username,
            String pictureUrl,
            Map<String, Object> attributes,
            Instant linkedAt,
            Instant lastLoginAt,
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

        this.userId = Objects.requireNonNull(userId, "User ID is required");
        this.provider = Objects.requireNonNull(provider, "Provider is required");
        this.providerSubject = requireText(
                providerSubject,
                "Provider subject"
        );

        this.email = email;
        this.emailVerified = emailVerified;
        this.displayName = displayName;
        this.username = username;
        this.pictureUrl = pictureUrl;
        this.attributes = attributes == null ? Map.of() : Map.copyOf(attributes);

        this.linkedAt = Objects.requireNonNull(linkedAt, "Linked at is required");
        this.lastLoginAt = lastLoginAt;

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
                    "Deleted at is required for deleted identity"
            );
        }
        if (deleted && (deletedBy == null || deletedBy.isBlank())) {
            throw new IllegalArgumentException(
                    "Deleted by is required for deleted identity"
            );
        }
    }

    public static Identity reconstitute(
            IdentityId id,
            UserId userId,
            IdentityProvider provider,
            String providerSubject,
            String email,
            boolean emailVerified,
            String displayName,
            String username,
            String pictureUrl,
            Map<String, Object> attributes,
            Instant linkedAt,
            Instant lastLoginAt,
            boolean deleted,
            Instant deletedAt,
            String deletedBy,
            Instant createdAt,
            String createdBy,
            Instant updatedAt,
            String updatedBy,
            long version
    ) {
        Objects.requireNonNull(id, "Identity ID is required");

        return new Identity(
                id,
                userId,
                provider,
                providerSubject,
                email,
                emailVerified,
                displayName,
                username,
                pictureUrl,
                attributes,
                linkedAt,
                lastLoginAt,
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

    public void updateProviderProfile(
            String email,
            boolean emailVerified,
            String displayName,
            String username,
            String pictureUrl,
            Map<String, Object> attributes,
            String actor
    ) {
        ensureNotDeleted();

        this.email = email;
        this.emailVerified = emailVerified;
        this.displayName = displayName;
        this.username = username;
        this.pictureUrl = pictureUrl;
        this.attributes = attributes == null ? Map.of() : Map.copyOf(attributes);

        touch(actor);
    }

    public void recordLogin(String actor) {
        ensureNotDeleted();
        this.lastLoginAt = Instant.now();
        touch(actor);
    }

    public void unlink(String actor) {
        ensureNotDeleted();

        this.deleted = true;
        this.deletedAt = Instant.now();
        this.deletedBy = requireText(actor, "Deleted by");

        touch(actor);
    }

    public UserId getUserId() {
        return userId;
    }

    public IdentityProvider getProvider() {
        return provider;
    }

    public String getProviderSubject() {
        return providerSubject;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUsername() {
        return username;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Instant getLinkedAt() {
        return linkedAt;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
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

    private void ensureNotDeleted() {
        if (deleted) {
            throw new IllegalStateException(
                    "Unlinked identity cannot be modified"
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


package kh.edu.istad.iam.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.iam.domain.valueobject.PermissionEffect;
import kh.edu.istad.iam.domain.valueobject.PermissionId;
import kh.edu.istad.iam.domain.valueobject.RoleId;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User extends AggregateRoot<UserId> {

    private String username;
    private String email;
    private boolean emailVerified;
    private String passwordHash;

    private String givenName;
    private String familyName;
    private String middleName;
    private String nickname;
    private String gender;
    private LocalDate birthdate;
    private String profileUrl;
    private String pictureUrl;
    private String websiteUrl;
    private String zoneInfo;
    private String locale;
    private String phoneNumber;
    private boolean phoneNumberVerified;
    private String streetAddress;
    private String locality;
    private String region;
    private String postalCode;
    private String country;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    private int failedLoginAttempts;
    private Instant lastLoginAt;
    private Instant passwordChangedAt;

    private boolean deleted;
    private Instant deletedAt;
    private String deletedBy;

    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    private long version;

    private final List<UserRole> roles = new ArrayList<>();
    private final List<UserPermission> permissions = new ArrayList<>();


    public User(
            UserId id,
            String username,
            String email,
            String passwordHash,
            String givenName,
            String familyName,
            String locale,
            String createdBy) {

        super(id);

        this.username = requireText(username, "Username");
        this.email = requireText(email, "Email").toLowerCase();
        this.passwordHash = passwordHash;
        this.givenName = requireText(givenName, "Given name");
        this.familyName = requireText(familyName, "Family name");
        this.locale = requireText(locale, "Locale");
        if (!"en".equals(this.locale) && !"km".equals(this.locale)) {
            throw new IllegalArgumentException("Locale must be en or km");
        }

        this.enabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;

        this.emailVerified = false;
        this.phoneNumberVerified = false;
        this.failedLoginAttempts = 0;
        this.deleted = false;

        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.createdBy = requireText(createdBy, "Created by");
        this.updatedBy = this.createdBy;

        this.version = 0;
    }


    public static User reconstitute(
            UserId id,
            String username,
            String email,
            boolean emailVerified,
            String passwordHash,

            String givenName,
            String familyName,
            String middleName,
            String nickname,
            String gender,
            LocalDate birthdate,
            String profileUrl,
            String pictureUrl,
            String websiteUrl,
            String zoneInfo,
            String locale,
            String phoneNumber,
            boolean phoneNumberVerified,
            String streetAddress,
            String locality,
            String region,
            String postalCode,
            String country,

            boolean enabled,
            boolean accountNonExpired,
            boolean accountNonLocked,
            boolean credentialsNonExpired,

            int failedLoginAttempts,
            Instant lastLoginAt,
            Instant passwordChangedAt,

            boolean deleted,
            Instant deletedAt,
            String deletedBy,

            Instant createdAt,
            String createdBy,
            Instant updatedAt,
            String updatedBy,
            long version,

            List<UserRole> roles,
            List<UserPermission> permissions) {

        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (createdAt == null || updatedAt == null) {
            throw new IllegalArgumentException(
                    "User creation and update timestamps cannot be null"
            );
        }

        User user = new User(id);

        user.username = requireText(username, "Username");
        user.email = requireText(email, "Email").toLowerCase();
        user.emailVerified = emailVerified;
        user.passwordHash = passwordHash;

        user.givenName = requireText(givenName, "Given name");
        user.familyName = requireText(familyName, "Family name");
        user.middleName = middleName;
        user.nickname = nickname;
        user.gender = gender;
        user.birthdate = birthdate;
        user.profileUrl = profileUrl;
        user.pictureUrl = pictureUrl;
        user.websiteUrl = websiteUrl;
        user.zoneInfo = zoneInfo;
        user.locale = requireText(locale, "Locale");
        user.phoneNumber = phoneNumber;
        user.phoneNumberVerified = phoneNumberVerified;
        user.streetAddress = streetAddress;
        user.locality = locality;
        user.region = region;
        user.postalCode = postalCode;
        user.country = country;

        user.enabled = enabled;
        user.accountNonExpired = accountNonExpired;
        user.accountNonLocked = accountNonLocked;
        user.credentialsNonExpired = credentialsNonExpired;

        if (failedLoginAttempts < 0) {
            throw new IllegalArgumentException(
                    "Failed login attempts cannot be negative"
            );
        }

        user.failedLoginAttempts = failedLoginAttempts;
        user.lastLoginAt = lastLoginAt;
        user.passwordChangedAt = passwordChangedAt;

        user.deleted = deleted;
        user.deletedAt = deletedAt;
        user.deletedBy = deletedBy;

        if (deleted && deletedAt == null) {
            throw new IllegalArgumentException(
                    "Deleted at is required for a deleted user"
            );
        }

        if (deleted && (deletedBy == null || deletedBy.isBlank())) {
            throw new IllegalArgumentException(
                    "Deleted by is required for a deleted user"
            );
        }

        user.createdAt = createdAt;
        user.createdBy = requireText(createdBy, "Created by");
        user.updatedAt = updatedAt;
        user.updatedBy = requireText(updatedBy, "Updated by");
        user.version = version;

        if (roles != null) {
            user.roles.addAll(roles);
        }

        if (permissions != null) {
            user.permissions.addAll(permissions);
        }

        return user;
    }

    private User(UserId id) {
        super(id);
    }


    public void assignRole(
            RoleId roleId,
            String actor,
            Instant expiresAt,
            String reason) {

        ensureNotDeleted();

        boolean alreadyAssigned = roles.stream()
                .anyMatch(userRole ->
                        userRole.getRoleId().equals(roleId)
                                && userRole.isActive()
                );

        if (alreadyAssigned) {
            throw new IllegalStateException(
                    "User already has this active role"
            );
        }

        UserRole userRole = new UserRole(
                null,
                roleId,
                actor,
                expiresAt,
                reason
        );

        roles.add(userRole);
        touch(actor);
    }

    public void revokeRole(
            RoleId roleId,
            String actor,
            String reason) {

        ensureNotDeleted();

        UserRole userRole = roles.stream()
                .filter(assignment ->
                        assignment.getRoleId().equals(roleId)
                                && assignment.isActive()
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Active role assignment not found"
                        )
                );

        userRole.revoke(actor, reason);

        touch(actor);
    }


    public void grantPermission(
            PermissionId permissionId,
            String actor,
            Instant expiresAt,
            String reason) {

        assignPermission(
                permissionId,
                PermissionEffect.GRANT,
                actor,
                expiresAt,
                reason
        );
    }

    public void denyPermission(
            PermissionId permissionId,
            String actor,
            Instant expiresAt,
            String reason) {

        assignPermission(
                permissionId,
                PermissionEffect.DENY,
                actor,
                expiresAt,
                reason
        );
    }

    private void assignPermission(
            PermissionId permissionId,
            PermissionEffect effect,
            String actor,
            Instant expiresAt,
            String reason) {

        ensureNotDeleted();

        boolean alreadyAssigned = permissions.stream()
                .anyMatch(assignment ->
                        assignment.getPermissionId().equals(permissionId)
                                && assignment.getEffect() == effect
                                && assignment.isActive()
                );

        if (alreadyAssigned) {
            throw new IllegalStateException(
                    "User already has this active permission effect"
            );
        }

        UserPermission userPermission = new UserPermission(
                null,
                permissionId,
                effect,
                actor,
                expiresAt,
                reason
        );

        permissions.add(userPermission);
        touch(actor);
    }

    public void revokePermission(
            PermissionId permissionId,
            PermissionEffect effect,
            String actor,
            String reason) {

        ensureNotDeleted();

        UserPermission userPermission = permissions.stream()
                .filter(assignment ->
                        assignment.getPermissionId().equals(permissionId)
                                && assignment.getEffect() == effect
                                && assignment.isActive()
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Active permission assignment not found"
                        )
                );

        userPermission.revoke(actor, reason);

        touch(actor);
    }


    public void updateProfile(
            String givenName,
            String familyName,
            String middleName,
            String nickname,
            String gender,
            LocalDate birthdate,
            String profileUrl,
            String pictureUrl,
            String websiteUrl,
            String zoneInfo,
            String locale,
            String phoneNumber,
            String streetAddress,
            String locality,
            String region,
            String postalCode,
            String country,
            String actor) {

        ensureNotDeleted();
        String auditActor = requireText(actor, "Updated by");

        if (givenName != null) this.givenName = requireText(givenName, "Given name");
        if (familyName != null) this.familyName = requireText(familyName, "Family name");
        if (middleName != null) this.middleName = middleName;
        if (nickname != null) this.nickname = nickname;
        if (gender != null) this.gender = gender;
        if (birthdate != null) this.birthdate = birthdate;
        if (profileUrl != null) this.profileUrl = profileUrl;
        if (pictureUrl != null) this.pictureUrl = pictureUrl;
        if (websiteUrl != null) this.websiteUrl = websiteUrl;
        if (zoneInfo != null) this.zoneInfo = zoneInfo;
        if (locale != null) {
            String normalizedLocale = requireText(locale, "Locale");
            if (!"en".equals(normalizedLocale) && !"km".equals(normalizedLocale)) {
                throw new IllegalArgumentException("Locale must be en or km");
            }
            this.locale = normalizedLocale;
        }
        if (phoneNumber != null && !phoneNumber.equals(this.phoneNumber)) {
            if (!phoneNumber.matches("^\\+[1-9][0-9]{1,14}$")) {
                throw new IllegalArgumentException("Phone number must use E.164 format");
            }
            this.phoneNumber = phoneNumber;
            this.phoneNumberVerified = false;
        }
        if (streetAddress != null) this.streetAddress = streetAddress;
        if (locality != null) this.locality = locality;
        if (region != null) this.region = region;
        if (postalCode != null) this.postalCode = postalCode;
        if (country != null) this.country = country;

        touch(auditActor);
    }


    public void enable(String actor) {
        ensureNotDeleted();
        this.enabled = true;
        touch(actor);
    }

    public void disable(String actor) {
        ensureNotDeleted();
        this.enabled = false;
        touch(actor);
    }

    public void lock(String actor) {
        ensureNotDeleted();
        this.accountNonLocked = false;
        touch(actor);
    }

    public void unlock(String actor) {
        ensureNotDeleted();
        this.accountNonLocked = true;
        this.failedLoginAttempts = 0;
        touch(actor);
    }


    public void recordSuccessfulLogin(String actor) {
        ensureNotDeleted();

        this.lastLoginAt = Instant.now();
        this.failedLoginAttempts = 0;

        touch(actor);
    }

    public void recordFailedLogin(String actor) {
        ensureNotDeleted();

        this.failedLoginAttempts++;

        touch(actor);
    }


    public void changePassword(String newPasswordHash, String actor) {
        ensureNotDeleted();

        this.passwordHash = requireText(
                newPasswordHash,
                "Password hash"
        );

        this.passwordChangedAt = Instant.now();
        this.credentialsNonExpired = true;

        touch(actor);
    }

    public void verifyEmail(String actor) {
        ensureNotDeleted();
        this.emailVerified = true;
        touch(actor);
    }

    public void verifyPhoneNumber(String actor) {
        ensureNotDeleted();
        this.phoneNumberVerified = true;
        touch(actor);
    }

    public void softDelete(String actor) {
        ensureNotDeleted();

        this.deleted = true;
        this.deletedAt = Instant.now();
        this.deletedBy = requireText(actor, "Deleted by");

        touch(actor);
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getZoneInfo() {
        return zoneInfo;
    }

    public String getLocale() {
        return locale;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getLocality() {
        return locality;
    }

    public String getRegion() {
        return region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public Instant getPasswordChangedAt() {
        return passwordChangedAt;
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

    public List<UserRole> getRoles() {
        return List.copyOf(roles);
    }

    public List<UserPermission> getPermissions() {
        return List.copyOf(permissions);
    }

    public String getFullName() {
        return (givenName + " " + familyName).trim();
    }


    private void ensureNotDeleted() {
        if (deleted) {
            throw new IllegalStateException(
                    "Deleted user cannot be modified"
            );
        }
    }

    private void touch(String actor) {
        this.updatedAt = Instant.now();
        this.updatedBy = actor;
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

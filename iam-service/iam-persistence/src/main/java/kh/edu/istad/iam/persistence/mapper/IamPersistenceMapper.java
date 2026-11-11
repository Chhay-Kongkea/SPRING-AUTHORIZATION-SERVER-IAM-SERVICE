
package kh.edu.istad.iam.persistence.mapper;

import kh.edu.istad.iam.domain.entity.*;
import kh.edu.istad.iam.domain.valueobject.IdentityId;
import kh.edu.istad.iam.domain.valueobject.PermissionId;
import kh.edu.istad.iam.domain.valueobject.RoleId;
import kh.edu.istad.iam.domain.valueobject.UserId;
import kh.edu.istad.iam.persistence.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface IamPersistenceMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", expression = "java(user.getId().value())")
    @Mapping(target = "zoneinfo", source = "zoneInfo")
    @Mapping(target = "version", ignore = true)
    UserJpaEntity toJpaEntity(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "zoneinfo", source = "zoneInfo")
    @Mapping(target = "version", ignore = true)
    void updateJpaEntity(
            User user,
            @MappingTarget UserJpaEntity entity
    );

    default User toDomain(
            UserJpaEntity entity,
            List<UserRole> roles,
            List<UserPermission> permissions
    ) {
        return User.reconstitute(
                new UserId(entity.getUuid()),
                entity.getUsername(),
                entity.getEmail(),
                entity.isEmailVerified(),
                entity.getPasswordHash(),
                entity.getGivenName(),
                entity.getFamilyName(),
                entity.getMiddleName(),
                entity.getNickname(),
                entity.getGender(),
                entity.getBirthdate(),
                entity.getProfileUrl(),
                entity.getPictureUrl(),
                entity.getWebsiteUrl(),
                entity.getZoneinfo(),
                entity.getLocale(),
                entity.getPhoneNumber(),
                entity.isPhoneNumberVerified(),
                entity.getStreetAddress(),
                entity.getLocality(),
                entity.getRegion(),
                entity.getPostalCode(),
                entity.getCountry(),
                entity.isEnabled(),
                entity.isAccountNonExpired(),
                entity.isAccountNonLocked(),
                entity.isCredentialsNonExpired(),
                entity.getFailedLoginAttempts(),
                entity.getLastLoginAt(),
                entity.getPasswordChangedAt(),
                entity.isDeleted(),
                entity.getDeletedAt(),
                entity.getDeletedBy(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedAt(),
                entity.getUpdatedBy(),
                entity.getVersion(),
                roles,
                permissions
        );
    }

    default UserRole toDomain(UserRoleJpaEntity entity) {
        return UserRole.reconstitute(
                entity.getId(),
                new RoleId(entity.getRole().getUuid()),
                entity.getAssignedAt(),
                entity.getAssignedBy(),
                entity.getExpiresAt(),
                entity.getRevokedAt(),
                entity.getRevokedBy(),
                entity.getReason()
        );
    }

    default UserPermission toDomain(UserPermissionJpaEntity entity) {
        return UserPermission.reconstitute(
                entity.getId(),
                new PermissionId(entity.getPermission().getUuid()),
                entity.getEffect(),
                entity.getAssignedAt(),
                entity.getAssignedBy(),
                entity.getExpiresAt(),
                entity.getRevokedAt(),
                entity.getRevokedBy(),
                entity.getReason()
        );
    }

    default UserRoleJpaEntity toJpaEntity(
            UserRole userRole,
            UserJpaEntity user,
            RoleJpaEntity role
    ) {
        UserRoleJpaEntity entity = new UserRoleJpaEntity();

        entity.setId(userRole.getId());
        entity.setUser(user);
        entity.setRole(role);
        entity.setAssignedAt(userRole.getAssignedAt());
        entity.setAssignedBy(userRole.getAssignedBy());
        entity.setExpiresAt(userRole.getExpiresAt());
        entity.setRevokedAt(userRole.getRevokedAt());
        entity.setRevokedBy(userRole.getRevokedBy());
        entity.setReason(userRole.getReason());

        return entity;
    }

    default UserPermissionJpaEntity toJpaEntity(
            UserPermission userPermission,
            UserJpaEntity user,
            PermissionJpaEntity permission
    ) {
        UserPermissionJpaEntity entity = new UserPermissionJpaEntity();

        entity.setId(userPermission.getId());
        entity.setUser(user);
        entity.setPermission(permission);
        entity.setEffect(userPermission.getEffect());
        entity.setAssignedAt(userPermission.getAssignedAt());
        entity.setAssignedBy(userPermission.getAssignedBy());
        entity.setExpiresAt(userPermission.getExpiresAt());
        entity.setRevokedAt(userPermission.getRevokedAt());
        entity.setRevokedBy(userPermission.getRevokedBy());
        entity.setReason(userPermission.getReason());

        return entity;
    }

    // =========================================================
    // ROLE
    // =========================================================

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", expression = "java(role.getId().value())")
    @Mapping(target = "version", ignore = true)
    RoleJpaEntity toJpaEntity(Role role);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateJpaEntity(
            Role role,
            @MappingTarget RoleJpaEntity entity
    );

    default Role toDomain(
            RoleJpaEntity entity,
            List<RolePermission> permissions
    ) {
        return Role.reconstitute(
                new RoleId(entity.getUuid()),
                entity.getName(),
                entity.getDescription(),
                entity.isSystem(),
                entity.isActive(),
                entity.isDeleted(),
                entity.getDeletedAt(),
                entity.getDeletedBy(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedAt(),
                entity.getUpdatedBy(),
                entity.getVersion() == null ? 0L : entity.getVersion(),
                permissions
        );
    }

    default RolePermission toDomain(RolePermissionJpaEntity entity) {
        return RolePermission.reconstitute(
                entity.getId(),
                new PermissionId(entity.getPermission().getUuid()),
                entity.getAssignedAt(),
                entity.getAssignedBy(),
                entity.getExpiresAt(),
                entity.getRevokedAt(),
                entity.getRevokedBy(),
                entity.getReason()
        );
    }

    default RolePermissionJpaEntity toJpaEntity(
            RolePermission rolePermission,
            RoleJpaEntity role,
            PermissionJpaEntity permission
    ) {
        RolePermissionJpaEntity entity = new RolePermissionJpaEntity();

        entity.setId(rolePermission.getId());
        entity.setRole(role);
        entity.setPermission(permission);
        entity.setAssignedAt(rolePermission.getAssignedAt());
        entity.setAssignedBy(rolePermission.getAssignedBy());
        entity.setExpiresAt(rolePermission.getExpiresAt());
        entity.setRevokedAt(rolePermission.getRevokedAt());
        entity.setRevokedBy(rolePermission.getRevokedBy());
        entity.setReason(rolePermission.getReason());

        return entity;
    }


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", expression = "java(permission.getId().value())")
    @Mapping(target = "version", ignore = true)
    PermissionJpaEntity toJpaEntity(Permission permission);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateJpaEntity(
            Permission permission,
            @MappingTarget PermissionJpaEntity entity
    );

    default Permission toDomain(PermissionJpaEntity entity) {
        return Permission.reconstitute(
                new PermissionId(entity.getUuid()),
                entity.getName(),
                entity.getResource(),
                entity.getAction(),
                entity.getDescription(),
                entity.isSystem(),
                entity.isActive(),
                entity.isDeleted(),
                entity.getDeletedAt(),
                entity.getDeletedBy(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedAt(),
                entity.getUpdatedBy(),
                entity.getVersion() == null ? 0L : entity.getVersion()
        );
    }




    default IdentityJpaEntity toJpaEntity(
            Identity identity,
            Long userDatabaseId
    ) {
        IdentityJpaEntity entity = new IdentityJpaEntity();

        entity.setUuid(identity.getId().value());
        entity.setUserId(userDatabaseId);
        entity.setProvider(identity.getProvider());
        entity.setProviderSubject(identity.getProviderSubject());
        entity.setEmail(identity.getEmail());
        entity.setEmailVerified(identity.isEmailVerified());
        entity.setDisplayName(identity.getDisplayName());
        entity.setUsername(identity.getUsername());
        entity.setPictureUrl(identity.getPictureUrl());
        entity.setAttributes(identity.getAttributes());
        entity.setLinkedAt(identity.getLinkedAt());
        entity.setLastLoginAt(identity.getLastLoginAt());
        entity.setDeleted(identity.isDeleted());
        entity.setDeletedAt(identity.getDeletedAt());
        entity.setDeletedBy(identity.getDeletedBy());
        entity.setCreatedAt(identity.getCreatedAt());
        entity.setCreatedBy(identity.getCreatedBy());
        entity.setUpdatedAt(identity.getUpdatedAt());
        entity.setUpdatedBy(identity.getUpdatedBy());

        return entity;
    }

    default void updateJpaEntity(
            Identity identity,
            Long userDatabaseId,
            @MappingTarget IdentityJpaEntity entity
    ) {
        entity.setUserId(userDatabaseId);
        entity.setProvider(identity.getProvider());
        entity.setProviderSubject(identity.getProviderSubject());
        entity.setEmail(identity.getEmail());
        entity.setEmailVerified(identity.isEmailVerified());
        entity.setDisplayName(identity.getDisplayName());
        entity.setUsername(identity.getUsername());
        entity.setPictureUrl(identity.getPictureUrl());
        entity.setAttributes(identity.getAttributes());
        entity.setLinkedAt(identity.getLinkedAt());
        entity.setLastLoginAt(identity.getLastLoginAt());
        entity.setDeleted(identity.isDeleted());
        entity.setDeletedAt(identity.getDeletedAt());
        entity.setDeletedBy(identity.getDeletedBy());
        entity.setUpdatedAt(identity.getUpdatedAt());
        entity.setUpdatedBy(identity.getUpdatedBy());
    }

    default Identity toDomain(
            IdentityJpaEntity entity,
            UserId userId
    ) {
        return Identity.reconstitute(
                new IdentityId(entity.getUuid()),
                userId,
                entity.getProvider(),
                entity.getProviderSubject(),
                entity.getEmail(),
                entity.isEmailVerified(),
                entity.getDisplayName(),
                entity.getUsername(),
                entity.getPictureUrl(),
                entity.getAttributes(),
                entity.getLinkedAt(),
                entity.getLastLoginAt(),
                entity.isDeleted(),
                entity.getDeletedAt(),
                entity.getDeletedBy(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedAt(),
                entity.getUpdatedBy(),
                entity.getVersion() == null ? 0L : entity.getVersion()
        );
    }

}

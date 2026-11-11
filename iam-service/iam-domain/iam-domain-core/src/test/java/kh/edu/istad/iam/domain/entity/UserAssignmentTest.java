package kh.edu.istad.iam.domain.entity;

import kh.edu.istad.iam.domain.valueobject.PermissionEffect;
import kh.edu.istad.iam.domain.valueobject.PermissionId;
import kh.edu.istad.iam.domain.valueobject.RoleId;
import kh.edu.istad.iam.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAssignmentTest {

    @Test
    void normalizesEmailAndKeepsRevokedRoleAsAuditHistory() {
        User user = new User(UserId.generate(), "sokdara", "DARA@ISTAD.CO",
                "bcrypt-hash", "Dara", "Sok", "km", "test");
        RoleId roleId = RoleId.generate();

        user.assignRole(roleId, "admin-sub", null, "Initial assignment");
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().getFirst().isActive());

        user.revokeRole(roleId, "admin-sub", "Role no longer required");

        assertEquals("dara@istad.co", user.getEmail());
        assertEquals(1, user.getRoles().size(), "Revocation must retain the assignment row");
        assertFalse(user.getRoles().getFirst().isActive());
        assertNotNull(user.getRoles().getFirst().getRevokedAt());
    }

    @Test
    void directGrantAndDenyAreStoredAsSeparateEffects() {
        User user = new User(UserId.generate(), "sokdara", "dara@istad.co",
                "bcrypt-hash", "Dara", "Sok", "km", "test");
        PermissionId permissionId = PermissionId.generate();

        user.grantPermission(permissionId, "admin-sub", null, "Allow report read");
        user.denyPermission(permissionId, "admin-sub", null, "Explicit deny");

        assertEquals(2, user.getPermissions().size());
        assertTrue(user.getPermissions().stream().anyMatch(p -> p.getEffect() == PermissionEffect.GRANT));
        assertTrue(user.getPermissions().stream().anyMatch(p -> p.getEffect() == PermissionEffect.DENY));
    }

    @Test
    void softDeleteRetainsUserAndSetsDeletionAudit() {
        User user = new User(UserId.generate(), "sokdara", "dara@istad.co",
                "bcrypt-hash", "Dara", "Sok", "km", "test");

        user.softDelete("admin-sub");

        assertTrue(user.isDeleted());
        assertNotNull(user.getDeletedAt());
        assertEquals("admin-sub", user.getDeletedBy());
    }
}

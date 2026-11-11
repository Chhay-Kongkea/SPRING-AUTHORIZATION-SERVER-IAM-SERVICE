package kh.edu.istad.iam.domain.result;

import kh.edu.istad.iam.domain.valueobject.RoleId;

public record RoleSummaryResult(
        RoleId id,
        String name,
        String description,
        boolean system,
        boolean active
) {
}
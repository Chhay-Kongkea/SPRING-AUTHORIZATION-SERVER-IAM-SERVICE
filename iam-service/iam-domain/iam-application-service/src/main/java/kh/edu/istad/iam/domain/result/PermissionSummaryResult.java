package kh.edu.istad.iam.domain.result;

import kh.edu.istad.iam.domain.valueobject.PermissionId;

public record PermissionSummaryResult(
        PermissionId id,
        String name,
        String resource,
        String action,
        boolean active
) {
}
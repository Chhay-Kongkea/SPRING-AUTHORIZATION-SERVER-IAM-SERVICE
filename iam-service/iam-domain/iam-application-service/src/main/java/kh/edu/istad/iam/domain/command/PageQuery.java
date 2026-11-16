package kh.edu.istad.iam.domain.command;

public record PageQuery(
        int page,
        int size,
        String sortBy,
        SortDirection direction
) {

    public PageQuery {
        if (page < 0) {
            throw new IllegalArgumentException("Page cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Size must be between 1 and 100");
        }

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "createdAt";
        }

        if (direction == null) {
            direction = SortDirection.DESC;
        }
    }

    public enum SortDirection {
        ASC,
        DESC
    }
}
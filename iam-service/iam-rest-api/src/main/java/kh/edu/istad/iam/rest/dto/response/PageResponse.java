package kh.edu.istad.iam.rest.dto.response;

import kh.edu.istad.iam.domain.result.PageResult;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(List<T> content, PageInfo page) {

    public record PageInfo(
            int number,
            int size,
            long totalElements,
            int totalPages) {
    }

    public static <S, T> PageResponse<T> from(PageResult<S> result, Function<S, T> mapper) {
        return new PageResponse<>(
                result.content().stream().map(mapper).toList(),
                new PageInfo(result.page(), result.size(),
                             result.totalElements(), result.totalPages()));
    }
}

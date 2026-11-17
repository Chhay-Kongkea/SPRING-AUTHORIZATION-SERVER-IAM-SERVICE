package kh.edu.istad.iam.rest.exception;

import jakarta.validation.ConstraintViolationException;
import kh.edu.istad.iam.domain.exception.ConflictDomainException;
import kh.edu.istad.iam.domain.exception.InvalidCredentialsDomainException;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);
    private static final String BASE = "https://iam.istad.co/problems/";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validation(MethodArgumentNotValidException exception) {
        ProblemDetail problem = problem(HttpStatus.BAD_REQUEST, "Validation failed",
                "Request validation failed", "validation-error");
        problem.setProperty("errors", exception.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of("field", error.getField(),
                        "message", error.getDefaultMessage() == null ? "Invalid value" : error.getDefaultMessage()))
                .toList());
        return problem;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ProblemDetail constraintViolation(ConstraintViolationException exception) {
        ProblemDetail problem = problem(HttpStatus.BAD_REQUEST, "Validation failed",
                "Request validation failed", "validation-error");
        List<Map<String, String>> errors = exception.getConstraintViolations().stream()
                .map(v -> Map.of("field", v.getPropertyPath().toString(),
                        "message", v.getMessage()))
                .toList();
        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    ProblemDetail unreadable(Exception exception) {
        return problem(HttpStatus.BAD_REQUEST, "Malformed request",
                "The request body or a path/query parameter is malformed",
                "malformed-request");
    }

    @ExceptionHandler(NotFoundDomainException.class)
    ProblemDetail notFound(NotFoundDomainException exception) {
        return problem(HttpStatus.NOT_FOUND, "Resource not found", exception.getMessage(), "not-found");
    }

    @ExceptionHandler(ConflictDomainException.class)
    ProblemDetail conflict(ConflictDomainException exception) {
        log.warn("Domain conflict: {}", exception.getMessage());

        return problem(
                HttpStatus.CONFLICT,
                "Conflict",
                exception.getMessage(),
                "conflict"
        );
    }

    @ExceptionHandler(InvalidCredentialsDomainException.class)
    ProblemDetail invalidCredentials(InvalidCredentialsDomainException exception) {
        return problem(HttpStatus.BAD_REQUEST, "Invalid credentials",
                exception.getMessage(), "invalid-credentials");
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    ProblemDetail concurrentModification(OptimisticLockingFailureException exception) {
        return problem(HttpStatus.CONFLICT, "Concurrent modification",
                "The resource was modified by someone else. Reload it and try again.",
                "concurrent-modification");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail dataIntegrity(DataIntegrityViolationException exception) {
        log.error("Database integrity violation", exception);

        return problem(
                HttpStatus.CONFLICT,
                "Conflict",
                "The operation violates a uniqueness or integrity rule",
                "conflict"
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    ProblemDetail invalidState(IllegalStateException exception) {
        return problem(HttpStatus.CONFLICT,
                "Operation cannot be completed",
                exception.getMessage(),
                "invalid-state");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail badRequest(IllegalArgumentException exception) {
        return problem(HttpStatus.BAD_REQUEST,
                "Bad request", exception.getMessage(),
                "bad-request");
    }

    @ExceptionHandler(AccessDeniedException.class)
    ProblemDetail forbidden(AccessDeniedException exception) {
        return problem(HttpStatus.FORBIDDEN,
                "Forbidden",
                "You do not have the required authority",
                "forbidden");
    }

    @ExceptionHandler(AuthenticationException.class)
    ProblemDetail unauthorized(AuthenticationException exception) {
        return problem(HttpStatus.UNAUTHORIZED,
                "Unauthorized",
                "A valid Bearer access token is required",
                "unauthorized");
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail unexpected(Exception exception) {
        log.error("Unhandled API error", exception);
        return problem(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                "An unexpected error occurred",
                "internal-error");
    }

    private static ProblemDetail problem(HttpStatus status, String title, String detail, String type) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(URI.create(BASE + type));
        return problem;
    }
}

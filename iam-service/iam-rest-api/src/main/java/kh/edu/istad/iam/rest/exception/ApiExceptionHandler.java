package kh.edu.istad.iam.rest.exception;

import kh.edu.istad.iam.domain.exception.ConflictDomainException;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validation(MethodArgumentNotValidException exception) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Request validation failed");
        problem.setType(URI.create("https://iam.istad.co/problems/validation-error"));
        problem.setTitle("Validation failed");
        problem.setProperty("errors", exception.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of("field", error.getField(), "message",
                        error.getDefaultMessage() == null ? "Invalid value" : error.getDefaultMessage())).toList());

        return problem;
    }

    @ExceptionHandler(ConflictDomainException.class)
    ProblemDetail conflict(RuntimeException exception) {
        return problem(HttpStatus.CONFLICT, "Conflict", exception.getMessage(), "conflict");
    }

    @ExceptionHandler(NotFoundDomainException.class)
    ProblemDetail notFound(RuntimeException exception) {
        return problem(HttpStatus.NOT_FOUND, "Resource not found",
                exception.getMessage(), "not-found");
    }

    @ExceptionHandler(IllegalStateException.class)
    ProblemDetail invalidState(RuntimeException exception) {
        return problem(HttpStatus.CONFLICT, "Operation cannot be completed",
                exception.getMessage(), "invalid-state");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail badRequest(RuntimeException exception) {
        return problem(HttpStatus.BAD_REQUEST, "Bad request",
                exception.getMessage(), "bad-request");
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail, String type) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(URI.create("https://iam.istad.co/problems/" + type));
        return problem;
    }
}

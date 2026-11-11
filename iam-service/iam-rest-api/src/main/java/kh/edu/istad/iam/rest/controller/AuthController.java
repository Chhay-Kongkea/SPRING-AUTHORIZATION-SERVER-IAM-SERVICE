package kh.edu.istad.iam.rest.controller;

import kh.edu.istad.iam.domain.command.RegisterUserCommand;
import kh.edu.istad.iam.domain.result.UserResult;
import kh.edu.istad.iam.domain.usecase.RegisterUserUseCase;
import kh.edu.istad.iam.rest.dto.RegisterUserRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserUseCase registerUser;

    public AuthController(RegisterUserUseCase registerUser) {
        this.registerUser = registerUser;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> register(
            @Valid @RequestBody RegisterUserRequest request
    ) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Password confirmation does not match");
        }

        UserResult result = registerUser.execute(new RegisterUserCommand(
                request.username(),
                request.email(),
                request.password(),
                request.givenName(),
                request.familyName(),
                request.locale()
        ));

        return Map.of(
                "uuid", result.id().value(),
                "username", result.username(),
                "email", result.email(),
                "emailVerified", result.emailVerified(),
                "name", result.givenName() + " " + result.familyName(),
                "enabled", result.enabled(),
                "createdAt", result.createdAt()
        );
    }
}
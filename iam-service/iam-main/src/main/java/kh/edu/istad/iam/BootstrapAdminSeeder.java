package kh.edu.istad.iam;

import kh.edu.istad.iam.domain.command.AssignRolesToUserCommand;
import kh.edu.istad.iam.domain.command.RegisterUserCommand;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.usecase.AssignRolesToUserUseCase;
import kh.edu.istad.iam.domain.usecase.RegisterUserUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BootstrapAdminSeeder implements ApplicationRunner {
    private final String username;
    private final String email;
    private final String password;
    private final RegisterUserUseCase registerUser;
    private final AssignRolesToUserUseCase assignRoles;
    private final RoleRepository roles;
    private final UserRepository users;

    public BootstrapAdminSeeder(
            @Value("${iam.bootstrap-admin.username:}") String username,
            @Value("${iam.bootstrap-admin.email:}") String email,
            @Value("${iam.bootstrap-admin.password:}") String password,
            RegisterUserUseCase registerUser,
            AssignRolesToUserUseCase assignRoles,
            RoleRepository roles,
            UserRepository users) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.registerUser = registerUser;
        this.assignRoles = assignRoles;
        this.roles = roles;
        this.users = users;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) return;
        if (users.findByUsername(username).isPresent()) return;

        var user = registerUser.execute(new RegisterUserCommand(
                username, email, password, "IAM", "Administrator", "en"));
        var adminRole = roles.findByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException("ADMIN role was not seeded"));
        assignRoles.execute(new AssignRolesToUserCommand(
                user.id().value(), List.of(adminRole.getId().value()),
                null, "Initial environment-provisioned administrator"));
    }
}

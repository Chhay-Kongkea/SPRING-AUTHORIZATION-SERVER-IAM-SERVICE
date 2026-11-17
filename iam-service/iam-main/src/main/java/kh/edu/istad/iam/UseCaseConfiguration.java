package kh.edu.istad.iam;

import kh.edu.istad.iam.domain.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kh.edu.istad.iam.domain.port.out.*;

@Configuration
public class UseCaseConfiguration {

    @Bean
    ActivatePermissionUseCase activatePermissionUseCase(PermissionRepository permissionRepository,
                                                        CurrentActorProvider currentActorProvider) {
        return new ActivatePermissionUseCase(permissionRepository, currentActorProvider);
    }

    @Bean
    ActivateRoleUseCase activateRoleUseCase(RoleRepository roleRepository,
                                            CurrentActorProvider currentActorProvider) {
        return new ActivateRoleUseCase(roleRepository, currentActorProvider);
    }

    @Bean
    AssignPermissionsToRoleUseCase assignPermissionsToRoleUseCase(RoleRepository roleRepository,
                                                                  PermissionRepository permissionRepository,
                                                                  CurrentActorProvider currentActorProvider) {
        return new AssignPermissionsToRoleUseCase(roleRepository,
                permissionRepository, currentActorProvider);
    }

    @Bean
    AssignRolesToUserUseCase assignRolesToUserUseCase(UserRepository userRepository,
                                                      RoleRepository roleRepository,
                                                      CurrentActorProvider currentActorProvider) {
        return new AssignRolesToUserUseCase(userRepository, roleRepository,
                currentActorProvider);
    }

    @Bean
    ChangePasswordUseCase changePasswordUseCase(UserRepository userRepository,
                                                PasswordHasher passwordHasher,
                                                CurrentActorProvider currentActorProvider) {
        return new ChangePasswordUseCase(userRepository, passwordHasher, currentActorProvider);
    }

    @Bean
    CreatePermissionUseCase createPermissionUseCase(PermissionRepository permissionRepository,
                                                    CurrentActorProvider currentActorProvider) {
        return new CreatePermissionUseCase(permissionRepository, currentActorProvider);
    }

    @Bean
    CreateRoleUseCase createRoleUseCase(RoleRepository roleRepository,
                                        PermissionRepository permissionRepository,
                                        CurrentActorProvider currentActorProvider) {
        return new CreateRoleUseCase(roleRepository, permissionRepository, currentActorProvider);
    }

    @Bean
    CreateUserUseCase createUserUseCase(UserRepository userRepository,
                                        PasswordHasher passwordHasher,
                                        CurrentActorProvider currentActorProvider,
                                        RoleRepository roleRepository) {
        return new CreateUserUseCase(userRepository, passwordHasher, currentActorProvider, roleRepository);
    }

    @Bean
    DeactivatePermissionUseCase deactivatePermissionUseCase(PermissionRepository permissionRepository,
                                                            CurrentActorProvider currentActorProvider) {
        return new DeactivatePermissionUseCase(permissionRepository, currentActorProvider);
    }

    @Bean
    DeactivateRoleUseCase deactivateRoleUseCase(RoleRepository roleRepository,
                                                CurrentActorProvider currentActorProvider) {
        return new DeactivateRoleUseCase(roleRepository, currentActorProvider);
    }

    @Bean
    DeletePermissionUseCase deletePermissionUseCase(PermissionRepository permissionRepository,
                                                    CurrentActorProvider currentActorProvider) {
        return new DeletePermissionUseCase(permissionRepository, currentActorProvider);
    }

    @Bean
    DeleteRoleUseCase deleteRoleUseCase(RoleRepository roleRepository,
                                        CurrentActorProvider currentActorProvider) {
        return new DeleteRoleUseCase(roleRepository, currentActorProvider);
    }

    @Bean
    DeleteUserUseCase deleteUserUseCase(UserRepository userRepository,
                                        CurrentActorProvider currentActorProvider) {
        return new DeleteUserUseCase(userRepository, currentActorProvider);
    }

    @Bean
    DisableUserUseCase disableUserUseCase(UserRepository userRepository,
                                          CurrentActorProvider currentActorProvider) {
        return new DisableUserUseCase(userRepository, currentActorProvider);
    }

    @Bean
    EnableUserUseCase enableUserUseCase(UserRepository userRepository,
                                        CurrentActorProvider currentActorProvider) {
        return new EnableUserUseCase(userRepository, currentActorProvider);
    }

    @Bean
    FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase(UserRepository userRepository,
                                                                            IdentityRepository identityRepository,
                                                                            CurrentActorProvider currentActorProvider,
                                                                            RoleRepository roleRepository) {
        return new FindOrProvisionFederatedUserUseCase(userRepository, identityRepository,
                currentActorProvider, roleRepository);
    }

    @Bean
    GetIdentityUseCase getIdentityUseCase(IdentityRepository identityRepository) {
        return new GetIdentityUseCase(identityRepository);
    }

    @Bean
    GetPermissionUseCase getPermissionUseCase(PermissionRepository permissionRepository) {
        return new GetPermissionUseCase(permissionRepository);
    }

    @Bean
    GetRoleUseCase getRoleUseCase(RoleRepository roleRepository,
                                  PermissionRepository permissionRepository) {
        return new GetRoleUseCase(roleRepository, permissionRepository);
    }

    @Bean
    GetUserAuthoritiesUseCase getUserAuthoritiesUseCase(UserRepository userRepository,
                                                        RoleRepository roleRepository,
                                                        PermissionRepository permissionRepository) {
        return new GetUserAuthoritiesUseCase(userRepository, roleRepository, permissionRepository);
    }

    @Bean
    GetUserIdentitiesUseCase getUserIdentitiesUseCase(UserRepository userRepository,
                                                      IdentityRepository identityRepository) {
        return new GetUserIdentitiesUseCase(userRepository, identityRepository);
    }

    @Bean
    GetUserPermissionsUseCase getUserPermissionsUseCase(UserRepository userRepository,
                                                        PermissionRepository permissionRepository) {
        return new GetUserPermissionsUseCase(userRepository, permissionRepository);
    }

    @Bean
    GetUserRolesUseCase getUserRolesUseCase(UserRepository userRepository,
                                            RoleRepository roleRepository) {
        return new GetUserRolesUseCase(userRepository, roleRepository);
    }

    @Bean
    GetUserUseCase getUserUseCase(UserRepository userRepository) {
        return new GetUserUseCase(userRepository);
    }

    @Bean
    GrantPermissionsToUserUseCase grantPermissionsToUserUseCase(UserRepository userRepository,
                                                                PermissionRepository permissionRepository,
                                                                CurrentActorProvider currentActorProvider) {
        return new GrantPermissionsToUserUseCase(userRepository, permissionRepository, currentActorProvider);
    }

    @Bean
    LinkIdentityUseCase linkIdentityUseCase(UserRepository userRepository,
                                            IdentityRepository identityRepository,
                                            CurrentActorProvider currentActorProvider) {
        return new LinkIdentityUseCase(userRepository, identityRepository, currentActorProvider);
    }

    @Bean
    ListConnectedApplicationsUseCase listConnectedApplicationsUseCase(ConnectedApplicationsRepository repository) {
        return new ListConnectedApplicationsUseCase(repository);
    }

    @Bean
    ListIdentitiesUseCase listIdentitiesUseCase(IdentityRepository identityRepository) {
        return new ListIdentitiesUseCase(identityRepository);
    }

    @Bean
    ListPermissionsUseCase listPermissionsUseCase(PermissionRepository permissionRepository) {
        return new ListPermissionsUseCase(permissionRepository);
    }

    @Bean
    ListRolesUseCase listRolesUseCase(RoleRepository roleRepository) {
        return new ListRolesUseCase(roleRepository);
    }

    @Bean
    ListUsersUseCase listUsersUseCase(UserRepository userRepository) {
        return new ListUsersUseCase(userRepository);
    }

    @Bean
    LoadUserForAuthenticationUseCase loadUserForAuthenticationUseCase(UserRepository userRepository,
                                                                      GetUserAuthoritiesUseCase getUserAuthoritiesUseCase) {
        return new LoadUserForAuthenticationUseCase(userRepository, getUserAuthoritiesUseCase);
    }

    @Bean
    LockUserUseCase lockUserUseCase(UserRepository userRepository,
                                    CurrentActorProvider currentActorProvider) {
        return new LockUserUseCase(userRepository, currentActorProvider);
    }

    @Bean
    RecordIdentityLoginUseCase recordIdentityLoginUseCase(IdentityRepository identityRepository,
                                                          CurrentActorProvider currentActorProvider) {
        return new RecordIdentityLoginUseCase(identityRepository, currentActorProvider);
    }

    @Bean
    RecordLoginAttemptUseCase recordLoginAttemptUseCase(UserRepository userRepository,
                                                        CurrentActorProvider currentActorProvider) {
        return new RecordLoginAttemptUseCase(userRepository, currentActorProvider);
    }

    @Bean
    RegisterUserUseCase registerUserUseCase(UserRepository userRepository,
                                            PasswordHasher passwordHasher,
                                            CurrentActorProvider currentActorProvider,
                                            RoleRepository roleRepository) {
        return new RegisterUserUseCase(userRepository, passwordHasher, currentActorProvider, roleRepository);
    }

    @Bean
    ResetUserPasswordUseCase resetUserPasswordUseCase(UserRepository userRepository,
                                                      PasswordHasher passwordHasher,
                                                      CurrentActorProvider currentActorProvider) {
        return new ResetUserPasswordUseCase(userRepository, passwordHasher, currentActorProvider);
    }

    @Bean
    RevokeConnectedApplicationUseCase revokeConnectedApplicationUseCase(ConnectedApplicationsRepository repository) {
        return new RevokeConnectedApplicationUseCase(repository);
    }

    @Bean
    RevokePermissionFromRoleUseCase revokePermissionFromRoleUseCase(RoleRepository roleRepository,
                                                                    CurrentActorProvider currentActorProvider) {
        return new RevokePermissionFromRoleUseCase(roleRepository, currentActorProvider);
    }

    @Bean
    RevokePermissionFromUserUseCase revokePermissionFromUserUseCase(UserRepository userRepository,
                                                                    CurrentActorProvider currentActorProvider) {
        return new RevokePermissionFromUserUseCase(userRepository, currentActorProvider);
    }

    @Bean
    RevokeRoleFromUserUseCase revokeRoleFromUserUseCase(UserRepository userRepository,
                                                        CurrentActorProvider currentActorProvider) {
        return new RevokeRoleFromUserUseCase(userRepository, currentActorProvider);
    }

    @Bean
    UnlinkIdentityUseCase unlinkIdentityUseCase(UserRepository userRepository,
                                                IdentityRepository identityRepository,
                                                CurrentActorProvider currentActorProvider) {
        return new UnlinkIdentityUseCase(userRepository, identityRepository, currentActorProvider);
    }

    @Bean
    UnlockUserUseCase unlockUserUseCase(UserRepository userRepository,
                                        CurrentActorProvider currentActorProvider) {
        return new UnlockUserUseCase(userRepository, currentActorProvider);
    }

    @Bean
    UpdateIdentityProfileUseCase updateIdentityProfileUseCase(IdentityRepository identityRepository,
                                                              CurrentActorProvider currentActorProvider) {
        return new UpdateIdentityProfileUseCase(identityRepository, currentActorProvider);
    }

    @Bean
    UpdatePermissionUseCase updatePermissionUseCase(PermissionRepository permissionRepository,
                                                    CurrentActorProvider currentActorProvider) {
        return new UpdatePermissionUseCase(permissionRepository, currentActorProvider);
    }

    @Bean
    UpdateRoleUseCase updateRoleUseCase(RoleRepository roleRepository,
                                        CurrentActorProvider currentActorProvider) {
        return new UpdateRoleUseCase(roleRepository, currentActorProvider);
    }

    @Bean
    UpdateUserProfileUseCase updateUserProfileUseCase(UserRepository userRepository,
                                                      CurrentActorProvider currentActorProvider) {
        return new UpdateUserProfileUseCase(userRepository, currentActorProvider);
    }

    @Bean
    GetClientDisplayNameUseCase getClientDisplayNameUseCase(ClientDirectory clientDirectory) {
        return new GetClientDisplayNameUseCase(clientDirectory);
    }
}

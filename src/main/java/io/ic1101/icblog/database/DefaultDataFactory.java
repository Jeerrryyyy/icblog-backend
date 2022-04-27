package io.ic1101.icblog.database;

import io.ic1101.icblog.api.user.UserService;
import io.ic1101.icblog.database.entity.RoleEntity;
import io.ic1101.icblog.database.entity.UserEntity;
import io.ic1101.icblog.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@AllArgsConstructor
public class DefaultDataFactory {

    private final UserService userService;

    @PostConstruct
    public void postConstruct() {
        boolean rolesShouldBeCreated = userService.getRoles().isEmpty();

        if (rolesShouldBeCreated) {
            this.createDefaultRoles();
        }

        boolean userShouldBeCreated = userService.getUsers().isEmpty();

        if (userShouldBeCreated) {
            this.createDefaultUser();
        }
    }

    private void createDefaultRoles() {
        // "ROLE_SUPER_ADMINISTRATOR", "ROLE_ADMINISTRATOR", "ROLE_DEVELOPER", "ROLE_USER"

        userService.saveRole(new RoleEntity(null, "ROLE_SUPER_ADMINISTRATOR"));
        userService.saveRole(new RoleEntity(null, "ROLE_ADMINISTRATOR"));
        userService.saveRole(new RoleEntity(null, "ROLE_DEVELOPER"));
        userService.saveRole(new RoleEntity(null, "ROLE_USER"));

        log.info("Created default roles [ROLE_SUPER_ADMINISTRATOR, ROLE_ADMINISTRATOR, ROLE_DEVELOPER, ROLE_USER]");
    }

    private void createDefaultUser() {
        String email = "admin@example.com";
        String randomPassword = StringUtils.generateRandomPassword(30);

        UserEntity userEntity = new UserEntity(null, "Max", "Mustermann", email, randomPassword, null);
        userService.saveUser(userEntity);

        userService.addRoleToUser(email, "ROLE_SUPER_ADMINISTRATOR");

        log.info("########################################################################");
        log.info("                                                                        ");
        log.info("Created new default user with super administrator permissions!");
        log.info("                                                                        ");
        log.info("    Email: " + email);
        log.info("    Password: " + randomPassword);
        log.info("                                                                        ");
        log.info("Please save these credentials! You will never see them again!");
        log.info("                                                                        ");
        log.info("########################################################################");
    }
}

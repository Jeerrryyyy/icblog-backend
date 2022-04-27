package io.ic1101.icblog.api.user;

import io.ic1101.icblog.api.user.dto.CreateUserDto;
import io.ic1101.icblog.api.user.dto.RoleToUserDto;
import io.ic1101.icblog.api.user.exception.custom.EmailAlreadyExistsException;
import io.ic1101.icblog.api.user.exception.custom.UserOrRoleNotFoundException;
import io.ic1101.icblog.database.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ROLE_SUPER_ADMINISTRATOR", "ROLE_ADMINISTRATOR", "ROLE_DEVELOPER"})
    public ResponseEntity<List<UserEntity>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> createUser(@RequestBody CreateUserDto createUserDto) throws EmailAlreadyExistsException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/v1/user/create").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(createUserDto));
    }

    @PostMapping("/addRole")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ROLE_SUPER_ADMINISTRATOR")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserDto roleToUserDto) throws UserOrRoleNotFoundException {
        userService.addRoleToUser(roleToUserDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/removeRole")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ROLE_SUPER_ADMINISTRATOR")
    public ResponseEntity<?> removeRoleFromUser(@RequestBody RoleToUserDto roleToUserDto) throws UserOrRoleNotFoundException {
        userService.removeRoleFromUser(roleToUserDto);
        return ResponseEntity.ok().build();
    }
}

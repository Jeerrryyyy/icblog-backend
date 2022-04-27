package io.ic1101.icblog.api.user;

import io.ic1101.icblog.api.user.dto.CreateUserDto;
import io.ic1101.icblog.api.user.dto.RoleToUserDto;
import io.ic1101.icblog.api.user.exception.custom.EmailAlreadyExistsException;
import io.ic1101.icblog.api.user.exception.custom.UserOrRoleNotFoundException;
import io.ic1101.icblog.database.entity.RoleEntity;
import io.ic1101.icblog.database.entity.UserEntity;
import io.ic1101.icblog.database.repository.RoleRepository;
import io.ic1101.icblog.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = this.getUser(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException("We could not find a user matching to this email!");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userEntity.getRoles().forEach(roleEntity -> authorities.add(new SimpleGrantedAuthority(roleEntity.getName())));

        return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
    }

    public UserEntity getUser(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    public UserEntity saveUser(CreateUserDto createUserDto) throws EmailAlreadyExistsException {
        if (this.getUser(createUserDto.getEmail()) != null) {
            throw new EmailAlreadyExistsException("User with email \"" + createUserDto.getEmail() + "\" already exists!");
        }

        UserEntity userEntity = new UserEntity(
                null,
                createUserDto.getFirstName(),
                createUserDto.getLastName(),
                createUserDto.getEmail(),
                createUserDto.getPassword(),
                new ArrayList<>()
        );

        return this.saveUser(userEntity);
    }

    public UserEntity saveUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    public void addRoleToUser(RoleToUserDto roleToUserDto) throws UserOrRoleNotFoundException {
        this.addRoleToUser(roleToUserDto.getEmail(), roleToUserDto.getRoleName());
    }

    public void removeRoleFromUser(RoleToUserDto roleToUserDto) throws UserOrRoleNotFoundException {
        this.removeRoleFromUser(roleToUserDto.getEmail(), roleToUserDto.getRoleName());
    }

    public void addRoleToUser(String email, String roleName) throws UserOrRoleNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        RoleEntity roleEntity = roleRepository.findByName(roleName);

        if (userEntity == null || roleEntity == null) {
            throw new UserOrRoleNotFoundException();
        }

        userEntity.getRoles().add(roleEntity);
    }

    public void removeRoleFromUser(String email, String roleName) throws UserOrRoleNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        RoleEntity roleEntity = roleRepository.findByName(roleName);

        if (userEntity == null || roleEntity == null) {
            throw new UserOrRoleNotFoundException();
        }

        userEntity.getRoles().remove(roleEntity);
    }

    public RoleEntity saveRole(RoleEntity roleEntity) {
        return roleRepository.save(roleEntity);
    }

    public List<RoleEntity> getRoles() {
        return roleRepository.findAll();
    }

    public long getUserCount() {
        return userRepository.count();
    }

    public long getRoleCount() {
        return roleRepository.count();
    }
}

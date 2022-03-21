package io.ic1101.icblog.api.service;

import io.ic1101.icblog.database.entity.UserEntity;
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
}

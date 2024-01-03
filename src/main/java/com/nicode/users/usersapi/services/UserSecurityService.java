package com.nicode.users.usersapi.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nicode.users.usersapi.models.entities.UserCredentials;
import com.nicode.users.usersapi.repositories.UserCredentialsRepository;

@Service
public class UserSecurityService implements UserDetailsService {

    private final UserCredentialsRepository credentialsRepository;

    @Autowired
    public UserSecurityService(UserCredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredentials> user = Optional.of(this.credentialsRepository.findByUsernameLike(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found")));

        String role = user.get().getRole();

        UserDetails userDet = null;
        userDet = User.builder()
        .username(user.get().getUsername())
        .password(user.get().getPassword())
        .authorities(new SimpleGrantedAuthority("ROLE_"+role))
        .accountLocked(user.get().getLocked())
        .disabled(user.get().getDisabled())
        .build();

        return userDet;
    }

}

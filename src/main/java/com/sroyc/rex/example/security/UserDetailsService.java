package com.sroyc.rex.example.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sroyc.rex.example.common.UserRole;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

@Service
public class UserDetailsService implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {

    private final Map<String, UserDetails> users = new ConcurrentHashMap<>();

    @Autowired
    private SystemCredential credentials;

    @Autowired
    private PasswordEncoder encoder;

    @PostConstruct
    public void init() {
        UserDetails user = User.builder().username(this.credentials.getUsername())
                .password(encoder.encode(this.credentials.getPassword()))
                .authorities(UserRole.ADMIN.value()).build();
        this.users.put(getKey(user.getUsername()), user);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return this.findSystemUser(username);
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        // Simple implementation that just returns the user as we are using in-memory
        // map initialized from properties
        return Mono.just(user);
    }

    private Mono<UserDetails> findSystemUser(String username) {
        String key = getKey(username);
        UserDetails result = this.users.get(key);
        return (result != null) ? Mono.just(User.withUserDetails(result).build()) : Mono.empty();
    }

    private String getKey(String username) {
        return username.toLowerCase();
    }
}

package com.sroyc.rex.example.bootstrap;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sroyc.rex.example.entity.User;
import com.sroyc.rex.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserSeeder implements DataSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<Boolean> seed() {
        log.info("Checking if user seeding is required...");
        return userRepository.count()
                .filter(count -> count == 0)
                .doOnNext(count -> log.info("Table is empty , executing user seeding. Do not stop server...."))
                .flatMapMany(count -> generateUsers(1000).buffer(10))
                .concatMap(users -> userRepository.saveAll(users).then(Mono.just(true)))
                .count()
                .doOnNext(count -> log.info("User seeding complete."))
                .thenReturn(true)
                .doOnError(e -> log.error("User seeding failed", e))
                .onErrorResume(e -> Mono.just(false))
                .switchIfEmpty(Mono.defer(() -> Mono.just(false)));
    }

    private Flux<User> generateUsers(int count) {
        return Flux.range(1, count)
                .map(i -> {
                    String uid = UUID.randomUUID().toString().substring(0, 8);
                    return User.builder()
                            .firstname("First" + uid)
                            .lastname("Last" + uid)
                            .email("user" + uid + "@example.com")
                            .password(passwordEncoder.encode("password"))
                            .isActive(true)
                            .build();
                });
    }

}

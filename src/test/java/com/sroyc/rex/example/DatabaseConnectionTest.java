package com.sroyc.rex.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;
import com.sroyc.rex.example.repository.UserRepository;
import com.sroyc.rex.example.repository.ProductRepository;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testR2dbcCanAccessLiquibaseTables() {
        // Users
        userRepository.count()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        // Products
        productRepository.count()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
}

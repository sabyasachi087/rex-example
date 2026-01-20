package com.sroyc.rex.example.bootstrap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.sroyc.rex.example.common.Category;
import com.sroyc.rex.example.entity.Product;
import com.sroyc.rex.example.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductSeeder implements DataSeeder {

    private final ProductRepository productRepository;
    private final Random random = new Random();

    @Override
    public Mono<Boolean> seed() {
        log.info("Checking if product seeding is required...");
        return productRepository.count()
                .filter(count -> count == 0)
                .doOnNext(
                        count -> log.info("Products table is empty, executing product seeding. Do not stop server...."))
                .flatMapMany(count -> generateProducts(10000).buffer(100))
                .concatMap(products -> productRepository.saveAll(products).then(Mono.just(true)))
                .count()
                .doOnNext(count -> log.info("Product seeding complete. Batches processed: {}", count))
                .thenReturn(true)
                .doOnError(e -> log.error("Product seeding failed", e))
                .onErrorResume(e -> Mono.just(false))
                .switchIfEmpty(Mono.defer(() -> Mono.just(false)));
    }

    private Flux<Product> generateProducts(int count) {
        return Flux.range(1, count)
                .map(i -> {
                    String uid = UUID.randomUUID().toString().substring(0, 8);
                    Category category = Category.values()[random.nextInt(Category.values().length)];
                    return Product.builder()
                            .code("PROD-" + uid)
                            .name("Product " + uid)
                            .description("Description for product " + uid)
                            .category(category)
                            .quantity(random.nextInt(100) + 1)
                            .price(BigDecimal.valueOf(random.nextDouble() * 1000).setScale(2, RoundingMode.HALF_UP))
                            .build();
                });
    }

}

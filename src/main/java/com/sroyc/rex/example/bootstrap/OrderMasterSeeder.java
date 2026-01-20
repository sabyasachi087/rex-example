package com.sroyc.rex.example.bootstrap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.sroyc.rex.example.common.OrderStatus;
import com.sroyc.rex.example.entity.OrderCartItem;
import com.sroyc.rex.example.entity.OrderMaster;
import com.sroyc.rex.example.entity.Product;
import com.sroyc.rex.example.entity.User;
import com.sroyc.rex.example.repository.OrderCartItemRepository;
import com.sroyc.rex.example.repository.OrderMasterRepository;
import com.sroyc.rex.example.repository.ProductRepository;
import com.sroyc.rex.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMasterSeeder implements DataSeeder {

    private final OrderMasterRepository orderMasterRepository;
    private final OrderCartItemRepository orderCartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final Random random = new Random();

    @Override
    public Mono<Boolean> seed() {
        log.info("Checking if order seeding is required...");
        Mono<Tuple2<List<Product>, List<User>>> zip = Mono.zip(productRepository.findAll().collectList(),
                userRepository.findAll().collectList());
        return orderMasterRepository.count()
                .filter(count -> count == 0)
                .doOnNext(c -> log.info("Orders table is empty, seeding orders..."))
                .flatMap(c -> zip)
                .filter(tup -> !tup.getT1().isEmpty() && !tup.getT2().isEmpty())
                .flatMapMany(tup -> Flux.range(1, 1000)
                        .flatMap(i -> createOrder(tup.getT1(), tup.getT2())))
                .count()
                .doOnNext(count -> log.info("Seeded {} orders", count))
                .thenReturn(true)
                .doOnError(e -> log.error("Order seeding failed", e))
                .onErrorResume(e -> Mono.just(false))
                .switchIfEmpty(Mono.defer(() -> Mono.just(false)));
    }

    private Mono<OrderMaster> createOrder(List<Product> products, List<User> users) {
        // Pick random status
        OrderStatus status = random.nextBoolean() ? OrderStatus.CART : OrderStatus.COMPLETED;

        // Pick random products for this order
        int numItems = random.nextInt(5) + 1;
        List<Product> selectedProducts = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            selectedProducts.add(products.get(random.nextInt(products.size())));
        }

        // Prepare items and calculate total
        BigDecimal total = BigDecimal.ZERO;
        List<OrderCartItem> items = new ArrayList<>();

        for (Product p : selectedProducts) {
            int qty = random.nextInt(3) + 1;
            BigDecimal price = p.getPrice();
            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(qty));
            total = total.add(itemTotal);

            items.add(OrderCartItem.builder()
                    .productId(p.getId())
                    .price(price)
                    .quantity(qty)
                    .createdOn(LocalDateTime.now())
                    .build());
        }

        // Create Order
        OrderMaster order = OrderMaster.builder()
                .userId(users.get(random.nextInt(users.size())).getUserId())
                .status(status)
                .totalPrice(total)
                .build();

        // Save Order first to get ID, then save items
        return orderMasterRepository.save(order)
                .flatMap(savedOrder -> {
                    items.forEach(item -> item.setOrderId(savedOrder.getOrderId()));
                    return orderCartItemRepository.saveAll(items)
                            .then(Mono.just(savedOrder));
                });
    }

}

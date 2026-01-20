package com.sroyc.rex.example.bootstrap;

import reactor.core.publisher.Mono;

public interface DataSeeder {

    Mono<Boolean> seed();

}

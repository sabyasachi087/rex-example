package com.sroyc.rex.example.bootstrap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import reactor.core.scheduler.Schedulers;

@Component
public class MasterDataSeeder implements InitializingBean {

    private UserSeeder userSeeder;
    private ProductSeeder productSeeder;
    private OrderMasterSeeder orderMasterSeeder;

    public MasterDataSeeder(UserSeeder userSeeder, ProductSeeder productSeeder, OrderMasterSeeder orderMasterSeeder) {
        this.userSeeder = userSeeder;
        this.productSeeder = productSeeder;
        this.orderMasterSeeder = orderMasterSeeder;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.userSeeder.seed().then(this.productSeeder.seed()).then(this.orderMasterSeeder.seed())
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

}

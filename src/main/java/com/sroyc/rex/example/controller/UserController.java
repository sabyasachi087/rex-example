package com.sroyc.rex.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sroyc.rex.example.entity.User;
import com.sroyc.rex.example.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/stream")
    public Flux<User> getAllUsers(@RequestParam(value = "limit", required = false) int limit) {
        if (limit > 0) {
            return userRepository.findAll().take(limit);
        }
        return userRepository.findAll();
    }

    @GetMapping("/email/{email}")
    public Mono<User> getUserByEmail(@PathVariable("email") String email) {
        return userRepository.findByEmail(email);
    }

    @GetMapping("/id/{id}")
    public Mono<User> getUserById(@PathVariable("id") Long id) {
        return userRepository.findById(id);
    }

}

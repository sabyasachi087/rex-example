package com.sroyc.rex.example.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class SystemCredential {

    @Value("${system.username:rex-exm}")
    private String username;

    @Value("${system.password:rex-exm}")
    private String password;

}

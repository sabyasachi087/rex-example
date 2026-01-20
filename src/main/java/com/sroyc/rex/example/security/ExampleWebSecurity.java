package com.sroyc.rex.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.sroyc.rex.example.common.UserRole;

@Configuration
@EnableWebFluxSecurity
public class ExampleWebSecurity {

    private static String[] swaggerPaths = { "/swagger-ui.html", "/swagger-ui/**", "/webjars/swagger-ui/**",
            "/v3/api-docs/**" };
    private static String[] actuatorPaths = { "/actuator/info/**", "/actuator/health/**", "/encrypt", "/decrypt" };

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(Customizer.withDefaults())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .authenticationEntryPoint((exchange, ex) -> {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(swaggerPaths).permitAll().pathMatchers(actuatorPaths)
                        .permitAll()
                        .pathMatchers("/*.css", "/*.js", "/assets/**").permitAll()
                        .pathMatchers("/user/**").hasAnyAuthority(UserRole.ADMIN.value())
                        .anyExchange().authenticated())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

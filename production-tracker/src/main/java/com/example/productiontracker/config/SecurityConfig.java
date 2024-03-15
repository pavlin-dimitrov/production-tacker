package com.example.productiontracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF
                .authorizeRequests(auth -> auth
                        .requestMatchers("/auth/register", "/login", "/public/**").permitAll() // Permit all for these paths
                        .anyRequest().authenticated() // All other requests need authentication
                )
                .formLogin(withDefaults())
                .logout(logout -> {
                    logout
                            .logoutSuccessUrl("/login?logout") // Custom logout redirect
                            .permitAll(); // Permit all for logout
                    // Additional logout configurations if needed
                });

        return http.build();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .permitAll())
//                .authorizeRequests(authorize -> {
//                    authorize
//                            .requestMatchers("/public/**").permitAll()
//                            .requestMatchers("/auth/register").permitAll()
//                            .requestMatchers("/orders/**").permitAll()
//                            .requestMatchers("/orders/{orderId}/items/**").permitAll()
//                            .requestMatchers("/progress/**").permitAll()
//                            .anyRequest().authenticated();
//                })
//                .logout(logout -> logout
//                        .permitAll()
//                        .logoutSuccessUrl("/login?logout"))
//                .sessionManagement(session -> session
//                        .invalidSessionUrl("/login?invalid")
//                        .maximumSessions(1)
//                        .expiredUrl("/login?expired"));
//        return http.build();
//    }
}
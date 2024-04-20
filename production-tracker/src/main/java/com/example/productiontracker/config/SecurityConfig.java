package com.example.productiontracker.config;

import com.example.productiontracker.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/register", "/login", "/public/**").permitAll()
                    .requestMatchers(
                            "/createOrder",
                            "/saveOrder",
                            "/addItems",
                            "/finishOrder",
                            "/editOrder/**",
                            "/updateOrder/**",
                            "/editItems/**",
                            "/updateItems/**",
                            "/deleteItem/**",
                            "/deleteOrder/**").hasAuthority(Role.ADMIN.authority())
                    .requestMatchers(
                            "/edit-progress/**",
                            "/update-saveProgress/**",
                            "/getProgressInfo",
                            "/filter",
                            "/report",
                            "/export/excel").hasAnyAuthority(Role.ADMIN.authority(), Role.USER.authority())
                    .anyRequest().authenticated())
            .formLogin(withDefaults())
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .addLogoutHandler(new SecurityContextLogoutHandler())  // Clears the security context
                    .addLogoutHandler(new LogoutHandler() {
                        @Override
                        public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                            HttpSession session = request.getSession(false);
                            if (session != null) {
                                session.removeAttribute("SPRING_SECURITY_SAVED_REQUEST");
                            }
                        }
                    })
                    .permitAll())
            .requestCache(requestCache -> requestCache
                    .requestCache(customRequestCache()));

    return http.build();
}

    @Bean
    public RequestCache customRequestCache() {
        HttpSessionRequestCache cache = new HttpSessionRequestCache();
        cache.setRequestMatcher(new AntPathRequestMatcher("/non-cached-path/**", "GET")); // Customize to match paths you don't want to cache
        return cache;
    }
}

package com.g12.tpo.server.controllers.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import com.g12.tpo.server.entity.Role;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req -> req
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/error/**").permitAll()
                .requestMatchers("/categories/**").authenticated()
                .requestMatchers("/carts/**").hasAuthority(Role.USER.name())
                .anyRequest()
                .authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider) // Keep custom provider
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // DaoAuthenticationProvider configurando UserDetailsService para evadir warn
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoderDAP());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoderDAP() {
        return new BCryptPasswordEncoder();
    }
}

// package com.g12.tpo.server.controllers.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// // import org.springframework.security.web.authentication.logout.LogoutHandler;
// // import org.springframework.security.core.context.SecurityContextHolder;

// import com.g12.tpo.server.entity.Role;

// import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

// import lombok.RequiredArgsConstructor;

// @Configuration
// @EnableWebSecurity
// @RequiredArgsConstructor
// public class SecurityConfig {

//         private final JwtAuthenticationFilter jwtAuthFilter;
//         private final AuthenticationProvider authenticationProvider;

//         @Bean
//         public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//                 http
//                                 .csrf(AbstractHttpConfigurer::disable)
//                                 .authorizeHttpRequests(req -> req.requestMatchers("/api/v1/auth/**").permitAll()
//                                                 .requestMatchers("/error/**").permitAll()
//                                                 .requestMatchers("/categories/**").hasAnyAuthority(Role.USER.name())
//                                                 .anyRequest()
//                                                 .authenticated())
//                                 .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
//                                 .authenticationProvider(authenticationProvider)
//                                 .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

//                 return http.build();
//         }
// }
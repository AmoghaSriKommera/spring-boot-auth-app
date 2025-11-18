package com.example.springauth;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    public SecurityConfig(CustomUserDetailsService uds) { this.userDetailsService = uds; }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .csrf().disable() // for demo; enable CSRF for production
          .authorizeHttpRequests(authorize -> authorize
              .requestMatchers("/register", "/css/**", "/js/**", "/", "/h2-console/**").permitAll()
              .anyRequest().authenticated()
          )
          .authenticationProvider(authProvider())
          .formLogin(form -> form
              .loginPage("/login")           // your login page endpoint
              .loginProcessingUrl("/login")  // POST action processed by Spring Security
              .defaultSuccessUrl("/home", true)
              .permitAll()
          )
          .logout(logout -> logout
              .logoutUrl("/logout")
              .logoutSuccessUrl("/login")
              .permitAll()
          );

        // Allow H2 console iframe (if using H2)
        http.headers().frameOptions().disable();

        return http.build();
    }
}

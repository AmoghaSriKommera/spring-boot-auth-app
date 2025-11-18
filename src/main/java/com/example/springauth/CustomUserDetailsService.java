package com.example.springauth;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repo;
    public CustomUserDetailsService(UserRepository repo) { this.repo = repo; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // no roles used here; provide a simple USER role
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(), // password is stored hashed (BCrypt)
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}

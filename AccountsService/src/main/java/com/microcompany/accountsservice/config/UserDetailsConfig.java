package com.microcompany.accountsservice.config;

import com.microcompany.accountsservice.model.ERole;
import com.microcompany.accountsservice.model.User;
import com.microcompany.accountsservice.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
public class UserDetailsConfig {

    @Autowired
    private UserRepository userRepo;
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {

            BCryptPasswordEncoder enc = new BCryptPasswordEncoder();

            List<User> users = List.of(
                    new User(1, "cajero1@mail.com",enc.encode("my_pass"), ERole.CAJERO),
                    new User(2, "cajero2@mail.com",enc.encode("my_pass"), ERole.CAJERO),
                    new User(3, "cajero3@mail.com",enc.encode("my_pass"), ERole.CAJERO),
                    new User(4, "director@mail.com",enc.encode("my_pass"), ERole.DIRECTOR)
            );

            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return users.stream()
                        .filter(u-> u.getEmail().equals(email))
                        .findFirst()
                        .orElseThrow(()->new UsernameNotFoundException("Usuario con email: "+email+" no encontrado"));
            }

        };
    }

}

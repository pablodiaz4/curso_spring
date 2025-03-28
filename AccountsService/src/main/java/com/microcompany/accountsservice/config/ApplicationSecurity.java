package com.microcompany.accountsservice.config;

import com.microcompany.accountsservice.jwt.JwtTokenFilter;
import com.microcompany.accountsservice.model.ERole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;


@Configuration
@EnableMethodSecurity(securedEnabled = false)
public class ApplicationSecurity {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationSecurity.class);


    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private UserDetailsService userDetailsService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
//        logger.info("Entra authenticationManager!!!!");
        return authConfig.getAuthenticationManager();
    }

    @Bean
    @Profile({"dev", "prod"}) // para diferenciar de test
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .authorizeHttpRequests((requests) -> requests
                                .antMatchers("/auth/login",
                                        "/docs/**",
                                        "/users",
                                        "/h2-ui/**",
                                        "/configuration/ui",
                                        "/swagger-resources/**",
                                        "/configuration/security",
                                        "/swagger-ui.html",
                                        "/webjars/**"
                                ).permitAll() // HABILITAR ESPACIOS LIBRES
                                .antMatchers(HttpMethod.GET, "/account/**").hasAnyAuthority(ERole.DIRECTOR.name(), ERole.CAJERO.name())//Para acceder a cuentas debe ser USER
                                .antMatchers("/account/**").hasAnyAuthority(ERole.DIRECTOR.name()) //DIRECTOR puede hacer de todo
                                .anyRequest().authenticated()
                );

        http.headers(headers ->
                headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin())
        );

        http.exceptionHandling((exception) -> exception.authenticationEntryPoint(
                (request, response, ex) -> {
                    response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            ex.getMessage()
                    );
                }
        ));

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
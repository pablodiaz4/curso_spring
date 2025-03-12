package com.banana.config;

import com.banana.persistence.StudentsRepository;
import com.banana.persistence.StudentsRepositoryInf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("Classpath:application.properties")
public class ReposConfig {
    @Bean
    public StudentsRepositoryInf getStudentRepositoryBean(){

        return new StudentsRepository();
    }
}

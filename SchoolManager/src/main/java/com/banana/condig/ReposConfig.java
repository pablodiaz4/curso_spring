package com.banana.condig;

import com.banana.persistence.StudentRepositoryProd;
import com.banana.persistence.StudentsRepository;
import com.banana.persistence.StudentsRepositoryInf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ReposConfig {

    @Bean
    @Profile("default")
    public StudentsRepositoryInf getStudentRepoBean(){
        System.out.println("Creando StudentsRepository dedault...");
        return new StudentsRepository();
    }

    @Bean
    @Profile("prod")
    public StudentsRepositoryInf getStudentRepoProdBean(){
        System.out.println("Creando StudentsRepository prod...");
        return new StudentRepositoryProd();
    }

}

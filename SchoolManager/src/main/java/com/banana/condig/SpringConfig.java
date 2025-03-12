package com.banana.condig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"com.banana.persistence","com.banana.services"})
@Import({ReposConfig.class, ServicesConfig.class})
public class SpringConfig {

}

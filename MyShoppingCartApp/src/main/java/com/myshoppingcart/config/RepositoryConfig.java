package com.myshoppingcart.config;

import com.myshoppingcart.persistence.CompraDBRepository;
import com.myshoppingcart.persistence.ICompraRepository;
import com.myshoppingcart.persistence.IProductoRepository;
import com.myshoppingcart.persistence.IUsuarioRepository;
import com.myshoppingcart.persistence.ProductoDBRepository;
import com.myshoppingcart.persistence.UsuarioDBRepository;
import com.myshoppingcart.persistence.UsuarioInMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config.properties")
public class RepositoryConfig {
    @Bean
    public ICompraRepository getCompraDBRepositoryBean(){
        return new CompraDBRepository();
    }

    @Bean
    public IProductoRepository getProductoDBRepositoryBean(){
        return new ProductoDBRepository();
    }

    @Bean
    @Profile("PROD")
    public IUsuarioRepository getUsuarioDBRepositoryBean(){
        System.out.println("Creando IUsuarioRepository para entorno de PROD...");
        return new UsuarioDBRepository();
    }

    @Bean
    @Profile("DEV")
    public IUsuarioRepository getUsuarioInMemoryRepositoryBean(){
        System.out.println("Creando IUsuarioRepository para entorno de DEV...");
        return new UsuarioInMemoryRepository();
    }
}

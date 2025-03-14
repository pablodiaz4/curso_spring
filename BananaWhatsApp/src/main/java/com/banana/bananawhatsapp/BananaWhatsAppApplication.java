package com.banana.bananawhatsapp;

import com.banana.bananawhatsapp.config.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BananaWhatsAppApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SpringConfig.class);
        context.refresh();
    }
}

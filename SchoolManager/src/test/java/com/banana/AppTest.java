package com.banana;

import com.banana.condig.SpringConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@ActiveProfiles({"prod"})
public class AppTest {

    @Autowired
    ApplicationContext context;

    @Value("${url_conn}")
    private String urlConn;

    @Autowired
    Environment env;

    @Test
    public void shouldAnswerWithTrue() {
        assertNotNull(context);
        System.out.println("urlConn:" + urlConn);
        System.out.println("urlConn env:" + env.getProperty("url_conn", String.class));
    }

}

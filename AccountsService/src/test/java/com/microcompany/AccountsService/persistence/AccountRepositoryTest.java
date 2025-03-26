package com.microcompany.AccountsService.persistence;


import com.microcompany.accountsservice.enums.AccountAction;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.persistence.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource( locations = "classpath:application-integrationtest.properties")
public class AccountRepositoryTest {

    @Mock
    private EntityManager em;

    @Mock
    private AccountRepository repository;

    @Test
    public void operateTest(){
        Account account = Account.builder().id(1L).type("PERSONAL").balance(10000.00).openingDate(new Date()).ownerId(1L).build();
        TypedQuery query = Mockito.mock(TypedQuery.class);

        Mockito.when(em.createQuery("", Account.class)).thenReturn(query);

        Assertions.assertNotNull(repository.operate(account, 100D, AccountAction.INGRESAR));
    }
}

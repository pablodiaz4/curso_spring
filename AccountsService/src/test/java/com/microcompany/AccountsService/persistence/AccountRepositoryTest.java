package com.microcompany.AccountsService.persistence;


import com.microcompany.accountsservice.enums.AccountAction;
import com.microcompany.accountsservice.exception.AccountNotBalanceException;
import com.microcompany.accountsservice.exception.AccountNotfoundException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.persistence.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Sql(value = "classpath:testing.sql")
@ComponentScan(basePackages = {"com.microcompany.accountsservice"})
public class AccountRepositoryTest {

    @Mock
    private EntityManager em;

    @Autowired
    private AccountRepository repository;

    @Test
    public void operateAddIdAccountIsValidBalanceIsValidActionIsValid(){
        Account accountRequest = Account.builder().id(1L).type("PERSONAL").balance(10000.00).openingDate(new Date()).ownerId(1L).build();
        Account accountResponse = new Account(1L, "PERSONAL", new Date(), 11000.00, 1L);
        Mockito.when(em.find(any(), anyLong())).thenReturn(accountResponse);

        Assertions.assertNotNull(repository.operate(accountRequest, 1000D, AccountAction.INGRESAR));
    }

    @Test
    void operateRetireIdAccountIsValidBalanceIsValidActionIsValid() {
        Account accountRequest = new Account(1L, "PERSONAL", new Date(), 2500.00, 1L);
        Account accountResponse = new Account(1L, "PERSONAL", new Date(), 1500.00, 1L);

        Mockito.when(em.find(any(), anyLong())).thenReturn(accountResponse);

        Assertions.assertNotNull(repository.operate(accountRequest, 1000D, AccountAction.RETIRAR));
    }

    @Test
    void OperateAddIdAccountNullBalanceIsValidActionIsValid() {
        Throwable exception = Assertions.assertThrows(AccountNotfoundException.class,
                ()->{repository.operate(null, 1000D, AccountAction.INGRESAR);} );

        Assertions.assertEquals("Account not found", exception.getMessage());
    }

    @Test
    void OperateRetireIdAccountNullBalanceIsValidActionIsValid() {
        Throwable exception = Assertions.assertThrows(AccountNotfoundException.class,
                ()->{repository.operate(null, 1000D, AccountAction.RETIRAR);} );

        Assertions.assertEquals("Account not found", exception.getMessage());
    }

    @Test
    void OperateRetireIdAccountIsValidlBalanceIsNotValidActionIsValid() {
        Account accountRequest = new Account(1L, "PERSONAL", new Date(), 2500.00, 1L);
        Account account1 = new Account(1L, "PERSONAL", new Date(), 2500.00, 1L);
        Account account2 = new Account(2L, "PERSONAL", new Date(), 1500.00, 1L);
        List<Account> accounts = new ArrayList<>();
        TypedQuery query = Mockito.mock(TypedQuery.class);
        accounts.add(account1);
        accounts.add(account2);

        Mockito.when(em.createQuery(anyString(), any())).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(accounts);

        Throwable exception = Assertions.assertThrows(AccountNotBalanceException.class,
                ()->{repository.operate(accountRequest, 10000D, AccountAction.RETIRAR);} );

        Assertions.assertEquals("Customer with id: " + accountRequest.getOwnerId() + " not balance abalaible", exception.getMessage());
    }
}

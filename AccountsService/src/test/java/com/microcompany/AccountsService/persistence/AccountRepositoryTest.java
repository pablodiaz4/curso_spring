package com.microcompany.AccountsService.persistence;


import com.microcompany.accountsservice.enums.AccountAction;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.persistence.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Sql(value = "classpath:testing.sql")
@ComponentScan(basePackages = {"com.microcompany.accountsservice"})
public class AccountRepositoryTest {

    @Mock
    private EntityManager em;

    @MockBean
    private AccountRepository repository;

    @Test
    public void operateAddIdAccountIsValidBalanceIsValidActionIsValid(){
        String sql = "SELECT a FROM Account a WHERE a.ownerId=?1";
        Account account = Account.builder().id(1L).type("PERSONAL").balance(10000.00).openingDate(new Date()).ownerId(1L).build();
        List<Account> accounts = new ArrayList<>();
        TypedQuery query = Mockito.mock(TypedQuery.class);
        accounts.add(account);

        Mockito.when(em.createQuery(sql, Account.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(accounts);
        Mockito.when(em.find(Account.class, account.getId())).thenReturn(account);

        Assertions.assertNotNull(repository.operate(account, 1000D, AccountAction.INGRESAR));
    }

    @Test
    void operateRetireIdAccountIsValidBalanceIsValidActionIsValid() {
        String sql = "SELECT a FROM Account a WHERE a.ownerId=?1";
        Account account = new Account(1L, "PERSONAL", new Date(), 2500.00, 1L);
        List<Account> accounts = new ArrayList<>();
        TypedQuery query = Mockito.mock(TypedQuery.class);
        accounts.add(account);

        Mockito.when(em.createQuery(sql, Account.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(accounts);
        Mockito.when(em.find(Account.class, account.getId())).thenReturn(account);
        Assertions.assertNotNull(repository.operate(account, 1000D, AccountAction.RETIRAR));
    }

    @Test
    void OperateAddIdAccountNullBalanceIsValidActionIsValid() {
        String sql = "SELECT a FROM Account a WHERE a.ownerId=?1";
        Account account = new Account(null, "PERSONAL", new Date(), 2500.00, 1L);
        List<Account> accounts = new ArrayList<>();
        TypedQuery query = Mockito.mock(TypedQuery.class);
        accounts.add(account);

        Mockito.when(em.createQuery(sql, Account.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(accounts);
        Mockito.when(em.find(Account.class, account.getId())).thenReturn(account);
        Assertions.assertNull(repository.operate(null, 1000D, AccountAction.INGRESAR));
    }

    @Test
    void OperateRetireIdAccountNullBalanceIsValidActionIsValid() {
        String sql = "SELECT a FROM Account a WHERE a.ownerId=?1";
        Account account = new Account(null, "PERSONAL", new Date(), 2500.00, 1L);
        List<Account> accounts = new ArrayList<>();
        TypedQuery query = Mockito.mock(TypedQuery.class);
        accounts.add(account);

        Mockito.when(em.createQuery(sql, Account.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(accounts);
        Mockito.when(em.find(Account.class, account.getId())).thenReturn(account);
        Assertions.assertNull(repository.operate(null, 1000D, AccountAction.RETIRAR));
    }

    @Test
    void OperateRetireIdAccountIsValidlBalanceIsNotValidActionIsValid() {
        String sql = "SELECT a FROM Account a WHERE a.ownerId=?1";
        Account account = new Account(1L, "PERSONAL", new Date(), 2500.00, 1L);
        List<Account> accounts = new ArrayList<>();
        TypedQuery query = Mockito.mock(TypedQuery.class);
        accounts.add(account);

        Mockito.when(em.createQuery(sql, Account.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(accounts);
        Mockito.when(em.find(Account.class, account.getId())).thenReturn(account);
        Assertions.assertNull(repository.operate(account, 10000D, AccountAction.RETIRAR));
    }
}

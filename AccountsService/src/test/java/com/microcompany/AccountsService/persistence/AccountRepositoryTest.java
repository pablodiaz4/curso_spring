package com.microcompany.AccountsService.persistence;


import com.microcompany.accountsservice.enums.AccountAction;
import com.microcompany.accountsservice.exception.AccountNotBalanceException;
import com.microcompany.accountsservice.exception.AccountNotfoundException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.persistence.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Date;

@DataJpaTest()
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Sql(value = "classpath:testing.sql")
@ComponentScan(basePackages = {"com.microcompany.accountsservice"})
public class AccountRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private AccountRepository repository;

    @Test
    @Transactional
    public void operateAddIdAccountIsValidBalanceIsValidActionIsValid(){
        repository.findByOwnerId(1L).forEach(account -> {em.remove(account);});

        em.flush();

        Account accountRequest = Account.builder().type("PERSONAL").balance(2500D).openingDate(new Date()).ownerId(1L).build();
        em.persist(accountRequest);

        em.flush();

        Assertions.assertNotNull(repository.operate(accountRequest, 1000D, AccountAction.INGRESAR));
    }

    @Test
    @Transactional
    void operateRetireIdAccountIsValidBalanceIsValidActionIsValid() {
        repository.findByOwnerId(1L).forEach(account -> {em.remove(account);});

        em.flush();

        Account accountRequest = Account.builder().type("PERSONAL").balance(2500D).openingDate(new Date()).ownerId(1L).build();
        em.persist(accountRequest);

        em.flush();

        Assertions.assertNotNull(repository.operate(accountRequest, 1000D, AccountAction.RETIRAR));
    }

    @Test
    @Transactional
    void operateRetireIdAccountIsValidBalanceIsValidForMultipleAccountsActionIsValid() {
        repository.findByOwnerId(1L).forEach(account -> {em.remove(account);});

        em.flush();

        Account account1 = Account.builder().type("PERSONAL").balance(2500D).openingDate(new Date()).ownerId(1L).build();
        em.persist(account1);

        Account account2 = Account.builder().type("PERSONAL").balance(1500D).openingDate(new Date()).ownerId(1L).build();
        em.persist(account2);

        Account account3 = Account.builder().type("PERSONAL").balance(1000D).openingDate(new Date()).ownerId(1L).build();
        em.persist(account3);

        em.flush();

        Account accountRequest = repository.findById(account1.getId()).orElse(Account.builder().id(1L).type("PERSONAL").balance(10000.00).openingDate(new Date()).ownerId(1L).build());

        Assertions.assertNotNull(repository.operate(accountRequest, 5000D, AccountAction.RETIRAR));
    }

    @Test
    @Transactional
    void OperateAddIdAccountNullBalanceIsValidActionIsValid() {
        Throwable exception = Assertions.assertThrows(AccountNotfoundException.class,
                ()->{repository.operate(null, 1000D, AccountAction.INGRESAR);} );

        Assertions.assertEquals("Account not found", exception.getMessage());
    }

    @Test
    @Transactional
    void OperateRetireIdAccountNullBalanceIsValidActionIsValid() {
        Throwable exception = Assertions.assertThrows(AccountNotfoundException.class,
                ()->{repository.operate(null, 1000D, AccountAction.RETIRAR);} );

        Assertions.assertEquals("Account not found", exception.getMessage());
    }

    @Test
    @Transactional
    void OperateRetireIdAccountIsValidlBalanceIsNotValidActionIsValid() {
        repository.findByOwnerId(1L).forEach(account -> {em.remove(account);});

        em.flush();

        Account account1 = Account.builder().type("PERSONAL").balance(2500D).openingDate(new Date()).ownerId(1L).build();
        em.persist(account1);

        Account account2 = Account.builder().type("PERSONAL").balance(1500D).openingDate(new Date()).ownerId(1L).build();
        em.persist(account2);

        Account account3 = Account.builder().type("PERSONAL").balance(1000D).openingDate(new Date()).ownerId(1L).build();
        em.persist(account3);

        em.flush();

        Account accountRequest = repository.findById(account1.getId()).orElse(Account.builder().id(1L).type("PERSONAL").balance(10000.00).openingDate(new Date()).ownerId(1L).build());

        Throwable exception = Assertions.assertThrows(AccountNotBalanceException.class,
                ()->{repository.operate(accountRequest, 10000D, AccountAction.RETIRAR);} );

        Assertions.assertEquals("Customer with id: " + accountRequest.getOwnerId() + " not balance abalaible", exception.getMessage());
    }
}

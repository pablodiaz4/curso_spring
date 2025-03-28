package com.microcompany.AccountsService.service;

import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.persistence.AccountRepository;
import com.microcompany.accountsservice.services.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class AccountServiceTest {
    @TestConfiguration
    static class AccountServiceConf{
        @Bean
        public AccountService getAccountServiceBean(){
            return new AccountService();
        }
    }

    @Autowired
    AccountService accService;

    //    @Mock // más puro
    @MockBean
    AccountRepository accountRepositoryMock;

    @MockBean
    EntityManagerFactory emf;

    @Test
    void getAccountByOwnerIdIsValid() {
        Account account = new Account(1L, "cuenta1", new Date(), 2000d, 1l);
        List<Account> accounts =  new ArrayList<>();
        accounts.add(account);

        Mockito.when(accountRepositoryMock.findByOwnerId(1L)).thenReturn(accounts);

        List<Account> cuentas = accService.getAccountByOwnerId(1L);

        Assertions.assertNotNull(cuentas);
        Assertions.assertTrue(cuentas.size() > 0);
    }

    @Test
    void getAccountByOwnerIdIsNotResult() {
        List<Account> accounts =  new ArrayList<>();
        Mockito.when(accountRepositoryMock.findByOwnerId(1L)).thenReturn(accounts);

        List<Account> cuentas = accService.getAccountByOwnerId(99L);
        Assertions.assertNotNull(cuentas);
        Assertions.assertTrue(cuentas.size() == 0);
    }

    @Test
    void getAccountByOwnerIdIsNull() {
        List<Account> cuentas = accService.getAccountByOwnerId(null);
        Assertions.assertNotNull(cuentas);
        Assertions.assertTrue(cuentas.size() == 0);
    }
}

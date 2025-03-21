package com.microcompany.accountsservice.services;

import com.microcompany.accountsservice.enums.AccountAction;
import com.microcompany.accountsservice.exception.AccountNotBalanceException;
import com.microcompany.accountsservice.exception.AccountNotfoundException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.model.Customer;
import com.microcompany.accountsservice.persistence.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService implements IAccountService {
    private Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public Account create(Account account) {
        Date current_Date = new Date();
        account.setOpeningDate(current_Date);
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        return account;
    }

    @Override
    public List<Account> getAccountByOwnerId(Long ownerId) {
        return accountRepository.findByOwnerId(ownerId);
    }

    @Override
    @Transactional
    public Account updateAccount(Long id, Account account) {
        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        newAccount.setType(account.getType());
        return accountRepository.save(newAccount);
    }

    @Override
    @Transactional
    public Account addBalance(Long id, int amount, Long ownerId) {
        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        Customer owner = null;// Will be gotten from user service
        Double newBalance = newAccount.getBalance() + amount;
        newAccount.setBalance(newBalance);
        return accountRepository.save(newAccount);
    }

    @Override
    @Transactional
    public Account withdrawBalance(Long id, int amount, Long ownerId) {
        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        Customer owner = null; // Will be gotten from user service
        Double newBalance = newAccount.getBalance() - amount;
        newAccount.setBalance(newBalance);
        return accountRepository.save(newAccount);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        this.accountRepository.delete(account);
    }

    @Override
    @Transactional
    public void deleteAccountsUsingOwnerId(Long ownerId) {
        List<Account> accounts = accountRepository.findByOwnerId(ownerId);
        for (Account account : accounts) {
            this.accountRepository.delete(account);
        }
    }

    @Override
    @Transactional
    public Account operate(Account cuenta, Double cantidad, AccountAction accion) {
        if (cuenta != null){
            if (AccountAction.INGRESAR.equals(accion)){
                cuenta.setBalance(cuenta.getBalance()+cantidad);
            }
            else if (AccountAction.RETIRAR.equals(accion)){
                if (cuenta.getBalance() >= cantidad){
                    cuenta.setBalance(cuenta.getBalance()-cantidad);
                }
                else{
                    // Si no hay suficiente dinero en la cuenta calculo el debe del cliente
                    Double resto = cantidad - cuenta.getBalance();

                    Double totalCliente = getAccountByOwnerId(cuenta.getOwnerId()).stream().map(Account::getBalance).reduce(0D, Double::sum);

                    // Si el cliente no tiene dinero suficiente en otras cuentas devolvemos una excepción
                    if (totalCliente < cantidad){
                        throw new AccountNotBalanceException(cuenta.getOwnerId());
                    }
                    else{
                        final Long cuentaId = cuenta.getId();

                        // Obtenemos el resto de cuentas del cliente
                        List<Account> cuentas = getAccountByOwnerId(cuenta.getOwnerId()).stream().filter(a -> !a.getId().equals(cuentaId)).collect(Collectors.toList());

                        // Vamos retirando dinero de las otras cuentas hasta que el total a retirar sea 0
                        for (Account a: cuentas){
                            if (resto > 0){
                                if (resto > a.getBalance()){
                                    resto = resto - a.getBalance();
                                    a.setBalance(0D);
                                }
                                else{
                                    a.setBalance(a.getBalance()-resto);
                                    resto = 0D;
                                }

                                updateAccount(a.getId(), a);
                            }
                            else{
                                break;
                            }
                        }
                    }

                    cuenta.setBalance(0D);
                }
            }

            // Modifico la cuenta con la nueva cantidad disponible
            cuenta = updateAccount(cuenta.getId(), cuenta);

            return cuenta;
        }
        else{
            throw new AccountNotfoundException();
        }
    }
}

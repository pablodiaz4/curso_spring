package com.microcompany.accountsservice.persistence;

import com.microcompany.accountsservice.enums.AccountAction;
import com.microcompany.accountsservice.exception.AccountNotBalanceException;
import com.microcompany.accountsservice.exception.AccountNotfoundException;
import com.microcompany.accountsservice.model.Account;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

public class AccountRepositoryExtendImpl implements AccountRepositoryExtend {

    @PersistenceContext
    private EntityManager em;

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

                    String sql = "SELECT a FROM Account a WHERE a.ownerId=?1";
                    TypedQuery query = em.createQuery(sql, Account.class);
                    query.setParameter(1, cuenta.getOwnerId());

                    List<Account> cuentas = query.getResultList();
                    Double totalCliente = cuentas.stream().map(Account::getBalance).reduce(0D, Double::sum);

                    // Si el cliente no tiene dinero suficiente en otras cuentas devolvemos una excepción
                    if (totalCliente < cantidad){
                        throw new AccountNotBalanceException(cuenta.getOwnerId());
                    }
                    else{
                        final Long cuentaId = cuenta.getId();
                        // Obtenemos el resto de cuentas del cliente
                        cuentas = cuentas.stream().filter(a -> !a.getId().equals(cuentaId)).collect(Collectors.toList());

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

                                // Modifico la cuenta con la nueva cantidad disponible
                                Account account = em.find(Account.class, cuenta.getId());
                                account.setBalance(a.getBalance());

                                em.persist(account);
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
            Account account = em.find(Account.class, cuenta.getId());
            account.setBalance(cuenta.getBalance());

            em.persist(account);

            return em.find(Account.class, cuenta.getId());
        }
        else{
            throw new AccountNotfoundException();
        }
    }
}

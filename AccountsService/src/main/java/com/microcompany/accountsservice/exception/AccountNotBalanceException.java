package com.microcompany.accountsservice.exception;

public class AccountNotBalanceException extends GlobalException{

    protected static final long serialVersionUID = 3L;

    public AccountNotBalanceException() {
        super("Customer not balance abalaible");
    }

    public AccountNotBalanceException(Long customerId) {
        super("Customer with id: " + customerId + " not balance abalaible");
    }
}

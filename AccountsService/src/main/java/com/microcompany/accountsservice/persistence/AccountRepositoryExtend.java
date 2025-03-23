package com.microcompany.accountsservice.persistence;

import com.microcompany.accountsservice.enums.AccountAction;
import com.microcompany.accountsservice.model.Account;

public interface AccountRepositoryExtend {
    Account operate (Account cuenta, Double cantidad, AccountAction accion);
}

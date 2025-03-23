package com.microcompany.accountsservice.persistence;

import com.microcompany.accountsservice.enums.AccountAction;
import com.microcompany.accountsservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryExtend {
    List<Account> findByOwnerId(Long OwnerId);
}

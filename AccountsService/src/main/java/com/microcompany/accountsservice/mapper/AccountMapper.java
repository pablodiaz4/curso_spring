package com.microcompany.accountsservice.mapper;

import com.microcompany.accountsservice.dto.AccountDTO;
import com.microcompany.accountsservice.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "type", target = "tipo")
    @Mapping(source = "openingDate", target = "fechaApertura")
    @Mapping(source = "balance", target = "saldo")
    @Mapping(source = "ownerId", target = "clienteId")
    AccountDTO accountToAccountDTO(Account account);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "tipo", target = "type")
    @Mapping(source = "fechaApertura", target = "openingDate")
    @Mapping(source = "saldo", target = "balance")
    @Mapping(source = "clienteId", target = "ownerId")
    Account accountDTOToAccount(AccountDTO productDTO);

    public Collection<AccountDTO> accountsToAccountDTOs(Collection<Account> accounts);

    public Collection<Account> accountDTOsToAccounts(Collection<AccountDTO> accountDTOs);
}

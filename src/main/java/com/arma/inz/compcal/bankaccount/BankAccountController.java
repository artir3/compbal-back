package com.arma.inz.compcal.bankaccount;

import java.util.Collection;
import java.util.Set;

public interface BankAccountController {
    Set<BankAccount> saveOrUpdate(Collection<BankAccountDTO> bankAccountSet);

    BankAccount saveOrUpdate(BankAccountDTO dto);

    boolean delete(Long id);

    Set<BankAccountDTO> copyToDTO(Collection<BankAccount> bankAccountSet);

    BankAccountDTO copyToDTO(BankAccount account);
}

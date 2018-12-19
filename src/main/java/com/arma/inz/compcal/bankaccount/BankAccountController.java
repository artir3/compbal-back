package com.arma.inz.compcal.bankaccount;

import com.arma.inz.compcal.contractor.Contractor;
import com.arma.inz.compcal.users.BaseUser;

import java.util.Collection;
import java.util.Set;

public interface BankAccountController {
    void saveOrUpdate(Collection<BankAccountDTO> bankAccountSet, Contractor contractor);

    void saveOrUpdate(Collection<BankAccountDTO> bankAccountSet, BaseUser baseUser);

    void saveOrUpdate(BankAccountDTO bankAccountDTO, BaseUser baseUser, Contractor contractor);

    boolean delete(Long id);

    Set<BankAccountDTO> copyToDTO(Collection<BankAccount> bankAccountSet);

    BankAccountDTO copyToDTO(BankAccount account);
}

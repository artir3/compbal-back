package com.arma.inz.compcal.bankaccount;

import com.arma.inz.compcal.currency.CurrencyEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Controller
public class BankAccountControllerImpl implements BankAccountController {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public Set<BankAccount> saveOrUpdate(Collection<BankAccountDTO> collection) {
        Set<BankAccount> bankAccounts = new HashSet<>();
        for (BankAccountDTO dto: collection) {
            bankAccounts.add(saveOrUpdate(dto));
        }
        return bankAccounts;
    }

    @Override
    public BankAccount saveOrUpdate(BankAccountDTO dto) {
        BankAccount bankAccount;
        if(dto.getId() != null){
            bankAccount = bankAccountRepository.findById(dto.getId()).get();
        } else {
            bankAccount = new BankAccount();
            bankAccount.setCreatedAt(LocalDateTime.now());
//                    bankAccount.setBaseUser(entity);
        }
        BeanUtils.copyProperties(dto, bankAccount, "id", "baseUser");
        bankAccount.setCurrency(CurrencyEnum.valueOf(dto.getCurrency()));
        bankAccount.setModifiedAt(LocalDateTime.now());
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public boolean delete(Long id) {
        if (id == null){ return false; }
        bankAccountRepository.deleteById(id);
        return true;
    }

    @Override
    public Set<BankAccountDTO> copyToDTO(Collection<BankAccount> bankAccountSet) {
        Set<BankAccountDTO> bankAccounts = new HashSet<>();
        for (BankAccount account: bankAccountSet) {
            BankAccountDTO dto = copyToDTO(account);
            bankAccounts.add(dto);
        }
        return bankAccounts;
    }

    @Override
    public BankAccountDTO copyToDTO(BankAccount account) {
        BankAccountDTO dto = new BankAccountDTO();
        BeanUtils.copyProperties(account, dto);
        dto.setCurrency(account.getCurrency().name());
        return dto;
    }
}

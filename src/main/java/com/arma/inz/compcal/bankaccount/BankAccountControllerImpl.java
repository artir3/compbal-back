package com.arma.inz.compcal.bankaccount;

import com.arma.inz.compcal.MapperToJson;
import com.arma.inz.compcal.contractor.Contractor;
import com.arma.inz.compcal.currency.CurrencyEnum;
import com.arma.inz.compcal.users.BaseUser;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Controller
public class BankAccountControllerImpl implements BankAccountController {
    private final BankAccountRepository bankAccountRepository;

    @Override
    public void saveOrUpdate(Collection<BankAccountDTO> collection, Contractor contractor) {
        for (BankAccountDTO dto: collection) {
            saveOrUpdate(dto, null, contractor);
        }
    }

    @Override
    public void saveOrUpdate(Collection<BankAccountDTO> bankAccountSet, BaseUser baseUser) {
        for (BankAccountDTO dto: bankAccountSet) {
            saveOrUpdate(dto, baseUser, null);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdate(BankAccountDTO dto, BaseUser baseUser, Contractor contractor) {
        if (dto != null) {
            BankAccount bankAccount;
            if (dto.getId() != null) {
                bankAccount = bankAccountRepository.findById(dto.getId()).get();
            } else {
                bankAccount = new BankAccount();
                bankAccount.setCreatedAt(LocalDateTime.now());
                bankAccount.setContractor(contractor);
                bankAccount.setBaseUser(baseUser);
            }
            BeanUtils.copyProperties(dto, bankAccount, "id", "baseUser", "contractor", "createdAt");
            bankAccount.setCurrency(CurrencyEnum.valueOf(dto.getCurrency()));
            bankAccount.setModifiedAt(LocalDateTime.now());
            MapperToJson.convertToJson(bankAccount, this.getClass().getName() + "saveMessage");

            bankAccountRepository.save(bankAccount);
        }
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
        if (bankAccounts != null) {
            for (BankAccount account : bankAccountSet) {
                BankAccountDTO dto = copyToDTO(account);
                bankAccounts.add(dto);
            }
        }
        return bankAccounts;
    }

    private BankAccountDTO copyToDTO(BankAccount account) {
        BankAccountDTO dto = new BankAccountDTO();
        BeanUtils.copyProperties(account, dto);
        dto.setCurrency(account.getCurrency().name());
        MapperToJson.convertToJson(dto, this.getClass().getName() + "dto");

        return dto;
    }
}

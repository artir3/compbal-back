package com.arma.inz.compcal.contractor;


import com.arma.inz.compcal.bankaccount.BankAccount;
import com.arma.inz.compcal.bankaccount.BankAccountController;
import com.arma.inz.compcal.bankaccount.BankAccountDTO;
import com.arma.inz.compcal.contractor.dto.ContractorDTO;
import com.arma.inz.compcal.contractor.dto.ContractorMiniDTO;
import com.arma.inz.compcal.users.BaseUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class ContractorControllerImpl implements ContractorController {

    @Autowired
    private ContractorRepository contractorRepository;

    @Autowired
    private BankAccountController bankAccountController;

    @Override
    public List<ContractorMiniDTO> getAll() {
        List<Contractor> contractors = contractorRepository.findAll();
        List<ContractorMiniDTO> miniDTOS = new ArrayList<>();
        for (Contractor contractor : contractors) {
            ContractorMiniDTO dto = new ContractorMiniDTO();
            BeanUtils.copyProperties(contractor, dto);
            dto.setAddress(contractor.getPrettyAddress());
            dto.setPersonName(contractor.getPrettyName());
            dto.setContact(contractor.getPrettyContact());
            miniDTOS.add(dto);
        }
        return miniDTOS;
    }

    @Override
    public ContractorDTO getOne(Long id) {
        Optional<Contractor> entity = contractorRepository.findById(id);
        ContractorDTO dto = null;
        if (entity != null) {
            Contractor contractor = entity.get();
            dto = new ContractorDTO();
            BeanUtils.copyProperties(contractor, dto);
            Set<BankAccountDTO> bankAccountDTOSet = bankAccountController.copyToDTO(contractor.getBankAccounts());
            dto.setBankAccount(bankAccountDTOSet);
        }
        return dto;
    }

    @Override
    public Boolean updateOne(ContractorDTO dto) {
        Optional<Contractor> optional = contractorRepository.findById(dto.getId());
        if (optional != null) {
            Contractor contractor = optional.get();
            BeanUtils.copyProperties(dto, contractor, "id", "baseUser", "createdAt", "kpirList");
            contractor.setModifiedAt(LocalDateTime.now());
            Set<BankAccount> bankAccounts = bankAccountController.saveOrUpdate(dto.getBankAccount());
            contractor.setBankAccounts(bankAccounts);
            contractorRepository.save(contractor);
        }
        return optional != null;
    }

    @Override
    public Boolean createOne(BaseUser baseUser, ContractorDTO dto) {
        Contractor entity = new Contractor();
        BeanUtils.copyProperties(dto, entity, "id");
        entity.setBaseUser(baseUser);
        entity.setModifiedAt(LocalDateTime.now());
        entity.setCreatedAt(LocalDateTime.now());
        Set<BankAccount> bankAccounts = bankAccountController.saveOrUpdate(dto.getBankAccount());
        entity.setBankAccounts(bankAccounts);
        entity = contractorRepository.save(entity);
        return entity.getId() != null;
    }

    @Override
    public Boolean deleteOne(Long id) {
        Optional<Contractor> optional = contractorRepository.findById(id);
        if (optional != null) {
            contractorRepository.delete(optional.get());
        }
        return optional != null;
    }
}

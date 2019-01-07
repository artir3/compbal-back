package com.arma.inz.compcal.contractor;

import com.arma.inz.compcal.bankaccount.BankAccountController;
import com.arma.inz.compcal.bankaccount.BankAccountDTO;
import com.arma.inz.compcal.contractor.dto.ContractorDTO;
import com.arma.inz.compcal.contractor.dto.ContractorFilterDTO;
import com.arma.inz.compcal.contractor.dto.ContractorMiniDTO;
import com.arma.inz.compcal.contractor.dto.ContractorSelectDTO;
import com.arma.inz.compcal.users.BaseUser;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Controller
public class ContractorControllerImpl implements ContractorController {
    private final ContractorRepository contractorRepository;
    private final BankAccountController bankAccountController;

    @Override
    public ContractorMiniDTO parseToDTO(Contractor contractor) {
        ContractorMiniDTO dto = new ContractorMiniDTO();
        BeanUtils.copyProperties(contractor, dto);
        dto.setAddress(contractor.getPrettyAddress());
        dto.setPersonName(contractor.getPrettyName());
        if (contractor.getCreditor() == null) {
            contractor.setCreditor(Boolean.FALSE);
            contractor.setCreditorAmount(BigDecimal.ZERO);
        }
        if (contractor.getDebtor() == null) {
            contractor.setDebtor(Boolean.FALSE);
            contractor.setDebtorAmount(BigDecimal.ZERO);
        }
        return dto;
    }

    @Override
    public List<ContractorMiniDTO> getAll(BaseUser baseUser, ContractorFilterDTO filterDTO) {
        List<Contractor> list = contractorRepository.findAll(ContractorSpecification.getAllByFilter(baseUser, filterDTO));
        List<ContractorMiniDTO> result = new ArrayList<>();
        for (Contractor contractor : list) {
            ContractorMiniDTO dto = parseToDTO(contractor);
            result.add(dto);
        }
        return result;
    }

    @Override
    public ContractorDTO getOne(Long id) {
        Optional<Contractor> entity = contractorRepository.findById(id);
        ContractorDTO dto = null;
        if (entity != null && entity.get() != null) {
            Contractor contractor = entity.get();
            dto = new ContractorDTO();
            BeanUtils.copyProperties(contractor, dto);
            Set<BankAccountDTO> bankAccountDTOSet = bankAccountController.copyToDTO(contractor.getBankAccounts());
            dto.setBankAccounts(bankAccountDTOSet);
        }
        return dto;
    }

    @Override
    @Transactional
    public Contractor getOneEntity(Long id) {
        Optional<Contractor> entity = contractorRepository.findById(id);
        return entity != null ? entity.get() : null;
    }

    @Override
    @Transactional
    public Boolean updateOne(ContractorDTO dto) {
        Optional<Contractor> optional = contractorRepository.findById(dto.getId());
        if (optional != null) {
            Contractor entity = optional.get();
            BeanUtils.copyProperties(dto, entity, "id", "baseUser", "createdAt", "kpirList",
                    "bankAccounts", "creditor", "creditorAmount", "debtor", "debtorAmount");
            entity.setModifiedAt(LocalDateTime.now());
            if (entity.getCreditor() == null) {
                entity.setCreditor(Boolean.FALSE);
                entity.setCreditorAmount(BigDecimal.ZERO);
            }
            if (entity.getDebtor() == null) {
                entity.setDebtor(Boolean.FALSE);
                entity.setDebtorAmount(BigDecimal.ZERO);
            }
            entity = contractorRepository.save(entity);
            bankAccountController.saveOrUpdate(dto.getBankAccounts(), entity);
        }
        return optional != null;
    }

    @Override
    @Transactional
    public Boolean createOne(BaseUser baseUser, ContractorDTO dto) {
        Contractor entity = new Contractor();
        BeanUtils.copyProperties(dto, entity, "id", "kpirList", "bankAccounts");
        entity.setBaseUser(baseUser);
        entity.setModifiedAt(LocalDateTime.now());
        entity.setCreatedAt(LocalDateTime.now());
        entity = contractorRepository.save(entity);
        entity.setDebtor(Boolean.FALSE);
        entity.setCreditor(Boolean.FALSE);
        entity.setDebtorAmount(BigDecimal.ZERO);
        entity.setCreditorAmount(BigDecimal.ZERO);
        bankAccountController.saveOrUpdate(dto.getBankAccounts(), entity);
        dto.setId(entity.getId());
        return entity.getId() != null;
    }

    @Override
    @Transactional
    public Boolean deleteOne(Long id) {
        Optional<Contractor> optional = contractorRepository.findById(id);
        if (optional != null) {
            Contractor contractor = optional.get();
            contractor.setDeleted(Boolean.TRUE);
            contractorRepository.save(contractor);
//            contractorRepository.delete(optional.get());
        }
        return optional != null;
    }

    @Override
    public List<ContractorSelectDTO> getAllToSelect(BaseUser baseUser) {
        List<Contractor> list = contractorRepository.findAll(ContractorSpecification.byBaseUser(baseUser));
        List<ContractorSelectDTO> result = new ArrayList<>();
        for (Contractor contractor : list) {
            ContractorSelectDTO target = new ContractorSelectDTO();
            BeanUtils.copyProperties(contractor, target);
            result.add(target);
        }
        return result;
    }


}

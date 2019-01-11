package com.arma.inz.compcal.kpir;


import com.arma.inz.compcal.contractor.Contractor;
import com.arma.inz.compcal.contractor.ContractorController;
import com.arma.inz.compcal.contractor.ContractorRepository;
import com.arma.inz.compcal.currency.CurrencyEnum;
import com.arma.inz.compcal.currency.CurrencyExchange;
import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUser;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class KpirControllerImpl implements KpirController {
    private final KpirRepository kpirRepository;
    private final ContractorController contractorController;
    private final ContractorRepository contractorRepository;
    private final CurrencyExchange currencyExchange;

    @Override
    public List<KpirDTO> getAll(BaseUser baseUser, KpirFilterDTO filterDTO) {
        Sort sort = Sort.by("economicEventDate").descending();
        List<Kpir> list = getKpirs(baseUser, filterDTO, sort);
        return parseListToDTO(list);
    }

    @Override
    public KpirCreateDTO getOne(Long id) {
        Optional<Kpir> optional = kpirRepository.findById(id);
        KpirCreateDTO dto = null;
        if (optional != null) {
            Kpir kpir = optional.get();
            dto = new KpirCreateDTO();
            BeanUtils.copyProperties(kpir, dto);
            dto.setContractor(kpir.getContractor().getId());
            dto.setOverduePayment(calculateOverduePayment(kpir));
            if (kpir.getCurrency() != null){
                dto.setCurrency(kpir.getCurrency().name());
            }
        }
        return dto;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean createOne(BaseUser baseUser, KpirCreateDTO dto) {
        Kpir entity = new Kpir();
        BeanUtils.copyProperties(dto, entity, "id", "kpirList", "bankAccounts", "contractor", "type", "overduePayment");
        entity.setBaseUser(baseUser);
        entity.setModifiedAt(LocalDateTime.now());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setType(KpirTypeEnum.valueOf(dto.getType()));

        boolean isTodaysKpir = LocalDate.now().equals(entity.getEconomicEventDate().toLocalDate());
        if (isTodaysKpir) {
            entity.setIdx(dto.getIdx());
        } else {
            entity.setIdx(getNextIdx(entity.getEconomicEventDate(), baseUser));
        }
        Contractor one = contractorController.getOneEntity(dto.getContractor());
        entity.setContractor(one);
        if (dto.getCurrency() != null){
            entity.setCurrency(CurrencyEnum.valueOf(dto.getCurrency()));
        }
        entity = kpirRepository.save(entity);

        if (!isTodaysKpir){
            recalculateIdx(entity.getBaseUser(), entity.getEconomicEventDate());
        }
        if (calculateOverduePayment(entity)) {
            addDebtorAndCreditor(entity, entity.getContractor());
        }
        return entity.getId() != null;
    }

    @Override
    @Transactional
    public Boolean updateOne(KpirCreateDTO kpirDTO) {
        Optional<Kpir> optional = kpirRepository.findById(kpirDTO.getId());
        if (optional != null) {
            Kpir entity = optional.get();
            boolean addDebtorOrCreditor = !entity.getEconomicEventDate().equals(kpirDTO.getEconomicEventDate());
            boolean removeDebtorOrCreditor = kpirDTO.getPayed() && kpirDTO.getPayed() != entity.getPayed();
            BeanUtils.copyProperties(kpirDTO, entity, "id", "idx", "kpirList", "bankAccounts", "contractor", "createdAt", "type", "overduePayment");
            entity.setModifiedAt(LocalDateTime.now());
            entity.setCurrency(CurrencyEnum.valueOf(kpirDTO.getCurrency()));
            if(entity.getContractor().getId() != kpirDTO.getContractor()){
                entity.setContractor(contractorController.getOneEntity(kpirDTO.getContractor()));
            }
            entity = kpirRepository.save(entity);
            if (addDebtorOrCreditor){
                recalculateIdx(entity.getBaseUser(), entity.getEconomicEventDate());
                removeDebtorAndCreditor(entity, entity.getContractor());
            }
            if (calculateOverduePayment(entity)) {
                addDebtorAndCreditor(entity, entity.getContractor());
            }
            if (removeDebtorOrCreditor) {
                removeDebtorAndCreditor(entity, entity.getContractor());
            }

        }
        return optional != null;
    }

    @Override
    public Boolean deleteOne(Long id) {
        Optional<Kpir> optional = kpirRepository.findById(id);
        if (optional != null) {
            Kpir entity = optional.get();
            entity.setType(KpirTypeEnum.DELETED);
            if (calculateOverduePayment(entity)) {
                removeDebtorAndCreditor(entity, entity.getContractor());
            }
            recalculateIdx(optional.get().getBaseUser(), optional.get().getEconomicEventDate());
            kpirRepository.save(entity);
        }
        return optional != null;
    }

    @Override
    public Integer getNextIdx(BaseUser baseUser) {
        return getNextIdx(LocalDateTime.now(),baseUser);
    }

    @Override
    public Integer getNextIdx(LocalDateTime economicEventDate, BaseUser baseUser) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("idx").descending());
        Page nextKpir = kpirRepository.findAll(KpirSpecification.getAllByYearAndUser(economicEventDate.getYear(), baseUser), pageable);
        int idx = (nextKpir == null || nextKpir.isEmpty() ? 0 : ((Kpir) nextKpir.getContent().get(0)).getIdx());
        return idx + 1;
    }

    @Override
    public void recalculateIdx(BaseUser baseUser, LocalDateTime localDateTime){
        KpirFilterDTO filterDTO = new KpirFilterDTO();
        filterDTO.setSelectedYear(localDateTime.getYear());
        Sort sort = Sort.by("economicEventDate").ascending();
        List<Kpir> list = getKpirs(baseUser, filterDTO, sort);
        int idx = 1;
        for (Kpir entity: list) {
            entity.setIdx(idx++);
            entity.setModifiedAt(LocalDateTime.now());
            entity = kpirRepository.save(entity);
        }
    }

    @Override
    public List<KpirDTO> getAllForPrint(BaseUser baseUser, KpirFilterDTO filterDTO) {
        filterDTO.setType(null);
        filterDTO.setSelectedMonth(null);
        List<Kpir> list = getKpirs(baseUser, filterDTO, Sort.by("idx").ascending());
        return parseListToDTO(list);
    }

    private List<KpirDTO> parseListToDTO(List<Kpir> list) {
        List<KpirDTO> result = new ArrayList<>();
        for (Kpir kpir : list) {
            KpirDTO dto = parseToDTO(kpir);
            result.add(dto);
        }
        return result;
    }

    private List<Kpir> getKpirs(BaseUser baseUser, KpirFilterDTO filterDTO, Sort sort) {
        return kpirRepository.findAll(KpirSpecification.getAllByFilter(baseUser, filterDTO), sort);
    }

    private KpirDTO parseToDTO(Kpir kpir) {
        KpirDTO dto = new KpirDTO();
        BeanUtils.copyProperties(kpir, dto);
        if (kpir.getContractor() != null) {
            dto.setAddress(kpir.getContractor().getPrettyAddress());
            dto.setFullName(kpir.getContractor().getCompany());
        } else {
            dto.setAddress("Brak danych!");
            dto.setFullName("Brak danych!");
        }
        dto.setOverduePayment(calculateOverduePayment(kpir));
        currencyExchange.exchangeSumALlIncome(kpir, dto);
        return dto;
    }

    private boolean calculateOverduePayment(Kpir kpir) {
        return Boolean.FALSE.equals(kpir.getPayed()) && kpir.getPaymentDateMax().isBefore(LocalDateTime.now());
    }

    public void addDebtorAndCreditor(Kpir kpir, Contractor contractor) {
        if (KpirTypeEnum.INCOME.equals(kpir.getType())) {
            BigDecimal amount = contractor.getDebtorAmount() == null ? BigDecimal.ZERO : contractor.getDebtorAmount();
            amount = amount.add(currencyExchange.exchangeSumALlIncome(kpir));
            contractor.setDebtor(amount.compareTo(BigDecimal.ZERO) > 0);
            contractor.setDebtorAmount(amount);
        }
        if (KpirTypeEnum.COSTS.equals(kpir.getType())) {
            BigDecimal amount = contractor.getCreditorAmount() == null ? BigDecimal.ZERO : contractor.getCreditorAmount();
            amount = amount.add(currencyExchange.exchangeSumAllCosts(kpir));
            contractor.setCreditor(amount.compareTo(BigDecimal.ZERO) > 0);
            contractor.setCreditorAmount(amount);
        }
        contractorRepository.save(contractor);
    }
    public void removeDebtorAndCreditor(Kpir kpir, Contractor contractor) {
        if (KpirTypeEnum.INCOME.equals(kpir.getType())) {
            BigDecimal amount = contractor.getDebtorAmount() == null ? BigDecimal.ZERO : contractor.getDebtorAmount();
            amount = amount.subtract(currencyExchange.exchangeSumALlIncome(kpir));
            contractor.setDebtor(amount.compareTo(BigDecimal.ZERO) > 0);
            contractor.setDebtorAmount(amount);
        }
        if (KpirTypeEnum.COSTS.equals(kpir.getType())) {
            BigDecimal amount = contractor.getCreditorAmount() == null ? BigDecimal.ZERO : contractor.getCreditorAmount();
            amount = amount.subtract(currencyExchange.exchangeSumAllCosts(kpir));
            contractor.setCreditor(amount.compareTo(BigDecimal.ZERO) > 0);
            contractor.setCreditorAmount(amount);
        }
        contractorRepository.save(contractor);
    }

}

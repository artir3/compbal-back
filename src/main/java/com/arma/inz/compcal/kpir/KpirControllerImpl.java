package com.arma.inz.compcal.kpir;


import com.arma.inz.compcal.contractor.Contractor;
import com.arma.inz.compcal.contractor.ContractorController;
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
            dto.setOverduePayment(calculateOverduePayment(dto));
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
        entity = kpirRepository.save(entity);

        if (!isTodaysKpir){
            recalculateIdx(entity.getBaseUser(), entity.getEconomicEventDate());
        }
        return entity.getId() != null;
    }

    @Override
    public Boolean updateOne(KpirCreateDTO kpirDTO) {
        Optional<Kpir> optional = kpirRepository.findById(kpirDTO.getId());
        if (optional != null) {
            Kpir entity = optional.get();
            boolean recalculate = !entity.getEconomicEventDate().equals(kpirDTO.getEconomicEventDate());
            BeanUtils.copyProperties(kpirDTO, entity, "id", "idx", "kpirList", "bankAccounts", "contractor", "createdAt", "type", "overduePayment");
            entity.setModifiedAt(LocalDateTime.now());
            kpirRepository.save(entity);
            if (recalculate){
                recalculateIdx(entity.getBaseUser(), entity.getEconomicEventDate());
            }
        }
        return optional != null;
    }

    @Override
    public Boolean deleteOne(Long id) {
        Optional<Kpir> optional = kpirRepository.findById(id);
        if (optional != null) {
            kpirRepository.delete(optional.get());
            recalculateIdx(optional.get().getBaseUser(), optional.get().getEconomicEventDate());
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
            kpirRepository.save(entity);
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
        dto.setAddress(kpir.getContractor().getPrettyAddress());
        dto.setFullName(kpir.getContractor().getCompany());
        dto.setOverduePayment(calculateOverduePayment(dto));
        return dto;
    }

    private boolean calculateOverduePayment(KpirDTO dto) {
        return Boolean.FALSE.equals(dto.getPayed()) && dto.getPaymentDateMax().isBefore(LocalDateTime.now());
    }

    private boolean calculateOverduePayment(KpirCreateDTO dto) {
        return Boolean.FALSE.equals(dto.getPayed()) && dto.getPaymentDateMax().isBefore(LocalDateTime.now());
    }
}

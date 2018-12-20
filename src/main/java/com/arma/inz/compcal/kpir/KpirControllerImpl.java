package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.contractor.Contractor;
import com.arma.inz.compcal.contractor.ContractorController;
import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class KpirControllerImpl implements KpirController {
    @Autowired
    private KpirRepository kpirRepository;

    @Autowired
    private ContractorController contractorController;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<KpirDTO> getAll(BaseUser baseUser, KpirFilterDTO filterDTO) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Kpir> query = builder.createQuery(Kpir.class);
        Root<Kpir> root = query.from(Kpir.class);
        query = query.select(root).distinct(true);
        List<Predicate> predicates = new ArrayList<>();
        if (filterDTO.getCompany()!= null && !filterDTO.getCompany().isEmpty()) {
            predicates.add(builder.like(builder.lower(root.<String> get("company")),"%" + filterDTO.getCompany().trim().toLowerCase() + "%"));
        }
        if (filterDTO.getNip()!= null && !filterDTO.getNip().isEmpty()) {
            predicates.add(builder.like(builder.lower(root.<String> get("nip")),"%" + filterDTO.getNip().trim().toLowerCase() + "%"));
        }
        if (filterDTO.getRegistrationNumber()!= null && !filterDTO.getRegistrationNumber().isEmpty()) {
            predicates.add(builder.like(builder.lower(root.<String> get("registrationNumber")),"%" + filterDTO.getRegistrationNumber().trim().toLowerCase() + "%"));
        }
        if (filterDTO.getIsPayed() != null && Boolean.TRUE.equals(filterDTO.getIsPayed())) {
            predicates.add(builder.equal(root.<Boolean> get("payed"),filterDTO.getIsPayed()));
        }
        if (filterDTO.getNotPayed() != null && Boolean.TRUE.equals(filterDTO.getNotPayed())) {
            predicates.add(builder.equal(root.<Boolean> get("payed"),filterDTO.getNotPayed()));
        }

        if (filterDTO.getEconomicEventDate() != null && Boolean.TRUE.equals(filterDTO.getNotPayed())) {
            predicates.add(builder.equal(root.<LocalDateTime> get("economicEventDate"),filterDTO.getEconomicEventDate()));
        }

        //    private Integer selectedYear;
        //    private Integer selectedMonth;
        //    private Boolean overdue;

        predicates.add(builder.equal(root.get("baseUser"), baseUser));
        query.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));

        List<Kpir> list = entityManager.createQuery(query).getResultList();
        List<KpirDTO> result = new ArrayList<>();
        for (Kpir kpir : list) {
            KpirDTO dto = parseToDTO(kpir);
            result.add(dto);
        }
        return result;
    }

    private KpirDTO parseToDTO(Kpir kpir) {
        KpirDTO dto = new KpirDTO();
        BeanUtils.copyProperties(kpir, dto);
        dto.setAddress(kpir.getContractor().getPrettyAddress());
        dto.setFullName(kpir.getContractor().getCompany());
        return dto;
    }

    @Override
    public KpirCreateDTO getOne(Long id) {
        Optional<Kpir> optional = kpirRepository.findById(id);
        KpirCreateDTO dto = null;
        if (optional != null){
            Kpir kpir = optional.get();
            dto = new KpirCreateDTO();
            BeanUtils.copyProperties(kpir, dto);
            dto.setContractor(kpir.getContractor().getId());
        }
        return dto;
    }

    @Override
    public Boolean createOne(BaseUser baseUser, KpirCreateDTO dto) {
        Kpir entity = new Kpir();
        BeanUtils.copyProperties(dto, entity, "id", "kpirList", "bankAccounts", "contractor");
        entity.setBaseUser(baseUser);
        entity.setModifiedAt(LocalDateTime.now());
        entity.setCreatedAt(LocalDateTime.now());
        Contractor one = contractorController.getOneEntity(dto.getContractor());
        entity.setContractor(one);
        entity = kpirRepository.save(entity);
        return entity.getId() != null;    }

    @Override
    public Boolean updateOne(KpirCreateDTO kpirDTO) {
        Optional<Kpir> optional = kpirRepository.findById(kpirDTO.getId());
        if (optional != null){
            Kpir entity = optional.get();
            BeanUtils.copyProperties(kpirDTO, entity, "id", "kpirList", "bankAccounts", "contractor", "createdAt");
            entity.setModifiedAt(LocalDateTime.now());
            kpirRepository.save(entity);
        }
        return optional != null;
    }

    @Override
    public Boolean deleteOne(Long id) {
        Optional<Kpir> optional = kpirRepository.findById(id);
        if (optional != null) {
            kpirRepository.delete(optional.get());
        }
        return optional != null;
    }
}

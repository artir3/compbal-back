package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.contractor.Contractor;
import com.arma.inz.compcal.contractor.ContractorController;
import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUser;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
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

        if (filterDTO.getEconomicEventDate() != null) {
            predicates.add(builder.equal(root.<LocalDateTime> get("economicEventDate"),filterDTO.getEconomicEventDate()));
        }

        Expression<Integer> year = builder.function("year", Integer.class, root.<LocalDateTime> get("economicEventDate"));
        predicates.add(builder.equal(year, filterDTO.getSelectedYear()));

        if (filterDTO.getSelectedMonth() != null) {
            Expression<Integer> month = builder.function("month", Integer.class, root.<LocalDateTime> get("economicEventDate"));
            predicates.add(builder.equal(month, filterDTO.getSelectedMonth()));
        }

        if (Boolean.TRUE.equals(filterDTO.getOverdue())) {
            predicates.add(builder.equal(root.<Boolean> get("payed"),Boolean.FALSE));
            predicates.add(builder.lessThan(root.<LocalDateTime> get("paymentDateMax"),LocalDateTime.now()));
        }

        predicates.add(builder.equal(root.get("baseUser"), baseUser));
        query.where(builder.and(builder.and(predicates.toArray(new Predicate[predicates.size()]))));

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

    @Override
    public Integer getNextIdx(BaseUser baseUser) {
        return kpirRepository.getLastIdByThisYearAndBaseUser(LocalDateTime.now().getYear(), baseUser) + 1;
//return 0;

    }
}

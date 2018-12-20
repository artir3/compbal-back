package com.arma.inz.compcal.contractor;


import com.arma.inz.compcal.CompliancePagination;
import com.arma.inz.compcal.PageResultImpl;
import com.arma.inz.compcal.bankaccount.BankAccount;
import com.arma.inz.compcal.bankaccount.BankAccountController;
import com.arma.inz.compcal.bankaccount.BankAccountDTO;
import com.arma.inz.compcal.contractor.dto.ContractorDTO;
import com.arma.inz.compcal.contractor.dto.ContractorFilterDTO;
import com.arma.inz.compcal.contractor.dto.ContractorMiniDTO;
import com.arma.inz.compcal.users.BaseUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    @PersistenceContext(unitName = "local")
    private EntityManager em;

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


    public PageResultImpl<ContractorMiniDTO> getSubjectList(Integer page, Integer pageSize, String sort, ContractorFilterDTO filterDTO) {
        return new CompliancePagination<Contractor, ContractorFilterDTO, ContractorMiniDTO>(em, Contractor.class) {

            @Override
            public void whereQuery(List<Predicate> predicates, ContractorFilterDTO filterDTO, CriteriaBuilder builder,
                                   CriteriaQuery<?> query, Root<Contractor> root) {
//                if (CommonUtils.isStringValid(filterDTO.getName())) {
//                    predicates.add(builder.or(
//                            builder.like(builder.lower(root.<String> get("name")),
//                                    "%" + filterDTO.getName().trim().toLowerCase() + "%"),
//                            builder.like(
//                                    builder.lower(builder.concat(builder.concat(root.<String> get("firstname"), " "),
//                                            root.<String> get("surname"))),
//                                    "%" + filterDTO.getName().trim().toLowerCase() + "%")));
//                }

                // if (!CommonUtils.isCollectionEmpty(filterDTO.getType())) {
                // List<Predicate> list = new ArrayList<>();
                // for (SubjectFilterDTO.SubjectType status :
                // filterDTO.getType()) {
                // if (SubjectFilterDTO.SubjectType.COMPANY.equals(status)) {
                // list.add(builder.equal(root.<String>get("type"),
                // Subject.SubjectType.COMPANY));
                // } else if
                // (SubjectFilterDTO.SubjectType.NATURAL_PERSON.equals(status))
                // {
                // list.add(builder.equal(root.<String>get("type"),
                // Subject.SubjectType.NATURAL_PERSON));
                // }
                // }
                // predicates.add(builder.or(list.toArray(new
                // Predicate[list.size()])));
                // }
//                if (!CommonUtils.isCollectionEmpty(filterDTO.getAddress())) {
//                    predicates.add(
//                            builder.concat(
//                                    builder.concat(
//                                            builder.concat(
//                                                    builder.concat(builder.concat(root.<String> get("street"), " "),
//                                                            builder.concat(root.<String> get("houseNo"), " ")),
//                                                    builder.concat(root.<String> get("zipcode"), " ")),
//                                            builder.concat(root.<String> get("city"), " ")),
//                                    root.<String> get("country")).in(filterDTO.getAddress()));
//                }
//
//                if (!CommonUtils.isCollectionEmpty(filterDTO.getFunction())) {
//                    List<Predicate> list = new ArrayList<>();
//                    for (SubjectFilterDTO.FunctionType status : filterDTO.getFunction()) {
//                        if (SubjectFilterDTO.FunctionType.ADMIN.equals(status)) {
//                            list.add(builder.equal(root.<Boolean> get("admin"), Boolean.TRUE));
//                        } else if (SubjectFilterDTO.FunctionType.PROCESOR.equals(status)) {
//                            list.add(builder.equal(root.<String> get("procesor"), Boolean.TRUE));
//                        } else if (SubjectFilterDTO.FunctionType.RECIPIENT.equals(status)) {
//                            list.add(builder.equal(root.<String> get("recipient"), Boolean.TRUE));
//                        }
//                    }
//                    predicates.add(builder.or(list.toArray(new Predicate[list.size()])));
//                }
            }

            @Override
            public List<ContractorMiniDTO> copyToMiniDTO(List<Contractor> list) {
                List<ContractorMiniDTO> result = new ArrayList<>();
                for (Contractor contractor : list) {
                    ContractorMiniDTO dto = new ContractorMiniDTO();
                    BeanUtils.copyProperties(contractor, dto);
                    dto.setAddress(contractor.getPrettyAddress());
                    dto.setPersonName(contractor.getPrettyName());
                    dto.setContact(contractor.getPrettyContact());
                    result.add(dto);
                }
                return result;
            }
        }.paginationList(page, pageSize, sort, filterDTO);
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
            dto.setBankAccounts(bankAccountDTOSet);
        }
        return dto;
    }

    @Override
    public Boolean updateOne(ContractorDTO dto) {
        Optional<Contractor> optional = contractorRepository.findById(dto.getId());
        if (optional != null) {
            Contractor contractor = optional.get();
            BeanUtils.copyProperties(dto, contractor, "id", "baseUser", "createdAt", "kpirList", "bankAccounts");
            contractor.setModifiedAt(LocalDateTime.now());
            Contractor entity = contractorRepository.save(contractor);
            bankAccountController.saveOrUpdate(dto.getBankAccounts(), entity);
        }
        return optional != null;
    }

    @Override
    public Boolean createOne(BaseUser baseUser, ContractorDTO dto) {
        Contractor entity = new Contractor();
        BeanUtils.copyProperties(dto, entity, "id", "kpirList", "bankAccounts");
        entity.setBaseUser(baseUser);
        entity.setModifiedAt(LocalDateTime.now());
        entity.setCreatedAt(LocalDateTime.now());
        entity = contractorRepository.save(entity);
        bankAccountController.saveOrUpdate(dto.getBankAccounts(), entity);
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

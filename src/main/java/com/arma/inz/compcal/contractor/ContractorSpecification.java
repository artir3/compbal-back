package com.arma.inz.compcal.contractor;

import com.arma.inz.compcal.contractor.dto.ContractorFilterDTO;
import com.arma.inz.compcal.kpir.Kpir;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUser;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ContractorSpecification {
    //    https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

    public static Specification<Contractor> byBaseUser(BaseUser baseUser) {
        return (root, query, builder) -> builder.equal(root.get("baseUser"), baseUser);
    }

    public static Specification<Kpir> getAllByFilter(BaseUser baseUser, ContractorFilterDTO filterDTO) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filterDTO.getCompany()!= null && !filterDTO.getCompany().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.<String> get("company")),"%" + filterDTO.getCompany().trim().toLowerCase() + "%"));
            }
            if (filterDTO.getNip()!= null && !filterDTO.getNip().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.<String> get("nip")),"%" + filterDTO.getNip().trim().toLowerCase() + "%"));
            }
            if (filterDTO.getTrade()!= null && !filterDTO.getTrade().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.<String> get("trade")),"%" + filterDTO.getTrade().trim().toLowerCase() + "%"));
            }
            if (filterDTO.getPerson() != null && !filterDTO.getPerson().isEmpty()) {
                Expression<String> concat = builder.concat(builder.lower(root.<String>get("firstname")), " ");
                Expression<String> fullName = builder.concat(concat, builder.lower(root.<String>get("surname")));
                predicates.add(builder.like(fullName,"%" + filterDTO.getPerson().trim().toLowerCase() + "%"));
            }
            predicates.add(builder.equal(root.get("baseUser"), baseUser));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

}

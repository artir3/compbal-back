package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUser;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class KpirSpecification {
    //    https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

    public static Specification<Kpir> getAllByYearAndUser(Integer searchByYear, BaseUser baseUser) {
        return (root, query, builder) -> {
            Expression<Integer> year = builder.function("year", Integer.class, root.<LocalDateTime>get("economicEventDate"));
            Predicate predicateYear = builder.equal(year, searchByYear);
            Predicate predicateUser = builder.equal(root.get("baseUser"), baseUser);
            return builder.and(predicateUser, predicateYear);
        };
    }

    public static Specification<Kpir> getAllByFilter(BaseUser baseUser, KpirFilterDTO filterDTO) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filterDTO.getCompany() != null && !filterDTO.getCompany().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.<String>get("company")), "%" + filterDTO.getCompany().trim().toLowerCase() + "%"));
            }
            if (filterDTO.getNip() != null && !filterDTO.getNip().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.<String>get("nip")), "%" + filterDTO.getNip().trim().toLowerCase() + "%"));
            }
            if (filterDTO.getRegistrationNumber() != null && !filterDTO.getRegistrationNumber().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.<String>get("registrationNumber")), "%" + filterDTO.getRegistrationNumber().trim().toLowerCase() + "%"));
            }
            if (filterDTO.getIsPayed() != null && Boolean.TRUE.equals(filterDTO.getIsPayed())) {
                predicates.add(builder.equal(root.<Boolean>get("payed"), Boolean.TRUE));
            }
            if (filterDTO.getNotPayed() != null && Boolean.TRUE.equals(filterDTO.getNotPayed())) {
                Predicate isNotPayed = builder.equal(root.<Boolean>get("payed"), Boolean.FALSE);
                Predicate isNull = builder.isNull(root.<Boolean>get("payed"));
                predicates.add(builder.or(isNotPayed,isNull));
            }

            if (filterDTO.getEconomicEventDate() != null) {
                predicates.add(builder.between(root.<LocalDateTime>get("economicEventDate"),
                        LocalDateTime.of(filterDTO.getEconomicEventDate().toLocalDate(), LocalTime.of(0,0)),
                        LocalDateTime.of(filterDTO.getEconomicEventDate().plusDays(1l).toLocalDate(), LocalTime.of(0,0))));
            } else {
                Expression<Integer> year = builder.function("year", Integer.class, root.<LocalDateTime>get("economicEventDate"));
                predicates.add(builder.equal(year, filterDTO.getSelectedYear()));

                if (filterDTO.getSelectedMonth() != null) {
                    Expression<Integer> month = builder.function("month", Integer.class, root.<LocalDateTime>get("economicEventDate"));
                    predicates.add(builder.equal(month, filterDTO.getSelectedMonth()));
                }
            }

            if (Boolean.TRUE.equals(filterDTO.getOverdue())) {
                Predicate isNotPayed = builder.equal(root.<Boolean>get("payed"), Boolean.FALSE);
                Predicate isNull = builder.isNull(root.<Boolean>get("payed"));
                predicates.add(builder.or(isNotPayed,isNull));
                predicates.add(builder.lessThan(root.<LocalDateTime>get("paymentDateMax"), LocalDateTime.now()));
            }

            if (filterDTO.getType() != null) {
                predicates.add(builder.equal(root.<KpirTypeEnum>get("type"), KpirTypeEnum.valueOf(filterDTO.getType())));
            }

            predicates.add(builder.equal(root.get("baseUser"), baseUser));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

}

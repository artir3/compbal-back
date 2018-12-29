package com.arma.inz.compcal.kpir;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;

public class KpirSpecs {
//    https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

    public static Specification<Kpir> isLongTermCustomer() {
        return new Specification<Kpir>() {
            public Predicate toPredicate(Root<Kpir> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                LocalDate date = new LocalDate().minusYears(2);
                return builder.lessThan(root.get(_Kpir.createdAt), date);
            }
        };

}

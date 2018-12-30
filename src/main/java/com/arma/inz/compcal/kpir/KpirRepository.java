package com.arma.inz.compcal.kpir;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface KpirRepository extends JpaRepository<Kpir, Long>, JpaSpecificationExecutor {
//    List<Kpir> findAll(Specification<Kpir> spec);
}

package com.arma.inz.compcal.kpir;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface KpirRepository extends JpaRepository<Kpir, Long>, JpaSpecificationExecutor {
}

package com.arma.inz.compcal.contractor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContractorRepository extends JpaRepository<Contractor, Long>, JpaSpecificationExecutor {

//    Set<Contractor> findAllByBaseUserIs(BaseUser baseUser);
}

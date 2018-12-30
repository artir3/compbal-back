package com.arma.inz.compcal.contractor;

import com.arma.inz.compcal.users.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

public interface ContractorRepository extends JpaRepository<Contractor, Long>, JpaSpecificationExecutor {

//    Set<Contractor> findAllByBaseUserIs(BaseUser baseUser);
}

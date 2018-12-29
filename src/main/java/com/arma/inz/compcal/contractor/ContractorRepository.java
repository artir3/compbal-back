package com.arma.inz.compcal.contractor;

import com.arma.inz.compcal.users.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ContractorRepository extends JpaRepository<Contractor, Long> {

    Set<Contractor> findAllByBaseUser(BaseUser baseUser);
}

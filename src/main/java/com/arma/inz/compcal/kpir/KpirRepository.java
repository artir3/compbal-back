package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.users.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KpirRepository extends JpaRepository<Kpir, Long> {
    @Query("SELECT k.idx FROM Kpir k WHERE function(extract( day FROM  k.economicEventDate)) =:year AND k.baseUser=:baseUser ORDER BY k.idx DESC nulls first ")
    Integer getLastIdByThisYearAndBaseUser(@Param("year") int year, @Param("baseUser") BaseUser baseUser);
}

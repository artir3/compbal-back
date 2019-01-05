package com.arma.inz.compcal.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseUserRepository extends JpaRepository<BaseUser, Long> {
    BaseUser findByEmail(String email);

    BaseUser findOneByHash(String hash);

    @Query("select b.id from BaseUser b where b.hash like %:hash%")
    Long findOneLikeHash(@Param("hash") String hash);

}


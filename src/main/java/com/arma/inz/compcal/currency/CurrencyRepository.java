package com.arma.inz.compcal.currency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

public interface CurrencyRepository extends JpaRepository<Currency, Long>, JpaSpecificationExecutor<Currency> {
    @Query(value = "select distinct(c.added_at) from currency c order by c.added_at desc limit 1", nativeQuery = true)
    Date getLastInsertDate();

}

package com.arma.inz.compcal.currency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    @Query(value = "select distinct(c.added_at) from currency c order by c.added_at desc limit 1", nativeQuery = true)
    LocalDate getLastInsertDate();
}

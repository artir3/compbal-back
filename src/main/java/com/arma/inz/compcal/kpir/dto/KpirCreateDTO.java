package com.arma.inz.compcal.kpir.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class KpirCreateDTO {
    private Long id;
    private Long idx;
    private LocalDateTime economicEventDate;
    private String registrationNumber;
    private Long contractor;
    private String description;
    private BigDecimal soldIncome;
    private BigDecimal otherIncome;
    private BigDecimal allIncome;
    private BigDecimal purchaseCosts;
    private BigDecimal purchaseSideCosts;
    private BigDecimal marketingCosts;
    private BigDecimal paymentCost;
    private BigDecimal otherCosts;
    private BigDecimal sumCosts;
    private String other;
    private String comments;
}
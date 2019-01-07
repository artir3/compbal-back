package com.arma.inz.compcal.kpir.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KpirCreateDTO {
    private Long id;
    private Integer idx;
    private LocalDateTime economicEventDate;
    private String registrationNumber;
    private Long contractor;
    private String description;
    private BigDecimal soldIncome;
    private BigDecimal otherIncome;
    private BigDecimal allIncome;
    private BigDecimal purchaseCosts;
    private BigDecimal purchaseSideCosts;
    private BigDecimal paymentCost;
    private BigDecimal otherCosts;
    private BigDecimal sumCosts;
    private BigDecimal other;
    private BigDecimal radCosts;
    private String radDescription;
    private String comments;
    private Boolean payed;
    private LocalDateTime paymentDateMin;
    private LocalDateTime paymentDateMax;
    private String type;
    private Boolean overduePayment;
}

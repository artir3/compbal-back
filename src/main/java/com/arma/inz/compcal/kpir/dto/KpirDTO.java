package com.arma.inz.compcal.kpir.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class KpirDTO {
    private Long id;
    private Integer idx;
    private LocalDateTime economicEventDate;
    private String registrationNumber;
    private String fullName;
    private String address;
    private String description;
    private BigDecimal soldIncome;
    private BigDecimal otherIncome;
    private BigDecimal allIncome;
    private BigDecimal purchaseCosts;
    private BigDecimal purchaseSideCosts;
    private BigDecimal paymentCost;
    private BigDecimal otherCosts;
    private BigDecimal sumCosts;
    private String other;
    private String comments;
    private BigDecimal radCosts;
    private String radDescription;
    private Boolean payed;
    private LocalDateTime paymentDateMin;
    private LocalDateTime paymentDateMax;
}

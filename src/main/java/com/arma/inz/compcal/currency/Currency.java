package com.arma.inz.compcal.currency;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column
    private LocalDateTime addedAt;

    @Column
    @Enumerated(EnumType.STRING)
    private CurrencyEnum code;

    @Column(precision = 8, scale = 4)
    private BigDecimal exchangeValue;
}

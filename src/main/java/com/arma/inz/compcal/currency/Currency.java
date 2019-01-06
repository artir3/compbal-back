package com.arma.inz.compcal.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "addedAt")
    private LocalDateTime addedAt;

    @Column(name = "code")
    @Enumerated(EnumType.STRING)
    private CurrencyEnum code;

    @Column(precision = 8, scale = 4, name = "exchangeValue")
    private BigDecimal exchangeValue;
}

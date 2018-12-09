package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.contractor.Contractor;
import lombok.Data;
//import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table
//@Audited
public class Kpir {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime modifiedAt;
    @Column
    private Integer idx;
    @Column
    private LocalDateTime economicEventDate;
    @Column
    private String registrationNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    private Contractor contractor;
    @Column
    private String description;
    @Column
    private BigDecimal soldIncome;
    @Column
    private BigDecimal otherIncome;
    @Column
    private BigDecimal allIncome;
    @Column
    private BigDecimal purchaseCosts;
    @Column
    private BigDecimal purchaseSideCosts;
    @Column
    private BigDecimal marketingCosts;
    @Column
    private BigDecimal paymentCost;
    @Column
    private BigDecimal otherCosts;
    @Column
    private BigDecimal sumCosts;
    @Column
    private String other;
    @Column
    private String comments;
    @Column
    private Boolean payed;
    @Column
    private LocalDateTime paymentDateMin;
    @Column
    private LocalDateTime paymentDateMax;


}

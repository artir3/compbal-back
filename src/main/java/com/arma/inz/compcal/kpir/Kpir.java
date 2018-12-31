package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.contractor.Contractor;
import com.arma.inz.compcal.users.BaseUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table
@ToString(exclude = "baseUser")
public class Kpir {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
    @Column(name = "modifiedAt")
    private LocalDateTime modifiedAt;
    @Column(name = "idx")
    private Integer idx;
    @Column(name = "economicEventDate")
    private LocalDateTime economicEventDate;
    @Column(name = "registrationNumber")
    private String registrationNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Contractor contractor;
    @Column(name = "description")
    private String description;
    @Column(name = "soldIncome")
    private BigDecimal soldIncome;
    @Column(name = "otherIncome")
    private BigDecimal otherIncome;
    @Column(name = "allIncome")
    private BigDecimal allIncome;
    @Column(name = "purchaseCosts")
    private BigDecimal purchaseCosts;
    @Column(name = "purchaseSideCosts")
    private BigDecimal purchaseSideCosts;
    @Column(name = "paymentCost")
    private BigDecimal paymentCost;
    @Column(name = "otherCosts")
    private BigDecimal otherCosts;
    @Column(name = "sumCosts")
    private BigDecimal sumCosts;
    @Column(name = "other")
    private String other;
    @Column(name = "radCosts")
    private BigDecimal radCosts;
    @Column(name = "radDescription")
    private String radDescription;
    @Column(name = "comments")
    private String comments;
    @Column(name = "payed")
    private Boolean payed;
    @Column(name = "paymentDateMin")
    private LocalDateTime paymentDateMin;
    @Column(name = "paymentDateMax")
    private LocalDateTime paymentDateMax;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private KpirTypeEnum type;
    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @JoinColumn(name="baseUser_id", nullable=false)
    private BaseUser baseUser;
}

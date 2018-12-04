package com.arma.inz.compcal.users;

import com.arma.inz.compcal.currency.CurrencyEnum;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "accountNo")
    private String accountNo;
    @Column
    @Enumerated(EnumType.STRING)
    private CurrencyEnum currency;
    @ManyToOne(fetch = FetchType.LAZY)
    private BaseUser baseUser;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime modifiedAt;
    @Column
    private String swift;


}

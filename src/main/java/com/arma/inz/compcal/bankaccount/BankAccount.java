package com.arma.inz.compcal.bankaccount;

import com.arma.inz.compcal.contractor.Contractor;
import com.arma.inz.compcal.currency.CurrencyEnum;
import com.arma.inz.compcal.users.BaseUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table
@ToString(exclude = {"baseUser", "contractor"})
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "accountNo")
    private String accountNo;
    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private CurrencyEnum currency;
    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "baseUser_id")
    private BaseUser baseUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "contractor_id")
    private Contractor contractor;
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
    @Column(name = "modifiedAt")
    private LocalDateTime modifiedAt;
    @Column(name = "swift")
    private String swift;


}

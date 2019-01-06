package com.arma.inz.compcal.contractor;

import com.arma.inz.compcal.bankaccount.BankAccount;
import com.arma.inz.compcal.kpir.Kpir;
import com.arma.inz.compcal.users.BaseUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"baseUser", "bankAccounts", "kpirList"})
public class Contractor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
    @Column(name = "modifiedAt")
    private LocalDateTime modifiedAt;
    @OneToMany(mappedBy = "contractor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Kpir> kpirList;
    @Column(unique = true, name = "email")
    private String email;
    @Column(name = "firstName")
    private String firstname;
    @Column(name = "company")
    private String company;
    @Column(name = "nip")
    private String nip;
    @Column(name = "surname")
    private String surname;
    @Column(name = "street")
    private String street;
    @Column(name = "parcelNo")
    private String parcelNo;
    @Column(name = "homeNo")
    private String homeNo;
    @Column(name = "zip")
    private String zip;
    @Column(name = "city")
    private String city;
    @Column(name = "country")
    private String country;
    @Column(name = "phone")
    private String phone;
    @Column(name = "trade")
    private String trade;
    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "baseUser_id", nullable = false)
    private BaseUser baseUser;
    @OneToMany(mappedBy = "contractor", cascade = CascadeType.ALL, fetch = FetchType.LAZY/*, orphanRemoval = true*/)
    private Set<BankAccount> bankAccounts;

    public String getPrettyName() {
        return this.firstname + " " + this.surname;
    }

    public String getPrettyAddress() {
        return "ul. " + this.street + " " + this.parcelNo + (this.homeNo != null ? "/" + this.homeNo : "")
                + ", " + this.zip + " " + this.city + " " + this.country;
    }
}

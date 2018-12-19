package com.arma.inz.compcal.users;

import com.arma.inz.compcal.users.enums.RolesEnum;
import com.arma.inz.compcal.users.enums.TaxFormEnum;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table
@ToString(exclude = "bankAccountSet")
public class BaseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "active")
    private boolean active;
    @Column(name = "hash")
    private String hash;
    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private RolesEnum roles;
    @Column(unique=true, name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "firstName")
    private String firstName;
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
    @Column(name = "taxForm")
    @Enumerated(EnumType.STRING)
    private TaxFormEnum taxForm;
    @Column(name = "pkd")
    private String pkd;
    @Column(name = "regon")
    private String regon;
    @OneToMany(/*mappedBy = "baseUser",*/ cascade = CascadeType.ALL, fetch=FetchType.LAZY/*, orphanRemoval = true*/)
    private Set<BankAccount> bankAccounts;
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
    @Column(name = "modifiedAt")
    private LocalDateTime modifiedAt;
    @Column(name = "country")
    private String country;

}
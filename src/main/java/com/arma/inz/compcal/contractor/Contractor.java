package com.arma.inz.compcal.contractor;

import com.arma.inz.compcal.kpir.Kpir;
import com.arma.inz.compcal.users.BaseUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table
@ToString(exclude = "baseUser")
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
    @Column(unique=true, name = "email")
    private String email;
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
    @Column(name = "country")
    private String country;
    @Column(name = "phone")
    private String phone;
    @Column(name = "trade")
    private String trade;
    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @JoinColumn(name="baseUser_id", nullable=false)
    private BaseUser baseUser;

    public String getPrettyName(){
        return this.firstName + " " + this.surname;
    }

    public String getPrettyAddress(){
        return this.street + " " + this.parcelNo + "/" + this.homeNo
                + "\n" + this.zip + ", " + this.city + " " + this.country;
    }

    public String getPrettyContact() {
        return "Email: " + this.email + "\nTelefon: " + this.phone;
    }
}

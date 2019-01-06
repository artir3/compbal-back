package com.arma.inz.compcal.users.dto;

import com.arma.inz.compcal.bankaccount.BankAccountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@ToString(exclude = "bankAccountSet")
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String email;
    private String firstName;
    private String company;
    private String nip;
    private String surname;
    private String street;
    private String parcelNo;
    private String homeNo;
    private String zip;
    private String city;
    private String taxForm;
    private String pkd;
    private String regon;
    private String country;
    private String password;
    private Set<BankAccountDTO> bankAccountSet;
}

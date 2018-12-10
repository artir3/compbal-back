package com.arma.inz.compcal.users.dto;

import lombok.Data;

import java.util.Set;

@Data
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
    private Set<BankAccountDTO> bankAccountSet;
}

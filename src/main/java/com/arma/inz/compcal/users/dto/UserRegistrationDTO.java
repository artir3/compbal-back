package com.arma.inz.compcal.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {
    private String email;
    private String firstName;
    private String surname;
    private String company;
    private String nip;
    private String street;
    private String parcelNo;
    private String homeNo;
    private String zip;
    private String city;
    private String country;
    private String password;
    private String taxForm;
}

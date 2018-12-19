package com.arma.inz.compcal.contractor.dto;

import com.arma.inz.compcal.bankaccount.BankAccountDTO;
import lombok.Data;

import java.util.Set;

@Data
public class ContractorDTO {
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
    private String country;
    private String phone;
    private String trade;
    private Set<BankAccountDTO> bankAccounts;
}

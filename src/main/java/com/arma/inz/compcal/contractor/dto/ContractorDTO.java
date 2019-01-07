package com.arma.inz.compcal.contractor.dto;

import com.arma.inz.compcal.bankaccount.BankAccountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractorDTO {
    private Long id;
    private String email;
    private String firstname;
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
    private Boolean creditor;
    private Boolean debtor;
    private BigDecimal creditorAmount;
    private BigDecimal debtorAmount;
}

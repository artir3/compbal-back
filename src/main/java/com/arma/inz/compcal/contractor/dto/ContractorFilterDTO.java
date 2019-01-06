package com.arma.inz.compcal.contractor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractorFilterDTO {
    private String company;
    private String nip;
    private String person;
    private String trade;
}

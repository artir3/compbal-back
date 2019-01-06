package com.arma.inz.compcal.contractor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractorMiniDTO {
    private Long id;
    private String personName;
    private String company;
    private String nip;
    private String email;
    private String phone;
    private String address;
    private String trade;

}

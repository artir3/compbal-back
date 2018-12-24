package com.arma.inz.compcal.contractor.dto;

import lombok.Data;

@Data
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

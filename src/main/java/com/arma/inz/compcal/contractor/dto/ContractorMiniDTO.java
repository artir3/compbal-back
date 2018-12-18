package com.arma.inz.compcal.contractor.dto;

import com.arma.inz.compcal.kpir.Kpir;
import com.arma.inz.compcal.users.BaseUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ContractorMiniDTO {
    private Long id;
    private String personName;
    private String company;
    private String nip;
    private String contact;
    private String address;
    private String trade;

}

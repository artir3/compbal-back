package com.arma.inz.compcal.kpir.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KpirFilterDTO {
    private Integer selectedYear;
    private Integer selectedMonth;
    private LocalDateTime economicEventDate;
    private String company;
    private String nip;
    private Boolean isPayed;
    private Boolean notPayed;
    private Boolean overdue;
    private String registrationNumber;
    private String type;
}

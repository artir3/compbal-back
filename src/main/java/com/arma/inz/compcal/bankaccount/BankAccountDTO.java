package com.arma.inz.compcal.bankaccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDTO {
    private Long id;
    private String accountNo;
    private String currency;
    private String swift;
}

package com.arma.inz.compcal.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
class CurrencyDayTableADTO {
    private String table;
    private String no;
    private String effectiveDate;
    private CurrencyDayTableACurrencyDTO[] rates;
}

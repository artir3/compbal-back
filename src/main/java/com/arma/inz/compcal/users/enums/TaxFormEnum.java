package com.arma.inz.compcal.users.enums;

public enum TaxFormEnum {
    GENERAL_RULES_TAX_SCALE("Zasady ogólne - skala podatkowa"),
    GENERAL_RULES_FLAT_TAX("Zasady ogólne - podatek liniowy"),
    LUMP_SUM_REGISTRED_INCOME("Ryczałt od dochodów ewidencjonowanych"),
    TAX_CARD("Karta podatkowa"),
    ;

    private String label;
    TaxFormEnum(String label){
        this.label = label;
    }
    public String getLabel() {
        return this.label;
    }
}

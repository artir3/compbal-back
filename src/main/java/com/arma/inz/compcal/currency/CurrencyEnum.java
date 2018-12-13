package com.arma.inz.compcal.currency;

public enum CurrencyEnum {
    PLN("złotówka"),
    THB("bat"),
    USD("dolar amerykański"),
    AUD("dolar australijski"),
    HKD("dolar Hongkongu"),
    CAD("dolar kanadyjski"),
    NZD("dolar nowozelandzki"),
    SGD("dolar singapurski"),
    EUR("euro"),
    HUF("forint"),
    CHF("frank szwajcarski"),
    GBP("funt szterling"),
    UAH("hrywna"),
    JPY("jen"),
    CZK("korona czeska"),
    DKK("korona duńska"),
    ISK("korona islandzka"),
    NOK("korona norweska"),
    SEK("korona szwedzka"),
    HRK("kuna"),
    RON("lej rumuński"),
    BGN("lew"),
    TRY("lira turecka"),
    ILS("nowy izraelski"),
    CLP("peso chilijskie"),
    MXN("peso meksykańskie"),
    PHP("piso filipińskie"),
    ZAR("rand"),
    BRL("real"),
    MYR("ringgit"),
    RUB("rubel rosyjski"),
    IDR("rupia indonezyjska"),
    INR("rupia indyjska"),
    KRW("won południowokoreański"),
    CNY("yuan renminbi"),
    XDR("SDR")
    ;

    private final String label;
    CurrencyEnum(String label){
        this.label = label;
    }

    public String getLabel(){
        return this.label;
    }
}

package com.arma.inz.compcal.printer;

import lombok.Getter;

public enum JasperEnum {
    KPIR("Księga przychodów i rozchodów", "jasper/kpir.jrxml", "/jasper/kpir.jasper"),
    ;

    @Getter
    private final String label;
    @Getter
    private final String jrxmlPath;
    @Getter
    private final String jasperPath;

    JasperEnum(String label, String jrxmlPath, String jasperPath){
        this.label = label;
        this.jrxmlPath = jrxmlPath;
        this.jasperPath = jasperPath;
    }

}

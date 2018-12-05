package com.arma.inz.compcal.printer;

import lombok.Getter;

public enum JasperEnum {
    KPIR("Księga przychodów i rozchodów", "jasper/kpir.jrxml", "jasper/kpir.jasper"),
    ;

    @Getter
    private String label;
    @Getter
    private String jrxmlPath;
    @Getter
    private String jasperPath;

    JasperEnum(String label, String jrxmlPath, String jasperPath){
        this.label = label;
        this.jrxmlPath = jrxmlPath;
        this.jasperPath = jasperPath;
    }

}

package com.arma.inz.compcal.printer;

import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface JasperController {

    void compileFile(JasperEnum file) throws JRException;

    void export(String name, Map<String, Object> parameters, OutputStream outputStream) throws JRException;

    void export(JasperEnum jasperFile, Map<String, Object> parameters, OutputStream outputStream) throws JRException;

    byte[] generateKpir(String hash, KpirFilterDTO kpirFilterDTO) throws IOException;
}

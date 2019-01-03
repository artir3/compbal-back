package com.arma.inz.compcal.printer;

import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUser;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface JasperController {

    void compileFile(JasperEnum file) throws JRException;

    Resource getKpirResource(BaseUser baseUser, KpirFilterDTO kpirFilterDTO) throws IOException, JRException;

    byte[] getKpirBytes(BaseUser baseUser, KpirFilterDTO kpirFilterDTO) throws IOException, JRException;
}

package com.arma.inz.compcal.printer;

import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUser;
import org.springframework.core.io.Resource;

import java.io.File;

public interface JasperController {

    void compileFile(JasperEnum file);

    Resource getKpirResource(BaseUser baseUser, KpirFilterDTO kpirFilterDTO);

    byte[] getKpirBytes(BaseUser baseUser, KpirFilterDTO kpirFilterDTO);

    File getKpirFile(BaseUser baseUser, KpirFilterDTO kpirFilterDTO);

    void sendKpirPdftoMail(BaseUser baseUser, KpirFilterDTO kpirFilterDTO);
}

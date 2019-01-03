package com.arma.inz.compcal.printer;

import com.arma.inz.compcal.AuthorizationHeaderUtils;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@AllArgsConstructor
@RestController
public class JasperServiceImpl implements JasperService {

    private final JasperController jasperController;
    private final AuthorizationHeaderUtils header;

    @Override
    public ResponseEntity<Resource> getKpir(String authorization, KpirFilterDTO kpirFilterDTO) throws IOException, JRException {
        Resource file = jasperController.getKpirResource(header.getUserFromAuthorization(authorization), kpirFilterDTO);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }


}
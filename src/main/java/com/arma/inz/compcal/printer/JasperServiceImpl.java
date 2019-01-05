package com.arma.inz.compcal.printer;

import com.arma.inz.compcal.AuthorizationHeaderUtils;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUser;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@AllArgsConstructor
@RestController
public class JasperServiceImpl implements JasperService {

    private final JasperController jasperController;
    private final AuthorizationHeaderUtils header;

    @Override
    public ResponseEntity<Resource> getKpirPdfAsResponse(String authorization, KpirFilterDTO kpirFilterDTO) {
        Resource file = jasperController.getKpirResource(header.getUserFromAuthorization(authorization), kpirFilterDTO);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @Override
    public @ResponseBody
    byte[] getKpirPdfAsBytes(String authorization, KpirFilterDTO kpirFilterDTO) {
        return jasperController.getKpirBytes(header.getUserFromAuthorization(authorization), kpirFilterDTO);
    }

    @Override
    public void sendKpirPdfViaMail(String authorization, KpirFilterDTO kpirFilterDTO)  {
        BaseUser baseUser = header.getUserFromAuthorization(authorization);
        jasperController.sendKpirPdftoMail(baseUser, kpirFilterDTO);
    }
}
package com.arma.inz.compcal.printer;

import com.arma.inz.compcal.AuthorizationHeaderUtils;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@AllArgsConstructor
@RestController
public class JasperServiceImpl implements JasperService {

    private final JasperController jasperController;
    private final AuthorizationHeaderUtils header;

    @Override
    public ResponseEntity<byte[]> getKpir(String authorization, KpirFilterDTO kpirFilterDTO) throws IOException {
        byte[] pdf = jasperController.generateKpir(header.getUserFromAuthorization(authorization), kpirFilterDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set
        return new ResponseEntity(pdf, httpHeaders, HttpStatus.OK);
    }

}
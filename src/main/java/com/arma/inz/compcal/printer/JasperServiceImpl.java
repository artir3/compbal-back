package com.arma.inz.compcal.printer;

import com.arma.inz.compcal.AuthorizationHeaderUtils;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class JasperServiceImpl implements JasperService {

    @Autowired
    private JasperController jasperController;

    @Autowired
    private AuthorizationHeaderUtils header;

    @Override
    public ResponseEntity<byte[]> getKpir(String authorization, KpirFilterDTO kpirFilterDTO) throws IOException {
        byte[] pdf = jasperController.generateKpir(header.hashFromHeader(authorization), kpirFilterDTO);
        return new ResponseEntity(pdf, HttpStatus.OK);
    }

}
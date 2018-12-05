package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.AuthorizationHeaderUtils;
import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class KpirServiceImpl implements KpirService {
    @Autowired
    private KpirController kpirController;

    @Autowired
    private AuthorizationHeaderUtils header;

    @Override
    public ResponseEntity get(String authorization, KpirFilterDTO kpirFilterDTO) {
        List<KpirDTO> list = kpirController.getListByFilter(header.hashFromHeader(authorization), kpirFilterDTO);
        return new ResponseEntity( list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity get(Long id) {
        KpirCreateDTO dto = kpirController.getOne(id);
        return new ResponseEntity( dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity create(KpirCreateDTO kpirDTO) {
        Long id = kpirController.create(kpirDTO);
        return new ResponseEntity( id, HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(KpirCreateDTO kpirDTO) {
        Boolean update = kpirController.update(kpirDTO);
        return new ResponseEntity( update, HttpStatus.OK);
    }
}

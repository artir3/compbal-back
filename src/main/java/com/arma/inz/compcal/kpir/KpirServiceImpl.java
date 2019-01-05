package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.AuthorizationHeaderUtils;
import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.printer.JasperController;
import com.arma.inz.compcal.users.BaseUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
public class KpirServiceImpl implements KpirService {
    private final KpirController kpirController;
    private final AuthorizationHeaderUtils header;
    private final JasperController jasperController;

    @Override
    public ResponseEntity get(String authorization, KpirFilterDTO kpirFilterDTO) {
        BaseUser baseUser = header.getUserFromAuthorization(authorization);
        if (baseUser == null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        List<KpirDTO> list = kpirController.getAll(baseUser, kpirFilterDTO);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity get(Long id) {
        KpirCreateDTO kpirCreateDTO = kpirController.getOne(id);
        return new ResponseEntity<>(kpirCreateDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(KpirCreateDTO dto) {
        Boolean update = kpirController.updateOne(dto);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @Override
    public ResponseEntity create(String authorization, KpirCreateDTO dto) {
        BaseUser baseUser = header.getUserFromAuthorization(authorization);
        if (baseUser == null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        Boolean created = kpirController.createOne(baseUser, dto);
        return new ResponseEntity<>(created, HttpStatus.OK);
    }

    @Override
    public ResponseEntity delete(Long id) {
        Boolean deleted = kpirController.deleteOne(id);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getNextIdx(String authorization) {
        BaseUser baseUser = header.getUserFromAuthorization(authorization);
        if (baseUser == null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        Integer result = kpirController.getNextIdx(baseUser);
        return new ResponseEntity<>(result, HttpStatus.OK);    }
}

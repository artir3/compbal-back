package com.arma.inz.compcal.contractor;

import com.arma.inz.compcal.AuthorizationHeaderUtils;
import com.arma.inz.compcal.contractor.dto.ContractorDTO;
import com.arma.inz.compcal.contractor.dto.ContractorFilterDTO;
import com.arma.inz.compcal.contractor.dto.ContractorMiniDTO;
import com.arma.inz.compcal.contractor.dto.ContractorSelectDTO;
import com.arma.inz.compcal.users.BaseUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://46.101.227.24:8080", maxAge = 3600)
public class ContractorServiceImpl implements ContractorService {
    private final ContractorController contractorController;
    private final AuthorizationHeaderUtils header;

    @Override
    public ResponseEntity get(String authorization, ContractorFilterDTO contractorFilterDTO) {
        BaseUser baseUser = header.getUserFromAuthorization(authorization);
        if (baseUser == null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        List<ContractorMiniDTO> list = contractorController.getAll(baseUser, contractorFilterDTO);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity get(Long id) {
        ContractorDTO contractorDTO = contractorController.getOne(id);
        return new ResponseEntity<>(contractorDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(ContractorDTO dto) {
        Boolean update = contractorController.updateOne(dto);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @Override
    public ResponseEntity create(String authorization, ContractorDTO dto) {
        BaseUser baseUser = header.getUserFromAuthorization(authorization);
        if (baseUser == null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        Boolean created = contractorController.createOne(baseUser, dto);
        return new ResponseEntity<>(created, HttpStatus.OK);
    }

    @Override
    public ResponseEntity delete(Long id) {
        Boolean deleted = contractorController.deleteOne(id);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAllToSelect(String authorization) {
        BaseUser baseUser = header.getUserFromAuthorization(authorization);
        if (baseUser == null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        List<ContractorSelectDTO> list = contractorController.getAllToSelect(baseUser);
        return new ResponseEntity<>(list, HttpStatus.OK);    }
}

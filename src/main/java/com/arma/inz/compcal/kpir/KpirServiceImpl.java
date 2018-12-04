package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public class KpirServiceImpl implements KpirService {
    @Autowired
    private KpirRepository kpirRepository;
    @Override
    public ResponseEntity get(KpirFilterDTO kpirFilterDTO) {
        findKpirByFIlter(kpirFilterDTO);
        return new ResponseEntity( false, HttpStatus.OK);
    }

    @Override
    public ResponseEntity get(Long id) {
        Optional<Kpir> optional = kpirRepository.findById(id);
        KpirCreateDTO dto = null;
        if (optional != null){
            dto = new KpirCreateDTO();
            BeanUtils.copyProperties(optional.get(), dto);

        }
        return new ResponseEntity( dto, HttpStatus.OK);
    }

    private void findKpirByFIlter(KpirFilterDTO kpirFilterDTO) {

    }

    @Override
    public ResponseEntity create(KpirCreateDTO kpirDTO) {
        Kpir entity = new Kpir();
        BeanUtils.copyProperties(kpirDTO, entity);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setModifiedAt(LocalDateTime.now());
        entity = kpirRepository.save(entity);
        return new ResponseEntity( entity.getId() != null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(KpirCreateDTO kpirDTO) {
        Optional<Kpir> optional = kpirRepository.findById(kpirDTO.getId());
        if (optional != null){
            Kpir entity = optional.get();
            BeanUtils.copyProperties(kpirDTO, entity, "id",  "contractor", "createdAt");
            entity.setModifiedAt(LocalDateTime.now());
            kpirRepository.save(entity);
        }
        return new ResponseEntity( optional != null, HttpStatus.OK);
    }
}

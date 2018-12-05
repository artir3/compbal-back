package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class KpirControllerImpl implements KpirController {
    @Autowired
    private KpirRepository kpirRepository;

    private void findKpirByFIlter(KpirFilterDTO kpirFilterDTO) {

    }

    @Override
    public List<KpirDTO> getListByFilter(String hash, KpirFilterDTO kpirFilterDTO) {
        findKpirByFIlter(kpirFilterDTO);
        return new ArrayList<KpirDTO>();
    }

    @Override
    public KpirCreateDTO getOne(Long id) {
        Optional<Kpir> optional = kpirRepository.findById(id);
        KpirCreateDTO dto = null;
        if (optional != null){
            dto = new KpirCreateDTO();
            BeanUtils.copyProperties(optional.get(), dto);

        }
        return dto;
    }

    @Override
    public Long create(KpirCreateDTO kpirDTO) {
        Kpir entity = new Kpir();
        BeanUtils.copyProperties(kpirDTO, entity);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setModifiedAt(LocalDateTime.now());
        entity = kpirRepository.save(entity);
        return entity.getId();
    }

    @Override
    public Boolean update(KpirCreateDTO kpirDTO) {
        Optional<Kpir> optional = kpirRepository.findById(kpirDTO.getId());
        if (optional != null){
            Kpir entity = optional.get();
            BeanUtils.copyProperties(kpirDTO, entity, "id",  "contractor", "createdAt");
            entity.setModifiedAt(LocalDateTime.now());
            kpirRepository.save(entity);
        }
        return optional != null;
    }
}

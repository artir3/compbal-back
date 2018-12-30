package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.contractor.Contractor;
import com.arma.inz.compcal.contractor.ContractorController;
import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class KpirControllerImpl implements KpirController {
    @Autowired
    private KpirRepository kpirRepository;

    @Autowired
    private ContractorController contractorController;

    @Override
    public List<KpirDTO> getAll(BaseUser baseUser, KpirFilterDTO filterDTO) {
        Sort sort = Sort.by("economicEventDate").descending();
        List<Kpir> list = kpirRepository.findAll(KpirSpecification.getAllByFilter(baseUser, filterDTO), sort);
        List<KpirDTO> result = new ArrayList<>();
        for (Kpir kpir : list) {
            KpirDTO dto = parseToDTO(kpir);
            result.add(dto);
        }
        return result;
    }

    private KpirDTO parseToDTO(Kpir kpir) {
        KpirDTO dto = new KpirDTO();
        BeanUtils.copyProperties(kpir, dto);
        dto.setAddress(kpir.getContractor().getPrettyAddress());
        dto.setFullName(kpir.getContractor().getCompany());
        return dto;
    }

    @Override
    public KpirCreateDTO getOne(Long id) {
        Optional<Kpir> optional = kpirRepository.findById(id);
        KpirCreateDTO dto = null;
        if (optional != null) {
            Kpir kpir = optional.get();
            dto = new KpirCreateDTO();
            BeanUtils.copyProperties(kpir, dto);
            dto.setContractor(kpir.getContractor().getId());
        }
        return dto;
    }

    @Override
    public Boolean createOne(BaseUser baseUser, KpirCreateDTO dto) {
        Kpir entity = new Kpir();
        BeanUtils.copyProperties(dto, entity, "id", "kpirList", "bankAccounts", "contractor");
        entity.setBaseUser(baseUser);
        entity.setModifiedAt(LocalDateTime.now());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setIdx(dto.getIdx());
        Contractor one = contractorController.getOneEntity(dto.getContractor());
        entity.setContractor(one);
        entity = kpirRepository.save(entity);
        return entity.getId() != null;
    }

    @Override
    public Boolean updateOne(KpirCreateDTO kpirDTO) {
        Optional<Kpir> optional = kpirRepository.findById(kpirDTO.getId());
        if (optional != null) {
            Kpir entity = optional.get();
            BeanUtils.copyProperties(kpirDTO, entity, "id", "idx", "kpirList", "bankAccounts", "contractor", "createdAt");
            entity.setModifiedAt(LocalDateTime.now());
            kpirRepository.save(entity);
        }
        return optional != null;
    }

    @Override
    public Boolean deleteOne(Long id) {
        Optional<Kpir> optional = kpirRepository.findById(id);
        if (optional != null) {
            kpirRepository.delete(optional.get());
        }
        return optional != null;
    }

    @Override
    public Integer getNextIdx(BaseUser baseUser) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("idx").descending());
        Page nextKpir = kpirRepository.findAll(KpirSpecification.getNextIdx(LocalDateTime.now().getYear(), baseUser), pageable);
        Integer idx = (nextKpir == null || nextKpir.isEmpty() ? 0 : ((Kpir) nextKpir.getContent().get(0)).getIdx());
        return idx + 1;
    }
}

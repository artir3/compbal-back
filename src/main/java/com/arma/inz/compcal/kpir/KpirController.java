package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import org.springframework.stereotype.Controller;

import java.util.List;

public interface KpirController {

    List<KpirDTO> getListByFilter(String hash, KpirFilterDTO kpirFilterDTO);
    KpirCreateDTO getOne(Long id);
    Long create(KpirCreateDTO kpirDTO);
    Boolean update(KpirCreateDTO kpirDTO);

}

package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUser;

import java.time.LocalDateTime;
import java.util.List;

public interface KpirController {

    List<KpirDTO> getAll(BaseUser baseUser, KpirFilterDTO kpirFilterDTO);

    KpirCreateDTO getOne(Long id);

    Boolean updateOne(KpirCreateDTO dto);

    Boolean createOne(BaseUser baseUser, KpirCreateDTO dto);

    Boolean deleteOne(Long id);

    Integer getNextIdx(BaseUser baseUser);

    Integer getNextIdx(LocalDateTime economicEventDate, BaseUser baseUser);

    void recalculateIdx(BaseUser baseUser, LocalDateTime localDateTime);
}

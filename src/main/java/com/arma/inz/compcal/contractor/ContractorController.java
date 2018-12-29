package com.arma.inz.compcal.contractor;


import com.arma.inz.compcal.contractor.dto.ContractorDTO;
import com.arma.inz.compcal.contractor.dto.ContractorFilterDTO;
import com.arma.inz.compcal.contractor.dto.ContractorMiniDTO;
import com.arma.inz.compcal.contractor.dto.ContractorSelectDTO;
import com.arma.inz.compcal.users.BaseUser;

import java.util.List;

public interface ContractorController {
    List<ContractorMiniDTO> getAll(BaseUser baseUser, ContractorFilterDTO contractorFilterDTO);

    ContractorMiniDTO parseToDTO(Contractor contractor);

    ContractorDTO getOne(Long id);

    Contractor getOneEntity(Long id);

    Boolean updateOne(ContractorDTO dto);

    Boolean createOne(BaseUser baseUser, ContractorDTO dto);

    Boolean deleteOne(Long id);

    List<ContractorSelectDTO> getAllToSelect(BaseUser baseUser);
}

package com.arma.inz.compcal.contractor;

import com.arma.inz.compcal.bankaccount.BankAccountController;
import com.arma.inz.compcal.contractor.dto.ContractorDTO;
import com.arma.inz.compcal.contractor.dto.ContractorFilterDTO;
import com.arma.inz.compcal.contractor.dto.ContractorMiniDTO;
import com.arma.inz.compcal.contractor.dto.ContractorSelectDTO;
import com.arma.inz.compcal.database.DatabaseModelsFromJsons;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.users.BaseUser;
import com.arma.inz.compcal.users.BaseUserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@TestComponent
@SpringBootTest
@ActiveProfiles("test")
public class ContractorControllerImplTest {
    @Autowired
    private ContractorRepository contractorRepository;
    @Autowired
    private BankAccountController bankAccountController;
    @Autowired
    private ContractorController contractorController;
    @Autowired
    private BaseUserRepository baseUserRepository;
    private BaseUser baseUser;
    private List<ContractorDTO> initContractorDTOS;

    @Before
    public void setUp() throws Exception {
        baseUser = baseUserRepository.save(DatabaseModelsFromJsons.baseUser());
        initContractorDTOS = DatabaseModelsFromJsons.contractorDTOList();
        for (ContractorDTO contractorDTO : initContractorDTOS) {
            contractorController.createOne(baseUser, contractorDTO);
        }
    }

    @After
    public void tearDown() throws Exception {
        contractorRepository.deleteAll();
        baseUserRepository.deleteAll();
    }

    @Test
    public void parseToDTO() {
        List<Contractor> contractors = contractorRepository.findAll();
        assertThat(contractors).isNotEmpty();
        Contractor contractor = contractors.get(0);
        assertThat(contractor).isNotNull();
        Contractor entity = contractorController.getOneEntity(contractor.getId());
        assertThat(entity).isNotNull();
        ContractorMiniDTO miniDTO = contractorController.parseToDTO(entity);
        assertThat(miniDTO).isNotNull();
    }

    @Test
    public void getAll() {
        ContractorFilterDTO filterDTO = DatabaseModelsFromJsons.ContractorFilterDTOEmpty();
        assertThat(filterDTO).isNotNull();
        List<ContractorMiniDTO> contractorMiniDTOList = contractorController.getAll(baseUser, filterDTO);
        assertThat(contractorMiniDTOList).isNotNull();
        assertThat(contractorMiniDTOList).isNotEmpty();
        long count = contractorRepository.count();
        assertThat(initContractorDTOS.size()).isEqualTo(count);
    }

    @Test
    public void getAllWithContractorFilter() {
        ContractorFilterDTO filterDTO = DatabaseModelsFromJsons.ContractorFilterDTOFContractorBrand();
        assertThat(filterDTO).isNotNull();
        List<ContractorMiniDTO> contractorMiniDTOList = contractorController.getAll(baseUser, filterDTO);
        assertThat(contractorMiniDTOList).isNotNull();
        assertThat(contractorMiniDTOList).isNotEmpty();
        Long count = contractorRepository.count();
        assertThat(contractorMiniDTOList.size()).isLessThan(count.intValue());
        assertThat(contractorMiniDTOList.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    public void getOne() {
        List<Contractor> contractors = contractorRepository.findAll();
        assertThat(contractors).isNotEmpty();
        Contractor contractor = contractors.get(0);
        ContractorDTO dto = contractorController.getOne(contractor.getId());
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(contractor.getId());
        assertThat(dto.getNip()).isEqualTo(contractor.getNip());

    }

    @Test
    public void getOneEntity() {
        List<Contractor> contractors = contractorRepository.findAll();
        assertThat(contractors).isNotEmpty();
        Contractor contractor = contractors.get(0);
        Contractor one = contractorController.getOneEntity(contractor.getId());
        assertThat(one).isNotNull();
        assertThat(one.getNip()).isEqualTo(contractor.getNip());
    }

    @Test
    public void updateOne() {
        ContractorDTO dto = DatabaseModelsFromJsons.contractorDTO(1);
        assertThat(dto).isNotNull();
        Contractor contractor = getContractor(dto);
        dto.setCompany("New name");
        Boolean updated = contractorController.updateOne(dto);
        assertThat(updated).isTrue();
        Contractor entity = contractorController.getOneEntity(contractor.getId());
        assertThat(entity).isNotNull();
        assertThat(entity.getCompany()).isEqualTo(dto.getCompany());
        assertThat(entity.getCompany()).isNotEqualTo(contractor.getCompany());
    }

    @Test
    public void createOne() {
        contractorRepository.deleteAll();
        ContractorDTO dto = DatabaseModelsFromJsons.contractorDTO(1);
        assertThat(dto).isNotNull();
        Boolean one = contractorController.createOne(baseUser, dto);
        assertThat(one).isTrue();
        Contractor contractor = getContractor(dto);
        assertThat(contractor.getCompany()).isEqualTo(dto.getCompany());
        assertThat(contractor.getNip()).isEqualTo(dto.getNip());
    }

    @Test
    public void deleteOne() {
        List<Contractor> contractors = contractorRepository.findAll();
        assertThat(contractors).isNotEmpty();
        Contractor contractor = contractors.get(0);
        Boolean deleted = contractorController.deleteOne(contractor.getId());
        assertThat(deleted).isTrue();
        Optional<Contractor> entity = contractorRepository.findById(contractor.getId());
        assertThat(entity).isEmpty();
        Long count = contractorRepository.count();
        assertThat(initContractorDTOS.size()).isGreaterThan(count.intValue());

    }

    @Test
    public void getAllToSelect() {
        List<ContractorSelectDTO> contractorMiniDTOList = contractorController.getAllToSelect(baseUser);
        assertThat(contractorMiniDTOList).isNotNull();
        assertThat(contractorMiniDTOList).isNotEmpty();
        long count = contractorRepository.count();
        assertThat(initContractorDTOS.size()).isEqualTo(count);
    }

    private Contractor getContractor(ContractorDTO contractorDTO) {
        Contractor contractor = new Contractor();
        contractor.setBaseUser(baseUser);
        contractor.setNip(contractorDTO.getNip());
        contractor.setCompany(contractorDTO.getCompany());
        Example example = Example.of(contractor);
        Optional<Contractor> one = contractorRepository.findOne(example);
        assertThat(one).isNotNull();
        assertThat(one).isNotEmpty();
        contractorDTO.setId(one.get().getId());
        return one.get();
    }
}
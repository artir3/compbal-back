package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.contractor.Contractor;
import com.arma.inz.compcal.contractor.ContractorController;
import com.arma.inz.compcal.contractor.ContractorRepository;
import com.arma.inz.compcal.contractor.dto.ContractorDTO;
import com.arma.inz.compcal.database.DatabaseModelsFromJsons;
import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
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
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@TestComponent
@SpringBootTest
@ActiveProfiles("test")
public class KpirControllerImplTest {
    @Autowired
    private KpirRepository kpirRepository;

    @Autowired
    private ContractorController contractorController;

    @Autowired
    private KpirController kpirController;

    @Autowired
    private BaseUserRepository baseUserRepository;

    @Autowired
    private ContractorRepository contractorRepository;

    private BaseUser baseUser;
    private List<KpirCreateDTO> kpirCreateDTOList;
    private Map<Long, Long> contractorMap = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        baseUser = baseUserRepository.save(DatabaseModelsFromJsons.baseUser());
        for (ContractorDTO contractorDTO : DatabaseModelsFromJsons.contractorDTOList()) {
            Long id = contractorDTO.getId();
            contractorController.createOne(baseUser, contractorDTO);
            Contractor one = getContractor(contractorDTO);
            contractorMap.put(id, one.getId());
        }
        kpirCreateDTOList = DatabaseModelsFromJsons.kpirCreateDTOList();
        for (KpirCreateDTO dtp : kpirCreateDTOList) {
            Long contactorID = contractorMap.get(dtp.getContractor());
            dtp.setContractor(contactorID);
            kpirController.createOne(baseUser, dtp);
        }
    }


    @After
    public void tearDown() throws Exception {
        kpirRepository.deleteAll();
        contractorRepository.deleteAll();
        baseUserRepository.deleteAll();
    }

    @Test
    public void getAll() {
        KpirFilterDTO filterDTO = DatabaseModelsFromJsons.kpirFilterDTOPrint();
        assertThat(filterDTO).isNotNull();
        List<KpirDTO> allList = kpirController.getAll(baseUser, filterDTO);
        assertThat(allList).isNotNull();
        assertThat(allList).isNotEmpty();
        filterDTO = DatabaseModelsFromJsons.KpirFilterDTOIncomeEmpty();
        assertThat(filterDTO).isNotNull();
        List<KpirDTO> incomeList = kpirController.getAll(baseUser, filterDTO);
        assertThat(incomeList).isNotNull();
        assertThat(incomeList).isNotEmpty();
        filterDTO = DatabaseModelsFromJsons.kpirFilterDTOCostsEmpty();
        assertThat(filterDTO).isNotNull();
        List<KpirDTO> costList = kpirController.getAll(baseUser, filterDTO);
        assertThat(costList).isNotNull();
        assertThat(costList).isNotEmpty();
        Long count = kpirRepository.count();
        assertThat(allList.size()).isLessThanOrEqualTo(count.intValue());
        assertThat(incomeList.size()).isLessThanOrEqualTo(allList.size());
        assertThat(costList.size()).isLessThanOrEqualTo(allList.size());
        assertThat(allList.size()).isEqualTo(incomeList.size() + costList.size());
    }

    @Test
    public void getOne() {
        Kpir lastKpir = getKpir();
        KpirCreateDTO kpirCreateDTO = kpirController.getOne(lastKpir.getId());
        assertThat(kpirCreateDTO).isNotNull();
    }

    @Test
    public void getOverduePayment() {
        Kpir lastKpir = getKpir();
        KpirCreateDTO kpirCreateDTO = kpirController.getOne(lastKpir.getId());
        assertThat(kpirCreateDTO).isNotNull();
        assertThat(kpirCreateDTO.getOverduePayment()).isNotNull();
    }

    @Test
    public void createOneFirst() {
        kpirRepository.deleteAll();
        KpirCreateDTO kpirCreateDTO = DatabaseModelsFromJsons.kpirCreateDTOIncome(2);
        assertThat(kpirCreateDTO).isNotNull();
        Long contractorId = contractorMap.get(kpirCreateDTO.getContractor());
        assertThat(contractorId).isNotNull();
        kpirCreateDTO.setContractor(contractorId);
        Boolean one = kpirController.createOne(baseUser, kpirCreateDTO);
        assertThat(one).isTrue();
        Kpir kpir = getKpir();
        assertThat(kpir.getId()).isNotNull();
        assertThat(kpir.getIdx()).isNotNull();
        assertThat(kpir.getIdx()).isNotEqualTo(0);
        assertThat(kpir.getIdx()).isEqualTo(1);
    }

    @Test
    public void createOne() {
        kpirRepository.deleteAll();
        KpirCreateDTO kpirCreateDTO = DatabaseModelsFromJsons.kpirCreateDTOIncome(2);
        assertThat(kpirCreateDTO).isNotNull();
        Long contractorId = contractorMap.get(kpirCreateDTO.getContractor());
        assertThat(contractorId).isNotNull();
        kpirCreateDTO.setContractor(contractorId);
        Boolean one = kpirController.createOne(baseUser, kpirCreateDTO);
        assertThat(one).isTrue();
        KpirFilterDTO filterDTO = DatabaseModelsFromJsons.KpirFilterDTOIncomeEmpty();
        assertThat(filterDTO).isNotNull();
        filterDTO.setSelectedYear(kpirCreateDTO.getEconomicEventDate().getYear());
        List<KpirDTO> incomeList = kpirController.getAllForPrint(baseUser, filterDTO);
        int count = 1;
        for (KpirDTO dto : incomeList) {
            assertThat(dto.getIdx()).isEqualTo(count++);
        }
    }

    private Kpir getKpir() {
        List<Kpir> all = kpirRepository.findAll(Sort.by(Sort.Order.desc("id")));
        assertThat(all).isNotNull();
        assertThat(all).isNotEmpty();
        return all.get(0);
    }

    @Test
    public void updateOne() {
    }

    @Test
    public void deleteOne() {
    }

    @Test
    public void getNextIdx() {
        Integer nextIdx = kpirController.getNextIdx(baseUser);
        assertThat(nextIdx).isNotNull();
        assertThat(nextIdx).isNotZero();
    }

    @Test
    public void getNextIdx1() {
        Integer nextIdx = kpirController.getNextIdx(LocalDateTime.MIN, baseUser);
        assertThat(nextIdx).isNotNull();
        assertThat(nextIdx).isNotZero();
        assertThat(nextIdx).isOne();
    }

    @Test
    public void getAllForPrint() {
        KpirFilterDTO filterDTO = DatabaseModelsFromJsons.kpirFilterDTOPrint();
        assertThat(filterDTO).isNotNull();
        List<KpirDTO> allList = kpirController.getAll(baseUser, filterDTO);
        assertThat(allList).isNotNull();
        assertThat(allList).isNotEmpty();
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
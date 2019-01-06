package com.arma.inz.compcal.printer;

import com.arma.inz.compcal.contractor.Contractor;
import com.arma.inz.compcal.contractor.ContractorController;
import com.arma.inz.compcal.contractor.ContractorRepository;
import com.arma.inz.compcal.contractor.dto.ContractorDTO;
import com.arma.inz.compcal.database.DatabaseModelsFromJsons;
import com.arma.inz.compcal.kpir.KpirController;
import com.arma.inz.compcal.kpir.KpirRepository;
import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import com.arma.inz.compcal.mail.Email;
import com.arma.inz.compcal.mail.EmailRepository;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@TestComponent
@SpringBootTest
@ActiveProfiles("test")
public class JasperControllerImplTest {

    @Autowired
    private JasperController jasperController;

    @Autowired
    private KpirController kpirController;

    @Autowired
    private BaseUserRepository baseUserRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private ContractorController contractorController;

    @Autowired
    private ContractorRepository contractorRepository;

    @Autowired
    private KpirRepository kpirRepository;

    private BaseUser baseUser;

    @Before
    public void setUp() throws Exception {
        Map<Long, Long> contractorMap = new HashMap<>();
        baseUser = baseUserRepository.save(DatabaseModelsFromJsons.baseUser());
        for (ContractorDTO contractorDTO : DatabaseModelsFromJsons.contractorDTOList()) {
            contractorController.createOne(baseUser, contractorDTO);
            Contractor one = getContractor(contractorDTO);
            contractorMap.put(contractorDTO.getId(), one.getId());
        }
        for (KpirCreateDTO dtp : DatabaseModelsFromJsons.kpirCreateDTOList()) {
            Long contractorId = contractorMap.get(dtp.getContractor());
            dtp.setContractor(contractorId);
            kpirController.createOne(baseUser, dtp);
        }

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
        return one.get();
    }

    @After
    public void tearDown() throws Exception {
        kpirRepository.deleteAll();
        contractorRepository.deleteAll();
        emailRepository.deleteAll();
        baseUserRepository.deleteAll();
    }

    @Test
    public void getKpirFile() throws IOException {
        KpirFilterDTO filterDTO = DatabaseModelsFromJsons.kpirFilterDTOPrint();
        File pdfFile = jasperController.getKpirFile(baseUser, filterDTO);
        assertThat(pdfFile).isNotNull();
        assertThat(Files.exists(pdfFile.toPath())).isTrue();
        assertThat(Files.size(pdfFile.toPath())).isGreaterThan(0);
        Files.delete(pdfFile.toPath());
    }

    @Test
    public void sendKpirPdftoMail() {
        KpirFilterDTO filterDTO = DatabaseModelsFromJsons.kpirFilterDTOPrint();
        assertThat(emailRepository.count()).isEqualTo(0);
        jasperController.sendKpirPdftoMail(baseUser, filterDTO);
        List<Email> emails = emailRepository.findAll();
        assertThat(emails).isNotNull();
        assertThat(emails).isNotEmpty();
    }
}
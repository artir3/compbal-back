package com.arma.inz.compcal.users;

import com.arma.inz.compcal.bankaccount.BankAccountRepository;
import com.arma.inz.compcal.database.DatabaseModelsFromJsons;
import com.arma.inz.compcal.mail.Email;
import com.arma.inz.compcal.mail.EmailRepository;
import com.arma.inz.compcal.users.dto.ActivateDTO;
import com.arma.inz.compcal.users.dto.UserDTO;
import com.arma.inz.compcal.users.dto.UserLoginDTO;
import com.arma.inz.compcal.users.dto.UserRegistrationDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@TestComponent
@SpringBootTest
@ActiveProfiles("test")
public class BaseUserControllerImplTest {

    @Autowired
    private BaseUserController baseUserController;

    @Autowired
    private BaseUserRepository baseUserRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;


    @Before
    public void setUp() throws Exception {
        baseUserRepository.save(DatabaseModelsFromJsons.baseUser());
    }

    @After
    public void tearDown() throws Exception {
        emailRepository.deleteAll();
        bankAccountRepository.deleteAll();
        baseUserRepository.deleteAll();
    }

    @Test
    public void registration() {
        baseUserRepository.deleteAll();
        UserRegistrationDTO dto = DatabaseModelsFromJsons.userRegistrationDTO();
        boolean registration = baseUserController.registration(dto);
        BaseUser user = baseUserRepository.findByEmail(dto.getEmail());
        assertThat(registration).isTrue();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("butu@mailinator.net");
        List<Email> emails = emailRepository.findAll();
        assertThat(emails).isNotEmpty();
    }

    @Test
    public void login() {
        baseUserRepository.deleteAll();
        baseUserRepository.save(DatabaseModelsFromJsons.baseUserActivated());
        UserLoginDTO dto = DatabaseModelsFromJsons.userLoginDTO();
        boolean login = baseUserController.login(dto);
        assertThat(login).isTrue();
    }

    @Test
    public void loginByHash() {
        baseUserRepository.deleteAll();
        baseUserRepository.save(DatabaseModelsFromJsons.baseUserActivated());
        String dto = DatabaseModelsFromJsons.hash();
        boolean login = baseUserController.loginByHash(dto);
        assertThat(login).isTrue();
    }

    @Test
    public void getUserDTO() {
        String hash = DatabaseModelsFromJsons.hash();
        UserDTO userDTO = baseUserController.getUserDTO(hash);
        assertThat(userDTO.getEmail()).isEqualTo("butu@mailinator.net");
    }

    @Test
    public void getBaseUser() {
        String hash = DatabaseModelsFromJsons.hash();
        BaseUser user = baseUserController.getBaseUser(hash);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("butu@mailinator.net");
    }

    @Test
    public void updateBaseUser() {
        UserDTO baseUserDTO = DatabaseModelsFromJsons.userDTOPassword();
        assertThat(baseUserDTO).isNotNull();
        BaseUser actualBaseUser = baseUserRepository.findByEmail(baseUserDTO.getEmail());
        assertThat(actualBaseUser).isNotNull();
        baseUserDTO.setId(actualBaseUser.getId());
        baseUserController.updateBaseUser(baseUserDTO);
        BaseUser upgradedBaseUser = baseUserRepository.findById(actualBaseUser.getId()).get();
        assertThat(upgradedBaseUser).isNotNull();
        assertThat(upgradedBaseUser.getHash()).isNotEqualTo(actualBaseUser.getHash());
        assertThat(upgradedBaseUser.getPassword()).isNotEqualTo(actualBaseUser.getPassword());
        assertThat(upgradedBaseUser.getCreatedAt()).isEqualTo(actualBaseUser.getCreatedAt());
    }

    @Test
    public void authorizeFullHash() {
        ActivateDTO dto = DatabaseModelsFromJsons.activateFullDTO();
        boolean activate = baseUserController.authorize(dto);
        assertThat(activate).isTrue();
        BaseUser user = baseUserRepository.findOneByHash(DatabaseModelsFromJsons.hash());
        assertThat(user).isNotNull();
        assertThat(user.isActive()).isTrue();
    }

    @Test
    public void authorizeHashFromAngular() {
        ActivateDTO dto = DatabaseModelsFromJsons.activateDTOFromAngular();
        boolean activate = baseUserController.authorize(dto);
        assertThat(activate).isTrue();
        BaseUser user = baseUserRepository.findOneLikeHash(DatabaseModelsFromJsons.hash());
        assertThat(user).isNotNull();
        assertThat(user.isActive()).isTrue();
    }

    @Test
    public void authorizeWrongHash() {
        ActivateDTO dto = new ActivateDTO("wrong");
        boolean activate = baseUserController.authorize(dto);
        assertThat(activate).isFalse();
        BaseUser user = baseUserRepository.findOneByHash(DatabaseModelsFromJsons.hash());
        assertThat(user).isNotNull();
        assertThat(user.isActive()).isFalse();
    }

    @Test
    public void registrationDate() {
        String hash = DatabaseModelsFromJsons.hash();
        LocalDateTime registrationDate = baseUserController.registrationDate(hash);
        assertThat(registrationDate).isNotNull();
    }

}
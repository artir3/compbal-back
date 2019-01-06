package com.arma.inz.compcal.mail;

import com.arma.inz.compcal.database.DatabaseModelsFromJsons;
import com.arma.inz.compcal.users.BaseUser;
import com.arma.inz.compcal.users.BaseUserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@TestComponent
@SpringBootTest
@ActiveProfiles("test")
public class EmailControllerImplTest {

    @Autowired
    private EmailController emailController;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private BaseUserRepository baseUserRepository;

    BaseUser baseUser;

    @Before
    public void setUp() throws Exception {
        baseUser = baseUserRepository.save(DatabaseModelsFromJsons.baseUser());
    }

    @After
    public void tearDown() throws Exception {
        emailRepository.deleteAll();
        baseUserRepository.deleteAll();
    }

    @Test
    public void sendSimpleMessage() {
        Email email = DatabaseModelsFromJsons.email(baseUser);
        emailController.sendSimpleMessage("test@localhost", email.getSubject(), email.getText(), null);
        Optional<Email> optional = emailRepository.findById(2l);
        assertThat(optional.get()).isNotNull();
        assertThat(optional.get().getStatus()).isEqualTo(EmailStatusEnum.SENT);
        assertThat(optional.get().getFileName()).isNull();
    }

    @Test
    public void sendMessageWithAttachment() throws IOException {
        Email email = DatabaseModelsFromJsons.email(baseUser);
        File file = File.createTempFile("test","pdf");
        emailController.sendMessageWithAttachment("test@localhost", email.getSubject(), email.getText(), file,baseUser);
        Optional<Email> optional = emailRepository.findById(2l);
        assertThat(optional.get()).isNotNull();
        assertThat(optional.get().getStatus()).isEqualTo(EmailStatusEnum.SENT);
        assertThat(optional.get().getFileName()).isNotNull();
        assertThat(optional.get().getFileName()).isEqualToIgnoringCase("test.pdf");
    }
}
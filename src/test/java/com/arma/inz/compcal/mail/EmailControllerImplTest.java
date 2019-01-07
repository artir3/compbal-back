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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;
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

    private BaseUser baseUser;

    @Before
    public void setUp() {
        baseUser = baseUserRepository.save(DatabaseModelsFromJsons.baseUser());
    }

    @After
    public void tearDown() {
        emailRepository.deleteAll();
        baseUserRepository.deleteAll();
    }

    @Test
    public void sendSimpleMessage() {
        Email email = DatabaseModelsFromJsons.email(baseUser);
        emailController.sendSimpleMessage("test@localhost", email.getSubject(), email.getText(), baseUser);
        Email email1 = getEmail();
        assertThat(email1.getStatus()).isEqualTo(EmailStatusEnum.SENT);
        assertThat(email1.getFileName()).isNull();
    }

    @Test
    public void sendMessageWithAttachment() throws IOException {
        Email email = DatabaseModelsFromJsons.email(baseUser);
        File file = File.createTempFile("test", ".pdf");
        emailController.sendMessageWithAttachment("test@localhost", email.getSubject(), email.getText(), file,baseUser);
        Email email1 = getEmail();
        assertThat(email1.getStatus()).isEqualTo(EmailStatusEnum.SENT);
        assertThat(email1.getFileName()).isNotNull();
        assertThat(email1.getFileName()).matches("test\\w+\\.pdf");
    }

    private Email getEmail() {
        List<Email> list = emailRepository.findAll(Sort.by(Sort.Order.desc("id")));
        assertThat(list).isNotNull();
        assertThat(list).isNotEmpty();
        return list.get(0);
    }

}
package com.arma.inz.compcal.users;

import com.arma.inz.compcal.database.DatabaseModelsFromJsons;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class BaseUserRepositoryTest {

    @Autowired
    private BaseUserRepository repository;

    @Before
    public void initialize() {
        this.repository.save(DatabaseModelsFromJsons.baseUser());
    }

    @After
    public void clean() {
        this.repository.deleteAll();
    }

    @Test
    public void testFindByEmail() {
        BaseUser user = this.repository.findByEmail("butu@mailinator.net");
        assertThat(user.getHash()).isEqualTo("YnV0dUBtYWlsaW5hdG9yLm5ldDpjeXd5Z2V4YQ==");
    }

    @Test
    public void testFindOneByHash() {
        BaseUser user = this.repository.findOneByHash("YnV0dUBtYWlsaW5hdG9yLm5ldDpjeXd5Z2V4YQ==");
        assertThat(user.getEmail()).isEqualTo("butu@mailinator.net");
    }

    @Test
    public void testFindOneLikeHash() {
        BaseUser user = this.repository.findOneLikeHash("YnV0dUBtYWlsaW5hdG9yLm5ldDpjeXd5Z2V4YQ");
        assertThat(user.getEmail()).isEqualTo("butu@mailinator.net");
    }

    @Test
    public void testNotFindOneLikeHash() {
        BaseUser user = this.repository.findOneLikeHash("YnV0dUBtYWlsaadsW5hddG9yLm5ldDpjeXd5Z2V4YQ");
        assertThat(user).isNull();
    }

}
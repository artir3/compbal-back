package com.arma.inz.compcal.users;

import com.arma.inz.compcal.database.DatabaseModelsFromJsons;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@TestComponent
@SpringBootTest
@ActiveProfiles("test")
public class AuthorizationHeaderUtilsTest {

    @Autowired
    private AuthorizationHeaderUtils authorizationHeaderUtils;

    @Autowired
    private BaseUserRepository baseUserRepository;

    @Before
    public void setUp() throws Exception {
        baseUserRepository.save(DatabaseModelsFromJsons.baseUserPassword());
    }

    @After
    public void tearDown() throws Exception {
        baseUserRepository.deleteAll();
    }

    @Test
    public void hashFromHeaderIsNotNull() {
        String authorization = DatabaseModelsFromJsons.authorization();
        String hashFromHeader = authorizationHeaderUtils.hashFromHeader(authorization);
        assertThat(hashFromHeader).isNotNull();
    }
    @Test
    public void hashFromHeaderIsNotEmpty() {
        String authorization = DatabaseModelsFromJsons.authorization();
        String hashFromHeader = authorizationHeaderUtils.hashFromHeader(authorization);
        assertThat(hashFromHeader).isNotEmpty();
    }
    @Test
    public void hashFromHeaderIsEqual() {
        String hash = DatabaseModelsFromJsons.hashUpdated();
        String authorization = DatabaseModelsFromJsons.authorization();
        String hashFromHeader = authorizationHeaderUtils.hashFromHeader(authorization);
        assertThat(hashFromHeader).isEqualTo(hash);
    }

    @Test
    public void getUserFromAuthorization() {
        String authorization = DatabaseModelsFromJsons.authorization();
        BaseUser baseUser = authorizationHeaderUtils.getUserFromAuthorization(authorization);
        assertThat(baseUser).isNotNull();
        BaseUser user = DatabaseModelsFromJsons.baseUserPassword();
        assertThat(baseUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(baseUser.getPassword()).isEqualTo(user.getPassword());
    }
}
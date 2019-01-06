package com.arma.inz.compcal.users;

import com.arma.inz.compcal.bankaccount.BankAccountController;
import com.arma.inz.compcal.database.DatabaseModelsFromJsons;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BaseUserControllerImplTest {

    @Autowired
    private BaseUserRepository repository;

    @Autowired
    private BankAccountController bankAccountController;

    @Autowired
    private BaseUserMailSender baseUserMailSender;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setUp() throws Exception {
        this.repository.save(DatabaseModelsFromJsons.baseUser());
    }

    @After
    public void tearDown() throws Exception {
        this.repository.deleteAll();
    }

    @Test
    public void registration() {
    }

    @Test
    public void login() {
    }

    @Test
    public void loginByHash() {
    }

    @Test
    public void getUserDTO() {
    }

    @Test
    public void getBaseUser() {
    }

    @Test
    public void updateBaseUser() {
    }

    @Test
    public void authorize() {
    }

    @Test
    public void registrationDate() {
    }
}
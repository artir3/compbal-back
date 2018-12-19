package com.arma.inz.compcal.bankaccount;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private BankAccountController bankAccountController;

    @Override
    public ResponseEntity deleteAccount(Long id) {
        boolean delete = bankAccountController.delete(id);
        return new ResponseEntity<>(delete, HttpStatus.OK);
    }
}

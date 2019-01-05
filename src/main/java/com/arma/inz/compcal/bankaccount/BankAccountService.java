package com.arma.inz.compcal.bankaccount;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins ="http://46.101.227.24:8080")
@RequestMapping("/account")
public interface BankAccountService {
    @CrossOrigin
    @DeleteMapping("/{id}")
    ResponseEntity deleteAccount(@PathVariable Long id);
}

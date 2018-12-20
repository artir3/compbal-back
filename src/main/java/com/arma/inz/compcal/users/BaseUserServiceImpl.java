package com.arma.inz.compcal.users;

import com.arma.inz.compcal.AuthorizationHeaderUtils;
import com.arma.inz.compcal.users.dto.UserDTO;
import com.arma.inz.compcal.users.dto.UserLoginDTO;
import com.arma.inz.compcal.users.dto.UserRegistrationDTO;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Log
@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class BaseUserServiceImpl implements BaseUserService {

    @Autowired
    private BaseUserController baseUserController;

    @Autowired
    private AuthorizationHeaderUtils header;

    @Override
    public ResponseEntity registration(UserRegistrationDTO user) {
        boolean registration = baseUserController.registration(user);
        return new ResponseEntity<>(registration, HttpStatus.OK);
    }

    @Override
    public ResponseEntity login(UserLoginDTO user) {
        boolean login = baseUserController.login(user);
        return new ResponseEntity<>(login, HttpStatus.OK);
    }

    @Override
    public ResponseEntity loginHash(String authorization) {
        boolean login = baseUserController.loginByHash(header.hashFromHeader(authorization));
        return new ResponseEntity<>(login, HttpStatus.OK);
    }

    @Override
    public ResponseEntity get(String authorization) {
        UserDTO baseUser = baseUserController.getUserDTO(header.hashFromHeader(authorization));
        return new ResponseEntity<>(baseUser, HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(UserDTO userDTO) {
        boolean update = baseUserController.updateBaseUser(userDTO);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @Override
    public ResponseEntity authorize(String authorizationHash) {
        boolean authorize = baseUserController.authorize(authorizationHash);
        return new ResponseEntity<>(authorize, HttpStatus.OK);
    }

    @Override
    public ResponseEntity registrationDate(String authorization) {
        LocalDateTime registrationDate = baseUserController.registrationDate(header.hashFromHeader(authorization));
        return new ResponseEntity<>(registrationDate, HttpStatus.OK);
    }


}
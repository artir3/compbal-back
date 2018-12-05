package com.arma.inz.compcal.users;

import com.arma.inz.compcal.AuthorizationHeaderUtils;
import com.arma.inz.compcal.users.dto.UserDTO;
import com.arma.inz.compcal.users.dto.UserLoginDTO;
import com.arma.inz.compcal.users.dto.UserRegistrationDTO;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
public class BaseUserServiceImpl implements BaseUserService {

    @Autowired
    private BaseUserController baseUserController;
    
    @Autowired
    private AuthorizationHeaderUtils header;

    @Override
    public ResponseEntity registration(UserRegistrationDTO user) {
        boolean registration = baseUserController.registration(user);
        return new ResponseEntity( registration, HttpStatus.OK);
    }

    @Override
    public ResponseEntity login(UserLoginDTO user) {
        boolean login = baseUserController.login(user);
        return new ResponseEntity( login, HttpStatus.OK);
    }

    @Override
    public ResponseEntity loginByHash(String authorization) {
        boolean login = baseUserController.loginByHash(header.hashFromHeader(authorization));
        return new ResponseEntity( login, HttpStatus.OK);
    }

    @Override
    public ResponseEntity get(String authorization) {
        BaseUser baseUser = baseUserController.getBaseUser(header.hashFromHeader(authorization));
        return new ResponseEntity( baseUser, HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(UserDTO userDTO) {
        boolean update = baseUserController.updateBaseUser(userDTO);
        return new ResponseEntity( update, HttpStatus.OK);
    }

    @Override
    public ResponseEntity authorize(String authorizationHash) {
        boolean authorize = baseUserController.authorize(authorizationHash);
        return new ResponseEntity( authorize, HttpStatus.OK);
    }

}
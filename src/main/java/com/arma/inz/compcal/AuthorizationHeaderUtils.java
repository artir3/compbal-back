package com.arma.inz.compcal;

import com.arma.inz.compcal.users.BaseUser;
import com.arma.inz.compcal.users.BaseUserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHeaderUtils {

    @Autowired
    private BaseUserController baseUserController;


    public String hashFromHeader(String authorization){
        return authorization.substring(6).trim();
    }

    public BaseUser getUserFromAuthorization(String authorization){
        return baseUserController.getBaseUser(hashFromHeader(authorization));
    }
}

package com.arma.inz.compcal.users;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AuthorizationHeaderUtils {
    private final BaseUserController baseUserController;

    public String hashFromHeader(String authorization){
        return authorization.substring(6).trim();
    }

    public BaseUser getUserFromAuthorization(String authorization){
        return baseUserController.getBaseUser(hashFromHeader(authorization));
    }
}

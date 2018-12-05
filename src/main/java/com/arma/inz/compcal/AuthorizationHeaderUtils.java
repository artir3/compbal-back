package com.arma.inz.compcal;

import org.springframework.stereotype.Component;

@Component
public class AuthorizationHeaderUtils {
    public String hashFromHeader(String authorization){
        return authorization.substring(6).trim();
    }
}

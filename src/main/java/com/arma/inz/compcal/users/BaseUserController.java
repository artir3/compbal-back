package com.arma.inz.compcal.users;

import com.arma.inz.compcal.users.dto.UserDTO;
import com.arma.inz.compcal.users.dto.UserLoginDTO;
import com.arma.inz.compcal.users.dto.UserRegistrationDTO;
import org.springframework.web.bind.annotation.RequestBody;

public interface BaseUserController {
    boolean registration(UserRegistrationDTO user);

    boolean login(UserLoginDTO user);

    boolean loginByHash(@RequestBody String hash);

    UserDTO getUserDTO(@RequestBody String hash);

    BaseUser getBaseUser(@RequestBody String hash);

    boolean updateBaseUser(UserDTO userDTO);

    boolean authorize(String authorizationHash);

    boolean deleteAccount(Long id);
}

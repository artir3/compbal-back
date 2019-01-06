package com.arma.inz.compcal.database;

import com.arma.inz.compcal.MapperToObject;
import com.arma.inz.compcal.mail.Email;
import com.arma.inz.compcal.users.BaseUser;
import com.arma.inz.compcal.users.dto.ActivateDTO;
import com.arma.inz.compcal.users.dto.UserLoginDTO;
import com.arma.inz.compcal.users.dto.UserRegistrationDTO;

import java.time.LocalDateTime;

public class DatabaseModelsInitialization {
    public static BaseUser baseUser() {
        BaseUser object = new BaseUser();
        object = (BaseUser) MapperToObject.FileToObject(object);
        object.setCreatedAt(LocalDateTime.now());
        object.setModifiedAt(LocalDateTime.now());
        return object;
    }

    public static UserRegistrationDTO userRegistrationDTO() {
        UserRegistrationDTO object = new UserRegistrationDTO();
        object = (UserRegistrationDTO) MapperToObject.FileToObject(object);
        return object;
    }

    public static ActivateDTO activateDTOFromAngular() {
        ActivateDTO object = new ActivateDTO();
        object = (ActivateDTO) MapperToObject.FileToObject(object);
        return object;
    }

    public static ActivateDTO activateFullDTO() {
        return new ActivateDTO("YnV0dUBtYWlsaW5hdG9yLm5ldDpjeXd5Z2V4YQ==");
    }

    public static Email email() {
        Email object = new Email();
        object = (Email) MapperToObject.FileToObject(object);
        return object;
    }

    public static UserLoginDTO userLoginDTO() {
        UserLoginDTO object = new UserLoginDTO();
        object = (UserLoginDTO) MapperToObject.FileToObject(object);
        return object;
    }
}

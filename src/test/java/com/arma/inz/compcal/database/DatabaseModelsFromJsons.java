package com.arma.inz.compcal.database;

import com.arma.inz.compcal.MapperToObject;
import com.arma.inz.compcal.mail.Email;
import com.arma.inz.compcal.users.BaseUser;
import com.arma.inz.compcal.users.dto.ActivateDTO;
import com.arma.inz.compcal.users.dto.UserLoginDTO;
import com.arma.inz.compcal.users.dto.UserRegistrationDTO;

import java.time.LocalDateTime;

public class DatabaseModelsFromJsons {
    public static BaseUser baseUser() {
        BaseUser object = new BaseUser();
        object = (BaseUser) MapperToObject.FileToObject(object);
        object.setCreatedAt(LocalDateTime.now());
        object.setModifiedAt(LocalDateTime.now());
        return object;
    }

    public static UserRegistrationDTO userRegistrationDTO() {
        return (UserRegistrationDTO) MapperToObject.FileToObject(new UserRegistrationDTO());
    }

    public static ActivateDTO activateDTOFromAngular() {
        return (ActivateDTO) MapperToObject.FileToObject(new ActivateDTO(), "ActivateDTOFromAngular");
    }

    public static ActivateDTO activateFullDTO() {
        return (ActivateDTO) MapperToObject.FileToObject(new ActivateDTO());
    }

    public static Email email(BaseUser baseUser) {
        Email object = (Email) MapperToObject.FileToObject(new Email());
        object.setCreatedAt(LocalDateTime.now());
        object.addBaseUser(baseUser);
        return object;
    }

    public static UserLoginDTO userLoginDTO() {
        return (UserLoginDTO) MapperToObject.FileToObject(new UserLoginDTO());
    }

    public static String hash() {
        return (String) MapperToObject.FileToObject("", "Hash");
    }
}

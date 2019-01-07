package com.arma.inz.compcal.mail;

import com.arma.inz.compcal.users.BaseUser;

import java.io.File;

public interface EmailController {
    void sendSimpleMessage(String to, String subject, String text, BaseUser baseUser);

    void sendSimpleMessage(BaseUser baseUser, String subject, String text);

    void sendMessageWithAttachment(BaseUser baseUser, String subject, String text, File file);

    void sendMessageWithAttachment(String to, String subject, String text, File file, BaseUser baseUser);

}

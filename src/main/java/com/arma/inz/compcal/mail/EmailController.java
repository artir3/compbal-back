package com.arma.inz.compcal.mail;

import java.io.File;

public interface EmailController {
    void sendSimpleMessage(String to, String subject, String text);

    void sendMessageWithAttachment(String to, String subject, String text, File file);

}

package com.arma.inz.compcal.mail;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);

    void sendActivationEmail(String email, String hash);
}

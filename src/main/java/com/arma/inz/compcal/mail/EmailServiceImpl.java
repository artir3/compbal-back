package com.arma.inz.compcal.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${service.address}")
    private String serviceAddress;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendActivationEmail(String email, String hash) {
        String header = "Comp-Cal - Aktywacja konta";
        String message = "Witam\n\n" +
                "Dziękujemy za rejestrację w CompCal, serwisie do obsługi finansów w firmie.\n\n" +
                "W celu aktywacji konta należy kliknąć w poniższy link, lub przekopiować go w adres przeglądarki.\n\n" +
                "http://" + serviceAddress + "/activate/:" + hash +
                "\n\n" +
                "Życzymy miłego dnia"
                ;
        sendSimpleMessage(email, header, message);
    }

}
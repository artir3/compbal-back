package com.arma.inz.compcal.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender emailSender;

    @Value("${service.address}")
    private String serviceAddress;

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

//    @Override
//    public void sendMessageWithAttachment(
//            String to, String subject, String text, String pathToAttachment) {
//        // ...
//
//        MimeMessage message = emailSender.createMimeMessage();
//
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//        helper.setTo(to);
//        helper.setSubject(subject);
//        helper.setText(text);
//
//        FileSystemResource file
//                = new FileSystemResource(new File(pathToAttachment));
//        helper.addAttachment("Invoice", file);
//
//        emailSender.send(message);
//        // ...
//    }
}
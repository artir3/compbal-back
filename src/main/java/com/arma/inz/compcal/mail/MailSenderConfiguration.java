package com.arma.inz.compcal.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSenderConfiguration {

    @Value("${smpt.mail.host}")
    private String smtpMailHost;
    @Value("${smpt.mail.port}")
    private int smtpMailPort;
    @Value("${smpt.mail.user}")
    private String smtpMailUser;
    @Value("${smpt.mail.password}")
    private String smtpMailPassword;
    @Value("${smpt.mail.protocol}")
    private String smtpMailProtocol;
    @Value("${smpt.mail.debug}")
    private String smtpMailDebug;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpMailHost);
        mailSender.setPort(smtpMailPort);

        mailSender.setUsername(smtpMailUser);
        mailSender.setPassword(smtpMailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", smtpMailProtocol);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", smtpMailDebug);

        return mailSender;
    }
}

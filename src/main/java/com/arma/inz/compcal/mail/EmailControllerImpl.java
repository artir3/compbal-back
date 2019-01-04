package com.arma.inz.compcal.mail;

import com.arma.inz.compcal.users.BaseUser;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class EmailControllerImpl implements EmailController {

    private final JavaMailSender emailSender;
    private final EmailRepository emailRepository;

    @Override
    public void sendSimpleMessage(String to, String subject, String text, BaseUser baseUser) {
        Long id = saveMessage(baseUser, subject, text, null);
        sendMessage(to, subject, text);
        markAsSent(id);
    }

    @Override
    public void sendMessageWithAttachment(String to, String subject, String text, File file, BaseUser baseUser) {
        Long id = saveMessage(baseUser, subject, text, file);
        sendMessage(to, subject, text, file);
        markAsSent(id);
    }

    private void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }


    private void sendMessage(String to, String subject, String text, File file) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            FileSystemResource fileSystemResource = new FileSystemResource(file);
            helper.addAttachment(file.getName(), fileSystemResource);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        emailSender.send(message);
    }

    private void markAsSent(Long id) {
        Optional<Email> optional = emailRepository.findById(id);
        if (!optional.isEmpty()) {
            optional.get().setStatus(EmailStatusEnum.SENT);
            emailRepository.save(optional.get());
        }
    }

    private Long saveMessage(BaseUser baseUser, String subject, String text, File file) {
        Email message = new Email(baseUser, EmailStatusEnum.READY_TO_SEND, subject, text, file.getName());
        message = emailRepository.save(message);
        return message.getId();
    }
}
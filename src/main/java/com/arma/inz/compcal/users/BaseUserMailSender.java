package com.arma.inz.compcal.users;

import com.arma.inz.compcal.mail.EmailController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class BaseUserMailSender {
    private final EmailController emailController;

    @Value("${service.address}")
    private String serviceAddress;

    public BaseUserMailSender(EmailController emailController) {
        this.emailController = emailController;
    }

    void sendActivationEmail(String email, String hash, BaseUser baseUser) {
        String header = "Comp-Cal - Aktywacja konta";
        String message = "Witam\n\n" +
                "Dziękujemy za rejestrację w CompCal, serwisie do obsługi finansów w firmie.\n\n" +
                "W celu aktywacji konta należy kliknąć w poniższy link, lub przekopiować go w adres przeglądarki.\n\n" +
                "http://" + serviceAddress + "/activate/" + hash + "\n\n" +
                "Życzymy miłego dnia\n";
        emailController.sendSimpleMessage(email, header, message, baseUser);
    }

}

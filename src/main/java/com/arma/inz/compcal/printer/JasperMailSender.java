package com.arma.inz.compcal.printer;

import com.arma.inz.compcal.mail.EmailController;
import com.arma.inz.compcal.users.BaseUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.io.File;

@AllArgsConstructor
@Controller
public class JasperMailSender {

    private final EmailController emailController;

    void sendKpirEmailWithFile(BaseUser baseUser, File file) {
        String header = "Comp-Cal - wydruk księgi przychodów i rozchodów";
        String message = "Witaj\n\n" +
                "Jeżeli nie chciałeś wygenerować swojej księgi przychodów i rozchodów to usuń tą wiadomość.\n\n" +
                "Twoją ksiegą przychodów i rozchodów została wygenerowana wedle twoich specyfikacji i dodana jako załącznik do bierzącej wiadomosci.\n\n" +
                "Życzymy miłego dnia!\n\n";
        emailController.sendMessageWithAttachment(baseUser.getEmail(), header, message, file, baseUser);
    }
}

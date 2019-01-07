package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.contractor.Contractor;
import com.arma.inz.compcal.contractor.ContractorController;
import com.arma.inz.compcal.mail.EmailController;
import com.arma.inz.compcal.users.BaseUser;
import com.arma.inz.compcal.users.BaseUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Controller
public class OverdueInformationViaMailScheduler {
    private final BaseUserRepository baseUserRepository;
    private final ContractorController contractorController;
    private final EmailController emailController;

    @Scheduled(cron = "${overdue.information.via.mail}")
    private void findAndSendAllOverdueKpir() {
        List<BaseUser> users = baseUserRepository.findAll();
        users.forEach(baseUser -> {
            StringBuilder debtorSB = new StringBuilder();
            StringBuilder creditorSB = new StringBuilder();
            prepareList(debtorSB, creditorSB, baseUser);
            String subject = "Comp-cal - Zestawienie nieopłaconych faktur";
            StringBuilder sb = new StringBuilder();
            sb.append("Witamy\n\n")
                    .append("Poniżej przesyłamy zestawienie z listą nieopłaconych ewidencji podatkowych. W celu zdobycia dodatkowych informacji piroszę zalogować się do serwisu.\n\n")
                    .append("Lista kontrachentów będących dłużnikami: \n\n").append(debtorSB.toString()).append("\n\n")
                    .append("Lista kontrachentów będących wierzycielami: \n\n").append(creditorSB.toString()).append("\n\n\n")
                    .append("Życzymy udanego dnia.");
            emailController.sendSimpleMessage(baseUser, subject, sb.toString());
        });
    }

    private void prepareList(StringBuilder debtorSB, StringBuilder creditorSB, BaseUser baseUser) {
        contractorController.getAllToSelect(baseUser).forEach(contractorSelectDTO -> {
            Contractor contracotr = contractorController.getOneEntity(contractorSelectDTO.getId());
            String prettyCompany = contracotr.getPrettyName() + " " + contracotr.getCompany() + "\n\n";
            if (contracotr.getDebtor()) {
                debtorSB.append(prettyCompany);
                debtorSB.append("jest dłużny na kwotę: " + contracotr.getDebtorAmount());
                debtorSB.append("\n\nLista nie opłaconych ewinecji:\n");
                contracotr.getKpirList().forEach(kpir -> {
                    if (KpirTypeEnum.INCOME.equals(kpir.getType()) && calculateOverduePayment(kpir)) {
                        debtorSB.append("\t - nr: ").append(kpir.getIdx())
                                .append(" z dnia ").append(kpir.getEconomicEventDate().toLocalDate())
                                .append(" z terminem płatności ").append(kpir.getPaymentDateMax().toLocalDate())
                                .append(" i numerze dowodu ").append(kpir.getRegistrationNumber())
                                .append(" na kwotę ").append(kpir.getAllIncome()).append("zł;");
                    }
                });
            }
            if (contracotr.getCreditor()) {
                creditorSB.append(prettyCompany);
                creditorSB.append(" oczekuje na zapłatę na kwotę: " + contracotr.getDebtorAmount());
                creditorSB.append("\n\nLista nie opłaconych ewinecji:\n");
                contracotr.getKpirList().forEach(kpir -> {
                    if (KpirTypeEnum.INCOME.equals(kpir.getType()) && calculateOverduePayment(kpir)) {
                        BigDecimal sum = kpir.getSumCosts().add(kpir.getRadCosts().add(kpir.getPaymentCost())).add(kpir.getOtherCosts());
                        creditorSB.append("\t - nr: ").append(kpir.getIdx())
                                .append(" z dnia ").append(kpir.getEconomicEventDate().toLocalDate())
                                .append(" z terminem płatności ").append(kpir.getPaymentDateMax().toLocalDate())
                                .append(" i numerze dowodu ").append(kpir.getRegistrationNumber())
                                .append(" na kwotę ").append(sum).append("zł;");
                    }
                });
            }
        });
    }


    private boolean calculateOverduePayment(Kpir kpir) {
        return Boolean.FALSE.equals(kpir.getPayed()) && kpir.getPaymentDateMax().isBefore(LocalDateTime.now());
    }
}

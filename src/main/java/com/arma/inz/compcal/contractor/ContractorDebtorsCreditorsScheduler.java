package com.arma.inz.compcal.contractor;

import com.arma.inz.compcal.kpir.Kpir;
import com.arma.inz.compcal.kpir.KpirTypeEnum;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
public class ContractorDebtorsCreditorsScheduler {
    private final ContractorRepository contractorRepository;

    private boolean calculateOverduePayment(Kpir kpir) {
        return Boolean.FALSE.equals(kpir.getPayed()) && kpir.getPaymentDateMax().isBefore(LocalDateTime.now());
    }

    @Scheduled(cron = "${recalculate.debtors.and.creditors}")
    private void recalculateAllDebtorAndCreditor() {
        List<Contractor> contractors = contractorRepository.findAll();
        for (Contractor contractor : contractors) {
            checkAndSaveContractor(contractor);
        }
    }

    @Transactional
    void checkAndSaveContractor(Contractor contractor) {
        BigDecimal debtorAmount = contractor.getDebtorAmount() == null ? BigDecimal.ZERO : contractor.getDebtorAmount();
        BigDecimal creditorAmount = contractor.getCreditorAmount() == null ? BigDecimal.ZERO : contractor.getCreditorAmount();
        for (Kpir kpir : contractor.getKpirList()) {
            if (!calculateOverduePayment(kpir)) {
                continue;
            } else if (KpirTypeEnum.INCOME.equals(kpir.getType())) {
                debtorAmount = debtorAmount.add(kpir.getSoldIncome()).add(kpir.getOtherIncome());
            } else if (KpirTypeEnum.COSTS.equals(kpir.getType())) {
                creditorAmount = creditorAmount.add(kpir.getOtherCosts())
                        .add(kpir.getPaymentCost()).add(kpir.getPurchaseCosts()).add(kpir.getRadCosts());
            }
        }
        contractor.setDebtor(debtorAmount.compareTo(BigDecimal.ZERO) > 0);
        contractor.setDebtorAmount(debtorAmount);
        contractor.setCreditor(creditorAmount.compareTo(BigDecimal.ZERO) > 0);
        contractor.setCreditorAmount(creditorAmount);
        contractorRepository.save(contractor);
    }
}

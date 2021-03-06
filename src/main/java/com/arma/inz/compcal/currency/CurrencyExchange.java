package com.arma.inz.compcal.currency;

import com.arma.inz.compcal.kpir.Kpir;
import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@AllArgsConstructor
public class CurrencyExchange {
    private final CurrencyRepository currencyRepository;

    public BigDecimal getCurrencyByDate(CurrencyEnum currencyEnum, LocalDateTime date) {
        Sort sort = Sort.by("addedAt").descending();

        Page<Currency> optional = currencyRepository.findAll(getCurrencySpecification(currencyEnum, date), PageRequest.of(0,1,sort));
        BigDecimal currency = !optional.getContent().isEmpty() ? optional.getContent().get(0).getExchangeValue() : BigDecimal.ONE;
        return currency;
    }

    private Specification<Currency> getCurrencySpecification(CurrencyEnum currencyEnum, LocalDateTime date) {
        return (root, query, builder) -> {
            Predicate codeQuery = builder.equal(root.get("code"), currencyEnum);
            LocalDateTime localDate = LocalDateTime.of(date.toLocalDate(), LocalTime.MIDNIGHT);
            LocalDateTime localPreviousDayDate = localDate.minusDays(1l);
            Predicate dateQuery = builder.between(root.get("addedAt"), localPreviousDayDate, localDate);
            return builder.and(codeQuery, dateQuery);
        };
    }

    public void exchangeSumALlIncome(Kpir kpir, KpirDTO dto) {
        isCurrencyEmptySetPLN(kpir);
        BigDecimal currency = getCurrencyByDate(kpir.getCurrency(), kpir.getEconomicEventDate());
        dto.setSoldIncome(getMultiply(currency, kpir.getSoldIncome()));
        dto.setOtherIncome(getMultiply(currency, kpir.getOtherIncome()));
        dto.setAllIncome(getMultiply(currency, kpir.getAllIncome()));
        dto.setPurchaseCosts(getMultiply(currency, kpir.getPurchaseCosts()));
        dto.setPurchaseSideCosts(getMultiply(currency, kpir.getPurchaseSideCosts()));
        dto.setPaymentCost(getMultiply(currency, kpir.getPaymentCost()));
        dto.setOtherCosts(getMultiply(currency, kpir.getOtherCosts()));
        dto.setSumCosts(getMultiply(currency, kpir.getSumCosts()));
        dto.setOther(getMultiply(currency, kpir.getOther()));
        dto.setRadCosts(getMultiply(currency, kpir.getRadCosts()));
    }

    public void exchangeSumALlIncome(Kpir kpir, KpirCreateDTO dto) {
        isCurrencyEmptySetPLN(kpir);
        BigDecimal currency = getCurrencyByDate(kpir.getCurrency(), kpir.getEconomicEventDate());
        dto.setSoldIncome(getMultiply(currency, kpir.getSoldIncome()));
        dto.setOtherIncome(getMultiply(currency, kpir.getOtherIncome()));
        dto.setAllIncome(getMultiply(currency, kpir.getAllIncome()));
        dto.setPurchaseCosts(getMultiply(currency, kpir.getPurchaseCosts()));
        dto.setPurchaseSideCosts(getMultiply(currency, kpir.getPurchaseSideCosts()));
        dto.setPaymentCost(getMultiply(currency, kpir.getPaymentCost()));
        dto.setOtherCosts(getMultiply(currency, kpir.getOtherCosts()));
        dto.setSumCosts(getMultiply(currency, kpir.getSumCosts()));
        dto.setOther(getMultiply(currency, kpir.getOther()));
        dto.setRadCosts(getMultiply(currency, kpir.getRadCosts()));
    }

    private BigDecimal getMultiply(BigDecimal currency, BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value.multiply(currency).round(new MathContext(2, RoundingMode.HALF_EVEN));
    }

    public BigDecimal exchangeSumALlIncome(Kpir kpir) {
        isCurrencyEmptySetPLN(kpir);
        BigDecimal currency = getCurrencyByDate(kpir.getCurrency(), kpir.getEconomicEventDate());
        BigDecimal soldIncome = getMultiply(currency, kpir.getSoldIncome());
        BigDecimal otherIncome = getMultiply(currency, kpir.getOtherIncome());
        return soldIncome.add(otherIncome);
    }

    public BigDecimal exchangeSumAllCosts(Kpir kpir) {
        isCurrencyEmptySetPLN(kpir);
        BigDecimal currency = getCurrencyByDate(kpir.getCurrency(), kpir.getEconomicEventDate());
        BigDecimal purchaseCosts = getMultiply(currency, kpir.getPurchaseCosts());
        BigDecimal purchaseSideCosts = getMultiply(currency, kpir.getPurchaseSideCosts());
        BigDecimal paymentCost = getMultiply(currency, kpir.getPaymentCost());
        BigDecimal otherCosts = getMultiply(currency, kpir.getOtherCosts());
        BigDecimal other = getMultiply(currency, kpir.getOther());
        BigDecimal radCosts = getMultiply(currency, kpir.getRadCosts());
        return purchaseCosts.add(purchaseSideCosts)
                .add(paymentCost)
                .add(other)
                .add(otherCosts)
                .add(radCosts);
    }

    private void isCurrencyEmptySetPLN(Kpir kpir) {
        if (kpir.getCurrency() == null) {
            kpir.setCurrency(CurrencyEnum.PLN);
        }
    }
}

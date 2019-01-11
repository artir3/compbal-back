package com.arma.inz.compcal.currency;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@Component
public class CurrencyCrawler {

    @Value("${crawler.nbp.api}")
    private String nbpApiTodayUrl;

    private final CurrencyRepository currencyRepository;

    public CurrencyCrawler(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    @Scheduled(cron = "${crawler.nbp.api.crone}")
    public void getNbpApiTableA(){
        Date time = this.currencyRepository.getLastInsertDate();
        if (time == null){
            parseLastX(50);
        } else {
            LocalDate lastInsertDate = time.toLocalDate();
            long daysBetween = ChronoUnit.DAYS.between(lastInsertDate, LocalDate.now());
            if (daysBetween > 0)
                parseLastX(daysBetween);
        }
    }

    private void parseToday(){
        CurrencyDayTableADTO[] response = getObjects(nbpApiTodayUrl + "/a");
        parseAndSaveTableA(response);
    }

    private void parseLastX(long daysNo){
        CurrencyDayTableADTO[] response = getObjects(nbpApiTodayUrl + "/a/last/" + daysNo);
        parseAndSaveTableA(response);
    }

    private CurrencyDayTableADTO[] getObjects(String url){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<CurrencyDayTableADTO[]> respEntity = restTemplate.exchange(url, HttpMethod.GET, entity, CurrencyDayTableADTO[].class);
        return respEntity.getBody();
    }

    private void parseAndSaveTableA(CurrencyDayTableADTO[] response) {
        if (response != null) {
            for (CurrencyDayTableADTO day : response) {
                for (CurrencyDayTableACurrencyDTO dto : day.getRates()) {
                    Currency entity = new Currency();
                    entity.setCode(CurrencyEnum.valueOf(dto.getCode()));
                    entity.setAddedAt(LocalDateTime.parse(day.getEffectiveDate() + "T00:00:00"));
                    entity.setExchangeValue(new BigDecimal(dto.getMid()).setScale(5, RoundingMode.HALF_EVEN));
                    currencyRepository.save(entity);
                }
            }
        }
    }

}


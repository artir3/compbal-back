package com.arma.inz.compcal.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class CurrencyCrawler {

    @Value("${crawler.nbp.api}")
    private String nbpApiTodayUrl;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Scheduled(cron = "${crawler.nbp.api.crone}")
    public void getNbpApiTableA(){
        long count = currencyRepository.count();
        if (count == 0){
            parseLastFifty();
        } else {
            parseToday();
        }
    }

    private void parseToday(){
        CurrencyDayTableADTO[] response = getObjects(nbpApiTodayUrl + "/a");
        parseAndSaveTableA(response);
    }

    private void parseLastFifty(){
        CurrencyDayTableADTO[] response = getObjects(nbpApiTodayUrl + "/a/last/10");
        parseAndSaveTableA(response);
    }

    private CurrencyDayTableADTO[] getObjects(String url){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
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


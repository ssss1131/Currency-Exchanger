package com.ssss.mapper;

import com.ssss.dto.ExchangeRatesDto;
import com.ssss.model.Currency;
import com.ssss.model.ExchangeRate;
import com.ssss.service.CurrencyService;

import java.math.BigDecimal;

public class ExchangeRatesMapper implements Mapper<ExchangeRatesDto, ExchangeRate>{

    private static final ExchangeRatesMapper INSTANCE = new ExchangeRatesMapper();
    private final CurrencyService currencyService = CurrencyService.getInstance();

    public static ExchangeRatesMapper getInstance() {
        return INSTANCE;
    }

    public ExchangeRate mapFrom(ExchangeRatesDto dto) {
        Currency baseCurrency = currencyService.getCurrencyByCode(dto.getBaseCurrencyCode());
        Currency targetCurrency = currencyService.getCurrencyByCode(dto.getTargetCurrencyCode());
        return ExchangeRate.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(new BigDecimal(dto.getRate()))
                .build();
    }

}

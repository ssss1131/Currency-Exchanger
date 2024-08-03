package com.ssss.service;

import com.ssss.dao.ExchangeRateDaoJdbc;
import com.ssss.dto.ExchangeRatesDto;
import com.ssss.model.Currency;
import com.ssss.model.ExchangeRate;

import java.util.Optional;

public class ExchangeRatesService {
    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();
    private final CurrencyService currencyService = CurrencyService.getInstance();
    private final ExchangeRateDaoJdbc exchangeRateDao = ExchangeRateDaoJdbc.getInstance();

    private ExchangeRatesService() {
    }

    public static ExchangeRatesService getInstance() {
        return INSTANCE;
    }

    public ExchangeRate createExchangeRate(ExchangeRatesDto dto) {
        Currency baseCurrency = currencyService.getCurrencyByCode(dto.getBaseCurrencyCode());
        Currency targetCurrency = currencyService.getCurrencyByCode(dto.getTargetCurrencyCode());
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(dto.getRate())
                .build();
        return exchangeRateDao.save(exchangeRate)
                .orElseThrow(() -> new IllegalArgumentException("Exchange rate already exists"));

    }
}

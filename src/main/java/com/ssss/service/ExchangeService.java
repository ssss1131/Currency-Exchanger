package com.ssss.service;

import com.ssss.dto.ExchangeDto;
import com.ssss.dto.ExchangedCurrenciesDto;
import com.ssss.exception.NotExistException;
import com.ssss.model.Currency;
import com.ssss.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Optional;

import static java.math.RoundingMode.HALF_UP;

public class ExchangeService {

    private static final ExchangeService INSTANCE = new ExchangeService();
    private final CurrencyService currencyService = CurrencyService.getInstance();
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();


    public static ExchangeService getInstance() {
        return INSTANCE;
    }

    private ExchangeService() {
    }

    public ExchangedCurrenciesDto convertCurrency(ExchangeDto exchangeDto) {
        Currency currencyBase = currencyService.getCurrencyByCode(exchangeDto.getFrom());
        Currency currencyTarget = currencyService.getCurrencyByCode(exchangeDto.getTo());
        Optional<ExchangeRate> exchangeRate = exchangeRateService.getExchangeRate(currencyBase.getCode(), currencyTarget.getCode());
        if (exchangeRate.isPresent()) {
            return forwardExchange(exchangeDto, exchangeRate.get(), currencyBase, currencyTarget);
        }
        Optional<ExchangeRate> exchangeRateBackward = exchangeRateService.getExchangeRate(currencyTarget.getCode(), currencyBase.getCode());
        if (exchangeRateBackward.isPresent()) {
            return backwardExchange(exchangeDto, exchangeRateBackward.get(), currencyBase, currencyTarget);
        }
        return crossExchange(exchangeDto, currencyBase, currencyTarget);

    }

    private ExchangedCurrenciesDto forwardExchange(ExchangeDto exchangeDto, ExchangeRate exchangeRate, Currency currencyBase, Currency currencyTarget) {
        BigDecimal rate = exchangeRate.getRate();
        BigDecimal amount = new BigDecimal(exchangeDto.getAmount());
        BigDecimal convertedAmount = amount.multiply(rate);
        return buildDto(currencyBase, currencyTarget, rate, amount, convertedAmount);
    }

    private ExchangedCurrenciesDto backwardExchange(ExchangeDto exchangeDto, ExchangeRate exchangeRateBackward, Currency currencyBase, Currency currencyTarget) {
        BigDecimal rate = exchangeRateBackward.getRate();
        BigDecimal amount = new BigDecimal(exchangeDto.getAmount());
        BigDecimal convertedAmount = amount.divide(rate, MathContext.DECIMAL32);
        return buildDto(currencyBase, currencyTarget, rate, amount, convertedAmount);
    }

    private ExchangedCurrenciesDto crossExchange(ExchangeDto exchangeDto, Currency currencyBase, Currency currencyTarget) {
        Optional<ExchangeRate> exchangeRateFrom = exchangeRateService.getExchangeRate("USD", currencyBase.getCode());
        Optional<ExchangeRate> exchangeRateTo = exchangeRateService.getExchangeRate("USD", currencyTarget.getCode());
        if (exchangeRateFrom.isPresent() && exchangeRateTo.isPresent()) {
            BigDecimal rateFrom = exchangeRateFrom.get().getRate();
            BigDecimal rateTo = exchangeRateTo.get().getRate();
            BigDecimal rate = rateTo.divide(rateFrom, MathContext.DECIMAL32);
            BigDecimal amount = new BigDecimal(exchangeDto.getAmount());
            BigDecimal convertedAmount = amount.multiply(rate);
            return buildDto(currencyBase, currencyTarget, rate, amount, convertedAmount);
        }
        throw new NotExistException("No exchange rate available for the given currencies");
    }


    private ExchangedCurrenciesDto buildDto(Currency currencyBase, Currency currencyTarget, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        return ExchangedCurrenciesDto.builder()
                .baseCurrency(currencyBase)
                .targetCurrency(currencyTarget)
                .rate(rate)
                .amount(amount)
                .convertedAmount(convertedAmount.setScale(2, HALF_UP))
                .build();
    }

}


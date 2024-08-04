package com.ssss.dto;

import com.ssss.model.Currency;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ExchangedCurrenciesDto {

    Currency baseCurrency;

    Currency targetCurrency;

    BigDecimal rate;

    BigDecimal amount;

    BigDecimal convertedAmount;

}

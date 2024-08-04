package com.ssss.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ExchangeRatesDto {

    String baseCurrencyCode;

    String targetCurrencyCode;

    BigDecimal rate;
}

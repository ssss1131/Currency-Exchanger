package com.ssss.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExchangeRatesDto {

    String baseCurrencyCode;

    String targetCurrencyCode;

    String rate;
}

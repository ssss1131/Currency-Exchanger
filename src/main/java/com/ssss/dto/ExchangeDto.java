package com.ssss.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ExchangeDto {

    String from;

    String to;

    String amount;

}

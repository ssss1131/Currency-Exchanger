package com.ssss.util;

import com.ssss.dto.ExchangeDto;
import com.ssss.dto.ExchangeRatesDto;
import com.ssss.exception.InvalidFieldException;
import com.ssss.exception.MissingFieldException;
import com.ssss.model.Currency;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ValidationUtils {

    public static void validateCurrencyCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new MissingFieldException("Currency code is missing in the request.");
        }
        Pattern pattern = Pattern.compile("^[a-zA-Z]{3}$");
        Matcher matcher = pattern.matcher(code);
        if (!matcher.matches()) {
            throw new InvalidFieldException("Currency code is invalid.");
        }
    }

    public static void validateNewCurrency(Currency currency) {
        String name = currency.getName();
        String code = currency.getCode();
        String sign = currency.getSign();
        validateCurrencyCode(code);
        if (name == null || name.isEmpty()) {
            throw new MissingFieldException("Currency name is missing in the request.");
        }
        if (sign == null || sign.isEmpty()) {
            throw new MissingFieldException("Currency sign is missing in the request.");
        }
    }

    public static void validateExchange(ExchangeDto exchangeDto) {
        validateEqualParameters(exchangeDto.getFrom(), exchangeDto.getTo());
        validateCurrencyCode(exchangeDto.getFrom());
        validateCurrencyCode(exchangeDto.getTo());
        validateNumber(exchangeDto.getAmount(), "amount");
    }

    public static void validatePath(String pathInfo) {
        String codes = pathInfo.replaceFirst("/", "");
        if (!codes.matches("^[a-zA-Z]{6}$")) {
            throw new InvalidFieldException("Currency codes are invalid.");
        }
    }

    public static void validatePath(String pathInfo, String rateParameter) {
        validatePath(pathInfo);
        validateNumber(rateParameter, "rate");
    }

    public static void validateNewExchangeRate(ExchangeRatesDto dto) {
        validateEqualParameters(dto.getBaseCurrencyCode(), dto.getTargetCurrencyCode());
        validateCurrencyCode(dto.getBaseCurrencyCode());
        validateCurrencyCode(dto.getTargetCurrencyCode());
        validateNumber(dto.getRate(), "rate");
    }

    private static void validateNumber(String parameter, String fieldName) {
        if (parameter == null || parameter.isEmpty()) {
            throw new MissingFieldException(fieldName + " is missing in the request.");
        }
        try {
            String rate = parameter.replace(fieldName + "=", "");
            new BigDecimal(rate);
        } catch (NumberFormatException e) {
            throw new InvalidFieldException(fieldName + " is invalid.");
        }
    }

    private static void validateEqualParameters(String currency1, String currency2) {
        if (currency1.equals(currency2)) {
            throw new InvalidFieldException("Currency " + currency1 + " and " + currency2 + " are the same.");
        }
    }

}

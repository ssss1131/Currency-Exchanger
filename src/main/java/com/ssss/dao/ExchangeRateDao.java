package com.ssss.dao;

import com.ssss.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeRateDao extends Dao<ExchangeRate> {

    Optional<ExchangeRate> findByCodes(String baseCode, String targetCode);

    ExchangeRate updateByCodes(String baseCode, String targetCode, BigDecimal newRate);



}

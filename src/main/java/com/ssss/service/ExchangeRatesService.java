package com.ssss.service;

import com.ssss.dao.ExchangeRateDao;
import com.ssss.dao.ExchangeRateDaoJdbc;
import com.ssss.dto.ExchangeRatesDto;
import com.ssss.mapper.ExchangeRatesMapper;
import com.ssss.model.ExchangeRate;

public class ExchangeRatesService {
    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDaoJdbc.getInstance();
    private final ExchangeRatesMapper exchangeRatesMapper = ExchangeRatesMapper.getInstance();

    private ExchangeRatesService() {
    }

    public static ExchangeRatesService getInstance() {
        return INSTANCE;
    }

    public ExchangeRate createExchangeRate(ExchangeRatesDto dto) {
        ExchangeRate exchangeRate = exchangeRatesMapper.mapFrom(dto);
        return exchangeRateDao.save(exchangeRate);
    }
}

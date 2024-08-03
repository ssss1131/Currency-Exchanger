package com.ssss.service;

import com.ssss.dao.CurrencyDaoJdbc;
import com.ssss.model.Currency;

import java.util.Optional;

public class CurrencyService {

    private static final CurrencyService INSTANCE = new CurrencyService();
    private final CurrencyDaoJdbc currencyDaoJdbc = CurrencyDaoJdbc.getInstance();

    public static CurrencyService getInstance() {
        return INSTANCE;
    }

    private CurrencyService() {
    }

    public Currency getCurrencyByCode(String currencyCode) {
        return currencyDaoJdbc.findByCode(currencyCode)
                .orElseThrow( () -> new IllegalArgumentException("Currency not found"));
    }




}

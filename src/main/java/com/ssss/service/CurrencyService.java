package com.ssss.service;

import com.ssss.dao.CurrencyDao;
import com.ssss.dao.CurrencyDaoJdbc;
import com.ssss.model.Currency;

import java.util.List;

public class CurrencyService {

    private static final CurrencyService INSTANCE = new CurrencyService();
    private final CurrencyDao currencyDaoJdbc = CurrencyDaoJdbc.getInstance();

    public static CurrencyService getInstance() {
        return INSTANCE;
    }

    private CurrencyService() {
    }

    public Currency getCurrencyByCode(String currencyCode) {
        return currencyDaoJdbc.findByCode(currencyCode);
    }

    public List<Currency> getAllCurrencies() {
        return currencyDaoJdbc.findAll();
    }

    public Currency save(Currency currency) {
        return currencyDaoJdbc.save(currency);
    }

}

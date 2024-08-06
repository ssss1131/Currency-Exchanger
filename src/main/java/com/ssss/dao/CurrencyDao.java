package com.ssss.dao;

import com.ssss.model.Currency;

public interface CurrencyDao extends Dao<Currency> {

    Currency findByCode(String code);

}

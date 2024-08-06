package com.ssss.service;

import com.ssss.dao.ExchangeRateDao;
import com.ssss.dao.ExchangeRateDaoJdbc;
import com.ssss.exception.NotExistException;
import com.ssss.model.ExchangeRate;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private final ExchangeRateDao dao = ExchangeRateDaoJdbc.getInstance();

    private ExchangeRateService() {
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }

    public ExchangeRate getExchangeRateFromPath(String pathInfo) {
        List<String> codes = getCodesFromPath(pathInfo);
        String baseCode = codes.get(0);
        String targetCode = codes.get(1);
        return dao.findByCodes(baseCode, targetCode)
                .orElseThrow(() -> new NotExistException("Exchange Rate with codes " + baseCode + "-" + targetCode + " not exist"));
    }

    public Optional<ExchangeRate> getExchangeRate(String baseCode, String targetCode) {
        return dao.findByCodes(baseCode, targetCode);
    }


    public ExchangeRate updateExchangeRate(String pathInfo, BigDecimal newRate) {
        List<String> codes = getCodesFromPath(pathInfo);
        String baseCode = codes.get(0);
        String targetCode = codes.get(1);
        if(dao.findByCodes(baseCode, targetCode).isEmpty()){
            throw new NotExistException("Exchange Rate with codes " + baseCode + "-" + targetCode + " not exist");
        }
        return dao.updateByCodes(baseCode, targetCode, newRate);
    }

    private List<String> getCodesFromPath(String pathInfo) {
        List<String> codes = new ArrayList<>();
        String baseCode = pathInfo.substring(1, 4);
        codes.add(baseCode);
        String targetCode = pathInfo.substring(4, 7);
        codes.add(targetCode);
        return codes;
    }
}

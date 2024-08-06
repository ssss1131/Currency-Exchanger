package com.ssss.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssss.model.ExchangeRate;
import com.ssss.service.ExchangeRateService;
import com.ssss.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;


import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String rateParameter = req.getReader().readLine();
        ValidationUtils.validatePath(pathInfo, rateParameter);
        String rate = rateParameter.replace("rate=", "");
        ExchangeRate exchangeRate = exchangeRateService.updateExchangeRate(pathInfo, new BigDecimal(rate));
        resp.setStatus(SC_OK);
        mapper.writeValue(resp.getWriter(), exchangeRate);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        ValidationUtils.validatePath(pathInfo);
        ExchangeRate exchangeRate = exchangeRateService.getExchangeRateFromPath(pathInfo);
        mapper.writeValue(resp.getWriter(), exchangeRate);
        resp.setStatus(SC_OK);
    }
}

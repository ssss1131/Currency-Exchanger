package com.ssss.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssss.dao.ExchangeRateDaoJdbc;
import com.ssss.model.ExchangeRate;
import com.ssss.service.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

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
        BigDecimal rate = new BigDecimal(rateParameter.replace("rate=", ""));

        System.out.println(rate);
        Optional<ExchangeRate> exchangeRate = exchangeRateService.updateExchangeRate(pathInfo, rate);
        System.out.println(exchangeRate);
        if (exchangeRate.isPresent()) {
            resp.setStatus(SC_OK);
            resp.setContentType("application/json");
            mapper.writeValue(resp.getWriter(), exchangeRate.get());
        }else{
            resp.sendError(SC_NOT_FOUND);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        TODO
//         split in try-catch bcs maybe there is no elm in array created by split
//         create exceptions for several situations and transfer all exceptions to servlets

        String pathInfo = req.getPathInfo();
        Optional<ExchangeRate> exchangeRate = exchangeRateService.getExchangeRate(pathInfo);
        if (exchangeRate.isPresent()) {
            resp.setContentType("application/json");
            mapper.writeValue(resp.getWriter(), exchangeRate.get());
            resp.setStatus(SC_OK);
        } else {
            resp.sendError(SC_NOT_FOUND, "NOOOO");
        }
    }
}

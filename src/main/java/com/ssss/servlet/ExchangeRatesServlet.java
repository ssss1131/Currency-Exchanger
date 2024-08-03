package com.ssss.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssss.dao.ExchangeRateDaoJdbc;
import com.ssss.dto.ExchangeRatesDto;
import com.ssss.model.ExchangeRate;
import com.ssss.service.ExchangeRatesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), ExchangeRateDaoJdbc.getInstance().findAll());
        resp.setStatus(SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO filter or validator bcs there is Integer.valueOf mb exception
        ExchangeRatesDto dto = ExchangeRatesDto.builder()
                .baseCurrencyCode(req.getParameter("baseCurrencyCode"))
                .targetCurrencyCode(req.getParameter(("targetCurrencyCode")))
                .rate(new BigDecimal(req.getParameter("rate")))
                .build();
        try{
            ExchangeRate exchangeRate = exchangeRatesService.createExchangeRate(dto);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(SC_CREATED);
            mapper.writeValue(resp.getWriter(), exchangeRate);
            System.out.println(exchangeRate);
        }catch(Exception e) {
            //TODO catch all possible exceptions
            resp.sendError(SC_BAD_REQUEST, "something короч не знаю потом сделаю лень щя");
        }
    }
}

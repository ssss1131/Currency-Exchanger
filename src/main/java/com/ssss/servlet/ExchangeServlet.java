package com.ssss.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssss.dto.ExchangeDto;
import com.ssss.dto.ExchangedCurrenciesDto;
import com.ssss.service.ExchangeService;
import com.ssss.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private final ExchangeService exchangeService = ExchangeService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeDto exchangeDto = ExchangeDto.builder()
                .from(req.getParameter("from"))
                .to(req.getParameter("to"))
                .amount(req.getParameter("amount"))
                .build();
        ValidationUtils.validateExchange(exchangeDto);

        ExchangedCurrenciesDto exchangedCurrenciesDto = exchangeService.convertCurrency(exchangeDto);
        objectMapper.writeValue(resp.getWriter(), exchangedCurrenciesDto);

    }
}

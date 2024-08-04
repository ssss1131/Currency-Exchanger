package com.ssss.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssss.dto.ExchangeDto;
import com.ssss.dto.ExchangedCurrenciesDto;
import com.ssss.exception.CurrencyConversionException;
import com.ssss.service.ExchangeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private final ExchangeService exchangeService = ExchangeService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeDto exchangeDto = ExchangeDto.builder()
                .from(req.getParameter("from"))
                .to(req.getParameter("to"))
                .amount(new BigDecimal(req.getParameter("amount")))
                .build();
        try{
            ExchangedCurrenciesDto exchangedCurrenciesDto = exchangeService.convertCurrency(exchangeDto);
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getWriter(), exchangedCurrenciesDto);
        }catch(Exception e){
            if(e.getCause() instanceof CurrencyConversionException){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                //TODO надо чекнуть как там ошибку надо передать чтобы фронт показал его
            }else{
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
}

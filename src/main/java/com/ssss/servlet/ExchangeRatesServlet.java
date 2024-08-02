package com.ssss.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssss.dao.ExchangeRateDaoJdbc;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ExchangeRateDaoJdbc exchangeRate  = ExchangeRateDaoJdbc.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), exchangeRate.findAll());
        resp.setStatus(SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO need to save but form give me Id not a Currency so i have like 2 options
        // 1.Create full new Currency and then in save method get its id
        // 2.Create Currency with only id field
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");


    }
}

package com.ssss.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssss.dao.ExchangeRateDaoJdbc;
import com.ssss.model.ExchangeRate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRateDaoJdbc exchangeRateDao = ExchangeRateDaoJdbc.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        TODO
//         split in try-catch bcs maybe there is no elm in array created by split
//         create exceptions for several situations and transfer all exceptions to servlets

        String pathInfo = req.getPathInfo().split("/")[1];
        if(pathInfo != null && pathInfo.length() == 6){
            String baseCode = pathInfo.substring(0, 3);
            String targetCode = pathInfo.substring(3, 6);
            Optional<ExchangeRate> exchangeRate = exchangeRateDao.findByCode(baseCode, targetCode);
            if(exchangeRate.isPresent()){
                resp.setContentType("application/json");
                mapper.writeValue(resp.getWriter(), exchangeRate.get());
                resp.setStatus(SC_OK);
            }else{
                resp.sendError(SC_NOT_FOUND);
            }

        }else{
            resp.sendError(SC_BAD_REQUEST, "Length");
        }

    }
}

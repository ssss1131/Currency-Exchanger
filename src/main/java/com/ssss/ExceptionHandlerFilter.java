package com.ssss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssss.dto.ShowExceptionDto;
import com.ssss.exception.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import static jakarta.servlet.http.HttpServletResponse.*;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlerFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try{
            filterChain.doFilter(servletRequest, servletResponse);
        }catch (DatabaseUnavailableException e){
            showException((HttpServletResponse) servletResponse, SC_INTERNAL_SERVER_ERROR, e);
        }catch(AlreadyExistsException e){
            showException((HttpServletResponse) servletResponse, SC_CONFLICT, e);
        }catch (MissingFieldException | InvalidFieldException e){
            showException((HttpServletResponse) servletResponse, SC_BAD_REQUEST, e);
        }catch(NotExistException e){
            showException((HttpServletResponse) servletResponse, SC_NOT_FOUND, e);
        }catch(Exception e){
            showException((HttpServletResponse) servletResponse, SC_INTERNAL_SERVER_ERROR, e);
        }
    }

    @SneakyThrows
    private void showException(HttpServletResponse response, int status, Throwable e){
        response.setStatus(status);

        objectMapper.writeValue(response.getWriter(), ShowExceptionDto.builder()
                        .status(status)
                        .message(e.getMessage())
                .build()
        );
    }
}

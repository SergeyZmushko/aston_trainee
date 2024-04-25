package com.aston.trainee.controller.impl;

import com.aston.trainee.service.exceptions.ServiceErrorCode;
import com.aston.trainee.service.exceptions.ServiceException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.aston.trainee.service.exceptions.ServiceErrorCode.PARSE_ID_EXCEPTION;

/**
 * Абстрактный класс
 */
public abstract class AbstractController extends HttpServlet {
    protected final Gson gson;

    protected AbstractController(){
        this.gson = new Gson();
    }

    @Override
    protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    @Override
    protected abstract void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    @Override
    protected abstract void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    @Override
    protected abstract void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    protected Long checkId(String pathInfo){
        try {
            return Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            throw new ServiceException(PARSE_ID_EXCEPTION.getErrorMessage(),PARSE_ID_EXCEPTION.getErrorCode());
        }
    }

    protected <T>T parseJson(String requestBody, Class<T> tClass){
        try {
            return gson.fromJson(requestBody, tClass);
        }catch (JsonSyntaxException e){
            throw new ServiceException(ServiceErrorCode.JSON_SYNTAX_EXC.getErrorMessage(), ServiceErrorCode.JSON_SYNTAX_EXC.getErrorCode());
        }
    }
}

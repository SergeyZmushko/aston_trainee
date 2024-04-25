package com.aston.trainee.controller.impl;

import com.aston.trainee.repository.util.DBConnectionProvider;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Класс для добавления экземпляра класса DBConnectionProvider в сервлет контекст и использования его в сервлетах.
 */
@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DBConnectionProvider connectionProvider = new DBConnectionProvider();
        sce.getServletContext().setAttribute("DBProvider", connectionProvider);
    }
}

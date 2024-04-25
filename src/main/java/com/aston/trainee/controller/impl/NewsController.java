package com.aston.trainee.controller.impl;

import com.aston.trainee.controller.ControllerConstants;
import com.aston.trainee.controller.exception_handling.ErrorResponse;
import com.aston.trainee.repository.impl.NewsRepository;
import com.aston.trainee.repository.util.DBConnectionProvider;
import com.aston.trainee.service.dto.*;
import com.aston.trainee.service.exceptions.ServiceException;
import com.aston.trainee.service.impl.NewsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * NewsController это класс, используемый сервлет, ответственный за управелние операциями, связанными с сущностью
 * новости.
 * Контроллер взаимодействует с NewsService классом для выполнения CRUD операций.
 */
@WebServlet("/news/*")
public class NewsController extends AbstractController {

    /**
     * Поле для взаимодействия с NewsService классом.
     */
    private NewsService newsService;

    /**
     * Метод инициализации сервлета
     *
     * @throws ServletException - при ошибках сзязанных с обрботкой запросов
     */
    @Override
    public void init() throws ServletException {
        super.init();
        DBConnectionProvider connectionProvider = (DBConnectionProvider) getServletContext()
                .getAttribute(ControllerConstants.DB_PROVIDER_STRING);
        newsService = new NewsService(new NewsRepository(connectionProvider));
    }

    /**
     * Метод перехватывает HTTP GET запрос по двум адресам /news и /news/{id}.
     * /news возвращает список всех объектов новостей, /news/{id} возвращает определенную новость по id.
     *
     * @param req  - запрос, содержащий id поиска новости.
     * @param resp - для отправки ответа пользователю.
     * @throws ServletException - при ошибках сзязанных с обрботкой запросов
     * @throws IOException      - при ошибках с I/O операциями
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        PrintWriter writer = resp.getWriter();
        resp.setContentType(ControllerConstants.APPLICATION_JSON);
        if (pathInfo == null || pathInfo.equals("/")) {
            List<NewsDtoResponse> resultList = newsService.readAll();
            writer.write(gson.toJson(resultList));
        } else {
            try {
                Long id = checkId(pathInfo);
                NewsDtoResponse result = newsService.readById(id);
                writer.write(gson.toJson(result));
            } catch (ServiceException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ErrorResponse response = new ErrorResponse(e.getCode(), e.getMessage());
                writer.write(gson.toJson(response));
                writer.flush();
            }
        }
        writer.close();
    }

    /**
     * Метод перехватывает HTTP POST запрос по адресу /news. Объект преобразует JSON объект в Body запроса
     * в объект, необходиый для преедачи в слой сервиса для дальнейшего сохранения объекта в базе данных.
     * Создает экземпляр автора в базе данных.
     *
     * @param req  - запрос, содержащий JSON объект автора в Body запроса.
     * @param resp - для отправки ответа пользователю.
     * @throws ServletException - при ошибках сзязанных с обрботкой запросов
     * @throws IOException      - при ошибках с I/O операциями
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        PrintWriter writer = resp.getWriter();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String requestBody = buffer.toString();
        try {
            NewsDtoRequest newsDtoRequest = parseJson(requestBody, NewsDtoRequest.class);
            NewsDtoResponse response = newsService.create(newsDtoRequest);
            resp.setContentType(ControllerConstants.APPLICATION_JSON);
            resp.getWriter().write(gson.toJson(response));
            resp.setStatus(201);
        } catch (ServiceException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorResponse response = new ErrorResponse(e.getCode(), e.getMessage());
            writer.write(gson.toJson(response));
            writer.flush();
        }
        writer.close();
    }

    /**
     * Метод перехватывает HTTP DELETE запросы по адресу /news/{id}.
     * Удаляет элемент из базы данных по указанному id.
     *
     * @param req  - запрос, содержащий id объекта для удаления
     * @param resp - для отправки ответа пользователю.
     * @throws ServletException - при ошибках сзязанных с обрботкой запросов
     * @throws IOException      - при ошибках с I/O операциями
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        PrintWriter writer = resp.getWriter();
        try {
            Long id = checkId(pathInfo);
            boolean result = newsService.deleteById(id);
            if (result) {
                writer.write(ControllerConstants.SUCCESSFUL_DELETE);
            }
        } catch (ServiceException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorResponse response = new ErrorResponse(e.getCode(), e.getMessage());
            writer.write(gson.toJson(response));
            writer.flush();
        }
        writer.close();
    }

    /**
     * Метод перехватывает HTTP PUT запросы по адресу /news.
     * Изменяет элемент в базе данных по переданному JSON объекту в Body.
     *
     * @param req  - запрос, содержащий JSON объект автора в Body запроса.
     * @param resp - для отправки ответа пользователю.
     * @throws ServletException - при ошибках сзязанных с обрботкой запросов
     * @throws IOException      - при ошибках с I/O операциями
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String requestBody = buffer.toString();
        PrintWriter writer = resp.getWriter();
        try {
            UpdateNewsDtoRequest updateNewsDtoRequest = parseJson(requestBody, UpdateNewsDtoRequest.class);
            resp.setContentType(ControllerConstants.APPLICATION_JSON);
            Long id = checkId(pathInfo);
            NewsDtoResponse response = newsService.update(id, updateNewsDtoRequest);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            writer.write(gson.toJson(response));
        } catch (ServiceException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorResponse response = new ErrorResponse(e.getCode(), e.getMessage());
            writer.write(gson.toJson(response));
            writer.flush();
        }
        writer.close();
    }
}

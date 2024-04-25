package com.aston.trainee.controller.impl;

import com.aston.trainee.controller.ControllerConstants;
import com.aston.trainee.controller.exception_handling.ErrorResponse;
import com.aston.trainee.repository.impl.AuthorRepository;
import com.aston.trainee.repository.util.DBConnectionProvider;
import com.aston.trainee.service.dto.AuthorDtoRequest;
import com.aston.trainee.service.dto.AuthorDtoResponse;
import com.aston.trainee.service.dto.UpdateAuthorDtoRequest;
import com.aston.trainee.service.exceptions.ServiceException;
import com.aston.trainee.service.impl.AuthorService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * AuthorController это класс, используемый сервлет, ответственный за управелние операциями, связанными с сущностью
 * автора.
 * Контроллер взаимодействует с AuthorService классом для выполнения CRUD операций.
 */
@WebServlet("/authors/*")
public class AuthorController extends AbstractController {

    /**
     * Поле для взаимодействия с AuthorService классом.
     */
    private AuthorService authorService;

    /**
     * Метод инициализации сервлета
     * @throws ServletException - при ошибках сзязанных с обрботкой запросов
     */
    @Override
    public void init() throws ServletException {
        super.init();
        DBConnectionProvider connectionProvider = (DBConnectionProvider) getServletContext()
                .getAttribute(ControllerConstants.DB_PROVIDER_STRING);
        authorService = new AuthorService(new AuthorRepository(connectionProvider));
    }

    /**
     * Метод перехватывает HTTP GET запрос по двум адресам /authors и /authors/{id}.
     * /authors возвращает список всех объектов авторов, /authors/{id} возвращает определенного автора по id.
     * @param req - запрос, содержащий id поиска автора.
     * @param resp - для отправки ответа пользователю.
     * @throws ServletException - при ошибках сзязанных с обрботкой запросов
     * @throws IOException - при ошибках с I/O операциями
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        PrintWriter writer = resp.getWriter();
        resp.setContentType(ControllerConstants.APPLICATION_JSON);
        if (pathInfo == null || pathInfo.equals("/")) {
            List<AuthorDtoResponse> resultList = authorService.readAll();
            writer.write(gson.toJson(resultList));
        } else {
            try {
                Long id = checkId(pathInfo);
                AuthorDtoResponse result = authorService.readById(id);
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
     * Метод перехватывает HTTP POST запрос по адресу /authors. Объект преобразует JSON объект в Body запроса
     * в объект, необходиый для преедачи в слой сервиса для дальнейшего сохранения объекта в базе данных.
     * Создает экземпляр автора в базе данных.
     *
     * @param req - запрос, содержащий JSON объект автора в Body запроса.
     * @param resp - для отправки ответа пользователю.
     * @throws ServletException - при ошибках сзязанных с обрботкой запросов
     * @throws IOException - при ошибках с I/O операциями
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
            AuthorDtoRequest authorDtoRequest = parseJson(requestBody, AuthorDtoRequest.class);
            AuthorDtoResponse response = authorService.create(authorDtoRequest);
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
     * Метод перехватывает HTTP DELETE запросы по адресу /authors/{id}.
     * Удаляет элемент из базы данных по указанному id.
     * @param req - запрос, содержащий id объекта для удаления
     * @param resp - для отправки ответа пользователю.
     * @throws ServletException - при ошибках сзязанных с обрботкой запросов
     * @throws IOException - при ошибках с I/O операциями
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        PrintWriter writer = resp.getWriter();
        try {
            Long id = checkId(pathInfo);
            boolean result = authorService.deleteById(id);
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
     * Метод перехватывает HTTP PUT запросы по адресу /authors.
     * Изменяет элемент в базе данных по переданному JSON объекту в Body.
     * @param req - запрос, содержащий JSON объект автора в Body запроса.
     * @param resp - для отправки ответа пользователю.
     * @throws ServletException - при ошибках сзязанных с обрботкой запросов
     * @throws IOException - при ошибках с I/O операциями
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String requestBody = buffer.toString();
        PrintWriter writer = resp.getWriter();
        UpdateAuthorDtoRequest authorDtoRequest = parseJson(requestBody, UpdateAuthorDtoRequest.class);
        resp.setContentType(ControllerConstants.APPLICATION_JSON);

        try {
            AuthorDtoResponse response = authorService.update(authorDtoRequest.id(), authorDtoRequest);
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

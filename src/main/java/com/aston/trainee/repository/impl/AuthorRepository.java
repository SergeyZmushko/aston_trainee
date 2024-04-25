package com.aston.trainee.repository.impl;

import com.aston.trainee.repository.exceptions.ConnectionException;
import com.aston.trainee.repository.exceptions.NotFoundException;
import com.aston.trainee.repository.exceptions.SaveObjectException;
import com.aston.trainee.repository.Repository;
import com.aston.trainee.repository.model.Author;
import com.aston.trainee.repository.util.Constants;
import com.aston.trainee.repository.util.DBConnectionProvider;
import com.aston.trainee.repository.util.DBQueryConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс для основных CRUD операций (создание, получение, редактирование,удаление) с объектом типа Author в базе данных
 */
public class AuthorRepository implements Repository<Author> {

    /**
     * Переменная для получения соединения к базе данных и выполнения SQL запросов.
     */
    private final DBConnectionProvider connectionProvider;

    /**
     * Конструктор для создания объекта AuthorRepository
     *
     * @param connectionProvider - переменная для получения соединения к базе данных и выполнения SQL запросов.
     */
    public AuthorRepository(DBConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
        createAuthorsTableIfNotExists();
    }

    /**
     * Метод возвращает список всех авторов, параметризованных типом Author из базы данных.
     *
     * @return список всех элементов типа <Author>, сохраненных в базе данных.
     */
    @Override
    public List<Author> readAll() {
        Connection cn = connectionProvider.getConnection();
        List<Author> authorList = new ArrayList<>();
        int idInd = 1;
        int nameInd = 2;
        try (Statement st = cn.createStatement()) {
            ResultSet results = st.executeQuery(DBQueryConstants.SELECT_ALL_AUTHORS);
            while (results.next()) {
                Author author = new Author();
                author.setId((long) results.getInt(idInd));
                author.setName(results.getString(nameInd));
                authorList.add(author);
            }
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
        return authorList;
    }

    /**
     * Метод возвращает объект типа Optional<Author> из базы данных по указанному индексу.
     *
     * @param authorId - индекс получаемого объекта
     * @return объект типа Optional<Author> из базы данных по указанному индексу.
     */
    @Override
    public Optional<Author> readById(Long authorId) {
        Connection cn = connectionProvider.getConnection();
        Author author = null;
        int idInd = 1;
        int nameInd = 2;
        try (PreparedStatement pst = cn.prepareStatement(DBQueryConstants.SELECT_AUTHOR_BY_ID)) {
            pst.setLong(1, authorId);
            ResultSet result = pst.executeQuery();
            if (result.next()) {
                author = new Author();
                author.setId(result.getLong(idInd));
                author.setName(result.getString(nameInd));
            }
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
        return Optional.ofNullable(author);
    }

    /**
     * Метод, сохраняет передаваемый в параметр объект Author в базу данных.
     *
     * @param model - объект, передаваемый для сохранения в базу данных;
     * @return созданный объект.
     */
    @Override
    public Author create(Author model) {
        Connection cn = connectionProvider.getConnection();
        Author author;
        try (PreparedStatement pst = cn.prepareStatement(DBQueryConstants.INSERT_AUTHOR,
                Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, model.getName());
            int affectedRows = pst.executeUpdate();
            if (affectedRows == 0) {
                throw new SaveObjectException(Constants.CREATE_AUTHOR_NO_ROWS_AFFECTED);
            }
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                author = new Author();
                int generatedId = rs.getInt(1);
                author.setId((long) generatedId);
                author.setName(model.getName());
            } else {
                throw new SaveObjectException(Constants.CREATE_AUTHOR_NO_ID_OBTAINED);
            }

        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
        return author;
    }

    /**
     * Метод обновляет поля переданного элемента в базе данных.
     *
     * @param model - элемент, передающий в своих полях новое состояние объекта;
     * @return обновленный элемент.
     */
    @Override
    public Author update(Author model) {
        Connection cn = connectionProvider.getConnection();
        try (PreparedStatement pst = cn.prepareStatement(DBQueryConstants.UPDATE_AUTHORS_BY_ID)) {
            pst.setString(1, model.getName());
            pst.setLong(2, model.getId());
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                return readById(model.getId()).get();
            } else {
                throw new NotFoundException(Constants.AUTHOR_ID_DOES_NOT_EXIST);
            }
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
    }

    /**
     * Метод удаляет элемент из базы данных по переданному id.
     *
     * @param authorId - индекс элемента для удаления.
     * @return булевое значение true при успешном удалении, false - при неудачном удалении.
     */
    @Override
    public Boolean deleteById(Long authorId) {
        Connection cn = connectionProvider.getConnection();
        try (PreparedStatement pst = cn.prepareStatement(DBQueryConstants.DELETE_AUTHORS_BY_ID)) {
            pst.setLong(1, authorId);
            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
    }

    /**
     * Метод считает количество элементов по имени в таблице базы данных и возвращает их количество.
     *
     * @param name - имя элемента для подсчета количества.
     * @return количество элементов в базе данных с указанным именем.
     */
    public int countByName(String name) {
        Connection cn = connectionProvider.getConnection();
        int count = 0;
        int idInd = 1;
        try (PreparedStatement pst = cn.prepareStatement(DBQueryConstants.CHECK_AUTHORS_COUNT_BY_NAME)) {
            pst.setString(1, name);

            ResultSet results = pst.executeQuery();
            while (results.next()) {
                count = results.getInt(idInd);
            }
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
        return count;
    }

    /**
     * Метод проверяет наличие элемента в базе данных по переданному id.
     *
     * @param authorId - индекс элемента для проверки.
     * @return булевое значение true, если элемент присутствует, false - при отсутствии элемента.
     */
    @Override
    public Boolean isExistedById(Long authorId) {
        Optional<Author> author = readById(authorId);
        if (author.isPresent()) {
            return true;
        } else {
            throw new NotFoundException(Constants.AUTHOR_ID_DOES_NOT_EXIST);
        }
    }

    /**
     * Метод ищет объект Author в базе данных по указанному имени и возвращает Optional<Author>.
     *
     * @param authorName - имя для поиска элемента в таблице базы данных.
     * @return Optional<Author> элемента из базы данных.
     */
    public Optional<Author> readByName(String authorName) {
        Connection cn = connectionProvider.getConnection();
        Author author = null;
        int idInd = 1;
        int nameInd = 2;
        try (PreparedStatement pst = cn.prepareStatement(DBQueryConstants.GET_AUTHOR_BY_NAME)) {
            pst.setString(1, authorName);
            ResultSet result = pst.executeQuery();
            if (result.next()) {
                author = new Author();
                author.setId(result.getLong(idInd));
                author.setName(result.getString(nameInd));
            }
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
        return Optional.ofNullable(author);
    }

    /**
     * Метод создает таблицу с авторами в базе данных если она еще не создана.
     */
    private void createAuthorsTableIfNotExists() {
        try (Connection conn = this.connectionProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DBQueryConstants.CREATE_AUTHOR_TABLE_IF_NOT_EXIST)) {
            pstmt.execute();
        } catch (SQLException e) {
            throw new ConnectionException(Constants.CREATE_AUTHOR_TABLE_PROBLEM);
        }
    }
}

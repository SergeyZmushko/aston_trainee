package com.aston.trainee.repository.impl;

import com.aston.trainee.repository.exceptions.ConnectionException;
import com.aston.trainee.repository.exceptions.NotFoundException;
import com.aston.trainee.repository.exceptions.SaveObjectException;
import com.aston.trainee.repository.Repository;
import com.aston.trainee.repository.model.Tag;
import com.aston.trainee.repository.util.Constants;
import com.aston.trainee.repository.util.DBConnectionProvider;
import com.aston.trainee.repository.util.DBQueryConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс для основных CRUD операций (создание, получение, редактирование,удаление) с объектом типа Tag в базе данных
 */
public class TagRepository implements Repository<Tag> {
    /**
     * Переменная для получения соединения к базе данных и выполнения SQL запросов.
     */
    private final DBConnectionProvider connectionProvider;

    /**
     * Конструктор для создания объекта TagRepository
     *
     * @param connectionProvider - переменная для получения соединения к базе данных и выполнения SQL запросов.
     */
    public TagRepository(DBConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
        createTagsTableIfNotExists();
    }

    /**
     * Метод возвращает список всех тэгов, параметризованных типом Tag из базы данных.
     *
     * @return список всех элементов типа <Tag>, сохраненных в базе данных.
     */
    @Override
    public List<Tag> readAll() {
        Connection cn = connectionProvider.getConnection();
        List<Tag> tagList = new ArrayList<>();
        try (Statement st = cn.createStatement()) {
            ResultSet results = st.executeQuery(DBQueryConstants.SELECT_ALL_TAGS);
            while (results.next()) {
                Tag tag = new Tag();
                tag.setId((long) results.getInt(1));
                tag.setName(results.getString(2));
                tagList.add(tag);
            }
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
        return tagList;
    }

    /**
     * Метод возвращает объект типа Optional<Tag> из базы данных по указанному индексу.
     *
     * @param tagId - индекс получаемого объекта
     * @return объект типа Optional<Tag> из базы данных по указанному индексу.
     */
    @Override
    public Optional<Tag> readById(Long tagId) {
        Connection cn = connectionProvider.getConnection();
        Tag tag = null;
        try (PreparedStatement pst = cn.prepareStatement(DBQueryConstants.SELECT_TAG_BY_ID)) {
            pst.setLong(1, tagId);
            ResultSet result = pst.executeQuery();
            if (result.next()) {
                tag = new Tag();
                tag.setId(result.getLong(1));
                tag.setName(result.getString(2));
            }
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
        return Optional.ofNullable(tag);
    }

    /**
     * Метод, сохраняет передаваемый в параметр объект Tag в базу данных.
     *
     * @param model - объект, передаваемый для сохранения в базу данных;
     * @return созданный объект.
     */
    @Override
    public Tag create(Tag model) {
        Connection cn = connectionProvider.getConnection();
        int idInd = 1;
        Tag tag;
        try (PreparedStatement pst = cn.prepareStatement(DBQueryConstants.INSERT_TAG,
                Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, model.getName());
            int affectedRows = pst.executeUpdate();
            if (affectedRows == 0) {
                throw new SaveObjectException(Constants.CREATE_TAG_NO_ROWS_AFFECTED);
            }
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                tag = new Tag();
                int generatedId = rs.getInt(idInd);
                tag.setId((long) generatedId);
                tag.setName(model.getName());
            } else {
                throw new SaveObjectException(Constants.CREATE_TAG_NO_ID_OBTAINED);
            }

        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
        return tag;
    }

    /**
     * Метод обновляет поля переданного элемента в базе данных.
     *
     * @param model - элемент, передающий в своих полях новое состояние объекта;
     * @return обновленный элемент.
     */
    @Override
    public Tag update(Tag model) {
        Connection cn = connectionProvider.getConnection();
        int setNameInd = 1;
        int setIdInd = 2;
        try (PreparedStatement pst = cn.prepareStatement(DBQueryConstants.UPDATE_TAG_BY_ID)) {
            pst.setString(setNameInd, model.getName());
            pst.setLong(setIdInd, model.getId());
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                return readById(model.getId()).get();
            } else {
                throw new NotFoundException(Constants.TAG_ID_DOES_NOT_EXIST);
            }
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
    }

    /**
     * Метод удаляет элемент из базы данных по переданному id.
     *
     * @param tagId - индекс элемента для удаления.
     * @return булевое значение true при успешном удалении, false - при неудачном удалении.
     */
    @Override
    public Boolean deleteById(Long tagId) {
        Connection cn = connectionProvider.getConnection();
        int idIndex = 1;
        try (PreparedStatement pst = cn.prepareStatement(DBQueryConstants.DELETE_TAG_BY_NAME)) {
            pst.setLong(idIndex, tagId);
            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
    }

    /**
     * Метод проверяет наличие элемента в базе данных по переданному id.
     *
     * @param tagId - индекс элемента для проверки.
     * @return булевое значение true, если элемент присутствует, false - при отсутствии элемента.
     */
    @Override
    public Boolean isExistedById(Long tagId) {
        Optional<Tag> tag = readById(tagId);
        if (tag.isPresent()) {
            return true;
        } else {
            throw new NotFoundException(Constants.TAG_ID_DOES_NOT_EXIST);
        }
    }

    /**
     * Метод считает количество элементов по имени в таблице базы данных и возвращает их количество.
     * @param name - имя элемента для подсчета количества.
     * @return количество элементов в базе данных с указанным именем.
     */
    public int countByName(String name) {
        Connection cn = connectionProvider.getConnection();
        int count = 0;
        int idIndex = 1;
        try (PreparedStatement pst = cn.prepareStatement(DBQueryConstants.CHECK_TAGS_COUNT_BY_NAME)) {
            pst.setString(1, name);

            ResultSet results = pst.executeQuery();
            while (results.next()) {
                count = results.getInt(idIndex);
            }
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
        return count;
    }

    /**
     * Метод ищет объект Tag в базе данных по указанному имени и возвращает Optional<Tag>.
     * @param tagName - имя для поиска элемента в таблице базы данных.
     * @return Optional<Tag> элемента из базы данных.
     */
    public Optional<Tag> readByName(String tagName) {
        Connection cn = connectionProvider.getConnection();
        int idIndex = 1;
        int nameIndex = 2;
        Tag tag = null;
        try (PreparedStatement pst = cn.prepareStatement(DBQueryConstants.GET_TAG_BY_NAME)) {
            pst.setString(1, tagName);
            ResultSet result = pst.executeQuery();
            if (result.next()) {
                tag = new Tag();
                tag.setId(result.getLong(idIndex));
                tag.setName(result.getString(nameIndex));
            }
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
        return Optional.ofNullable(tag);
    }

    /**
     * Метод создает таблицу с тэгами в базе данных если она еще не создана.
     */
    private void createTagsTableIfNotExists() {
        try (Connection conn = this.connectionProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DBQueryConstants.CREATE_TAG_TABLE_IF_NOT_EXIST)) {
            pstmt.execute();
        } catch (SQLException e) {
            throw new ConnectionException(Constants.CREATE_TAG_TABLE_PROBLEM);
        }
    }
}

package com.aston.trainee.repository.impl;

import com.aston.trainee.repository.exceptions.ConnectionException;
import com.aston.trainee.repository.exceptions.NotFoundException;
import com.aston.trainee.repository.exceptions.SaveObjectException;
import com.aston.trainee.repository.Repository;
import com.aston.trainee.repository.model.Author;
import com.aston.trainee.repository.model.News;
import com.aston.trainee.repository.model.Tag;
import com.aston.trainee.repository.util.Constants;
import com.aston.trainee.repository.util.DBConnectionProvider;
import com.aston.trainee.repository.util.DBQueryConstants;

import java.sql.*;
import java.util.*;

/**
 * Класс для основных CRUD операций (создание, получение, редактирование,удаление) с объектом типа News в базе данных
 */
public class NewsRepository implements Repository<News> {

    /**
     * Переменная для получения соединения к базе данных и выполнения SQL запросов.
     */
    private final DBConnectionProvider connectionProvider;
    /**
     * Переменная для обращения к репозиторию с авторами и выполнения CRUD операций с ними
     */
    AuthorRepository authorRepository;
    /**
     * Переменная для обращения к репозиторию с тэгами и выполнения CRUD операций с ними
     */
    TagRepository tagRepository;

    /**
     * Конструктор для создания объекта NewsRepository
     *
     * @param connectionProvider - переменная для получения соединения к базе данных и выполнения SQL запросов.
     */
    public NewsRepository(DBConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
        this.authorRepository = new AuthorRepository(connectionProvider);
        this.tagRepository = new TagRepository(connectionProvider);
        createNewsTableIfNotExists();
    }

    /**
     * Метод возвращает список всех новостей, параметризованных типом News из базы данных.
     *
     * @return список всех элементов типа <News>, сохраненных в базе данных.
     */
    @Override
    public List<News> readAll() {
        Connection cn = connectionProvider.getConnection();
        List<News> newsList = new ArrayList<>();
        Map<Long, News> newsMap = new HashMap<>();
        int idInd = 1;
        int titleInd = 2;
        int contentInd = 3;
        int authorIdInd = 4;
        int authorNameInd = 5;
        int tagIdInd = 6;
        int tagNameInd = 7;
        try (Statement st = cn.createStatement()) {
            ResultSet results = st.executeQuery(DBQueryConstants.SELECT_ALL_NEWS);
            while (results.next()) {
                Long id = results.getLong(idInd);
                String newsTitle = results.getString(titleInd);
                String newsContent = results.getString(contentInd);
                Long authorId = results.getLong(authorIdInd);
                String authorName = results.getString(authorNameInd);
                long tagId = results.getLong(tagIdInd);
                String tagName = results.getString(tagNameInd);

                Tag tag = null;
                if (tagId > 0) {
                    tag = new Tag();
                    tag.setId(tagId);
                    tag.setName(tagName);
                }

                News news = newsMap.get(id);
                if (news == null) {
                    news = new News();
                    news.setId(id);
                    news.setTitle(newsTitle);
                    news.setContent(newsContent);
                    news.setAuthor(new Author(authorId, authorName));
                    news.setTags(new ArrayList<>());
                    newsMap.put(id, news);
                    newsList.add(news);
                }
                news.getTags().add(tag);
            }

        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
        return newsList;
    }

    /**
     * Метод возвращает объект типа Optional<News> из базы данных по указанному индексу.
     *
     * @param newsId - индекс получаемого объекта
     * @return объект типа Optional<News> из базы данных по указанному индексу.
     */
    @Override
    public Optional<News> readById(Long newsId) {
        Connection cn = connectionProvider.getConnection();
        News news = null;
        int idInd = 1;
        int titleInd = 2;
        int contentInd = 3;
        int authorIdInd = 4;
        int authorNameInd = 5;
        int tagIdInd = 6;
        int tagNameInd = 7;
        Map<Long, News> newsMap = new HashMap<>();
        try (PreparedStatement pst = cn.prepareStatement(DBQueryConstants.SELECT_NEWS_BY_ID)) {
            pst.setLong(1, newsId);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                Long id = result.getLong(idInd);
                String newsTitle = result.getString(titleInd);
                String newsContent = result.getString(contentInd);
                Long authorId = result.getLong(authorIdInd);
                String authorName = result.getString(authorNameInd);
                long tagId = result.getLong(tagIdInd);
                String tagName = result.getString(tagNameInd);

                Tag tag = null;
                if (tagId > 0) {
                    tag = new Tag();
                    tag.setId(tagId);
                    tag.setName(tagName);
                }

                news = newsMap.get(id);
                if (news == null) {
                    news = new News();
                    news.setId(id);
                    news.setTitle(newsTitle);
                    news.setContent(newsContent);
                    news.setAuthor(new Author(authorId, authorName));
                    news.setTags(new ArrayList<>());
                    newsMap.put(id, news);
                }
                news.getTags().add(tag);
            }
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
        return Optional.ofNullable(news);
    }

    /**
     * Метод, сохраняет передаваемый в параметр объект News в базу данных.
     *
     * @param model - объект, передаваемый для сохранения в базу данных;
     * @return созданный объект.
     */
    @Override
    public News create(News model) {
        Connection cn = connectionProvider.getConnection();
        News news;
        int titleInd = 1;
        int contentInd = 2;
        int authorIdInd = 3;
        try (PreparedStatement pstNews = cn.prepareStatement(DBQueryConstants.INSERT_NEWS,
                Statement.RETURN_GENERATED_KEYS)) {

            long authorId = addAuthorIfNotExists(cn, model);

            pstNews.setString(titleInd, model.getTitle());
            pstNews.setString(contentInd, model.getContent());
            pstNews.setLong(authorIdInd, authorId);

            int affectedNewsRows = pstNews.executeUpdate();

            if (affectedNewsRows == 0) {
                throw new SaveObjectException(Constants.CREATE_NEWS_NO_ROWS_AFFECTED);
            }
            try (ResultSet generatedKeys = pstNews.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long newsId = generatedKeys.getInt(1);
                    news = new News();
                    news.setId(newsId);
                    news.setTitle(model.getTitle());
                    news.setContent(model.getContent());
                    news.setAuthor(new Author(authorId, model.getAuthor().getName()));

                    addTagsFromList(cn, model.getTags(), newsId, news);
                } else {
                    throw new SaveObjectException(Constants.CREATE_NEWS_NO_ID_OBTAINED);
                }
            }
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
        return news;
    }

    private long addAuthorIfNotExists(Connection cn, News model) {
        long authorId = 0L;
        int authorIdInd = 1;
        try (PreparedStatement pstAuthor = cn.prepareStatement(DBQueryConstants.INSERT_AUTHOR_IF_NOT_EXIST,
                Statement.RETURN_GENERATED_KEYS)) {
            if (authorRepository.countByName(model.getAuthor().getName()) == 0) {
                pstAuthor.setString(1, model.getAuthor().getName());
                int affectedAuthorRows = pstAuthor.executeUpdate();
                if (affectedAuthorRows > 0) {
                    try (ResultSet authorResult = pstAuthor.getGeneratedKeys()) {
                        if (authorResult.next()) {
                            authorId = authorResult.getLong(authorIdInd);
                        }
                    }
                }
            } else {
                authorId = authorRepository.readByName(model.getAuthor().getName()).get().getId();
            }
        } catch (SQLException e) {
            throw new ConnectionException(Constants.CREATE_AUTHOR_PROBLEM);
        }
        return authorId;
    }

    private void addTagsFromList(Connection cn, List<Tag> tags, long newsId, News news) {
        try (PreparedStatement pstTag = cn.prepareStatement(DBQueryConstants.INSERT_TAG,
                Statement.RETURN_GENERATED_KEYS);
             PreparedStatement pstNewsTag = cn.prepareStatement(DBQueryConstants.INSERT_INTO_NEWS_TAG)) {
            for (Tag tag : tags) {
                pstTag.setString(1, tag.getName());

                long tagId = addTagIfNotExists(cn, tag);

                Tag resultTag = new Tag();
                resultTag.setId(tagId);
                resultTag.setName(tag.getName());
                news.getTags().add(resultTag);

                pstNewsTag.setLong(1, newsId);
                pstNewsTag.setLong(2, tagId);

                pstNewsTag.addBatch();
            }

            pstNewsTag.executeBatch();
        } catch (SQLException e) {
            throw new ConnectionException(Constants.CREATE_TAG_LIST_PROBLEM);
        }
    }

    private long addTagIfNotExists(Connection cn, Tag tag) {
        long tagId = 0;
        try (PreparedStatement pstTag = cn.prepareStatement(DBQueryConstants.INSERT_TAG,
                Statement.RETURN_GENERATED_KEYS)) {
            if (tagRepository.countByName(tag.getName()) == 0) {
                pstTag.setString(1, tag.getName());
                int affectedTagsRows = pstTag.executeUpdate();
                if (affectedTagsRows > 0) {
                    try (ResultSet tagResult = pstTag.getGeneratedKeys()) {
                        if (tagResult.next()) {
                            tagId = tagResult.getInt(1);
                        }
                    }
                }
            } else {
                tagId = tagRepository.readByName(tag.getName()).get().getId();
            }
        } catch (SQLException e) {
            throw new ConnectionException(Constants.CREATE_TAG_PROBLEM);
        }
        return tagId;
    }

    /**
     * Метод обновляет поля переданного элемента в базе данных.
     *
     * @param model - элемент, передающий в своих полях новое состояние объекта;
     * @return обновленный элемент.
     */
    @Override
    public News update(News model) {
        Connection cn = connectionProvider.getConnection();
        try (PreparedStatement pst = cn.prepareStatement(DBQueryConstants.UPDATE_NEWS_BY_ID)) {

            long authorId = addAuthorIfNotExists(cn, model);

            pst.setString(1, model.getTitle());
            pst.setString(2, model.getContent());
            pst.setLong(3, authorId);
            pst.setLong(4, model.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                return readById(model.getId()).get();
            } else {
                throw new NotFoundException(Constants.NEWS_ID_DOES_NOT_EXIST);
            }
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
    }

    /**
     * Метод удаляет элемент из базы данных по переданному id.
     *
     * @param newsId - индекс элемента для удаления.
     * @return булевое значение true при успешном удалении, false - при неудачном удалении.
     */
    @Override
    public Boolean deleteById(Long newsId) {
        Connection cn = connectionProvider.getConnection();
        try (PreparedStatement pst = cn.prepareStatement(DBQueryConstants.DELETE_NEWS_BY_ID)) {
            pst.setLong(1, newsId);
            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new ConnectionException(Constants.ERROR_DATA_ERROR);
        }
    }

    /**
     * Метод проверяет наличие элемента в базе данных по переданному id.
     *
     * @param newsId - индекс элемента для проверки.
     * @return булевое значение true, если элемент присутствует, false - при отсутствии элемента.
     */
    @Override
    public Boolean isExistedById(Long newsId) {
        Optional<News> news = readById(newsId);
        if (news.isPresent()) {
            return true;
        } else {
            throw new NotFoundException(Constants.NEWS_ID_DOES_NOT_EXIST);
        }
    }

    /**
     * Метод создает таблицу с новостями в базе данных, если она еще не создана.
     */
    private void createNewsTableIfNotExists() {
        try (Connection conn = this.connectionProvider.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     DBQueryConstants.CREATE_NEWS_TABLE_IF_NOT_EXIST
             );
             PreparedStatement createNewsTagsTable = conn.prepareStatement(
                     DBQueryConstants.CREATE_NEWS_TAG_TABLE_IF_NOT_EXIST)) {

            pstmt.execute();
            createNewsTagsTable.executeUpdate();
        } catch (SQLException e) {
            throw new ConnectionException(Constants.CREATE_NEWS_TABLE_PROBLEM);
        }
    }
}

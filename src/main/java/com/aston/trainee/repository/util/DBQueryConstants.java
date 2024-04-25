package com.aston.trainee.repository.util;

/**
 * Утилитный класс, предоставляющий константы с SQL запросами к базе данных в слое репозитория в приложении.
 */
public class DBQueryConstants {

    /**
     * Приватный конструктор для предотвращения возможности создания экземпляра этого класса.
     */
    private DBQueryConstants() {
    }

    public static final String CREATE_TAG_TABLE_IF_NOT_EXIST = """
            create table if not exists tags (
            id SERIAL NOT NULL PRIMARY KEY,
            name varchar(100) NOT NULL
            )
            """;
    public static final String GET_TAG_BY_NAME = "SELECT * FROM tags WHERE name = ?";
    public static final String CHECK_TAGS_COUNT_BY_NAME = "SELECT COUNT(*) FROM tags WHERE name = ?";
    public static final String DELETE_TAG_BY_NAME = "DELETE FROM tags WHERE id = ?";
    public static final String UPDATE_TAG_BY_ID = "UPDATE tags SET name = ? WHERE id = ?";
    public static final String INSERT_TAG = "INSERT INTO tags (name) VALUES (?)";
    public static final String SELECT_TAG_BY_ID = "SELECT * FROM tags WHERE id = ?";
    public static final String SELECT_ALL_TAGS = "SELECT * FROM tags";

    public static final String CREATE_AUTHOR_TABLE_IF_NOT_EXIST = """
            create table if not exists authors (
            id SERIAL NOT NULL PRIMARY KEY,
            name varchar(100) NOT NULL
            )
            """;
    public static final String GET_AUTHOR_BY_NAME = "SELECT * FROM authors WHERE name = ?";
    public static final String CHECK_AUTHORS_COUNT_BY_NAME = "SELECT COUNT(*) FROM authors WHERE name = ?";
    public static final String DELETE_AUTHORS_BY_ID = "DELETE FROM authors WHERE id = ?";
    public static final String UPDATE_AUTHORS_BY_ID = "UPDATE authors SET name = ? WHERE id = ?";
    public static final String INSERT_AUTHOR = "INSERT INTO authors (name) VALUES (?)";
    public static final String SELECT_AUTHOR_BY_ID = "SELECT * FROM authors WHERE id = ?";
    public static final String SELECT_ALL_AUTHORS = "SELECT * FROM authors";
    public static final String SELECT_NEWS_BY_ID = """
            SELECT news.id, news.title, news.content, a.id, a.name, t.id, t.name FROM news
            Join authors a on news.author_id = a.id
            LEFT OUTER JOIN news_tags nt on nt.news_id = news.id
            LEFT OUTER JOIN tags t on nt.tag_id = t.id
            WHERE news.id = ?
            """;

    public static final String SELECT_ALL_NEWS = """
            SELECT news.id, news.title, news.content, a.id, a.name, t.id, t.name FROM news
            Join authors a on news.author_id = a.id
            LEFT OUTER JOIN news_tags nt on nt.news_id = news.id
            LEFT OUTER JOIN tags t on nt.tag_id = t.id
            """;
    public static final String DELETE_NEWS_BY_ID = "DELETE FROM news WHERE id = ?";
    public static final String UPDATE_NEWS_BY_ID = "UPDATE news SET title = ?, content = ?, author_id = ?  WHERE id = ?";
    public static final String INSERT_NEWS = "INSERT INTO news (title, content, author_id) VALUES (?, ?, ?)";
    public static final String INSERT_AUTHOR_IF_NOT_EXIST = "INSERT INTO authors (name) VALUES (?)";
    public static final String INSERT_INTO_NEWS_TAG = "INSERT INTO news_tags (news_id, tag_id) VALUES (?, ?)";

    public static final String CREATE_NEWS_TABLE_IF_NOT_EXIST = """
            create table if not exists news (
                id SERIAL NOT NULL PRIMARY KEY ,
                title varchar(100) NOT NULL,
                content varchar(100) NOT NULL,
                author_id bigint,
                CONSTRAINT FK_NEWS_AUTHOR FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE SET NULL
            )
            """;
    public static final String CREATE_NEWS_TAG_TABLE_IF_NOT_EXIST = """
            CREATE TABLE IF NOT EXISTS news_tags(
                news_id bigint NULL,
                tag_id bigint NULL,
                CONSTRAINT FK_TAG_NEWS FOREIGN KEY (news_id) REFERENCES news(id) ON DELETE CASCADE,
                CONSTRAINT FK_NEWS_TAG FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
            )
            """;
}

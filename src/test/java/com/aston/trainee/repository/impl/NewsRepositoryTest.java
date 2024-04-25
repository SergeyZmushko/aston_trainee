package com.aston.trainee.repository.impl;

import com.aston.trainee.repository.exceptions.NotFoundException;
import com.aston.trainee.repository.model.Author;
import com.aston.trainee.repository.model.News;
import com.aston.trainee.repository.model.Tag;
import com.aston.trainee.repository.util.DBConnectionProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NewsRepositoryTest {

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    private NewsRepository newsRepository;

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @BeforeEach
    void setUp() {
        DBConnectionProvider connectionProvider = new DBConnectionProvider(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );
        TagRepository tagRepository = new TagRepository(connectionProvider);
        AuthorRepository authorRepository = new AuthorRepository(connectionProvider);
        newsRepository = new NewsRepository(connectionProvider);
        truncateTable(connectionProvider);
        Author author1 = new Author(null, "Egor Semenov");
        Author author2 = new Author(null, "Nikita Matveev");
        Author author3 = new Author(null, "Sergey Nikitenko");
        Tag tagFinance = new Tag(null, "FINANCE");
        Tag tagFun = new Tag(null, "FUN");
        Tag tagCrime = new Tag(null, "CRIME");
        Tag tagMusic = new Tag(null, "MUSIC");
        newsRepository.create(new News(null,
                "Increase of ruble",
                "The value of Russian ruble was increased",
                author1,
                List.of(tagFinance)));
        newsRepository.create(new News(null,
                "The new song of Eminem",
                "Eminem produced the new song",
                author2,
                List.of(tagMusic, tagFun)));
        newsRepository.create(new News(null,
                "Robbers like money",
                "Robbers stolen money from a man in the central park",
                author3,
                List.of(tagCrime)));
    }

    private void truncateTable(DBConnectionProvider connectionProvider){
        try (Connection conn = connectionProvider.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("TRUNCATE TABLE news, tags, authors, news_tags RESTART IDENTITY");
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void readAllNewsTest() {
        List<News> newsList = newsRepository.readAll();
        assertEquals(3, newsList.size());
    }

    @Test
    void readByIdTest() {
        News newsResult = newsRepository.readById(1L).get();
        assertEquals("Increase of ruble", newsResult.getTitle());
        assertEquals("The value of Russian ruble was increased", newsResult.getContent());
        assertEquals("Egor Semenov", newsResult.getAuthor().getName());
    }

    @Test
    void createNewsTest() {
        News news = new News();
        String title = "Disaster in the New Zealand";
        String content = "The earthquake was in New Zealand";
        Author author = new Author(null, "Timur Baidu");

        news.setTitle(title);
        news.setContent(content);
        news.setAuthor(author);
        News result = newsRepository.create(news);

        assertEquals(title, result.getTitle());
        assertEquals(content, result.getContent());
        assertEquals(author.getName(), result.getAuthor().getName());
    }

    @Test
    void updateNewsTest(){
        News news = new News();
        String title = "Disaster in the New Zealand";
        String content = "The earthquake was in New Zealand";
        Author author = new Author(null,"Alex Fitch");
        news.setTitle(title);
        news.setContent(content);
        news.setAuthor(author);
        News expectedNews = newsRepository.create(news);

        expectedNews.setTitle("Earthquake with flu-dding");
        News resultNews = newsRepository.update(expectedNews);

        assertEquals(expectedNews.getTitle(), resultNews.getTitle());
    }

    @Test
    void deleteNewsByIdTest(){
        List<News> newsList = newsRepository.readAll();
        assertEquals(3, newsList.size());

        newsRepository.deleteById(1L);
        assertEquals(2, newsRepository.readAll().size());
    }

    @Test
    void isExistedByIdTest(){
        boolean existResult = newsRepository.isExistedById(1L);

        assertTrue(existResult);
    }

    @Test
    void isExistedByIdThrowNotFoundExceptionTest(){
        assertThrows(NotFoundException.class, () -> newsRepository.isExistedById(100L));
    }
}

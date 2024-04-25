package com.aston.trainee.repository.impl;

import com.aston.trainee.repository.exceptions.NotFoundException;
import com.aston.trainee.repository.model.Author;
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


class AuthorRepositoryTest {
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    private AuthorRepository authorRepository;

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
        authorRepository = new AuthorRepository(connectionProvider);
        truncateTable(connectionProvider);
        authorRepository.create(new Author(null, "David Yakubovich"));
        authorRepository.create(new Author(null, "Kirill Bober"));
    }

    private void truncateTable(DBConnectionProvider connectionProvider){
        try (Connection conn = connectionProvider.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("TRUNCATE TABLE authors RESTART IDENTITY");
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void readAllTest() {
        List<Author> authorList = authorRepository.readAll();
        assertEquals(2, authorList.size());
    }

    @Test
    void readByIdTest() {
        Author expectedResult = new Author(1L, "David Yakubovich");

        Author authorResult = authorRepository.readById(1L).get();
        assertEquals(expectedResult, authorResult);
    }

    @Test
    void createAuthorTest() {
        Author author = new Author();
        author.setName("Egor Nazimov");
        Author result = authorRepository.create(author);

        Author expectedResult = new Author(3L, author.getName());

        assertEquals(expectedResult, result);
    }

    @Test
    void updateAuthorTest(){
        Author author = new Author();
        author.setName("Egor Nazimov");
        Author expectedAuthor = authorRepository.create(author);

        expectedAuthor.setName("Nikolay Prokopenko");
        Author resultAuthor = authorRepository.update(expectedAuthor);

        assertEquals(expectedAuthor, resultAuthor);
    }

    @Test
    void deleteAuthorByIdTest(){
        List<Author> authorList = authorRepository.readAll();
        assertEquals(2, authorList.size());

        authorRepository.deleteById(1L);
        assertEquals(1, authorRepository.readAll().size());
    }

    @Test
    void countByNameTest(){
        int countWhenExist = authorRepository.countByName("David Yakubovich");
        assertEquals(1, countWhenExist);

        int countWhenNotExist = authorRepository.countByName("Yana Bobovaya");
        assertEquals(0, countWhenNotExist);
    }

    @Test
    void isExistedByIdTest(){
        boolean existResult = authorRepository.isExistedById(1L);

        assertTrue(existResult);
    }

    @Test
    void isExistedByIdThrowNotFoundExceptionTest(){
        assertThrows(NotFoundException.class, () -> authorRepository.isExistedById(100L));
    }

    @Test
    void readByNameTest(){
        Author expectedAuthor = new Author(1L, "David Yakubovich");

        Author result = authorRepository.readByName(expectedAuthor.getName()).get();
        assertEquals(expectedAuthor, result);
    }
}
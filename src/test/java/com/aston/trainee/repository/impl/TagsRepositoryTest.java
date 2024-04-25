package com.aston.trainee.repository.impl;

import com.aston.trainee.repository.exceptions.NotFoundException;
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

class TagsRepositoryTest {

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    private TagRepository tagRepository;

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
        tagRepository = new TagRepository(connectionProvider);
        truncateTable(connectionProvider);
        tagRepository.create(new Tag(null, "FUN"));
        tagRepository.create(new Tag(null, "FINANCE"));
    }

    private void truncateTable(DBConnectionProvider connectionProvider){
        try (Connection conn = connectionProvider.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("TRUNCATE TABLE tags RESTART IDENTITY");
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void readAllTagsTest() {
        List<Tag> tagList = tagRepository.readAll();
        assertEquals(2, tagList.size());
    }

    @Test
    void readByIdTest() {
        Tag expectedResult = new Tag(1L, "FUN");

        Tag tagResult = tagRepository.readById(1L).get();
        assertEquals(expectedResult, tagResult);
    }

    @Test
    void createTagTest() {
        Tag tag = new Tag();
        tag.setName("DISASTER");
        Tag result = tagRepository.create(tag);

        Tag expectedResult = new Tag(3L, tag.getName());

        assertEquals(expectedResult, result);
    }

    @Test
    void updateTagTest(){
        Tag tag = new Tag();
        tag.setName("CAR");
        Tag expectedTag = tagRepository.create(tag);

        expectedTag.setName("WEATHER");
        Tag resultTag = tagRepository.update(expectedTag);

        assertEquals(expectedTag, resultTag);
    }

    @Test
    void deleteTagByIdTest(){
        List<Tag> tagList = tagRepository.readAll();
        assertEquals(2, tagList.size());

        tagRepository.deleteById(1L);
        assertEquals(1, tagRepository.readAll().size());
    }

    @Test
    void countByNameTest(){
        int countWhenExist = tagRepository.countByName("FUN");
        assertEquals(1, countWhenExist);

        int countWhenNotExist = tagRepository.countByName("CRIME");
        assertEquals(0, countWhenNotExist);
    }

    @Test
    void isExistedByIdTest(){
        boolean existResult = tagRepository.isExistedById(1L);

        assertTrue(existResult);
    }

    @Test
    void isExistedByIdThrowNotFoundExceptionTest(){
        assertThrows(NotFoundException.class, () -> tagRepository.isExistedById(100L));
    }

    @Test
    void readByNameTest(){
        Tag expectedTag = new Tag(1L, "FUN");

        Tag result = tagRepository.readByName(expectedTag.getName()).get();
        assertEquals(expectedTag, result);
    }
}

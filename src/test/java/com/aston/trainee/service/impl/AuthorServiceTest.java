package com.aston.trainee.service.impl;

import com.aston.trainee.repository.impl.AuthorRepository;
import com.aston.trainee.repository.model.Author;
import com.aston.trainee.service.dto.AuthorDtoRequest;
import com.aston.trainee.service.dto.AuthorDtoResponse;
import com.aston.trainee.service.dto.UpdateAuthorDtoRequest;
import com.aston.trainee.service.interfaces.AuthorModelMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    AuthorRepository authorRepository;
    @Spy
    AuthorModelMapper authorModelMapper;

    @InjectMocks
    AuthorService authorService;

    @Test
    void readAll() {
        Author author1 = new Author(1L, "Sara Konor");
        Author author2 = new Author(2L, "Fedor Potapov");
        Author author3 = new Author(3L, "Kirill Bodaichuk");

        when(authorRepository.readAll()).thenReturn(List.of(author1, author2, author3));

        List<AuthorDtoResponse> result = authorService.readAll();

        assertEquals(3, result.size());
        assertEquals(author1.getName(), result.get(0).name());
        assertEquals(author2.getName(), result.get(1).name());
        assertEquals(author3.getName(), result.get(2).name());
    }

    @Test
    void readById() {
        String authorName = "David Semenov";
        Long authorId = 2L;
        when(authorRepository.readById(2L)).thenReturn(Optional.of(new Author(authorId, authorName)));

        AuthorDtoResponse resultAuthor = authorService.readById(2L);
        assertEquals(authorName, resultAuthor.name());
        assertEquals(authorId, resultAuthor.id());
    }

    @Test
    void create() {
        AuthorDtoRequest authorRequest = new AuthorDtoRequest("Evgeniy Smirnov");

        Author returnAuthor = new Author(4L, authorRequest.name());
        Author fromDtoAuthor = new Author(null, authorRequest.name());

        when(authorRepository.create(fromDtoAuthor)).thenReturn(returnAuthor);

        AuthorDtoResponse resultAuthor = authorService.create(authorRequest);

        assertEquals(authorRequest.name(), resultAuthor.name());
        assertEquals(4L, resultAuthor.id());
    }

    @Test
    void update() {
        UpdateAuthorDtoRequest author = new UpdateAuthorDtoRequest(4L, "Evgeniy Smirnov");
        Author returnAuthor = new Author(4L, "Evgeniy Smirnov");
        when(authorRepository.update(returnAuthor)).thenReturn(returnAuthor);

        AuthorDtoResponse resultAuthor = authorService.update(4L, author);

        assertEquals(author.name(), resultAuthor.name());
        assertEquals(4L, resultAuthor.id());
    }

    @Test
    void deleteById() {
        when(authorRepository.isExistedById(5L)).thenReturn(true);
        when(authorRepository.deleteById(5L)).thenReturn(true);

        assertTrue(authorService.deleteById(5L));
    }
}
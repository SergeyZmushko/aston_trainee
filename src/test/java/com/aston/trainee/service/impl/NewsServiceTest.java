package com.aston.trainee.service.impl;

import com.aston.trainee.repository.impl.NewsRepository;
import com.aston.trainee.repository.model.Author;
import com.aston.trainee.repository.model.News;
import com.aston.trainee.repository.model.Tag;
import com.aston.trainee.service.dto.*;
import com.aston.trainee.service.interfaces.NewsModelMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    NewsRepository newsRepository;
    @Spy
    NewsModelMapper newsModelMapper;

    @InjectMocks
    NewsService newsService;

    @Test
    void readAll() {
        Author author1 = new Author(null, "Egor Semenov");
        Author author2 = new Author(null, "Nikita Matveev");
        Author author3 = new Author(null, "Sergey Nikitenko");
        Tag tagFinance = new Tag(null, "FINANCE");
        Tag tagFun = new Tag(null, "FUN");
        Tag tagCrime = new Tag(null, "CRIME");
        Tag tagMusic = new Tag(null, "MUSIC");
        News news1 = new News(null,
                "Increase of ruble",
                "The value of Russian ruble was increased",
                author1,
                List.of(tagFinance));
        News news2 = new News(null,
                "The new song of Eminem",
                "Eminem produced the new song",
                author2,
                List.of(tagMusic, tagFun));
        News news3 = new News(null,
                "Robbers like money",
                "Robbers stolen money from a man in the central park",
                author3,
                List.of(tagCrime));
        List<News> newsList = new ArrayList<>();
        newsList.add(news1);
        newsList.add(news2);
        newsList.add(news3);

        when(newsRepository.readAll()).thenReturn(newsList);

        List<NewsDtoResponse> result = newsService.readAll();

        assertEquals(3, result.size());
        assertEquals(news1.getTitle(), result.get(0).getTitle());
        assertEquals(news2.getTitle(), result.get(1).getTitle());
        assertEquals(news3.getTitle(), result.get(2).getTitle());
    }

    @Test
    void readById() {
        Author author1 = new Author(null, "Egor Semenov");
        Tag tagFinance = new Tag(null, "FINANCE");
        News news1 = new News(null,
                "Increase of ruble",
                "The value of Russian ruble was increased",
                author1,
                List.of(tagFinance));
        Long newsId = 2L;

        when(newsRepository.readById(2L)).thenReturn(Optional.of(news1));

        NewsDtoResponse resultNews = newsService.readById(2L);
        assertEquals(news1.getTitle(), resultNews.getTitle());
        assertEquals(news1.getContent(), resultNews.getContent());
        assertEquals(news1.getAuthor().getName(), resultNews.getAuthorDto().name());
        assertEquals(news1.getTags().get(0).getName(), resultNews.getTagsDto().get(0).name());
    }

    @Test
    void create() {
        String title = "Disaster in the New Zealand";
        String content = "The earthquake was in New Zealand";
        Author author = new Author(null, "Timur Baidu");
        Tag tagCrime = new Tag(null, "CRIME");
        Tag tagMusic = new Tag(null, "MUSIC");
        NewsDtoRequest newsDtoRequest = new NewsDtoRequest(title, content, author.getName(), List.of(tagCrime.getName(), tagMusic.getName()));

        News returnNews = new News(4L, newsDtoRequest.title(), newsDtoRequest.content(), author, List.of(tagCrime, tagMusic));
        News fromDtoNews = new News(null, newsDtoRequest.title(), newsDtoRequest.content(), author, List.of(tagCrime, tagMusic));

        when(newsRepository.create(fromDtoNews)).thenReturn(returnNews);

        NewsDtoResponse resultNews = newsService.create(newsDtoRequest);

        assertEquals(newsDtoRequest.title(), resultNews.getTitle());
        assertEquals(newsDtoRequest.content(), resultNews.getContent());
        assertEquals(newsDtoRequest.author(), resultNews.getAuthorDto().name());
        assertEquals(newsDtoRequest.tags().get(0), resultNews.getTagsDto().get(0).name());
        assertEquals(4L, resultNews.getId());
    }

    @Test
    void update() {
        String title = "Disaster in the New Zealand";
        String content = "The earthquake was in New Zealand";
        Author author = new Author(null, "Timur Baidu");
        Tag tagCrime = new Tag(null, "CRIME");
        Tag tagMusic = new Tag(null, "MUSIC");
        UpdateNewsDtoRequest updateNewsDtoRequest = new UpdateNewsDtoRequest(title, content, author.getName(), List.of(tagCrime.getName(), tagMusic.getName()));


        News returnNews = new News(null, title, content,  author, List.of(tagCrime, tagMusic));
        when(newsRepository.update(returnNews)).thenReturn(returnNews);

        NewsDtoResponse resultNews = newsService.update(4L, updateNewsDtoRequest);

        assertEquals(updateNewsDtoRequest.title(), resultNews.getTitle());
        assertEquals(updateNewsDtoRequest.content(), resultNews.getContent());
        assertEquals(updateNewsDtoRequest.author(), resultNews.getAuthorDto().name());
        assertEquals(updateNewsDtoRequest.tags().get(0), resultNews.getTagsDto().get(0).name());
    }

    @Test
    void deleteById() {
        when(newsRepository.isExistedById(5L)).thenReturn(true);
        when(newsRepository.deleteById(5L)).thenReturn(true);

        assertTrue(newsService.deleteById(5L));
    }
}
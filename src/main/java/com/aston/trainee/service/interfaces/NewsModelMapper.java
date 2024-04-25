package com.aston.trainee.service.interfaces;

import com.aston.trainee.repository.model.Author;
import com.aston.trainee.repository.model.News;
import com.aston.trainee.repository.model.Tag;
import com.aston.trainee.service.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Класс для преобразования возвращаемых обектов новости из базы данных в репрезентуемый обект
 * * для пользователя и обратно.
 */
@Mapper(uses = {AuthorModelMapper.class, TagModelMapper.class})
public abstract class NewsModelMapper {

    /**
     * Инстанс объекта NewsModelMapper для вызова его методов.
     */
    public static final NewsModelMapper INSTANCE = Mappers.getMapper(NewsModelMapper.class);


    /**
     * Метод преобразует список новостей для представления их клиенту.
     *
     * @param newsList - список входных новостей для преобразования их в список объектов типа NewsDtoResponse
     * @return список новостей, преобразованных для представления их клиенту
     */
    public abstract List<NewsDtoResponse> newsListToDtoList(List<News> newsList);

    /**
     * Метод преобразует новость, преобразованную для представления клиенту.
     *
     * @param news - объект новости для преобразования
     * @return обект типа NewsDtoResponse
     */
    @Mapping(source = "author", target = "authorDto")
    @Mapping(source = "tags", target = "tagsDto")
    public abstract NewsDtoResponse modelToDto(News news);

    /**
     * Метод преобразует и возвращает объект типа News из объекта типа UpdateNewsDtoRequest.
     *
     * @param dtoRequest объект UpdateNewsDtoRequest для преобразования в объект типа News
     * @return обект типа News из объекта типа UpdateNewsDtoRequest;
     */
    @Mapping(target = "id", ignore = true)
    public News updateDtoToModel(UpdateNewsDtoRequest dtoRequest) {
        return getNews(dtoRequest.author(), dtoRequest.title(), dtoRequest.content(), dtoRequest.tags());
    }

    private News getNews(String author2, String title, String content, List<String> tags) {
        News news = new News();
        Author author = new Author();
        author.setName(author2);
        news.setTitle(title);
        news.setContent(content);
        news.setAuthor(author);
        news.setTags(tags.stream().map(name -> {
            Tag tag = new Tag();
            tag.setName(name);
            return tag;
        }).toList());
        return news;
    }

    /**
     * Метод преобразует объект типа NewsDtoRequest в объект типа News
     *
     * @param dtoRequest объект NewsDtoRequest для преобразования в объект типа News
     * @return обект типа News из объекта типа NewsDtoRequest;
     */
    @Mapping(target = "id", ignore = true)
    public News dtoToModel(NewsDtoRequest dtoRequest) {
        return getNews(dtoRequest.author(), dtoRequest.title(), dtoRequest.content(), dtoRequest.tags());
    }
}

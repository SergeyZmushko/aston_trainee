package com.aston.trainee.service.interfaces;

import com.aston.trainee.repository.model.Author;
import com.aston.trainee.service.dto.AuthorDtoRequest;
import com.aston.trainee.service.dto.AuthorDtoResponse;
import com.aston.trainee.service.dto.UpdateAuthorDtoRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Интерфейс для преобразования возвращаемых обектов автора из базы данных в репрезентуемый обект
 * для пользователя и обратно
 */
@Mapper
public interface AuthorModelMapper {

    /**
     * Инстанс объекта AuthorModelMapper для вызова его методов.
     */
    AuthorModelMapper INSTANCE = Mappers.getMapper(AuthorModelMapper.class);

    /**
     * Метод преобразует список авторов для представления их клиенту.
     *
     * @param authorList - список входных авторов для преобразования их в список объектов типа AuthorDtoResponse
     * @return список авторов, преобразованных для представления их клиенту
     */
    List<AuthorDtoResponse> authorListToDtoList(List<Author> authorList);

    /**
     * Метод преобразует автора, преобразованного для представления клиенту.
     *
     * @param author - объект автора для преобразования
     * @return обект типа AuthorDtoResponse
     */
    AuthorDtoResponse modelToDto(Author author);

    /**
     * Метод преобразует и возвращает объект типа Author из объекта типа UpdateAuthorDtoRequest. .
     *
     * @param dtoRequest объект UpdateAuthorDtoRequest для преобразования в объект типа Author
     * @return обект типа Author из объекта типа UpdateAuthorDtoRequest;
     */
    Author updateDtoToModel(UpdateAuthorDtoRequest dtoRequest);

    /**
     * Метод преобразует объект типа AuthorDtoRequest в объект типа Author
     *
     * @param dtoRequest объект AuthorDtoRequest для преобразования в объект типа Author
     * @return обект типа Author из объекта типа AuthorDtoRequest;
     */
    @Mapping(target = "id", ignore = true)
    Author dtoToModel(AuthorDtoRequest dtoRequest);
}

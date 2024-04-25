package com.aston.trainee.service.interfaces;

import com.aston.trainee.repository.model.Tag;
import com.aston.trainee.service.dto.*;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Класс для преобразования возвращаемых обектов тэга из базы данных в репрезентуемый обект
 * * для пользователя и обратно.
 */
@Mapper(uses = NewsModelMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class TagModelMapper {

    /**
     * Инстанс объекта TagModelMapper для вызова его методов.
     */
    public static final TagModelMapper INSTANCE = Mappers.getMapper(TagModelMapper.class);

    /**
     * Метод преобразует список тэгов для представления их клиенту.
     *
     * @param tagList - список входных тэгов для преобразования их в список объектов типа TagDtoResponse
     * @return список тэгов, преобразованных для представления их клиенту
     */
    public abstract List<TagDtoResponse> tagListToDtoList(List<Tag> tagList);

    /**
     * Метод преобразует тэг, преобразованную для представления клиенту.
     *
     * @param tag - объект новости для преобразования
     * @return обект типа TagDtoResponse
     */
    public abstract TagDtoResponse modelToDto(Tag tag);

    /**
     * Метод преобразует и возвращает объект типа Tag из объекта типа UpdateTagDtoRequest.
     *
     * @param dtoRequest объект UpdateTagDtoRequest для преобразования в объект типа Tag
     * @return обект типа Tag из объекта типа UpdateTagDtoRequest;
     */
    public abstract Tag updateDtoToModel(UpdateTagDtoRequest dtoRequest);

    /**
     * Метод преобразует объект типа TagDtoRequest в объект типа Tag
     *
     * @param dtoRequest объект TagDtoRequest для преобразования в объект типа Tag
     * @return обект типа Tag из объекта типа TagDtoRequest;
     */
    @Mapping(target = "id", ignore = true)
    public abstract Tag dtoToModel(TagDtoRequest dtoRequest);
}

package com.aston.trainee.service.impl;

import com.aston.trainee.repository.exceptions.ConnectionException;
import com.aston.trainee.repository.exceptions.NotFoundException;
import com.aston.trainee.repository.exceptions.SaveObjectException;
import com.aston.trainee.repository.impl.TagRepository;
import com.aston.trainee.repository.Repository;
import com.aston.trainee.repository.model.Tag;
import com.aston.trainee.service.BaseService;
import com.aston.trainee.service.dto.*;
import com.aston.trainee.service.exceptions.ServiceException;
import com.aston.trainee.service.interfaces.TagModelMapper;

import java.util.List;
import java.util.Optional;

import static com.aston.trainee.service.exceptions.ServiceErrorCode.*;

/**
 * Класс для обращения к слою репозитория для выполнения CRUD операций (создание, получение, обновление, удаление) объектов.
 */
public class TagService implements BaseService<TagDtoResponse, Long, TagDtoRequest, UpdateTagDtoRequest> {
    /**
     * Переменная для обращения к слою репозитория.
     */
    private final Repository<Tag> tagRepository;
    /**
     * Переменная для преобразования объекта Athor в репрезентуемый объект для клиента и обратно.
     */
    private final TagModelMapper mapper;

    /**
     * Конструктор - создание нового объекта и инициализация его полей.
     *
     * @param tagRepository - конкретная имплементация интерфейса Repository.
     */
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
        this.mapper = TagModelMapper.INSTANCE;
    }

    /**
     * Метод возвращает список всех тэгов из слоя репозитория.
     *
     * @return список всех элементов, сохраненных в базе данных.
     */
    @Override
    public List<TagDtoResponse> readAll() {
        try {
            List<Tag> tagList = tagRepository.readAll();
            return mapper.tagListToDtoList(tagList);
        } catch (ConnectionException e) {
            throw new ServiceException(ERROR_DATA.getErrorMessage(), ERROR_DATA.getErrorCode());
        }
    }

    /**
     * Метод возвращает объект тэга из слоя репозитория по указанному индексу.
     *
     * @param id - индекс получаемого объекта
     * @return объект тэга из слоя репозитория по указанному индексу.
     */
    @Override
    public TagDtoResponse readById(Long id) {
        try {
            Optional<Tag> tag = tagRepository.readById(id);
            if (tag.isPresent()) {
                return mapper.modelToDto(tag.get());
            } else {
                throw new NotFoundException(String.format(TAG_ID_DOES_NOT_EXIST.getErrorMessage(), id));
            }
        } catch (NotFoundException e) {
            throw new ServiceException(e.getMessage(), TAG_ID_DOES_NOT_EXIST.getErrorCode());
        } catch (ConnectionException e) {
            throw new ServiceException(ERROR_DATA.getErrorMessage(), ERROR_DATA.getErrorCode());
        }
    }

    /**
     * Метод, передает передаваемый в параметр объект в слой репозитория.
     *
     * @param createRequest - объект, передаваемый в слой репозитория для создания нового объекта;
     * @return созданный объект.
     */
    @Override
    public TagDtoResponse create(TagDtoRequest createRequest) {
        Tag tag = mapper.dtoToModel(createRequest);
        try {
            tag = tagRepository.create(tag);

        } catch (SaveObjectException e) {
            throw new ServiceException(e.getMessage(), SAVE_DATA_EXCEPTION.getErrorCode());
        } catch (ConnectionException e) {
            throw new ServiceException(e.getMessage(), ERROR_DATA.getErrorCode());
        }
        return mapper.modelToDto(tag);
    }

    /**
     * Метод передает элемент в слой репозитория для обновления объекта.
     *
     * @param id            - индекс элемента для обновления;
     * @param updateRequest - элемент, передающий в своих полях новое состояние объекта;
     * @return обновленный элемент.
     */
    @Override
    public TagDtoResponse update(Long id, UpdateTagDtoRequest updateRequest) {
        try {
            Tag tag = mapper.updateDtoToModel(updateRequest);
            return mapper.modelToDto(tagRepository.update(tag));
        } catch (NotFoundException | ConnectionException e) {
            throw new ServiceException(e.getMessage(), TAG_ID_DOES_NOT_EXIST.getErrorCode());
        }
    }

    /**
     * Метод обращающийся в слой репозитория для удаления объекта по указанному id.
     *
     * @param id - индекс элемента для удаления.
     * @return булевое значение true при успешном удалении, false - при неудачном удалении.
     */
    @Override
    public boolean deleteById(Long id) {
        if (tagRepository.isExistedById(id)) {
            tagRepository.deleteById(id);
            return true;
        } else {
            throw new ServiceException(TAG_ID_DOES_NOT_EXIST.getErrorMessage(), TAG_ID_DOES_NOT_EXIST.getErrorCode());
        }
    }
}

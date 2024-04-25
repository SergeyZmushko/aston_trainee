package com.aston.trainee.service.impl;

import com.aston.trainee.repository.exceptions.ConnectionException;
import com.aston.trainee.repository.exceptions.NotFoundException;
import com.aston.trainee.repository.exceptions.SaveObjectException;
import com.aston.trainee.repository.impl.AuthorRepository;
import com.aston.trainee.repository.Repository;
import com.aston.trainee.repository.model.Author;
import com.aston.trainee.service.BaseService;
import com.aston.trainee.service.dto.AuthorDtoRequest;
import com.aston.trainee.service.dto.AuthorDtoResponse;
import com.aston.trainee.service.dto.UpdateAuthorDtoRequest;
import com.aston.trainee.service.exceptions.ServiceException;
import com.aston.trainee.service.interfaces.AuthorModelMapper;

import java.util.List;
import java.util.Optional;

import static com.aston.trainee.service.exceptions.ServiceErrorCode.*;

/**
 * Класс для обращения к слою репозитория для выполнения CRUD операций (создание, получение, обновление, удаление) объектов.
 */
public class AuthorService implements BaseService<AuthorDtoResponse, Long, AuthorDtoRequest, UpdateAuthorDtoRequest> {

    /**
     * Переменная для обращения к слою репозитория.
     */
    private final Repository<Author> authorRepository;
    /**
     * Переменная для преобразования объекта Athor в репрезентуемый объект для клиента и обратно.
     */
    private final AuthorModelMapper mapper;

    /**
     * Конструктор - создание нового объекта и инициализация его полей.
     *
     * @param authorRepository - конкретная имплементация интерфейса Repository.
     */
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
        this.mapper = AuthorModelMapper.INSTANCE;
    }

    /**
     * Метод возвращает список всех авторов из слоя репозитория.
     *
     * @return список всех элементов, сохраненных в базе данных.
     */
    @Override
    public List<AuthorDtoResponse> readAll() {
        try {
            List<Author> authorList = authorRepository.readAll();
            return mapper.authorListToDtoList(authorList);
        } catch (ConnectionException e) {
            throw new ServiceException(ERROR_DATA.getErrorMessage(), ERROR_DATA.getErrorCode());
        }
    }

    /**
     * Метод возвращает объект автора из слоя репозитория по указанному индексу.
     *
     * @param id - индекс получаемого объекта
     * @return объект автора из слоя репозитория по указанному индексу.
     */
    @Override
    public AuthorDtoResponse readById(Long id) {
        try {
            Optional<Author> author = authorRepository.readById(id);
            if (author.isPresent()) {
                return mapper.modelToDto(author.get());
            } else {
                throw new NotFoundException(String.format(AUTHOR_ID_DOES_NOT_EXIST.getErrorMessage(), id));
            }
        } catch (NotFoundException e) {
            throw new ServiceException(e.getMessage(), AUTHOR_ID_DOES_NOT_EXIST.getErrorCode());
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
    public AuthorDtoResponse create(AuthorDtoRequest createRequest) {
        Author author = mapper.dtoToModel(createRequest);
        try {
            author = authorRepository.create(author);

        } catch (SaveObjectException e) {
            throw new ServiceException(e.getMessage(), SAVE_DATA_EXCEPTION.getErrorCode());
        } catch (ConnectionException e) {
            throw new ServiceException(e.getMessage(), ERROR_DATA.getErrorCode());
        }
        return mapper.modelToDto(author);
    }

    /**
     * Метод передает элемент в слой репозитория для обновления объекта.
     *
     * @param id            - индекс элемента для обновления;
     * @param updateRequest - элемент, передающий в своих полях новое состояние объекта;
     * @return обновленный элемент.
     */
    @Override
    public AuthorDtoResponse update(Long id, UpdateAuthorDtoRequest updateRequest) {
        try {
            Author author = mapper.updateDtoToModel(updateRequest);
            return mapper.modelToDto(authorRepository.update(author));
        } catch (NotFoundException | ConnectionException e) {
            throw new ServiceException(e.getMessage(), AUTHOR_ID_DOES_NOT_EXIST.getErrorCode());
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
        if (authorRepository.isExistedById(id)) {
            authorRepository.deleteById(id);
            return true;
        } else {
            throw new ServiceException(AUTHOR_ID_DOES_NOT_EXIST.getErrorMessage(), AUTHOR_ID_DOES_NOT_EXIST.getErrorCode());
        }
    }
}

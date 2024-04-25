package com.aston.trainee.service.impl;

import com.aston.trainee.repository.exceptions.ConnectionException;
import com.aston.trainee.repository.exceptions.NotFoundException;
import com.aston.trainee.repository.exceptions.SaveObjectException;
import com.aston.trainee.repository.impl.NewsRepository;
import com.aston.trainee.repository.Repository;
import com.aston.trainee.repository.model.News;
import com.aston.trainee.service.BaseService;
import com.aston.trainee.service.dto.*;
import com.aston.trainee.service.exceptions.ServiceException;
import com.aston.trainee.service.interfaces.NewsModelMapper;

import java.util.List;
import java.util.Optional;

import static com.aston.trainee.service.exceptions.ServiceErrorCode.*;

/**
 * Класс для обращения к слою репозитория для выполнения CRUD операций (создание, получение, обновление, удаление) объектов.
 */
public class NewsService implements BaseService<NewsDtoResponse, Long, NewsDtoRequest, UpdateNewsDtoRequest> {
    /**
     * Переменная для обращения к слою репозитория.
     */
    private final Repository<News> newsRepository;
    /**
     * Переменная для преобразования объекта Athor в репрезентуемый объект для клиента и обратно.
     */
    private final NewsModelMapper mapper;

    /**
     * Конструктор - создание нового объекта и инициализация его полей.
     *
     * @param newsRepository - конкретная имплементация интерфейса Repository.
     */
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
        this.mapper = NewsModelMapper.INSTANCE;
    }

    /**
     * Метод возвращает список всех новостей из слоя репозитория.
     *
     * @return список всех элементов, сохраненных в базе данных.
     */
    @Override
    public List<NewsDtoResponse> readAll() {
        try {
            List<News> newsList = newsRepository.readAll();
            return mapper.newsListToDtoList(newsList);
        } catch (ConnectionException e) {
            throw new ServiceException(ERROR_DATA.getErrorMessage(), ERROR_DATA.getErrorCode());
        }
    }

    /**
     * Метод возвращает объект новости из слоя репозитория по указанному индексу.
     *
     * @param id - индекс получаемого объекта
     * @return объект новости из слоя репозитория по указанному индексу.
     */
    @Override
    public NewsDtoResponse readById(Long id) {
        try {
            Optional<News> news = newsRepository.readById(id);
            if (news.isPresent()) {
                return mapper.modelToDto(news.get());
            } else {
                throw new NotFoundException(String.format(NEWS_ID_DOES_NOT_EXIST.getErrorMessage(), id));
            }
        } catch (NotFoundException e) {
            throw new ServiceException(e.getMessage(), NEWS_ID_DOES_NOT_EXIST.getErrorCode());
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
    public NewsDtoResponse create(NewsDtoRequest createRequest) {
        News news = mapper.dtoToModel(createRequest);
        try {
            news = newsRepository.create(news);
        } catch (SaveObjectException e) {
            throw new ServiceException(e.getMessage(), SAVE_DATA_EXCEPTION.getErrorCode());
        } catch (ConnectionException e) {
            throw new ServiceException(e.getMessage(), ERROR_DATA.getErrorCode());
        }
        return mapper.modelToDto(news);
    }

    /**
     * Метод передает элемент в слой репозитория для обновления объекта.
     *
     * @param id            - индекс элемента для обновления;
     * @param updateRequest - элемент, передающий в своих полях новое состояние объекта;
     * @return обновленный элемент.
     */
    @Override
    public NewsDtoResponse update(Long id, UpdateNewsDtoRequest updateRequest) {
        try {
            News news = mapper.updateDtoToModel(updateRequest);
            return mapper.modelToDto(newsRepository.update(news));
        } catch (NotFoundException | ConnectionException e) {
            throw new ServiceException(e.getMessage(), NEWS_ID_DOES_NOT_EXIST.getErrorCode());
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
        if (newsRepository.isExistedById(id)) {
            newsRepository.deleteById(id);
            return true;
        } else {
            throw new ServiceException(NEWS_ID_DOES_NOT_EXIST.getErrorMessage(), NEWS_ID_DOES_NOT_EXIST.getErrorCode());
        }
    }
}

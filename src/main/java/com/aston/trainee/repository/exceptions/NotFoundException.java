package com.aston.trainee.repository.exceptions;

/**
 * Класс для кастомного исключения, связанного отсутствием элементов в базе данных.
 */
public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){
        super(message);
    }
}

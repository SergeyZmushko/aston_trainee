package com.aston.trainee.repository.exceptions;

/**
 * Класс для кастомного исключения, связанного ошибкой при попытке сохранить объект в базу данных.
 */
public class SaveObjectException extends RuntimeException{

    public SaveObjectException(String message){
        super(message);
    }
}

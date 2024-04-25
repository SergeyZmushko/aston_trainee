package com.aston.trainee.repository.exceptions;

/**
 * Класс для кастомного исключения, связанного с подключением к базе данных и с выполнением SQL запросов.
 */
public class ConnectionException extends RuntimeException{

    public ConnectionException(String message){
        super(message);
    }
}

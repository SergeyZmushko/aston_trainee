package com.aston.trainee.repository.exceptions;

/**
 * Класс для кастомного исключения, чтением и получением конфигурационных данных и properties файла для подключения к
 * базе данных.
 */
public class ParseException extends RuntimeException{

    public ParseException(String message){
        super(message);
    }
}

package com.aston.trainee.service.exceptions;

/**
 * Класс для репрезентации ошибки и передачи ее дальше в слой Controller. Класс наследуется от класса RuntimeException,
 * при это есть возможность передать сообщение ошибки в родительский конструктор.
 */
public class ServiceException extends RuntimeException {
    /**
     * Строка для отобращения кода ошибки
     */
    private final String code;

    /**
     * Конструктор - создание объекта класса ServiceException и инициализиция его полей.
     * @param message - сообщение с информацией об ошибке;
     * @param code - код ошибки.
     */
    public ServiceException(String message, String code) {
        super(message);
        this.code = code;
    }

    /**
     * Метод возвращает код ошибки.
     * @return код ошибки.
     */
    public String getCode() {
        return code;
    }
}

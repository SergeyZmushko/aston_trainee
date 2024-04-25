package com.aston.trainee.controller.exception_handling;

/**
 * Класс для представления выбрасываемых исклюений в формате, удобном для чтения клиенту
 */
public class ErrorResponse {
    /**
     * Строка с кодом ошибки.
     */
    private final String code;
    /**
     * Строка с сообщением об ошибке.
     */
    private final String message;

    /**
     * Конструктор для создания экземпляра класса и инициализации полей.
     * @param code - код ошибки.
     * @param message - сообщение об ошибке.
     */
    public ErrorResponse(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}

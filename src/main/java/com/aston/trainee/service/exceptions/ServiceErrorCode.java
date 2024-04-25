package com.aston.trainee.service.exceptions;

/**
 * Класс предоставляет необходимые сообщения, выбрасываемые при спключениях в слое сервиса и сопоставление
 * их с кодами ошибок.
 */
public enum ServiceErrorCode {

    AUTHOR_ID_DOES_NOT_EXIST(Constants.ERROR_000002, "Author Id does not exist. Author Id is: %s"),
    NEWS_ID_DOES_NOT_EXIST(Constants.ERROR_000004, "News Id does not exist. News Id is: %s"),
    TAG_ID_DOES_NOT_EXIST(Constants.ERROR_000005, "Tag Id does not exist. Tag Id is: %s"),
    ERROR_DATA(Constants.ERROR_000001, "Error getting data from database."),
    SAVE_DATA_EXCEPTION(Constants.ERROR_000003, "Object does not saved"),
    PARSE_ID_EXCEPTION(Constants.ERROR_0000010, "Id must be a number and more than 0"),
    JSON_SYNTAX_EXC(Constants.ERROR_0000011, "Problem with parsing JSON");

    /**
     * Строка для указания сообщения при выбросе исключения.
     */
    private final String errorMessage;
    /**
     * Строка для указания кода ошибки при выбросе исключения.
     */
    private final String errorCode;

    /**
     * Конструктор - создание обеъкта
     * @param errorCode
     * @param errorMessage
     */
    ServiceErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    private static class Constants {
        private static final String ERROR_000001 = "000001";
        private static final String ERROR_000002 = "000002";
        private static final String ERROR_000003 = "000003";
        private static final String ERROR_000004 = "000004";
        private static final String ERROR_000005 = "000005";
        private static final String ERROR_0000010 = "0000010";
        private static final String ERROR_0000011 = "0000011";
    }
}

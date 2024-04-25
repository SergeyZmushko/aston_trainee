package com.aston.trainee.repository.util;

/**
 * Утилитный класс, предоставляющий константы для использования в слое репозитория в приложении.
 */
public class Constants {

    /**
     * Приватный конструктор для предотвращения возможности создания экземпляра этого класса.
     */
    private Constants(){}
    public static final String DB_URL_PROP = "db.url";
    public static final String DB_USER_PROP = "db.user";
    public static final String DB_PASSWORD_PROP = "db.password";
    public static final String DB_PSQL_DRIVER = "org.postgresql.Driver";
    public static final String SOURCE_ERROR = "Open source error";
    public static final String CONNECTION_ERROR = "Connection initialize error";
    public static final String ERROR_DATA_ERROR = "Error load data";
    public static final String AUTHOR_ID_DOES_NOT_EXIST = "Author Id does not exist";
    public static final String CREATE_AUTHOR_NO_ROWS_AFFECTED = "Creating author failed, no rows affected.";
    public static final String CREATE_AUTHOR_NO_ID_OBTAINED = "Creating author failed, no Id obtained.";
    public static final String TAG_ID_DOES_NOT_EXIST = "Tag Id does not exist";
    public static final String CREATE_TAG_NO_ROWS_AFFECTED = "Creating tag failed, no rows affected.";
    public static final String CREATE_TAG_NO_ID_OBTAINED = "Creating tag failed, no Id obtained.";
    public static final String NEWS_ID_DOES_NOT_EXIST = "News Id does not exist";
    public static final String CREATE_NEWS_NO_ROWS_AFFECTED = "Creating news failed, no rows affected.";
    public static final String CREATE_NEWS_NO_ID_OBTAINED = "Creating news failed, no Id obtained.";
    public static final String CREATE_TAG_TABLE_PROBLEM = "Problem with creating Tag table";
    public static final String CREATE_AUTHOR_TABLE_PROBLEM = "Problem with creating Author table";
    public static final String CREATE_NEWS_TABLE_PROBLEM = "Problem with creating News table";
    public static final String CREATE_AUTHOR_PROBLEM = "Problem with creating author";
    public static final String CREATE_TAG_LIST_PROBLEM = "Problem with creating tags list";
    public static final String CREATE_TAG_PROBLEM = "Problem with creating tag";
}

package by.Danik.lab.exceptions;

/**
 * Кастомные классы исключений необходимо унаследовать от этого абстрактного класса
 */
public abstract class AbstractExceptionHandler extends RuntimeException {
    String message;         // сообщение об ошибке
    int status;             // статус ответа (400 - по запросу ничего не найдено, 500 - ошибка сервера)

    protected AbstractExceptionHandler(String message, int status) {
        this.message = message;
        this.status = status;
    }
}

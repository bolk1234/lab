package by.Danik.lab.exceptions;

import lombok.Getter;

/**
 * Исключения выбрасываемые мной
 * паттерн строитель
 * удобно если появятся новые поля или если не хочется использовать все поля
 */
@Getter
public class CustomException extends RuntimeException {
    private final String message; // сообщение об ошибке
    private final int status;     // статус ответа

    // Закрытый конструктор, доступный только через Builder
    private CustomException(Builder builder) {
        super(builder.message);
        this.message = builder.message;
        this.status = builder.status;
    }

    // Статический внутренний класс Builder
    public static class Builder {
        private String message;
        private int status;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public CustomException build() {
            return new CustomException(this);
        }
    }
}

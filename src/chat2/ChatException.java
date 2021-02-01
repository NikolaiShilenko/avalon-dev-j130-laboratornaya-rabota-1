/*
 * Курс DEV-J130. Задание №4. Основы многопоточного программирования.
 */
package chat2;

/**
 *
 * @author Nikolai Shilenko
 */

/**
 * Класс, представляющий основное исключение для простого чата.
 */
class ChatException extends Exception {

    public ChatException() {
    }

    public ChatException(String string) {
        super(string);
    }

    public ChatException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatException(Throwable cause) {
        super(cause);
    }

    public ChatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

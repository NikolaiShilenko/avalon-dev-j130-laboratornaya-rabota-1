/*
 * DEV-J130. Задача №2.
 */
package jdbc;

/**
 * Класс представляет общее исключение, возникающее при работе с документами.
 *
 * @author Nikolai Shilenko
 */
public class DocumentException extends Exception {

    public DocumentException() {
    }

    public DocumentException(String string) {
        super(string);
    }

    public DocumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentException(Throwable cause) {
        super(cause);
    }

}

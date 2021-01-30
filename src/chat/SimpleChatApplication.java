/*
 * Курс DEV-J130. Задание №4. Основы многопоточного программирования.
 */
package chat;

/**
 * @author Nikolai Shilenko
 */

public class SimpleChatApplication {

    public static void main(String[] args) throws Exception {
        server(34567);
    }

    private static void client(String host, int port) {
        SimpleChatClient socket = new SimpleChatClient(host, port);
        Thread t = new Thread(socket);
        t.start();
    }

    private static void server(int port) {
        SimpleChatServer socket = new SimpleChatServer(port);
        Thread t = new Thread(socket);
        t.start();
    }

}

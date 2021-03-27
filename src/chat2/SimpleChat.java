/*
 * Курс DEV-J130. Задание №4. Основы многопоточного программирования.
 */
package chat2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Nikolai Shilenko
 */

/**
 ** Простой чат для двух собеседников. Реализация этого интерфейса должна
 * отличаться следующими особенностями:
 * <ol>
 * <li>класс реализации может работать в режиме сервера или клиента (режим
 * работы задаётся пользователем при старте приложения);</li>
 * <li>входящие сообщения, исходящие сообщения работают независимо друг от друга
 * в разных потоках;</li>
 * <li>при выходе из сеанса одной из сторон, второй стороне обязательно
 * передаётся соответствующее сообщение.</li>
 * </ol>
 *
 * Приложение может быть реализовано на основе использования стеков протоколов
 * UDP/IP или TCP/IP (по выбору программиста).
 */
public class SimpleChat implements ISimpleChat {

    /**
     * Стандартный хост, на который отправляется файл.
     */
    public static final String STANDARD_HOST = "localhost";

    public static final int STANDARD_SERVER_PORT = 45678;

    private final String host;

    private final int port;

    private Socket clientSocket;

    private ServerSocket serverSocket;

    public SimpleChat() {
        this.host = STANDARD_HOST;
        this.port = STANDARD_SERVER_PORT;
    }

    public SimpleChat(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        try (SimpleChat sch = new SimpleChat()) {
            sch.server();
            try {
                Thread.currentThread().join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } catch (ChatException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Запуск приложения в режиме клиента.
     *
     * @throws ChatException выбрасывается в случае общей ошибки в работе
     * приложения, например, в случае невозможности открыть соединение с
     * сервером.
     */
    @Override
    public void client() throws ChatException {
        try {
            clientSocket = new Socket(host, port);
            System.out.println("Chat client started...");
            sendMessage();
            printMessage();
        } catch (UnknownHostException e) {
            throw new ChatException("Couldn't get I/O for the connection to " + host, e);
        } catch (IOException e) {
            throw new ChatException("Couldn't get I/O for the connection to " + host, e);
        }
    }

    /**
     * Запуск приложения в режиме сервера.
     *
     * @throws ChatException выбрасывается в случае общей ошибки в работе
     * приложения, например, в случае невозможности занять стандартный порт
     * сервера.
     */
    @Override
    public void server() throws ChatException {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Chat server started...");
            clientSocket = serverSocket.accept();
            sendMessage();
            printMessage();
        } catch (UnknownHostException e) {
            throw new ChatException("Don't know about host " + host, e);
        } catch (IOException e) {
            throw new ChatException("Couldn't get I/O for the connection to " + host, e);
        }
    }

    /**
     * Метод печатает принятое сообщение.
     *
     * @return @throws ChatException выбрасывается в случае общей ошибки в
     * работе приложения.
     */
    @Override
    public void printMessage() throws ChatException {
        Thread thread = new Thread() {
            public void run() {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    String inputStr;
                    while ((inputStr = in.readLine()) != null) {
                        System.out.println(inputStr);
                    }
                } catch (IOException ex) {
                    try {
                        throw new ChatException(ex.getMessage(), ex);
                    } catch (ChatException ex1) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    /**
     * Метод отправляет сообщение.
     *
     * @throws ChatException выбрасывается в случае общей ошибки в работе
     * приложения.
     */
    @Override
    public void sendMessage() throws ChatException {
        Thread thread = new Thread() {
            public void run() {
                try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
                    String stdInputStr;
                    while ((stdInputStr = stdIn.readLine()) != null) {
                        out.println(stdInputStr);
                    }
                } catch (IOException ex) {
                    try {
                        throw new ChatException(ex.getMessage(), ex);
                    } catch (ChatException ex1) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    /**
     * Метод закрывает открытые сокеты.
     *
     * @throws ChatException выбрасывается в случае общей ошибки в работе
     * приложения.
     */
    @Override
    public void close() throws ChatException {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Server socket is closed...");
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
                System.out.println("Socket is closed...");
            }
        } catch (IOException ex) {
            try {
                throw new ChatException(ex.getMessage(), ex);
            } catch (ChatException ex1) {
                ex.printStackTrace();
            }
        }
    }

}

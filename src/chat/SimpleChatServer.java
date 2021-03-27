/*
 * Курс DEV-J130. Задание №4. Основы многопоточного программирования.
 */
package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Nikolai Shilenko
 */
public class SimpleChatServer implements Runnable {

    private final int port;

    public SimpleChatServer() {
        this.port = 34567;
    }

    public SimpleChatServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port);
                Socket soc = serverSocket.accept();
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter socketOutput = new PrintWriter(soc.getOutputStream(), true)) {
            String stdIn;
            while (true) {
                if (socketInput.ready()) {
                    System.out.println(socketInput.readLine());
                }
                if (userInput.ready()) {
                    if (!(stdIn = userInput.readLine()).equals("q")) {
                        socketOutput.println(stdIn);
                    } else {
                        socketOutput.println("user is offline");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

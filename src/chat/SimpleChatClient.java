/*
 * Курс DEV-J130. Задание №4. Основы многопоточного программирования.
 */
package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Nikolai Shilenko
 */
public class SimpleChatClient implements Runnable {

    private final String host;
    private final int port;

    public SimpleChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try (
                Socket soc = new Socket(host, port);
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
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}

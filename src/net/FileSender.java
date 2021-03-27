/*
 * Курс DEV-J130. Задание №3. Передача данных по сети с использованием стека
 * протоколов TCP/IP.
 */
package net;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Nikolai Shilenko
 */
/**
 * Класс предназначен для отправки произвольного файла по сети с использованием
 * стека протоколов TCP/IP.
 */
public class FileSender {

    /**
     * Стандартный порт, на который отправляется файл.
     */
    public static final int PORT = 34567;
    /**
     * Стандартный хост, на который отправляется файл.
     */
    public static final String HOST = "localhost";
    /**
     * Стандартный размер буфера.
     */
    public static final int BUFFER_SIZE = 4096;
    /**
     * Имя файла.
     */
    public static final String FILE_NAME = "C:\\Users\\Administrator\\Desktop\\test.txt";

    public static void main(String[] args) {
        System.out.println("File sender started...");
        new FileSender().run();
        System.out.println("File sender finished.");
    }

    /**
     * Метод обеспечивает установку соединения с сервером и отправку файла.
     */
    private void run() {
        try (Socket s = new Socket(HOST, PORT);
                InputStream in = s.getInputStream();
                OutputStream out = s.getOutputStream()) {
            byte[] buf = new byte[BUFFER_SIZE];
            int n = 0;
            out.write(FILE_NAME.getBytes());
            out.flush();
            try (FileInputStream fis = new FileInputStream(new File(FILE_NAME))) {
                while (true) {
                    n = fis.read(buf);
                    // В конце файла/потока метод read() возвращает -1 (EOF).
                    if (n < 0) {
                        break;
                    }
                    out.write(buf, 0, n);
                }
            }
            n = in.read(buf);
            System.out.println(new String(buf, 0, n));
        } catch (IOException e) {
            System.err.println("Error #1: " + e.getMessage());
        }
    }
}
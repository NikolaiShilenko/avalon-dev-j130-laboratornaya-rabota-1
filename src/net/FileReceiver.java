/*
 * Курс DEV-J130. Задание №3. Передача данных по сети с использованием стека
 * протоколов TCP/IP.
 */
package net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Nikolai Shilenko
 */
/**
 * Класс предназначен для получения произвольного файла по сети с использованием
 * стека протоколов TCP/IP.
 */
public class FileReceiver {

    /**
     * Стандартный порт, на котором принимается файл.
     */
    public static final int SERVER_PORT = 34567;
    /**
     * Стандартный размер буфера.
     */
    public static final int BUFFER_SIZE = 4096;
    /**
     * Стандартное расширение, которое добавляется к имени принятого файла.
     */
    private static final String SUFFIX = "-copy";

    /**
     * Стартовый метод приложения.
     *
     * @param args массив аргументов командной строки.
     */
    public static void main(String[] args) {
        System.out.println("File receiver started...");
        new FileReceiver().run();
        System.out.println("File receiver finished.");
    }

    /**
     * Метод обеспечивает установку соединения с клиентом и принятие файла.
     */
    private void run() {
        try (ServerSocket ss = new ServerSocket(SERVER_PORT);
                Socket s = ss.accept();
                InputStream in = s.getInputStream();
                OutputStream out = s.getOutputStream()) {
            byte[] buf = new byte[BUFFER_SIZE];
            // Имя файла приходит в виде сериализованной строки
            int n = in.read(buf);
            File file = createFile(new String(buf, 0, n));
            try (FileOutputStream fos = new FileOutputStream(file)) {
                //while (true) {
                while (in.available() != 0) { // изменил этот цикл, так как код в строке 73 не выполнялся
                    n = in.read(buf);
                    // В конце файла/потока метод read() возвращает -1 (EOF).
                    //if (n < 0) {
                    // break;
                    //}
                    fos.write(buf, 0, n);
                }
            }
            out.write("Transfer file finished.".getBytes());
        } catch (IOException e) {
            System.err.println("Error #1: " + e.getMessage());
        }
    }

    /**
     * Метод создаёт файл-копию присланного файла.
     *
     * @param fileName имя файла
     * @return ссылка на созданный файл-копию.
     * @throws IOException выбрасывается в случае общей ошибки ввода/вывода.
     */
    private File createFile(String fileName) throws IOException {
        if (fileName.isEmpty()) {
            return new File("default_name");
        }
        fileName = fileName.trim();
        String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
        //return new File(fileName + SUFFIX);
        return new File(tokens[0] + SUFFIX + "." + tokens[1]);
    }
}

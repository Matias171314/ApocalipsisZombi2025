/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package user.apocalipsiszombi2025.util;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author matin
 */
public class LoggerApocalipsis {
    private static final String FILE_NAME = "apocalipsis_log.txt";
    private static final Object lock = new Object();

    public static void registrar(String mensaje) {
        synchronized (lock) {
            try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME, true))) {
                String fechaHora = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                out.println("[" + fechaHora + "] " + mensaje);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
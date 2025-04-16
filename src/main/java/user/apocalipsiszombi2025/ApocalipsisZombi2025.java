/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package user.apocalipsiszombi2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author matin
 */
public class ApocalipsisZombi2025 {
    private static final int NUM_HUMANOS = 100; // Puedes poner 10_000 si quieres escalar
    private static final int NUM_TUNELES = 4;
    private static final int NUM_ZONAS = 4;
    private static final Random rand = new Random();
    
    public static void main(String[] args) {
        Refugio refugio = new Refugio();
        
        Tunel[] tuneles = new Tunel[NUM_TUNELES];
        for (int i = 0; i < NUM_TUNELES; i++) {
            tuneles[i] = new Tunel(i);
        }
        
        Exterior[] zonas = new Exterior[NUM_ZONAS];
        for (int i = 0; i < NUM_ZONAS; i++) {
            zonas[i] = new Exterior(i);
        }
        
        Zombi pacienteCero = new Zombi("Z0000", zonas);
        pacienteCero.start();
        
        for (int i = 1; i <= NUM_HUMANOS; i++) {
            String id = String.format("H%04d", i);
            Humano humano = new Humano(id, tuneles, refugio, zonas);
            humano.start();
            try {
                Thread.sleep(rand.nextInt(500,2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

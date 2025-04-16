/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package user.apocalipsiszombi2025;

import java.util.concurrent.Semaphore;
import user.apocalipsiszombi2025.util.LoggerApocalipsis;
import java.util.Random;

/**
 *
 * @author matin
 */
public class Refugio {
    private final Semaphore comedor = new Semaphore(5); // 5 plazas en el comedor
    private int comidaDisponible = 50; // cantidad inicial de raciones
    private final Object lockComida = new Object(); // para proteger acceso a la comida
    private Random rand = new Random();
    
    public void zonaComun (String humanoID) throws InterruptedException {
        LoggerApocalipsis.registrar("El humano " + humanoID + " esta en la zona comun.");
        Thread.sleep(rand.nextInt(1000, 2000));
    }
    
    public void comer(String humanoID) throws InterruptedException {
        LoggerApocalipsis.registrar("El humano " + humanoID + " quiere entrar al comedor");
        comedor.acquire();
        LoggerApocalipsis.registrar("El humano " + humanoID + " entra al comedor");
        
        Thread.sleep(1000);
        
        synchronized (lockComida) { 
            if (comidaDisponible > 0) {
                comidaDisponible--;
                LoggerApocalipsis.registrar("El humano " + humanoID + " comio. Comida restante: " + comidaDisponible);
            } else {
                LoggerApocalipsis.registrar("El humano " + humanoID + " no enconro comida");
            }
        }
        
        Thread.sleep(1000);
        
        comedor.release();
        LoggerApocalipsis.registrar("El humano " + humanoID + " sale del comedor");
    }
    
    public void descansar (String humanoID, boolean herido) throws InterruptedException {
        if (herido) {
            LoggerApocalipsis.registrar("El humano " + humanoID + " esta herido y necesita descansar mas");
            Thread.sleep(4000);
        } else {
            LoggerApocalipsis.registrar("El humano " + humanoID + " descansa");
            Thread.sleep(2000);
        }
    }
    
    public void reponerComida (int cantidad) {
        synchronized (lockComida) {
            comidaDisponible += cantidad;
        }
    }
    
    public int getComidaDisponible() {
        synchronized (lockComida) {
            return comidaDisponible;
        }
    }
}

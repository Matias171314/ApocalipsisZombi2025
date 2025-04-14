/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package user.apocalipsiszombi2025;

import java.util.Random;
import user.apocalipsiszombi2025.util.LoggerApocalipsis;

/**
 *
 * @author matin
 */
public class Zombi extends Thread {
    private final String zombiID;
    private final Exterior[] zonasInseguras;
    private int muertes = 0;
    private Random rand = new Random();
    
    public Zombi (String zombiID, Exterior[] zonasInseguras) {
        this.zombiID = zombiID;
        this.zonasInseguras = zonasInseguras;
    }
    
    @Override
    public void run() {
        while(true) {
            try {
                patrullar();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void patrullar () throws InterruptedException {
        int zonaID = rand.nextInt(zonasInseguras.length);
        Exterior zona = zonasInseguras[zonaID];
        
        LoggerApocalipsis.registrar("El zombi " + zombiID + " entra en la zona insegura " + zona.getID());
        
        Humano objetivo = zona.elegirHumano();
        
        if (objetivo != null && objetivo.estaVivo()) {
            LoggerApocalipsis.registrar("El zombi " + zombiID + " ataca al humano " + objetivo.getId());
            
            Thread.sleep(rand.nextInt(500, 1500));
            
            boolean defensaExitosa = rand.nextDouble() < (2.0 / 3);
            
            if (defensaExitosa) {
                objetivo.serHerido();
                LoggerApocalipsis.registrar("El humano " + objetivo.getId() + " se defendió del zombi " + zombiID);
            } else {
                objetivo.morir();
                muertes++;
                LoggerApocalipsis.registrar("El zombi " + zombiID + " mató al humano " + objetivo.getId() +
                        " (muertes: " + muertes + ")");
                zona.notificarMuerte(objetivo);
                // zona.convertirAHumanoEnZombi(objetivo); // método por implementar
            }
            Thread.sleep(rand.nextInt(500, 1000)); // espera corta tras el ataque
        } else {
            LoggerApocalipsis.registrar("El zombi " + zombiID + " no encontró humanos. Espera...");
            Thread.sleep(rand.nextInt(2000, 3000)); // espera al no encontrar nadie
        }
    }
    
    public String getID() {
        return zombiID;
    }

    public int getMuertes() {
        return muertes;
    } 
}
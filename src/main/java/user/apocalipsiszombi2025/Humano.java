/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package user.apocalipsiszombi2025;

import user.apocalipsiszombi2025.util.LoggerApocalipsis;
import java.util.Random;

/**
 *
 * @author matin
 */
public class Humano extends Thread {
    private final String humanoID;
    private final Tunel[] tuneles;
    private final Refugio refugio;
    private final Exterior[] zonasInseguras;
    private boolean herido = false;
    private boolean vivo = true;
    private Random rand = new Random();
    
    public Humano(String humanoID, Tunel[] tuneles, Refugio refugio, Exterior[] zonasInseguras) {
        this.humanoID = humanoID;
        this.tuneles = tuneles;
        this.refugio = refugio;
        this.zonasInseguras = zonasInseguras;
    }
    
    private void zonaComun() throws InterruptedException {
        LoggerApocalipsis.registrar("El humano " + humanoID + " entra a la ZONA COMUN.");
        Thread.sleep(rand.nextInt(1000, 2000)); // 1 a 2 seg
    }
    
    private void explorarMundo() throws InterruptedException {
        int camino = rand.nextInt(tuneles.length);
        
        for (int i=0; i < tuneles.length; i++) {
            int index = (camino + i) % tuneles.length;
            Tunel tunelElegido = tuneles[index];
            
            LoggerApocalipsis.registrar("El humano " + humanoID + " quiere salir por el túnel " + camino);
            
            tunelElegido.esperarGrupoParaSalir(humanoID);

            // Sale al exterior si forma grupo
            tunelElegido.salirRefugio(humanoID);
            LoggerApocalipsis.registrar("El humano " + humanoID + " ha llegado al exterior.");

            // Entra en la zona correspondiente al túnel usado
            Exterior zona = zonasInseguras[index];
            zona.entrarHumano(this);
            
            Thread.sleep(rand.nextInt(3000, 5000)); // explora
            zona.salirHumano(this);

            if (!vivo) {
                LoggerApocalipsis.registrar("El humano " + humanoID + " ha muerto y no regresa al refugio.");
                return;
            }
            
            LoggerApocalipsis.registrar("El humano " + humanoID + " regresa al refugio por el túnel " + camino);
            tunelElegido.entrarRefugio(humanoID);
            return; // terminó su viaje con éxito
        }
        
        // Si no pudo usar ningún túnel
        LoggerApocalipsis.registrar("El humano " + humanoID + " no pudo salir por ningún túnel esta vez.");
    }
    
    private void descansarYComer() throws InterruptedException {
        LoggerApocalipsis.registrar("El humano " + humanoID + " entra a la ZONA DE DESCANSO.");
        refugio.descansar(humanoID, herido);
        refugio.comer(humanoID);

        if (herido) {
            LoggerApocalipsis.registrar("El humano " + humanoID + " está herido y vuelve a descansar.");
            herido = false;
        }
    }
    
    public boolean estaVivo() {
        return vivo;
    }
    
    public String getID() {
        return humanoID;
    }
    
    public void serHerido() {
        herido = true;
    }
    
    public void morir() {
        vivo = false;
        LoggerApocalipsis.registrar("El humano " + humanoID + " ha muerto.");

        // Crear zombi con mismo número
        String idZombi = humanoID.replace('H', 'Z');
        Zombi nuevoZombi = new Zombi(idZombi, zonasInseguras);
        LoggerApocalipsis.registrar("El humano " + humanoID + " ha renacido como zombi " + idZombi);
        nuevoZombi.start();
    }
    
    @Override
    public void run() {
        try {
            while (vivo) {
                zonaComun();
                explorarMundo();
                if (!vivo) {
                    break;
                }
                descansarYComer();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
 
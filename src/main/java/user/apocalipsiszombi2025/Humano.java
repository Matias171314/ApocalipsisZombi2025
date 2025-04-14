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
        int tunelId = rand.nextInt(tuneles.length);
        Tunel tunelElegido = tuneles[tunelId];

        LoggerApocalipsis.registrar("El humano " + humanoID + " quiere salir por el túnel " + tunelId);
        tunelElegido.esperarGrupoParaSalir(humanoID);
        tunelElegido.salirRefugio(humanoID);

        LoggerApocalipsis.registrar("El humano " + humanoID + " ha llegado al exterior.");

        // Simular exploración en zona insegura (3 a 5 segundos)
        int zonaID = rand.nextInt(zonasInseguras.length);
        Exterior zona = zonasInseguras[(zonaID)];
        zona.entrarHumano(this);
        Thread.sleep(rand.nextInt(3000, 5000));

        // Simular ataque (esto luego será mejorado con interacción real con zombis)
        boolean fueAtacado = Math.random() < 0.3; // 30% de posibilidad de ser atacado

        if (fueAtacado) {
            LoggerApocalipsis.registrar("El humano " + humanoID + " fue atacado por un zombi.");

            boolean defensaExitosa = Math.random() < (2.0 / 3); // 2 de cada 3 sobrevive
            if (defensaExitosa) {
                serHerido();
                LoggerApocalipsis.registrar("El humano " + humanoID + " se defendió con éxito y quedó herido.");
            } else {
                morir();
                LoggerApocalipsis.registrar("El humano " + humanoID + " ha muerto en la zona exterior.");
                // Aquí se podría notificar al sistema para convertirlo en zombi
                return;
            }
        } else {
            LoggerApocalipsis.registrar("El humano " + humanoID + " ha recolectado comida y no fue atacado.");
        }

        zona.salirHumano(this);
        // Regresa al refugio por el mismo túnel (tiene prioridad)
        LoggerApocalipsis.registrar("El humano " + humanoID + " regresa al refugio por el túnel " + tunelId);
        tunelElegido.entrarRefugio(humanoID);
    }
    
    private void descansarYComer() throws InterruptedException {
        LoggerApocalipsis.registrar("El humano " + humanoID + " entra a la ZONA DE DESCANSO.");
        Thread.sleep(rand.nextInt(2000, 4000)); // 2 a 4 seg

        LoggerApocalipsis.registrar("El humano " + humanoID + " entra al COMEDOR.");
        //refugio.comedorEntrar(id); // pseudocódigo, lo implementamos luego
        Thread.sleep(rand.nextInt(3000, 5000)); // 3 a 5 seg
        //refugio.comedorSalir(id);

        if (herido) {
            LoggerApocalipsis.registrar("El humano " + humanoID + " está herido y vuelve a descansar.");
            Thread.sleep(rand.nextInt(3000, 5000)); // 3 a 5 seg
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
    }
    
    @Override
    public void run() {
        try {
            while (vivo) {
                zonaComun();
                explorarMundo();
                //regresarAlRefugio(); 
                descansarYComer();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
 
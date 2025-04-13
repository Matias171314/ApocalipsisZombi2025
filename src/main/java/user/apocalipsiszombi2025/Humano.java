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
    private final String id;
    private final Tunel[] tuneles;
    private final Refugio refugio;
    private final Exterior exterior;
    private boolean herido = false;
    private boolean vivo = true;
    private Random rand = new Random();
    
    public Humano(String id, Tunel[] tuneles, Refugio refugio, Exterior exterior) {
        this.id = id;
        this.tuneles = tuneles;
        this.refugio = refugio;
        this.exterior = exterior;
    }
    
    private void zonaComun() throws InterruptedException {
        LoggerApocalipsis.registrar("El humano " + id + " entra a la ZONA COMUN.");
        Thread.sleep(rand.nextInt(1000, 2000)); // 1 a 2 seg
    }
    
    private void explorarMundo() throws InterruptedException {
        int tunelId = (int) (Math.random() * tuneles.length);
        Tunel tunelElegido = tuneles[tunelId];

        LoggerApocalipsis.registrar("El humano " + id + " quiere salir por el túnel " + tunelId);

        // Esperar a que se forme un grupo de 3 humanos (internamente usa CyclicBarrier)
        tunelElegido.esperarGrupoParaSalir(id);

        // Cruza el túnel (sólo uno a la vez, sincronizado)
        tunelElegido.salirRefugio(id);

        LoggerApocalipsis.registrar("El humano " + id + " ha llegado al exterior.");

        // Simular exploración en zona insegura (3 a 5 segundos)
        Thread.sleep(rand.nextInt(3000, 5000));

        // Simular ataque (esto luego será mejorado con interacción real con zombis)
        boolean fueAtacado = Math.random() < 0.3; // 30% de posibilidad de ser atacado

        if (fueAtacado) {
            LoggerApocalipsis.registrar("El humano " + id + " fue atacado por un zombi.");

            boolean defensaExitosa = Math.random() < (2.0 / 3); // 2 de cada 3 sobrevive
            if (defensaExitosa) {
                herido = true;
                LoggerApocalipsis.registrar("El humano " + id + " se defendió con éxito y quedó herido.");
            } else {
                vivo = false;
                LoggerApocalipsis.registrar("El humano " + id + " ha muerto en la zona exterior.");
                // Aquí se podría notificar al sistema para convertirlo en zombi
                return;
            }
        } else {
            LoggerApocalipsis.registrar("El humano " + id + " ha recolectado comida y no fue atacado.");
        }

        // Regresa al refugio por el mismo túnel (tiene prioridad)
        LoggerApocalipsis.registrar("El humano " + id + " regresa al refugio por el túnel " + tunelId);
        tunelElegido.entrarRefugio(id);
    }
    
    private void descansarYComer() throws InterruptedException {
        LoggerApocalipsis.registrar("El humano " + id + " entra a la ZONA DE DESCANSO.");
        Thread.sleep(rand.nextInt(2000, 4000)); // 2 a 4 seg

        LoggerApocalipsis.registrar("El humano " + id + " entra al COMEDOR.");
        //refugio.comedorEntrar(id); // pseudocódigo, lo implementamos luego
        Thread.sleep(rand.nextInt(3000, 5000)); // 3 a 5 seg
        //refugio.comedorSalir(id);

        if (herido) {
            LoggerApocalipsis.registrar("El humano " + id + " está herido y vuelve a descansar.");
            Thread.sleep(rand.nextInt(3000, 5000)); // 3 a 5 seg
            herido = false;
        }
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
 
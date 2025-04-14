/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package user.apocalipsiszombi2025;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.Random;
import user.apocalipsiszombi2025.util.LoggerApocalipsis;

/**
 *
 * @author matin
 */
public class Exterior {
    private final int zonaID;
    private final List<Humano> humanosEnZona = new CopyOnWriteArrayList<>();
    private final Random rand = new Random();
    
    public Exterior (int zonaID) {
        this.zonaID = zonaID;
    }
    
    public int getID(){
        return zonaID;
    }
    
    public void entrarHumano(Humano humano) {
        humanosEnZona.add(humano);
        LoggerApocalipsis.registrar("El humano " + humano.getID() + " entra en la Zona " + zonaID);
    } 
    
    public void salirHumano(Humano humano) {
        humanosEnZona.remove(humano);
        LoggerApocalipsis.registrar("El humano " + humano.getID() + " sale de la Zona " + zonaID);
    }
    
    public Humano elegirHumano() {
        List<Humano> vivos = humanosEnZona.stream().filter(Humano::estaVivo).toList();
        if (vivos.isEmpty()) {
            return null;
        }
        return vivos.get(rand.nextInt(vivos.size()));
    }
    
    public void notificarMuerte(Humano humano) {
        salirHumano(humano);
        LoggerApocalipsis.registrar("El humano " + humano.getID() + " ha muerto en la zona " + zonaID);
    }
}

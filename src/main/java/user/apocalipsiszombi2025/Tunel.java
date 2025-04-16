/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package user.apocalipsiszombi2025;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import user.apocalipsiszombi2025.util.LoggerApocalipsis;

/**
 *
 * @author matin
 */
public class Tunel {
    private final int tunelID;
    private final Lock lock = new ReentrantLock();
    private final Condition puedeEntrar = lock.newCondition();
    private final Condition puedeSalir = lock.newCondition();
    private final CyclicBarrier barreraGrupo;
    
    private int esperandoEntrar = 0;
    private int esperandoSalir = 0;
    private boolean ocupado = false;

    public Tunel(int tunelID) {
        this.tunelID = tunelID;
        this.barreraGrupo = new CyclicBarrier(3);
    }
    
    public void esperarGrupoParaSalir(String humanoID) throws InterruptedException {
        try {
            System.out.println( "El humano " + humanoID + " espera grupo para Tunel " + tunelID);
            barreraGrupo.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
    
    public void entrarRefugio (String humanoID) throws InterruptedException {
        lock.lock();
        try{
            esperandoEntrar ++;
            while (ocupado) {
                puedeEntrar.await();
            }
            esperandoEntrar --;
            ocupado = true;
            
            LoggerApocalipsis.registrar("El humano " + humanoID + " entra al Tunel " + tunelID + " hacia el refugio.");
            Thread.sleep(1000);
            ocupado = false;
            
            if (esperandoEntrar > 0) {
                puedeEntrar.signal();
            } else {
                puedeSalir.signal();
            }
        } finally {
            lock.unlock();
        }
    }
    
    public void salirRefugio (String humanoId) throws InterruptedException {
        lock.lock();
        try {
            esperandoSalir++;
            while (ocupado || esperandoEntrar > 0) {
                puedeSalir.await();
            }
            esperandoSalir--;
            ocupado = true;

            LoggerApocalipsis.registrar( "El humano " + humanoId + " sale por el Tunel " + tunelID + " hacia el exterior.");
            Thread.sleep(1000);
            ocupado = false;

            if (esperandoEntrar > 0) {
                puedeEntrar.signal();
            } else {
                puedeSalir.signal();
            }
        } finally {
            lock.unlock();
        }
    }
    
    public int getId() {
        return tunelID;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package user.apocalipsiszombi2025;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author matin
 */
public class Tunel {
    private final int id;
    private final Lock lock = new ReentrantLock();
    private final Condition puedeEntrar = lock.newCondition();
    private final Condition puedeSalir = lock.newCondition();
    private final CyclicBarrier barreraGrupo;
    
    private int esperandoEntrar = 0;
    private int esperandoSalir = 0;
    private boolean ocupado = false;

    public Tunel(int id) {
        this.id = id;
        this.barreraGrupo = new CyclicBarrier(3);
    }
    
    public void esperarGrupoParaSalir(String humanoId) throws InterruptedException {
        try {
            System.out.println( humanoId + " espera grupo para Tunel " + id);
            barreraGrupo.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
    
    public void entrarRefugio (String humanoId) throws InterruptedException {
        lock.lock();
        try{
            esperandoEntrar ++;
            while (ocupado) {
                puedeEntrar.await();
            }
            esperandoEntrar --;
            ocupado = true;
            
            System.out.println(humanoId + " entra al Tunel " + id + " hacia el refugio.");
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

            System.out.println(humanoId + " sale por el Tunel " + id + " hacia el exterior.");
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
        return id;
    }
}

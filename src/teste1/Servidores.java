/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Servidores {
    private Servidor[] servidores;
    private ReentrantLock lockServidores;
    private Condition occupied;
    private int counter;
    private int proxPos;
    
    public Servidores(int n){
        this.servidores = new Servidor[n];
        this.counter = 0;
        this.proxPos = n-1;
        this.lockServidores = new ReentrantLock();
        this.occupied = this.lockServidores.newCondition();
    }
    
    // usada para povoar ->unicamente -< não é necessário ser multithread
    public void adicionaServidor(Servidor s){
        this.servidores[s.getId()] = s;
    }
    
    public Servidor alugaServer(){
        this.lockServidores.lock();
        try{
            while(this.counter==this.servidores.length){
                this.occupied.await();
            }
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Servidores.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            searchPos();
            Servidor s = this.servidores[this.proxPos];
            Servidor s1 = s;
            s.lock();
            System.out.println("Thread: " +Thread.currentThread().getName()+" ALUGA server "+s.getId());
            this.counter++;
            System.out.println("Counter "+this.counter);
            this.lockServidores.unlock();
            return s1;
        }
    }
    
    // é so chamado no alugaServer por isso não é necessário ter protecção para threads pois só acede 1 de cada vez
    public  boolean searchPos(){
        
        boolean flag = false;
        if(!(this.servidores[this.proxPos].isLocked())) flag = true;
        int i =0;
        while(i<this.servidores.length && !flag){
            if(!(this.servidores[this.proxPos].isLocked())) flag = true;
            else if (this.proxPos==0) this.proxPos = this.servidores.length-1 ;
            else this.proxPos--;
            i++;
        }
        return flag;
    }
    
    
    public void libertaServidor(int i){
        this.lockServidores.lock();
        try {
            Servidor s = this.servidores[i];
            this.counter--;
            s.unlock();
            System.out.println("Thread: " +Thread.currentThread().getName()+" LIBERTA server "+s.getId());
            System.out.println("Counter "+this.counter);
            this.proxPos = i;
            this.occupied.signalAll();
        } finally {
            this.lockServidores.unlock();
        }
    }
    
    public String toString(){
        StringBuilder s = new StringBuilder("##Servidores###\n");
        
        for(Servidor s1 : this.servidores){
            s.append(s1+"\n");
        }
        return s.toString();
    }
}
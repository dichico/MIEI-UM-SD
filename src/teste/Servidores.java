/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Diogo Nogueira
 */
public class Servidores {
    
    private Servidor[] servidores;
    private int posicaoEscrever;
    
    private ReentrantLock lock;
    private Condition isEmpty;

    public Servidores(int n){
        this.servidores = new Servidor[n];
        this.posicaoEscrever = 0;
        this.lock = new ReentrantLock();
        this.isEmpty = this.lock.newCondition();
    }
    
    // Uma vez que todo o array é povoado por nós logo de início, não existe necessidade de tornar atómico.
    public void adicionaServidor(Servidor s){
        this.servidores[this.posicaoEscrever++] = s; 
    }
    
    // synchronized 
    public Servidor getServidor() throws InterruptedException{
        
        this.lock.lock();
        
        try {
            
            if(this.posicaoEscrever==0) {
                System.out.println("Não existem servidores disponíveis de momento.");
                isEmpty.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
	}
        
        int posicaoLer = --this.posicaoEscrever;
        Servidor servidor = this.servidores[posicaoLer];
        
        this.isEmpty.signalAll();
        this.lock.unlock();
        
        return servidor;
    } 
    
    public synchronized void putServidor(Servidor s){
        this.servidores[posicaoEscrever++] = s;
    }
    
}

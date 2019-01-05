/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Servidores {
    private Servidor[] servidores;
    private ReentrantLock lockServidores;   // Lock para o servidores
    private Condition occupied;             // Condição para quando todos os servidores estiverem alugados
    private int counterA;                    // Nº de servidores alugados
    private int counterL;
    private int proxPos;                    // Próximo servidor a ser alugado
    
    public Servidores(int n){
        this.servidores = new Servidor[n];
        this.counterA = 0;
        this.counterL  = 0;
        this.proxPos = n-1;
        this.lockServidores = new ReentrantLock();
        this.occupied = this.lockServidores.newCondition();
    }
    
    
    // usada para povoar unicamente para povoar dai não ser um método protegido para concorrencia
    public void adicionaServidor(Servidor s){
        this.servidores[s.getId()] = s;
    }

    
    public int alugaServer(String tipoAluguer){
        this.lockServidores.lock();
        int id=-1;
        try{
            while(this.counterA==this.servidores.length){
                this.occupied.await();
            }
            searchPos();
            Servidor s = this.servidores[this.proxPos];
            id = s.getId();
            s.lock(tipoAluguer);
            System.out.println("Thread: " +Thread.currentThread().getName()+" ALUGA server "+s.getId());
            if (tipoAluguer.equals("aluguer")){
                System.out.println("Nº de Alugados "+this.counterA);
                this.counterA++;
            }
            else {
                System.out.println("Nº de Leiloados "+this.counterL);
                this.counterL++;
            }
            
            return id;
        } catch (InterruptedException ex) {
            System.err.println("Erro método alugaServer, classe Servidores" + ex.getMessage());
        } finally{
            this.lockServidores.unlock();
        }
        return id;
    }
    
    // é so chamado no alugaServer por isso não é necessário ter protecção para threads pois só acede 1 de cada vez
    public synchronized boolean searchPos(){    
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
    
    public synchronized void searchLeiloados(){
        int i;
        boolean flag = true;
        for(i=0;i<this.servidores.length && flag ;i++){
            if(this.servidores[this.proxPos].getTipoAluguer().equals("leilao")) flag = false;
            else if(this.proxPos==0) this.proxPos = this.servidores.length-1 ;
            else this.proxPos--;
        }
    }
    
    
    public void libertaServidor(int i,String tipoAluguer){
        this.lockServidores.lock();
        try {
            Servidor s = this.servidores[i];
            if(tipoAluguer.equals("aluguer")){
                this.counterA--;
                System.out.println("Counter Alugados "+this.counterA);
            }
            else {
                this.counterL--;
                System.out.println("Counter Leiloados "+this.counterL);
            }
            s.unlock();
            System.out.println("Thread: " +Thread.currentThread().getName()+" LIBERTA server "+s.getId());
            
            this.proxPos = i;
            this.occupied.signalAll();
        } finally {
            this.lockServidores.unlock();
        }
    }
    
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder("##Servidores###\n");
        
        for(Servidor s1 : this.servidores){
            s.append(s1+"\n");
        }
        return s.toString();
    }
}

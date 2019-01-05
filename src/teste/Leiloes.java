/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Leiloes {
    private HashMap<String,Leilao> leiloes;
    private ReentrantLock lockLeiloes;
    
    public Leiloes(){
        this.leiloes = new HashMap<>();
        this.lockLeiloes = new ReentrantLock();
    }
    
    public void entraLeilao(String tipoServer,String mail,ObjectOutputStream out){
        this.lockLeiloes.lock();
        try{
            if(this.leiloes.isEmpty() || !(this.leiloes.containsKey(tipoServer))){
                Leilao l = new Leilao();
                l.addUser(mail, out);
                l.startTimer();
                this.leiloes.put(tipoServer,l);
            } 
            else if(this.leiloes.containsKey(tipoServer)){
                Leilao l = this.leiloes.get(tipoServer);
                l.addUser(mail, out);
            }
        } finally{
            this.lockLeiloes.unlock();
        }
    }
    
    public synchronized void enviaMensagem(String value,String mail,String tipoServer){
        Leilao l = this.leiloes.get(tipoServer);
        l.multicast(value, mail);
    }
    
    public void removeUser(String mail, String tipoServer){
        Leilao l = this.leiloes.get(mail);
        l.removeUser(mail);
    }
    
    public synchronized boolean isTerminado(String tipoServer){
        Leilao l = this.leiloes.get(tipoServer);
        return l.isTerminado();
    }
    
    public boolean isWinner(String mail, String tipoServer){
        Leilao l = this.leiloes.get(tipoServer);
        return l.isWinner(mail);
    }
    
    public double getCustoMaior(String tipoServer){
        Leilao l = this.leiloes.get(tipoServer);
        return l.getCustoMaior();
    }
}

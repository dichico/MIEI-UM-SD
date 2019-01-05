/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reserva;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Leiloes implements Serializable{
    private HashMap<String,Leilao> leiloes;
    private ReentrantLock lockLeiloes;
    
    public Leiloes(){
        this.leiloes = new HashMap<>();
        this.lockLeiloes = new ReentrantLock();
    }

    public void entraLeilao(Leilao l,String tipoServer){
        this.lockLeiloes.lock();
        try{
            if(!(this.leiloes.containsKey(tipoServer))){
                this.leiloes.put(tipoServer,l);
            } 
            else {
                this.leiloes.put(tipoServer,l);
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
    
    public boolean isInicialized(String tipoServer){
        return ((this.leiloes.containsKey(tipoServer)));
    }
    
    public Leilao getLeilao(String tipoServer){
        if(this.leiloes.containsKey(tipoServer)){
            return this.leiloes.get(tipoServer);
        }
        return null;
    }

    public String toString(){
        StringBuilder s = new StringBuilder("Leiloes\n");
        for(Leilao l : this.leiloes.values()){
            s.append(l+"\n");
        }
        return s.toString();
    }
}

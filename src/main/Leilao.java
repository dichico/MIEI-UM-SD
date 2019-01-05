/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Leilao implements Serializable {
    private HashMap<String,ObjectOutputStream> currentUsers;
    private int idServer;
    private String mailUser;
    private double custoMaior;
    private boolean terminado;              //Se for true posso comecar um novo leilao! Inicio timer

    
    public Leilao(){
        this.custoMaior = 0;
        this.mailUser ="";
        this.currentUsers = new HashMap<>();
        this.terminado = true;                 
    }
    
    public void setTerminado(boolean flag){
        this.terminado=flag;
    }
    
    
    public synchronized void addUser(String mail, ObjectOutputStream out ){
        this.currentUsers.put(mail,out);
    }
    
    public synchronized boolean getTerminado(){
        return this.terminado;
    }
    
    public synchronized void removeUser(String mail){
        this.currentUsers.remove(mail);
    }
   
    /**
     * Eniva mensagens de texto para todos os users da rede
     * @param value 
     */
    public synchronized void multicastWinner(){
        String value = "Parabéns! Conseguiu alugar o servidor por "+this.custoMaior+"€/hora";
	try {
            ObjectOutputStream bw = this.currentUsers.get(mailUser);
            bw.writeObject(value);
            bw.flush();
        } catch (IOException e) {
            System.err.println("Erro método multicastMsg, classe Leilao " + e.getMessage());
        }
    }   
    
    public synchronized void multicast(String value,String mail,boolean flag){
        ObjectOutputStream bw;
        if (flag){
            double custoU = Double.parseDouble(value);
            if(custoU>this.custoMaior){
                this.custoMaior = custoU;
                this.mailUser = mail;
            }
        }
        try {
            
            for(String user : this.currentUsers.keySet()) {
                if(!user.equals(mail)){
                    bw = this.currentUsers.get(user);
                    bw.writeObject(value);
                    bw.flush();
                }
            }
        } catch (IOException ex) {
            System.err.println("Erro método multicast, classe Leilao " + ex.getMessage());
            Logger.getLogger(Leilao.class.getName()).log(Level.SEVERE, null, ex);   
        }
    }
    
    public void terminaLeilao(){
        if(!(this.currentUsers.isEmpty())){
            multicastWinner();
            this.multicast("Leilão terminado! Não consegui alugar o servidor", mailUser, false);
            this.multicast(null, "", false);
            this.terminado = true;
        }
    }
    
    public String toString(){
        StringBuilder s = new StringBuilder("### LEILAO ###\n");
        s.append("Custo maior "+this.custoMaior+"\n");
        s.append("Id do server "+this.idServer+"\n");
        s.append("Mail do user "+this.mailUser+"\n");
        s.append("Terminado ?"+this.terminado+"\n");
        return s.toString();
    }
}

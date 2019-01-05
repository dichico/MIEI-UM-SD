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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Leilao implements Serializable {
    private HashMap<String,ObjectOutputStream> currentUsers;            //String mail dos users e canal de escrita do ServerWorker
    private ReentrantLock lock;                                         // será necessário??
    private double custoMaior;                                          //valor da maior licitação
    private String mail;                                                //mail correspondente à maior licitação
    private boolean terminado;                                          //indica se o leilao terminou
    private Timer timer;                                                
    private TimerTask timerTask;
    private int idServer;
    
    
    public Leilao(){
        this.currentUsers = new HashMap<>();
        this.custoMaior = 0;
        this.mail = "";
        this.terminado = false;
    }
    
    public int getSize(){
       return this.currentUsers.size();
    }
    
    public boolean isTerminado(){
        return this.terminado;
    }
    
    public void setIdServer(int id){
        this.idServer = id;
    }
    
    public boolean isWinner(String mail){
        return this.mail.equals(mail);
    }

    public double getCustoMaior() {
        return custoMaior;
    }

    public String getMail() {
        return mail;
    }

    public int getIdServer() {
        return idServer;
    }
    
    public boolean isEmpty(){
        return this.currentUsers.isEmpty();
    }
    
    /**
     * Temporizador para 2 minutos. Ao fim de dois minutos termina o leilão.
     */
    /*public void startTimer(){
        if (!(this.currentUsers.isEmpty())){
            this.timer = new Timer();
            this.timerTask = new TimerTask() {

                @Override
                public void run() {
                   terminaLeilao();
                }
            };
            this.timer.schedule(timerTask, 1200);
        }
    }*/

    public synchronized void addUser(String mail, ObjectOutputStream out ){
        this.currentUsers.put(mail,out);      
    }
    
    public synchronized void removeUser(String mail){
        this.currentUsers.remove(mail);
    }
       
    
    public synchronized void multicastMsgUnica(String value,String mail){
        try {
            ObjectOutputStream bw = this.currentUsers.get(mail);
            bw.writeObject(value);
            bw.writeObject(false);
            bw.writeObject(this.idServer);
        } catch (IOException e) {
            System.err.println("Erro método multicastMsg, classe Leilao " + e.getMessage());
        }
    }
    
    public synchronized void multicast(String value,String mail){

	for(String user : this.currentUsers.keySet()) {
            if(!user.equals(mail)){
                try {
                    ObjectOutputStream bw = this.currentUsers.get(user);
                    bw.writeObject(value);
		} catch (IOException e) {
                    System.err.println("Erro método multicast, classe Leilao " + e.getMessage());
		}
            }
	}
    }
    public void removeUser(){
        this.currentUsers.clear();
    }
    
    public void terminaLeilao(){
        if(!(this.currentUsers.isEmpty())){
            String msgL = "Leilão terminado. Lance vencedor corresponde ao valor "+this.custoMaior+"€/hora pertencente ao mail "+this.mail+"";
            this.multicast(msgL,this.mail);
            String msgW = "Parabéns! Consegui o aluguer do servidor!";
            this.multicastMsgUnica(msgW, mail);
            this.terminado = true;
            removeUser();
        }
    }
    
    public void cancela(){
        this.timerTask.cancel();
    }
    
    public String toString(){
        StringBuilder s = new StringBuilder("SERVIDOR\n");
        s.append("Custo Maior"+this.custoMaior+"\n");
        s.append("Terminado? "+this.terminado+"\n");
        s.append("Id do Server "+this.idServer+"\n");
        for(String user : this.currentUsers.keySet()){
            s.append("Mail do user "+user+"\n");
        }
        return s.toString();
    }
}

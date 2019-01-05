/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal_MESMO;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Leilao implements Serializable {
    private HashMap<String,ObjectOutputStream> currentUsers;
    private Servidor servidor;
    
    public Leilao(){
        this.currentUsers = new HashMap<>();
    }
    
    public void setServidor(Servidor s){
        this.servidor = s;
    }
    
    public synchronized void addUser(String mail, ObjectOutputStream out ){
        this.currentUsers.put(mail,out);
    }
    
    
    public synchronized void multicastMsg(String value){

	for(String user : this.currentUsers.keySet()) {
            try {
                ObjectOutputStream bw = this.currentUsers.get(user);
                bw.writeObject(value);
            } catch (IOException e) {
                System.err.println("Erro método multicastMsg, classe Leilao " + e.getMessage());
            }
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
}

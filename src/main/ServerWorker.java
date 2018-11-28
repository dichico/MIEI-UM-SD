/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SarahTifanydaSilva
 */
public class ServerWorker implements Runnable {
    
    private Socket socket;
    private Users users;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    public ServerWorker(Socket socket,Users users){
        try {
            this.socket = socket;
            this.users = users;
            this.in = new ObjectInputStream(this.socket.getInputStream());
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    public void registar(){
        try {
            String name = (String) in.readObject();
            boolean flag = this.users.userExists(name);
            
            out.writeObject(flag);

            while(flag){
                name = (String) in.readObject();
                flag = this.users.userExists(name);
                out.writeObject(flag);
            }
            String pass = (String) in.readObject();
            User u = new User(name,pass);
            this.users.addUser(u);
        } catch (IOException ex) {
            Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        try {
  
            String value =(String) in.readObject();
            switch(value){
                case "1":
                    registar();
                    break;
            }
                       
        } catch (IOException ex) {
            Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}

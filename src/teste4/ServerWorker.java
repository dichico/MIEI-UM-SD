package teste4;

import teste2.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ServerWorker implements Runnable,Serializable {
    
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
    
    public void close(){
        try {
            this.socket.shutdownInput();
            this.socket.shutdownOutput();
            this.socket.close();
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
            User u = new User(name,pass,0);
            this.users.addUser(u);
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
  
    
    public void login(){
        try {
            String name = (String) this.in.readObject();
            String pass = (String) this.in.readObject();
            
            boolean flag = this.users.autentification(pass,name);
            
            this.out.writeObject(flag);
            while(!flag){
                name = (String) this.in.readObject();
                pass = (String) this.in.readObject();
                flag = this.users.autentification(pass,name);
                this.out.writeObject(flag);
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void secondMenu(){
         try {
            String value ="hello";
            while(!(value.equals("5"))){
                value =(String) in.readObject();
                switch(value){
                    case "1":
                        menuAluguer();
                        break;
                    case "2":
                        break;
                    case "3":
                        break;
                    case "4":
                        break;
                }
            }     
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void aluguer(String server){

    }
    
    public void menuAluguer(){
        try {
            String value ="hello";
            while(!(value.equals("4"))){
                value =(String) in.readObject();
               
                switch(value){
                    
                    case  "1":
                        aluguer("large");
                        //large
                        break;
                    case "2":
                        //medium
                        break;
                    case "3":
                        //normal
                        break;
                }
            }     
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        try {
  
            String value ="hello";
            while(!(value.equals("3"))){
                value =(String) in.readObject();
                switch(value){
                    case "1":
                        registar();
                        secondMenu();
                        break;
                    case "2":
                        login();
                        secondMenu();
                        break;
                }
               
            }
                      
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
         close();
    }   
}

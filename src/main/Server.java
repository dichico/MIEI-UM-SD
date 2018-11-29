/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Server {
    
    private ServerSocket serverSocket;
    private int port;
    private Users users;
    
    public Server(int port){
        this.port = port;
        this.users = new Users();
    }  
    
    public void startServer(){
        try {
            
            System.out.println("### INITIALIZING SERVER ###");
            this.serverSocket = new ServerSocket(this.port);
                        
            while(true){
                System.out.println("SERVER > waiting for a new connection...");              
                              
                Socket socket = this.serverSocket.accept();
                
                System.out.println("SERVER > Connection received!");
                
                ServerWorker sw = new ServerWorker(socket,users);
                new Thread(sw).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
        
    public static void main(String[] args){
        Server s = new Server(12345);
        s.startServer();
    }
}

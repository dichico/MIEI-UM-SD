/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.net.Socket;

/**
 *
 * @author SarahTifanydaSilva
 */
public class ServerWorker implements Runnable {
    
    private Socket socket;
    private Users users;
    
    public ServerWorker(Socket socket,Users users){
        this.socket = socket;
        this.users = users;
    }
       
    @Override
    public void run() {
        System.out.println("HELLO Thread");
    }
}

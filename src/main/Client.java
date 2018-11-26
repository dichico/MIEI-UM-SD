/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Client {
    
    private Socket socket;
    private String hostname;
    private int porto;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private BufferedReader systemIn;
    private User user;
    
    public Client(String hostname,int porto){
        this.hostname = hostname;
        this.porto = porto;
    }
    
    public void inicializing(){
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void clientStart(){
        try {
            System.out.println("### CLIENT ###");
            this.socket = new Socket(this.hostname,this.porto);
            // abrir canais de leitura e escrita;
            this.systemIn = new BufferedReader(new InputStreamReader(System.in));
            
           // this.in = new ObjectInputStream(socket.getInputStream());
	   // out = new ObjectOutputStream(socket.getOutputStream());
            String value = "hello";
            while(!(value.equals("3"))){
                mainMenu();
                System.out.print("> ");
                value = (String) this.systemIn.readLine();
                switch(value){
                    case "1":
                        System.out.print("Escolha o seu nickname\n> ");
                        String name = this.systemIn.readLine();
                        System.out.println(name); 
                        // enviar para o server
                        // receber codigo OK -> se existe ja o user na base ou NOT -> se 
                        System.out.print("Insira password\n> ");
                        String pass = this.systemIn.readLine();
                        // enviar para o server
                        // receber Mensagem de sucesso
                        // sair
                        break;
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void mainMenu(){
        System.out.println("Escolha uma das opções (1 a 3):");
        System.out.println("1.SIGN IN");
        System.out.println("2.LOGIN");
        System.out.println("3.SAIR");
    }
    
    public void close(){
        try {
            this.socket.shutdownInput();
            this.socket.shutdownOutput();
            this.socket.close();
            this.systemIn.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }    
    }
    
    public static void main(String[] args){
        Client c = new Client("127.0.0.1",12345);
        c.clientStart();
        
    }
}

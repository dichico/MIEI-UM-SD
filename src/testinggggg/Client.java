package testinggggg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static principal_MESMO.Menus.*;


public class Client implements Serializable{
    private Thread listener;
    private Socket socket;
    private final String hostname;
    private final int porto;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private BufferedReader systemIn;
    private ClientListener clientListener;

    
    public Client(String hostname,int porto){
        this.hostname = hostname;
        this.porto = porto;
    }
    
    public void inicializing(){
        try {
            this.listener = new Thread(new ClientListener());  
            this.listener.setDaemon(true);
            this.socket = new Socket(this.hostname,this.porto);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.systemIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException ex) {
            System.err.println("Erro método inicializing, classe Client " + ex.getMessage());
        }
    }
    
   

    /**
     * Método que regista um user.
     */
    public void registar(){
        try {
            System.out.println("### SIGN_IN ###");
            System.out.print("Insira o seu mail\n> ");
            
            String name = this.systemIn.readLine();
            
            this.out.writeObject(name);
            
            
            boolean flag = (Boolean) in.readObject();
            
            while(flag){
                System.out.print("O user com mail " +name+ " já existe! Insira outro mail\n> ");
                name = this.systemIn.readLine();
                out.writeObject(name);
                flag =(Boolean) in.readObject();
            }
            System.out.print("Insira password\n> ");
            String pass = this.systemIn.readLine();
            out.writeObject(pass);
            System.out.println("### User inserido com sucesso ###");
            
            this.out.flush();
        } catch (IOException| ClassNotFoundException ex) {
            System.err.println("Erro método registar, classe Client " + ex.getMessage());
        }
    }
    
    /**
     * Feito falta só garantir que o mesmo utilizador não é utilizado por
     * diferentes threads 
     */
    public void login(){
        try {
            
            System.out.println("### LOGIN ###");
            System.out.print("Mail :\n> ");
            String name = this.systemIn.readLine();
            this.out.writeObject(name);
            
            System.out.print("Password:\n> ");
            String pass = this.systemIn.readLine();
            this.out.writeObject(pass);
            
            boolean flag = (Boolean) this.in.readObject();
            while(!flag){
                System.out.println("Credenciais de acesso erradas ou User já fez login\nTente novamente");
                System.out.print("Mail:\n> ");
                name = this.systemIn.readLine();
                this.out.writeObject(name);
                System.out.print("Password:\n> ");
                pass = this.systemIn.readLine();
                this.out.writeObject(pass);
                flag = (Boolean) this.in.readObject();
            }
            System.out.println("Login efetuado com sucesso!");
            this.out.flush();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erro método login, classe Client " + ex.getMessage());
        }
                        
    }
    

    
    public void aluguer(){
        double custo;
        try {
            custo = (Double) in.readObject();
            System.out.println("Custo do server "+custo+ "€");
            String value = "ok";
            while(!value.equals("2")){
                String s = (String) this.in.readObject();
                System.out.println("Deseja continuar com o aluguer do servidor?");
                System.out.println("1.Sim");
                System.out.println("2.Não");
                System.out.print(">");
                value = (String) systemIn.readLine();
                this.out.writeObject(value);
            }
            this.out.flush();
        } catch (IOException | ClassNotFoundException ex) {
           System.err.println("Erro método aluguer, classe Client " + ex.getMessage());
        }
        
    }
    
    
    public void menuAluguer(){
        try {       
            String value ="ok";
            while(!(value.equals("4"))){
                presentigServers();
                System.out.print("> ");
                value = (String) this.systemIn.readLine();
                this.out.writeObject(value);
                
                if (!(value.equals("4"))) aluguer();
            }
        } catch (IOException ex) {
           System.err.println("Erro método menuAluguer, classe Client " + ex.getMessage());
        }
    }
    
    public void leilao(){
        try {
            listener.start();
            
            String msg;
            String value = "ok";
            System.out.println("Se pretender sair do leilao digit quit");

            while(!value.equals("quit")){
                System.out.print("> ");
                value = this.systemIn.readLine();
                this.out.writeObject(value);
            }
            System.out.println("Terminado Cliente");
            this.out.flush();
         } catch (IOException ex ) {
            System.err.println("Erro método leilão, classe Client " + ex.getMessage());
        } 
    }
    
    public void menuLeilao(){
        try {           
            String value ="ok";
            while(!(value.equals("4"))){
                presentigServers();
                System.out.print("> ");
                value = (String) this.systemIn.readLine();
                this.out.writeObject(value);
                if(!(value.equals("4"))) leilao();
            }
            this.out.flush();
        } catch (IOException ex) {
          System.err.println("Erro método menuLeilao, classe Client " + ex.getMessage());
        }
    }
    
    public void depositar(){
        try {
            System.out.print(">");
            String value = this.systemIn.readLine();
            double valor = Double.parseDouble(value);
            this.out.writeObject(valor);
            System.out.println("Saldo depositado com sucesso!");
            this.out.flush();
        } catch (IOException ex) {
            System.err.println("Erro método depositar, classe Client " + ex.getMessage());
        }
    }
    
    
    public void clientSecondMenu(){
        try {
            System.out.println("### Reserva de servidores ###");
        
            String value ="hello";
            while(!(value.equals("5"))){
                secondMenu();
                System.out.print("> ");
                value = (String) this.systemIn.readLine();
                this.out.writeObject(value);
                switch(value){
                    case "1":
                        menuAluguer();
                        break;
                    case "2":
                        menuLeilao();
                        break;
                    case "3":
                        depositar();
                        break;
                    case "4":
                        consultar();
                        break;
                }
            }
            this.out.flush();
        } catch (IOException ex) {
           System.err.println("Erro método clientSecondMenu, classe Client " + ex.getMessage());
        }
    }
    
    public void consultar(){
        try {
            double custo = (Double) this.in.readObject();
            System.out.println("Saldo atual "+custo+"€");
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erro método consultar, classe Client " + ex.getMessage());
        }
    }
    
    public void clientStart(){
        try {
            System.out.println("### CLIENT ###");

            inicializing();
            String value = "hello";
            while(!(value.equals("3"))){
                mainMenu();
                System.out.print("> ");
                value = (String) this.systemIn.readLine();
                this.out.writeObject(value);
                switch(value){
                    case "1":
                        registar();
                        clientSecondMenu();
                        break;
                    case "2":
                        login();
                        clientSecondMenu();
                        break;
                }
            }
        } catch (IOException ex) {
            System.err.println("Erro método clientStart, classe Client " + ex.getMessage());
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            this.out.flush();
        } catch (IOException ex) {
            System.err.println("Erro método clientStart, classe Client " + ex.getMessage());
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        close();
    }
    
    
    
    public void close() {
        try {
            this.socket.shutdownInput();
            this.socket.shutdownOutput();
            this.socket.close();
            this.systemIn.close();
            
        } catch (IOException ex) {
            System.err.println("Erro método close, classe Client " + ex.getMessage());
        }    
    }
    
    public static void main(String[] args){
        Client c = new Client("127.0.0.1",12345);
        c.clientStart();
    }
    
    public class ClientListener implements Runnable {
        private boolean terminado;
	public ClientListener(){}
	
        public synchronized void leilaoTerminado(){
            while(terminado){
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        public synchronized void leilaoIniciado(){
            System.out.println("Acordou!");
            this.notify();
        }
        
        @Override
	public void run(){
            String message;
            
            try {
                while(((message = (String) in.readObject())!= "quit") && !(terminado = (Boolean) in.readObject())){
                    System.out.println("");
                    System.out.println(terminado);
                    System.out.println(message);
                    System.out.print("> ");
                }
                if (terminado){
                    System.out.println("Thread Leilao Terminado");
                    leilaoTerminado();
                }
            }
            catch (SocketException e) {}
            catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
    }

}

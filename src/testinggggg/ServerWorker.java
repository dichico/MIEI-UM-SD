package testinggggg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;



public class ServerWorker implements Runnable,Serializable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Users users;
    private Servidores large;
    private Servidores normal;
    private Servidores micro;
    private Leilao leilao;
    private User user;
    
    
    public ServerWorker(Socket socket,Users users,Servidores large,Servidores normal, Servidores micro,Leilao leilao){
        try {
            this.users = users;
            this.large = large;
            this.normal = normal;
            this.micro = micro;
            this.leilao = leilao;
            this.user = new User();
            this.socket = socket;
            this.in = new ObjectInputStream(this.socket.getInputStream());
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException ex) {
             System.err.println("Erro construtor ServerWorker, classe ServerWorker " + ex.getMessage());
        }       
    }
       
    public void close(){
        try {
            this.socket.shutdownInput();
            this.socket.shutdownOutput();
            this.socket.close();
        } catch (IOException ex) {
             System.err.println("Erro método close, classe ServerWorker " + ex.getMessage());
        }
    }
    
    /**
     * Método que permite registar um cliente.
     */
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
            this.user = new User(name,pass,0);
            this.users.addUser(this.user);
            this.out.flush();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erro método registar, classe ServerWorker " + ex.getMessage());
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
            this.user = this.users.getUser(name);
            this.out.flush();
        } catch (IOException | ClassNotFoundException ex) {
             System.err.println("Erro método login, classe ServerWorker " + ex.getMessage());
        }
    }
    
    public void depositar(){
        try {
            Double custo = (Double) this.in.readObject();
            this.users.updateConta(this.user.getMail(), custo);
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erro método depositar, classe ServerWorker " + ex.getMessage());
        }
    }
    
    public void secondMenu(){
         try {
            String value ="ok";
            while(!(value.equals("5"))){
                value =(String) in.readObject();
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
            this.users.setAtivo(this.user.getMail(), false);
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erro método secondMenu, classe ServerWorker " + ex.getMessage());
        }
    }
    
    public void consultar(){
        double custo = this.user.getSaldo();
        try {
            this.out.writeObject(custo);
            this.out.flush();
        } catch (IOException ex) {
           System.err.println("Erro método consultar, classe ServerWorker " + ex.getMessage());
        }
    }
    
    public void aluguer(String server){
       Servidor s;
       int id=0;
       double custo = 0;
       if (server.equals("large")){
           custo = 1;
           id = this.large.alugaServer("aluguer");
       }
       else if (server.equals("normal")){
           custo = 0.9;
           id = this.normal.alugaServer("aluguer");
       }
       else {
           custo = 0.75;
           id = this.micro.alugaServer("aluguer");
       }
        try {
            this.out.writeObject(custo);
            String value ="ok";
            while(!value.equals("2")){
                // Supondo que é 1h
                Thread.sleep(3600);
                this.user.retiraSaldo(custo);
                this.out.writeObject("");
                value = (String) this.in.readObject();
            }
            if (server.equals("large")) this.large.libertaServidor(id,"aluguer");
            else if (server.equals("normal")) this.normal.libertaServidor(id,"aluguer");
            else this.micro.libertaServidor(id,"aluguer");
            this.out.flush();
        } catch (IOException | InterruptedException | ClassNotFoundException ex) {
             System.err.println("Erro método aluguer, classe ServerWorker " + ex.getMessage());
        }
    }
    
    public void menuAluguer(){
        try {
            String value ="ok";
            while(!(value.equals("4"))){
                value =(String) in.readObject();
                switch(value){
                    case  "1":
                        aluguer("large");
                        break;
                    case "2":
                        aluguer("normal");
                        break;
                    case "3":
                        aluguer("micro");
                        break;
                }
            }     
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erro método menuAluguer,classe ServerWorker " + ex.getMessage());
        }
    }
    
   
    public void leilao(String server){
        
        try {
            synchronized(this.leilao){
                if (this.leilao.getTerminado()){
                    this.leilao.setTerminado(false);
                    Temporizador t = new Temporizador(this.leilao);
                    t.start();
                }
            }
            
            System.out.println(this.leilao);
            String mail = this.user.getMail();
            
            this.leilao.addUser(mail, out);
            // enviei msg
            this.leilao.sendingMessage("User entrou no leilao", mail,false,false);
           
            String value = "ok";
            boolean flag = this.leilao.getTerminado();

            while(!value.equals("quit") && !flag){
                flag = this.leilao.getTerminado();
                value = (String) this.in.readObject();
                if (!value.equals("quit")&& !flag){    
                    this.leilao.sendingMessage(value, mail,true,false);
                }
            }
            
            this.out.flush();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erro método leilao, classe ServerWorker " + ex.getMessage());
        }
    }
    
    
    public void menuLeilao(){
        try {
            String value ="ok";
            while(!(value.equals("4"))){
                value =(String) in.readObject();
                switch(value){
                    case "1":
                        leilao("large");
                        break;
                    case "2":
                        leilao("normal");
                        break;
                    case "3":
                        leilao("micro");
                        break;
                }
            }     
        } catch (IOException | ClassNotFoundException ex) {
             System.err.println("Erro método menuLeilao, classe ServerWorker " + ex.getMessage());
        }
    }
    
    @Override
    public void run() {
        try {
  
            String value ="ok";
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
            System.err.println("Erro método run, classe ServerWorker " + ex.getMessage());
        }
        close();
    }   
}

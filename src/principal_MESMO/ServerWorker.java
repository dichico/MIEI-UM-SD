package principal_MESMO;

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
    
    
    public ServerWorker(Socket socket,Users users,Servidores large,Servidores normal, Servidores micro, Leilao leilao){
        try {
            this.users = users;
            this.large = large;
            this.normal = normal;
            this.micro = micro;
            this.leilao = leilao;
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
            User u = this.users.getUser(name);
            this.out.writeObject(u);
        } catch (IOException | ClassNotFoundException ex) {
             System.err.println("Erro método login, classe ServerWorker " + ex.getMessage());
        }
    }
    
    public void depositar(){
        try {
            User u = (User) this.in.readObject();
            this.users.updateConta(u);           
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
                        break;
                }
            }     
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erro método secondMenu, classe ServerWorker " + ex.getMessage());
        }
    }
    
    public void aluguer(String server){
        Servidor s;
       if (server.equals("large"))  s = this.large.alugaServer();
       else if (server.equals("normal")) s = this.normal.alugaServer();
       else s = this.micro.alugaServer();
       double custo = s.getCusto();
        try {
            this.out.writeObject(custo);
            String value ="ok";
            while(!value.equals("2")){
                // Supondo que é 1h
                Thread.sleep(3600);
                this.out.writeObject("");
                value = (String) this.in.readObject();
            }
            if (server.equals("large")) this.large.libertaServidor(s.getId());
            else if (server.equals("normal")) this.normal.libertaServidor(s.getId());
            else this.micro.libertaServidor(s.getId()); 

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
            String mail = (String) this.in.readObject();  
            this.leilao.addUser(mail, out);
            this.leilao.multicast("User entrou no leilao", mail);
            double max = 0;
            String value = "ok";
            while(!value.equals("quit")){
                value = (String) this.in.readObject();
                if (!(value.equals("quit"))){
                    double valueD = Double.parseDouble(value);
                    if (valueD>max){
                        System.out.println(max);
                        max = valueD;
                        this.leilao.multicast(value, mail);
                    }
                    
                }
            }
            
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erro método leilao, classe ServerWorker " + ex.getMessage());
        }
    }
    
    
    public void menuLeilao(){
        try {
            String value ="ok";
            while(!(value.equals("4"))){
                value =(String) in.readObject();
            if(!(value.equals("4"))) leilao("large");
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

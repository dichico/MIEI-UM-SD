package teste;

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
    private Leiloes leiloes;
    
    
    public ServerWorker(Socket socket,Users users,Servidores large,Servidores normal, Servidores micro, Leiloes leiloes){
        try {
            this.users = users;
            this.large = large;
            this.normal = normal;
            this.micro = micro;
            this.leiloes = leiloes;
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
       int id=0;
       double custo=0;
       try {
            switch(server){
                 case "large":
                    id = this.large.alugaServer("aluguer");
                    custo = 1;
                    break;
                 case "normal":
                     id = this.normal.alugaServer("aluguer");
                     custo = 0.9;
                     break;
                 case "micro":
                     id = this.micro.alugaServer("aluguer");
                     custo = 0.75;
                     break;
            }
            this.out.writeObject(custo);
            String value ="ok";
            while(!value.equals("2")){
                // Supondo que é 1h
                Thread.sleep(3600);
                this.out.writeObject("");
                value = (String) this.in.readObject();
            }
            if (server.equals("large")) this.large.libertaServidor(id,"aluguer");
            else if (server.equals("normal")) this.normal.libertaServidor(id,"aluguer");
            else this.micro.libertaServidor(id,"aluguer"); 

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
        int id =0;
        double custo =0;
        try {
            if (server.equals("large"))
                id = this.large.alugaServer("leilao");
            if (server.equals("normal"))
                id = this.normal.alugaServer("leilao");
            if (server.equals("micro")) 
                id = this.micro.alugaServer("leilao");
            // Servidores encontram-se todos ocupados"!
            if (id==-2){
                this.out.writeObject(false);
                return;
            }
            this.out.writeObject(true);
            //Obtenho mail
            String mail = (String) this.in.readObject();  
            //adiciono ao leilao
            this.leiloes.entraLeilao(server, mail, out);
            //envio mensagem aos outros que entrar um cliente no leilao
            this.leiloes.enviaMensagem("User entrou no leilao", mail, server);
            String value = "ok";
            boolean terminado;
            while(!(terminado=this.leiloes.isTerminado(server))){
                this.out.writeObject(terminado);
                value = (String) this.in.readObject();
                if (!terminado){
                    double valueD = Double.parseDouble(value);
                    this.leiloes.enviaMensagem(value, mail, server);
                }
            }
            if (terminado && this.leiloes.isWinner(mail,server)){
                this.out.writeObject(true);
                custo = this.leiloes.getCustoMaior(server);
                this.out.writeObject(custo);
                String value2 ="ok";
                while(!value2.equals("2")){
                    // Supondo que é 1h
                    Thread.sleep(3600);
                    this.out.writeObject("");
                    value2 = (String) this.in.readObject();
                }
                if (server.equals("large")) this.large.libertaServidor(id,"leilao");
                else if (server.equals("normal")) this.normal.libertaServidor(id,"leilao");
                else this.micro.libertaServidor(id,"leilao"); 
                }
        } catch (IOException | ClassNotFoundException | InterruptedException ex) {
            System.err.println("Erro método leilao, classe ServerWorker " + ex.getMessage());
        }
    }
    
    
    public void menuLeilao(){
        try {
            String value ="ok";
            while(!(value.equals("4"))){
                value =(String) in.readObject();
                switch(value){
                    case  "1":
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

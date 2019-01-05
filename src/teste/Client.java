package teste;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import static principal_MESMO.Menus.*;


public class Client implements Serializable{
    private Thread listener;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private BufferedReader systemIn;
    private final String hostname;
    private final int porto;
    private User user;
    private int idServer;
    
    /**
     * Contrutor de passagem de parâmetros
     * @param hostname
     * @param porto 
     */
    public Client(String hostname,int porto){
        this.hostname = hostname;
        this.porto = porto;
    }
    
    /**
     * Método que inicializa as variáves de instância.
     * Cria o socket para o cliente, abre o canal de escrita e de leitura no socket e
     * abre o canal de leitura do teclado.
     */
    public void inicializing(){
        try {
            this.socket = new Socket(this.hostname,this.porto);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.systemIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException ex) {
            System.err.println("Erro método inicializing, classe Client " + ex.getMessage());
        }
    }
       
    public void registar(){
        try {
            System.out.println("### SIGN_IN ###");
            System.out.print("Insira o seu mail\n> ");
            
            String name = this.systemIn.readLine();
            
            out.writeObject(name);
            
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
            this.user = new User(name,pass,0);
            System.out.println("### User inserido com sucesso ###");
        } catch (IOException| ClassNotFoundException ex) {
            System.err.println("Erro método registar, classe Client " + ex.getMessage());
        }
    }
    
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
                System.out.println("Credenciais de acesso erradas\nTente novamente");
                System.out.print("Mail:\n> ");
                name = this.systemIn.readLine();
                this.out.writeObject(name);
                System.out.print("Password:\n> ");
                pass = this.systemIn.readLine();
                this.out.writeObject(pass);
                flag = (Boolean) this.in.readObject();
            }
            System.out.println("Login efetuado com sucesso!");
            User u = (User) this.in.readObject();
            this.user = u;
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erro método login, classe Client " + ex.getMessage());
        }
                        
    }
    

    
    public void aluguerLeilao(){
        double custo;
        try {
            custo = (Double) in.readObject();
            System.out.println("Custo do server "+custo+ "€");
            String value = "ok";
            while(!value.equals("2")){
                String s = (String) this.in.readObject();
                this.user.retiraSaldo(custo);
                System.out.println("Deseja continuar com o aluguer do servidor?");
                System.out.println("1.Sim");
                System.out.println("2.Não");
                System.out.print(">");
                value = (String) systemIn.readLine();
                this.out.writeObject(value);
            }
            
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
                
                if (!(value.equals("4"))) aluguerLeilao();
            }
        } catch (IOException ex) {
           System.err.println("Erro método menuAluguer, classe Client " + ex.getMessage());
        }
    }
    
    public void leilao(){
        try {
            boolean flag =(Boolean) this.in.readObject();
            // Servidores encontram-se todos ocupados"!
            if (flag==false){
                System.out.println("Servidores encontram-se todos ocupados!\nTente novamente mais tarde!");
                return;
            }
            this.listener = new Thread(new ClientListener());
            listener.start();
            // Envio mail
            this.out.writeObject(this.user.getMail());

            String value = "ok";
            System.out.println("Digite a sua licitação");
            boolean terminado = false;
            while(!(terminado = (Boolean)this.in.readObject())){
                System.out.print(">");
                value = this.systemIn.readLine();
                this.out.writeObject(value);
            }
            System.out.println(terminado);
            boolean winner;
            if (terminado && (winner = (Boolean) this.in.readObject())){
                aluguerLeilao();
            }
        } catch (IOException | ClassNotFoundException ex ) {
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
        } catch (IOException ex) {
          System.err.println("Erro método menuLeilao, classe Client " + ex.getMessage());
        }
    }
    
    public void depositar(){
        try {
            System.out.print(">");
            String value = this.systemIn.readLine();
            double valor = Double.parseDouble(value);
            this.user.setSaldo(valor);
            this.out.writeObject(this.user);
            System.out.println("Saldo depositado com sucesso!");
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
                        System.out.println("Saldo: "+this.user.getSaldo()+"€");
                        break;
                }
            }
        } catch (IOException ex) {
           System.err.println("Erro método clientSecondMenu, classe Client " + ex.getMessage());
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
        }
        close();
    }
    
    
    
    public void close(){
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
     
    public class ClientListener implements Runnable{
        public ClientListener(){}
        
        @Override
        public void run() {
            String value = "ok";
            try {
                while((value = (String) in.readObject())!=null){
                    System.out.println(value);
                }
            } catch (SocketException ex) {} 
            catch (IOException | ClassNotFoundException ex) {
                System.err.println("Erro método run, classe ClientListener " + ex.getMessage());
            }
        }
        
    }
}

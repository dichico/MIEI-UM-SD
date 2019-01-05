package teste1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


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
            this.socket = new Socket(this.hostname,this.porto);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.systemIn = new BufferedReader(new InputStreamReader(System.in));
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
    
    public void secondMenu(){
        System.out.println("Escolha uma das opções (1 a 3):");
        System.out.println("1.Alugar servidor");
        System.out.println("2.Ir a leilão");
        System.out.println("3.Depositar dinheiro");
        System.out.println("4.Consultar conta");
        System.out.println("5.SAIR");
    }
    
    public void registar(){
        try {
            System.out.println("SIGN_IN");
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
            System.out.println("###User inserido com sucesso###");
        } catch (IOException| ClassNotFoundException ex) {
            Logger.getLogger (Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Feito falta só garantir que o mesmo utilizador não é utilizado por
     * diferentes threads 
     */
    public void login(){
        try {
            
            System.out.println("LOGIN");
            System.out.print("Mail :\n> ");
            String name = this.systemIn.readLine();
            this.out.writeObject(name);
            System.out.print("Password:\n> ");
            String pass = this.systemIn.readLine();
            this.out.writeObject(pass);
            boolean flag = (Boolean) this.in.readObject();
            while(!flag){
                System.out.println("Credenciais de acesso erras\nTente novamente");
                System.out.print("Mail :\n> ");
                name = this.systemIn.readLine();
                this.out.writeObject(name);
                System.out.print("Password:\n> ");
                pass = this.systemIn.readLine();
                this.out.writeObject(pass);
                flag = (Boolean) this.in.readObject();
            }
            System.out.println("Login efectuado com sucesso!");
            User u = (User) this.in.readObject();
            this.user = u;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
                        
    }
    
    public void presentigServers(){
        System.out.println("Escolha o tipo de servidor que pretende (1 a 3)");
        System.out.println("1.m5.large (1,00€/hora)");
        System.out.println("2.p4.normal (0,90€/hora)");
        System.out.println("3.t3.micro (0,75€/hora)");
        System.out.println("4.SAIR");
    }
    
    public void aluguer(){
        double custo;
        try {
            custo = (Double) in.readObject();
            System.out.println("Custo do server "+custo+ "€");
            String value = "hello";
            while(!value.equals("1")){
                String s = (String) this.in.readObject();
                this.user.retiraSaldo(custo);
                System.out.println("Deseja cancelar o aluguer?");
                System.out.println("1.Sim");
                System.out.println("2.Não");
                System.out.print(">");
                value = (String) systemIn.readLine();
                this.out.writeObject(value);
            }
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public void menuAluguer(){
        try {
            System.out.println("### Tipos de servidores ###");
        
            String value ="hello";
            while(!(value.equals("4"))){
                presentigServers();
                System.out.print("> ");
                value = (String) this.systemIn.readLine();
                this.out.writeObject(value);
                switch(value){
                    case "1":
                        aluguer();
                        break;
                    case "2":
                        aluguer();
                        break;
                    case "3":
                        aluguer();
                        break;
                }
            }
        } catch (IOException ex) {
           Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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
            ex.printStackTrace();
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
           Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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
            ex.printStackTrace();
        }    
    }
    
    public static void main(String[] args){
        Client c = new Client("127.0.0.1",12345);
        c.clientStart();
        
    }
}

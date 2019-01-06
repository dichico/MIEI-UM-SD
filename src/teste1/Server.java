package teste1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    
    private ServerSocket serverSocket;
    private int port;
    private Users users;
    private Servidores large;
    private Servidores normal;
    private Servidores micro;


    
    public Server(int port){
        this.port = port;
        this.users = new Users();
        this.large = new Servidores(5);
        this.normal = new Servidores(5);
        this.micro = new Servidores(5);
        for(int i=0;i<5;i++){
            Servidor s1 = new Servidor("m5.large",1,i);
            Servidor s2 = new Servidor("p4.normal",0.90,i);
            Servidor s3 = new Servidor("t3.micro",0.75,i);           
            this.large.adicionaServidor(s1);
            this.normal.adicionaServidor(s2);
            this.micro.adicionaServidor(s3);
        }
    }  
    
    public void startServer(){
        try {
            
            System.out.println("### INITIALIZING SERVER ###");
            this.serverSocket = new ServerSocket(this.port);
                        
            while(true){
                System.out.println("SERVER > waiting for a new connection...");              
                              
                Socket socket = this.serverSocket.accept();
                
                System.out.println("SERVER > Connection received!");
                
                
                ServerWorker sw = new ServerWorker(socket,users,large,normal,micro);
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
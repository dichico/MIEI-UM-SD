
package teste4;

import teste.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Servidor {
    private String nome;
    private double custoHora;
    private int quantidade;
    private ReentrantLock lockServer;
    private Condition isEmpty;

    public Servidor(){
        this.nome = "";
        this.custoHora = 0.0;
        this.quantidade = 0;
        this.lockServer = new ReentrantLock();
        this.isEmpty = this.lockServer.newCondition();
    }
    
    public Servidor(String nome, double custoHora,int quantidade) {
        this.nome = nome;
        this.custoHora = custoHora;
        this.quantidade = quantidade;
        this.lockServer = new ReentrantLock();
        this.isEmpty = this.lockServer.newCondition();
    }
 
    
    public String getName() {
        return this.nome;
    }
    
    public double getCusto(){
        return this.custoHora;
    }
    
    public int getQuantidade(){
        return this.quantidade;
    }
    
    public void decrementaQuantidade(){
        this.lockServer.lock();
        try{
            if (this.quantidade==0){
                System.out.println("Não existem servidores disponíveis");
                this.isEmpty.await();
            }
            this.quantidade--;
        } catch (InterruptedException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            this.lockServer.unlock();
        }
    }
    
    public void incrementaQuantidade(){
        this.lockServer.lock();
        try {
            this.quantidade++;
            this.isEmpty.signalAll();
        } finally {
            this.lockServer.unlock();
        }
    }
    
    public String toString(){
        StringBuilder s = new StringBuilder("### SERVIDOR ###\n");
        s.append("Quantidade: " +this.quantidade+"\n");
        s.append("Nome: " +this.nome+"\n");
        s.append("Custo por hora: " +this.custoHora+"€\n");
        return s.toString();
    }
}

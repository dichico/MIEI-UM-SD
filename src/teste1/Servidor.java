package teste1;

import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;


public class Servidor implements Serializable {
   
    private String nome;
    private double custoHora;
    private int id;
    private ReentrantLock lockServidor;

    public Servidor() {
        this.nome="";
        this.custoHora=0;
        this.id =0;
        this.lockServidor = new ReentrantLock();
    }
    
    public Servidor(String nome, double custoHora,int id){
        this.lockServidor = new ReentrantLock();
        this.nome = nome;
        this.custoHora= custoHora;
        this.id = id;
    }
    
    public String getName() {
        return this.nome;
    }
    
    public double getCusto(){
        return this.custoHora;
    }
    
    public int getId(){
        return this.id;
    }
    
    public void setId(int i){
        this.id=i;
    }
    
    public void lock(){
        this.lockServidor.lock();
    }
    
   public void unlock(){
        this.lockServidor.unlock();
    }
   
    public boolean isLocked(){
        return (this.lockServidor.isLocked());
    }
    
    public String toString(){
        StringBuilder s = new StringBuilder("### SERVIDOR ###\n");
        s.append("Nome: " +this.nome+"\n");
        s.append("Custo por hora: " +this.custoHora+"€\n");
        s.append("Id: " +this.id+"\n");
        if(this.isLocked()) s.append("Locked\n");
        else s.append("Unlocked\n");
        return s.toString();
    }
}

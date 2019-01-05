/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reserva;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Servidores {
    private Servidor[] servidores;              // Total de servidores em leilao.  
    private Queue<Integer> idsLeiloados;        // Ids dos servidores que foram leiloados
    private int counterL;                       // Nº de servidores que foram leiloados.
    private ReentrantLock lockServidores;       // Lock para o servidores
    private Condition occupied;                 // Condição para quando todos os servidores estiverem alugados           
    private int counterA;                       // Nº de servidores alugados
    private int proxPos;                        // Próximo servidor a ser alugado/leiloado
    
    public Servidores(int n){
        this.servidores = new Servidor[n];
        this.idsLeiloados = new LinkedList<>();
        this.counterL = 0;
        this.counterA = 0;
        this.proxPos = n-1;
        this.lockServidores = new ReentrantLock();
        this.occupied = this.lockServidores.newCondition();
    }
    
    // usada para povoar unicamente para povoar dai não ser um método protegido para concorrencia
    public void adicionaServidor(Servidor s){
        this.servidores[s.getId()] = s;
    }
    
    public int alugaServer(String tipo){
        this.lockServidores.lock();
        int id = -1;
        try{
            // Se os servidores estiverem todos a serem alugados espera-se
            while(this.counterA==this.servidores.length && tipo.equals("aluguer")){
                this.occupied.await();
            }
            // Se os servidores estiverem todos ocupados(leiloados e alugados, cancelo um servidore leiloado)
        /*    if (tipo.equals("aluguer") && (this.counterL>0) && ((this.counterA+this.counterL)==this.servidores.length)){
                int idL = cancelaLeilao();
                libertaServidor(idL,tipo);
            }
        */    // Se os servidores tiverem todos ocupados(leiloados e/ alugados) e é requerido um leilao retorna -2.
            if (tipo.equals("leilao") && ((this.counterA+this.counterL)==this.servidores.length) ) return -2;
            searchPos();
            Servidor s = this.servidores[this.proxPos];
            id = s.getId();
            s.lock();
            
            if (tipo.equals("leilao")){
                this.counterL++;
                System.out.println("Nº de servidores leiloado "+this.counterL);
                this.idsLeiloados.add(id);
                System.out.println("Thread: " +Thread.currentThread().getName()+" LEILOA server "+s.getId());
            }
            else{
                this.counterA++;
                System.out.println("Nº de servidores alugados "+this.counterA);
                System.out.println("Thread: " +Thread.currentThread().getName()+" ALUGA server "+s.getId());
            }
            return id;
        } catch (InterruptedException ex) {
            System.err.println("Erro método alugaServer, classe Servidores" + ex.getMessage());
        } finally{
            this.lockServidores.unlock();
        }
        return id;
    }
    
    
    public int cancelaLeilao(){
        int id = this.idsLeiloados.peek();
        return id;
    }
    
    
    // é so chamado no alugaServer por isso não é necessário ter protecção para threads pois só acede 1 de cada vez
    public  boolean searchPos(){
       
        if(!(this.servidores[this.proxPos].isLocked())) return true;
        int i;
        for(i=0;i<this.servidores.length;i++){
            if(!(this.servidores[this.proxPos].isLocked())) return true;
            else if (this.proxPos==0) this.proxPos = this.servidores.length-1 ;
            else this.proxPos--;
        }
        return false;
    }
    
    
    public void libertaServidor(int i,String tipo){
        this.lockServidores.lock();
        try {
            Servidor s = this.servidores[i];
            if (tipo.equals("aluguer")){
                this.counterA--;
                System.out.println("Nº de servidores alugados "+this.counterA);
            }
            else {
                this.counterL--;
                this.idsLeiloados.remove(i);
                System.out.println("Nº de servidores leiloado "+this.counterL);
            }
            s.unlock();
            System.out.println("Thread: " +Thread.currentThread().getName()+" LIBERTA server "+s.getId());
            this.proxPos = i;
            this.occupied.signalAll();
        } finally {
            this.lockServidores.unlock();
        }
    }
    
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder("##Servidores###\n");
        
        for(Servidor s1 : this.servidores){
            s.append(s1+"\n");
        }
        return s.toString();
    }
}

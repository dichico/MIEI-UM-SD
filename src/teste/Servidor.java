/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Diogo Nogueira
 */
public class Servidor {
    private String nome;
    private double custoHora;
    private int id;

    public Servidor(){
        this.id = 0;
        this.custoHora = 0.0;
        this.id = 0;
    }
    
    public Servidor(String nome, double custoHora,int id) {
        this.nome = nome;
        this.custoHora = custoHora;
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
    
    public String toString(){
        StringBuilder s = new StringBuilder("### SERVIDOR ###\n");
        s.append("Id: " +this.id+"\n");
        s.append("Nome: " +this.nome+"\n");
        s.append("Custo por hora: " +this.custoHora+"€\n");
        return s.toString();
    }
}

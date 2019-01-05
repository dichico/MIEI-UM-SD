/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste4;

import java.util.HashMap;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Servidores {
    
    private HashMap<String,Servidor> servidores;
    
    public Servidores(){
        this.servidores = new HashMap<>();
    }
    
    // Não é necessario ser protegido pois só vai servir para povoar.
    public void adicionaServidor(Servidor s){
        this.servidores.put(s.getName(), s);
    }
    
 
  
}

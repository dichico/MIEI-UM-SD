/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.HashMap;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Users {
    
    private HashMap<String,User> users;
    
    public Users(){
        this.users = new HashMap<>();
    }
    
    public synchronized void addUser(User u) {
        this.users.put(u.getMail(), u);
    }
    
    public boolean userExists(String mail){
         return(this.users.containsKey(mail));
    }
    
    // rever
    public boolean autentification(String pass, String mail) {
        if((this.users.containsKey(mail))){
            User u = this.users.get(mail);
            return(u.autentification(pass));
        }
        else return false;
    }
    
    public User getUser(String mail){
        User u =this.users.get(mail);
        return u;
    }
    
    public String toString(){
        StringBuilder s = new StringBuilder("Clientes:\n");
        for(User u: this.users.values()){
            s.append(u);
        }
        return s.toString();
    }
}

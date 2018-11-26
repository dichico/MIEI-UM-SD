/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Testing;

import main.*;
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
    
    public synchronized void addUser(User u) throws UserException{
        if (this.users.containsKey(u.getMail())) throw new UserException("User com o mail "+u.getMail()+ " já existe!");
        this.users.put(u.getMail(), u);
    }
    
    public boolean autentification(String pass, String mail) throws UserException{
        if(!(this.users.containsKey(mail))) throw new UserException("User com o mail "+mail+ " não existe!");
        User u = this.users.get(mail);
        return(u.autentification(pass));
    }
    
    public User getUser(String mail){
        User u =this.users.get(mail);
        return u;
    }
    
    public String toString(){
        StringBuilder s = new StringBuilder("###Users###\n");
        
        for(User u : this.users.values()){
            s.append(u+"\n");
        }
        return s.toString();
    }
}

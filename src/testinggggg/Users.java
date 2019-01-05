
package testinggggg;

import main.*;
import java.util.HashMap;


public class Users {
    
    private HashMap<String,User> users;
    
    public Users(){
        this.users = new HashMap<>();
    }
    
    /**
     * Método que regista um User-
     * @param u User que pretende se registar.
     */
    public synchronized void addUser(User u) {
        this.users.put(u.getMail(), u);
    }
    
    /**
     * Método que atualiza o saldo do useeer
     * @param u 
     */
    public void updateConta(String mail, double custo){
        User u1 = this.users.get(mail);
        u1.depositaSaldo(custo);
    }
    
    
    public boolean userExists(String mail){
         return(this.users.containsKey(mail));
    }
    
    public void setAtivo(String mail,boolean flag){
        User u = this.users.get(mail);
        u.setAtivo(flag);
    }
    
    // rever
    public synchronized boolean autentification(String pass, String mail) {
        if((this.users.containsKey(mail))){
            User u = this.users.get(mail);
            if (!(u.getAtivo())){
                u.setAtivo(true);
                return(u.autentification(pass));
            }
        }
        return false;
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

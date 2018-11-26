/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Testing;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Client1 implements Runnable {
    
    private Users users;
    
    public Client1(Users users){
        this.users = users;
    }

    @Override
    public void run() {
        try {
            User u = new User("antonio","25014588");
            this.users.addUser(u);
        } catch (UserException ex) {
            
        }
    }
}

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
public class Client2 implements Runnable {
    private Users users;
    
    public Client2(Users users){
        this.users = users;
    }

    @Override
    public void run() {
        
        try {
            User u = new User("antonio","25014588");
            this.users.addUser(u);
        } catch (UserException ex) {
            System.err.println("User já inserido!");
        }
    }
}

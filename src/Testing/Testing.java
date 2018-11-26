/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Testing;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Testing {
    
    public static void main(String[] args) throws UserException{
        
        
            Users users = new Users();
            int N = 10;
            Thread t1 = new Thread(new Client1(users));
            Thread t2 = new Thread(new Client1(users));
            
            t1.start();
            t2.start();
        try {    
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Testing.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(users);
    }
}

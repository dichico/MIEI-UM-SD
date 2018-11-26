/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Testing;

import main.*;

/**
 *
 * @author SarahTifanydaSilva
 */
public class User {
    
    private String mail;
    private String password;

    public User() {
        this.mail="";
        this.password="";
    } 
    
    public User(String mail, String password){
        this.mail = mail;
        this.password = password;
    }
    
    
    public User(User client){
        this.mail = client.getMail();
        this.password = client.getPassword();
    }
    
    // Getters
    public String getMail(){ 
        return this.mail;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    
    //Setters
    public void setMail(String mail){
        this.mail = mail;
    }
    
    public void setPass(String pass){
        this.password = pass;
    }
    
    //comparar passwords
    public boolean autentification(String pass){
        return (this.password.equals(pass));
    }
    
    //toString
    public String toString(){
        StringBuilder s = new StringBuilder("Cliente\n");
        s.append("Email: " +this.mail+"\n");
        s.append("Password: "+this.password+"\n");
        
        return s.toString();
    }    
    
    //equals
    public boolean equals(Object o){
        if (this == o) return true;
        if ((o==null) || (this.getClass() != o.getClass())) return false;
        User a = (User) o;
        return ((this.mail.equals(a.getMail())) && (this.password.equals(a.getPassword())));
    }
    
    //clone
    public User clone(){
        return new User(this);
    }
     
}

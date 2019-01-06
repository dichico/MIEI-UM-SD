package teste;

import principal_MESMO.*;
import java.io.Serializable;

public class User implements Serializable{
    
    private String mail;
    private String password;
    private double saldo;

    public User() {
        this.mail="";
        this.password="";
        this.saldo = 0;
    } 
    
    public User(String mail, String password,double saldo){
        this.mail = mail;
        this.password = password;
        this.saldo = saldo;
    }
    
    
    public User(User user){
        this.mail = user.getMail();
        this.password = user.getPassword();
        this.saldo = user.getSaldo();
    }
    
    // Getters
    public String getMail(){ 
        return this.mail;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public double getSaldo(){
        return this.saldo;
    }
    
    
    //Setters
    public void setMail(String mail){
        this.mail = mail;
    }
    
    public void setPass(String pass){
        this.password = pass;
    }
    
    public void setSaldo(double saldo){
        this.saldo = saldo;
    }
    
    //retira saldo
    // caso custo a retirar seja maior que o saldo
    
    public void retiraSaldo(double custo){
        this.saldo -= custo;
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
        s.append("Saldo: "+this.saldo+"\n");
        
        return s.toString();
    }    
    
 
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
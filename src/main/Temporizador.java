/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Temporizador {
    private Leilao leilao;
    private Timer temporizador;
    private TimerTask timerTask;
    
    public Temporizador(Leilao l){
        this.leilao = l;
    }
    
    public void start(){
        temporizador = new Timer();
        timerTask = new TimerTask() {

            @Override
            public void run() {
                System.out.println("Terminado");
                leilao.terminaLeilao();
            }
        };
       temporizador.schedule(timerTask,60000); 
    }
}

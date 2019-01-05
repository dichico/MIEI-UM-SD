/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author SarahTifanydaSilva
 */
public class Temporizador {
    private static Leilao leilao;
    private static Timer temporizador;
    private static TimerTask timerTask;
    
    public static void start(Leilao l){
        Temporizador.temporizador = new Timer();
        Temporizador.leilao = l;
        Temporizador.timerTask = new TimerTask() {

            @Override
            public void run() {
                int e = leilao.getSize();
                System.out.println("Valor "+e+"\n");
            }
        };
        Temporizador.temporizador.schedule(timerTask,120000);
        
    }
    public static void main(String[] args){
        Leilao l = new Leilao();
        Temporizador.start(l);
    }
}

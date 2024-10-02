package diningphilosophers;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Philosopher extends Thread {
    private final static int delai = 1000;
    private final ChopStick myLeftStick;
    private final ChopStick myRightStick;
    private boolean running = true;

    public Philosopher(String name, ChopStick left, ChopStick right) {
        super(name);
        myLeftStick = left;
        myRightStick = right;
    }

    private void think() throws InterruptedException {
        System.out.println("M."+this.getName()+" pense... ");
        sleep(delai+new Random().nextInt(delai+1));
        System.out.println("M."+this.getName()+" arrête de penser");
    }

    private void eat() throws InterruptedException {
        System.out.println("M."+this.getName() + " mange...");
        sleep(delai+new Random().nextInt(delai+1));
        //System.out.println("M."+this.getName()+" arrête de manger");
    }

    @Override
    public void run() {
        boolean BaguetteGauche = false;
        boolean BaguetteDroite = false;
        while (running) {
            try {
                think();
                // Aléatoirement prendre la baguette de gauche puis de droite ou l'inverse
                switch(new Random().nextInt(2)) {
                    case 0:
                        BaguetteGauche = myLeftStick.take();
                        think(); // pour augmenter la probabilité d'interblocage
                        BaguetteDroite = myRightStick.take();
                        break;
                    case 1:
                        BaguetteDroite = myRightStick.take();
                        think(); // pour augmenter la probabilité d'interblocage
                        BaguetteGauche = myLeftStick.take();
                }
                if(BaguetteDroite && BaguetteGauche)
                {
                    eat();
                }
            
                // On libère les baguettes :
                myLeftStick.release();
                myRightStick.release();
                // try again
            } catch (InterruptedException ex) {
                Logger.getLogger("Table").log(Level.SEVERE, "{0} Interrupted", this.getName());
            }
        }
    }

    // Permet d'interrompre le philosophe "proprement" :
    // Il relachera ses baguettes avant de s'arrêter
    public void leaveTable() {
        running = false;
    }

}

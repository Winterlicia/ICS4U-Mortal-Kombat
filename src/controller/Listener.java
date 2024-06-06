package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.*;

public class Listener implements ActionListener {
    GameState state;
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == state.ssm) {
            String msg = state.ssm.readText();
            //System.out.println("Reading message: "+msg);
            String[] msgComponents = msg.split(",");
            //System.out.println("Component[0]: "+msgComponents[0] + " Component[1]:"+msgComponents[1] + " Component[2]:"+msgComponents[2]);

            //Character Selection View Socket
            if (msgComponents[0].equals("Host")) {
                state.player1 = new Player(msgComponents[1]); //Update other screen
                state.player1.chooseFighter(msgComponents[2]);
                System.out.println(state.player1.fighter.name);
                //state.player1.isReady = msgComponents[3].equals("true");
            } else if (msgComponents[0].equals("Client")) {
                state.player2 = new Player(msgComponents[1]);
                state.player2.chooseFighter(msgComponents[2]);
                System.out.println(state.player2.fighter.name);
                //state.player2.isReady = msgComponents[3].equals("true");
            }

            //Game View Socket
            //Message Format: userType, currentX, currentY
            else if (msgComponents[0].equals("host") && state.player1 != null) {
                try {
                    state.player1.currentX = Integer.valueOf(msgComponents[1]);
                    state.player1.currentY = Integer.valueOf(msgComponents[2]);

                    //Attacking
                    if (msgComponents[3] != null && !msgComponents[3].isEmpty()) {
                    state.player1.isAttacking = msgComponents[3].equals("true");
                    }
                    //Check the specific action
                    if (msgComponents[4] != null && !msgComponents[4].isEmpty()) {
                        state.player1.currentAction = msgComponents[4];
                    }
                    //Movement Disabled Check
                    if (msgComponents[5] != null && !msgComponents[5].isEmpty()) {
                        state.player1.movementDisabled = msgComponents[5].equals("true");
                    }
                    //Stagger Has Run Check
                    if (msgComponents[6] != null && !msgComponents[6].isEmpty()) {
                        state.player1.hasRun = msgComponents[6].equals("true");
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                

            } else if (msgComponents[0].equals("client") && state.player2 != null) {
                try {
                    state.player2.currentX = Integer.valueOf(msgComponents[1]);
                    state.player2.currentY = Integer.valueOf(msgComponents[2]);

                    //Attacking
                    if (msgComponents[3] != null && !msgComponents[3].isEmpty()) {
                        state.player2.isAttacking = msgComponents[3].equals("true");
                    }
                    //Check the specific action
                    if (msgComponents[4] != null && !msgComponents[4].isEmpty()) {
                        state.player2.currentAction = msgComponents[4];
                    }         
                    //Movement Disabled Check
                    if (msgComponents[5] != null && !msgComponents[5].isEmpty()) {
                        state.player2.movementDisabled = msgComponents[5].equals("true");
                    }
                    //Stagger Has Run Check
                    if (msgComponents[6] != null && !msgComponents[6].isEmpty()) {
                        state.player2.hasRun = msgComponents[6].equals("true");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }       
                
            }
        }
    }

    public Listener(GameState state) {
        this.state = state;
    }
}
package view;
/*
 * THE VIEW CLASS CONTAINS CODE FOR FRONTEND PROGRAM -- ANIMATIONS AND SOME GAME LOGIC (i.e. hitboxes)
 */


import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Player;
import network.*;

public class MainView extends JPanel implements ActionListener, KeyListener {
    //Properties
    JFrame frame = new JFrame("View Testing");
    CardLayout cardLayout = new CardLayout();
    HomeView homeView = new HomeView();
    CharacterSelectionView characterSelectionView = new CharacterSelectionView();
    MulticastServer ms;
    MulticastClient mc;
    String hostPlayerName, clientPlayerName;

    //Methods
    @Override
    public void actionPerformed(ActionEvent evt) {
        //If the user presses the host button on the home screen, open MulticastClient to host their game
        if (evt.getSource() == mc) {
            System.out.println(mc.readText());
        }

        if (evt.getSource() == homeView.hostButton) {
            //One person has joined -- manipulate the characterSelectionView to have a "waiting for opponent" -- TO-DO
            ms = new MulticastServer("236.169.184.1", 15775);
            characterSelectionView.userType = "Host";
            hostPlayerName = homeView.usernameField.getText();
            try {
                ms.sendText(hostPlayerName+","+"236.169.184.1"+","+"15775");    
            } catch (IOException e) {
                e.printStackTrace();
            } 

        } else if (evt.getSource() == homeView.helpButton) {
            //Two people now in-game, show the Character Selection Screen 
            mc = new MulticastClient("236.169.184.1", 15775, this);
            mc.connect();
            characterSelectionView.userType = "Client";
            cardLayout.show(this, "characterSelectionView");
            clientPlayerName = homeView.usernameField.getText();
        
        } else if (evt.getSource() == homeView.helpButton) {
            //TO-DO: Interactive HelpScreen
        
        } else if (characterSelectionView.hostReady == true && characterSelectionView.clientReady == true) {
            //Two people done selecting, start game

        } 
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    /**
     * Draws the components of the panel, with animations.
     * @param g The graphics object used to draw the components
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
    }
    
    //Constructor (Testing):
    public MainView() {
        //Default settings. Using cardLayout to be able to switch between different screens throughout the game
        this.setPreferredSize(new Dimension(1280, 720));
        this.setLayout(cardLayout);
        this.add(homeView, "homeScreen");
        this.add(characterSelectionView, "characterSelectionView");
        cardLayout.show(this, "homeScreen");

        //Add action listeners to necessary JComponents:
        homeView.hostButton.addActionListener(this);
        homeView.joinButton.addActionListener(this);
        homeView.helpButton.addActionListener(this);

        //Default settings
        frame.setContentPane(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    //Testing purposes:
    public static void main(String[] args) {
        new MainView();
    }

}

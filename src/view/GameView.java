package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import controller.Hitbox;
import model.GameState;
import model.IceBall;

public class GameView extends JPanel implements ActionListener {
    //Properties
    GameState state;
    Hitbox hitbox;
    IceBall iceBall1, iceBall2;
    JLabel player1NameLabel = new JLabel(), player2NameLabel = new JLabel();
    JLabel player1HPCount = new JLabel(), player2HPCount = new JLabel();
    private final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    //private boolean hasRun = false;

    //Init images
    /**
     * Fight Background
     */
    BufferedImage imgBackground;
    Timer timer = new Timer(1000/60, this);

    //Methods
      /**
     * GameView's paintComponent paints the actual UI on the screen
     * @param g Graphics used to paint
    */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(imgBackground, 0, 0, null);

        // g2d.setColor(Color.BLACK);
        // g2d.setStroke(new BasicStroke(4.0f));
        // g2d.drawLine(0, 680, 1280, 680); //The ground

        //Health bars and Player name labels
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawRect(20, 10, 500, 50); //each point of health = 0.5px
        g2d.drawRect(760, 10, 500, 50);
        g2d.fillRect(20, 10, (int)(state.player1.fighter.HP * 0.5), 50);
        g2d.fillRect(760, 10, (int)(state.player2.fighter.HP * 0.5), 50);
        player1NameLabel.setText(state.player1.name);
        player2NameLabel.setText(state.player2.name);
        player1HPCount.setText(String.valueOf(state.player1.fighter.HP) + "/1000");
        player2HPCount.setText(String.valueOf(state.player2.fighter.HP) + "/1000");

        if (state.currentPlayer.currentAnimationImg.equals(state.currentPlayer.fighter.staggerRight))
        System.out.println(true);

        //Animation updates and settings
        //Images stored in the fighter subclasses
        if (!state.player1.isAttacking 
            && (state.player1.currentX <= state.player2.currentX) 
            && !state.player1.currentAction.equals("got punched") 
            && !state.player1.currentAction.equals("got kicked") 
            && !state.player1.currentAction.equals("got uppercut")) {
            state.player1.currentAnimationImg = state.player1.fighter.idleLeft;
        } else if (!state.player1.isAttacking 
            && (state.player1.currentX > state.player2.currentX) 
            && !state.player1.currentAction.equals("got punched") 
            && !state.player1.currentAction.equals("got kicked")
            && !state.player1.currentAction.equals("got uppercut")) {
            state.player1.currentAnimationImg = state.player1.fighter.idleRight;
        }
        
        if (!state.player2.isAttacking 
            && (state.player1.currentX <= state.player2.currentX) 
            && !state.player2.currentAction.equals("got punched") 
            && !state.player2.currentAction.equals("got kicked")
            && !state.player2.currentAction.equals("got uppercut")) {
            state.player2.currentAnimationImg = state.player2.fighter.idleRight;
        } else if (!state.player2.isAttacking 
            && (state.player1.currentX > state.player2.currentX) 
            && !state.player2.currentAction.equals("got punched") 
            && !state.player2.currentAction.equals("got kicked")
            && !state.player2.currentAction.equals("got uppercut")) {
            state.player2.currentAnimationImg = state.player2.fighter.idleLeft;
        }

        //Punching, update for other player to see
        if (state.currentPlayer.equals(state.player2) && state.player1.isAttacking && state.player1.currentAction.equals("punch")) {
            if (state.player1.currentX <= state.player2.currentX) {
                state.player1.currentAnimationImg = state.player1.fighter.punchLeft;
                hitbox.punchHitbox();

                if (state.player2.currentAction.equals("got punched") && !state.player2.hasRun) {
                    state.player2.currentAnimationImg = state.player2.fighter.staggerRight;
                    state.player2.currentX += 50;
                    // g.drawImage(state.player1.currentAnimationImg, state.player1.currentX, state.player1.currentY, this); //Player 1 (Host)
                    // g.drawImage(state.player2.fighter.staggerRight, state.player2.currentX, state.player2.currentY, this); // Client
                    state.player2.hasRun = true;
                }

            } else if (state.player1.currentX > state.player2.currentX) {
                state.player1.currentAnimationImg = state.player1.fighter.punchRight;
                hitbox.punchHitbox();

                if (state.player2.currentAction.equals("got punched") && !state.player2.hasRun) {
                    state.player2.currentAnimationImg = state.player2.fighter.staggerLeft;
                    state.player2.currentX -= 50;
                    // g.drawImage(state.player1.currentAnimationImg, state.player1.currentX, state.player1.currentY, this); //Player 1 (Host)
                    // g.drawImage(state.player2.fighter.staggerLeft, state.player2.currentX, state.player2.currentY, this); // Client
                    state.player2.hasRun = true;
                }

            }
        }

        if (state.currentPlayer.equals(state.player1) && state.player2.isAttacking && state.player2.currentAction.equals("punch")) {
            if (state.player1.currentX <= state.player2.currentX) {
                state.player2.currentAnimationImg = state.player2.fighter.punchRight;
                hitbox.punchHitbox();

                if (state.player1.currentAction.equals("got punched") && !state.player1.hasRun) {
                    state.player1.currentAnimationImg = state.player1.fighter.staggerLeft;
                    state.player1.currentX -= 50;
                    state.player1.hasRun = true;
                    //attackTimer("punch");
                }

            } else if (state.player1.currentX > state.player2.currentX) {
                state.player2.currentAnimationImg = state.player2.fighter.punchLeft;
                hitbox.punchHitbox();

                if (state.player1.currentAction.equals("got punched") && !state.player1.hasRun) {
                    state.player1.currentAnimationImg = state.player1.fighter.staggerRight;
                    state.player1.currentX += 50;
                    state.player1.hasRun = true;
                }
            }

        }

        //Kicking
        if (state.currentPlayer.equals(state.player2) && state.player1.isAttacking && state.player1.currentAction.equals("kick")) {
            
            if (state.player1.currentX <= state.player2.currentX) {
                state.player1.currentAnimationImg = state.player1.fighter.kickLeft;
                hitbox.kickHitbox();

                if (state.player2.currentAction.equals("got kicked") && !state.player2.hasRun) {
                    state.player2.currentAnimationImg = state.player2.fighter.staggerRight;
                    state.player2.currentX += 80;
                    state.player2.hasRun = true;
                }

            } else if (state.player1.currentX > state.player2.currentX) {
                state.player1.currentAnimationImg = state.player1.fighter.kickRight;
                hitbox.kickHitbox();

                if (state.player2.currentAction.equals("got kicked") && !state.player2.hasRun) {
                    state.player2.currentAnimationImg = state.player2.fighter.staggerLeft;
                    state.player2.currentX -= 80;
                    state.player2.hasRun = true;
                }

            }
        }

        if (state.currentPlayer.equals(state.player1) && state.player2.isAttacking && state.player2.currentAction.equals("kick")) {
            
            if (state.player1.currentX <= state.player2.currentX) {
                state.player2.currentAnimationImg = state.player2.fighter.kickRight;
                hitbox.kickHitbox();

                if (state.player1.currentAction.equals("got kicked") && !state.player1.hasRun) {
                    state.player1.currentAnimationImg = state.player1.fighter.staggerLeft;
                    state.player1.currentX -= 80;
                    state.player1.hasRun = true;
                    //attackTimer("kick");
                }

            } else if (state.player1.currentX > state.player2.currentX) {
                state.player2.currentAnimationImg = state.player2.fighter.kickLeft;
                hitbox.kickHitbox();

                if (state.player1.currentAction.equals("got kicked") && !state.player1.hasRun) {
                    state.player1.currentAnimationImg = state.player1.fighter.staggerRight;
                    state.player1.currentX += 80;
                    state.player1.hasRun = true;
                }
            }
        }

        //Uppercuts
        if (state.currentPlayer.equals(state.player2) && state.player1.isAttacking && state.player1.currentAction.equals("uppercut")) {
            //TODO: Fix
            if (state.player1.currentX <= state.player2.currentX) {
                state.player1.currentAnimationImg = state.player1.fighter.uppercutLeft;
                hitbox.uppercutHitbox();

                if (state.player2.currentAction.equals("got uppercut") && !state.player2.hasRun) {
                    state.player2.currentAnimationImg = state.player2.fighter.staggerRight;
                    state.player2.currentX += 60;
                    state.player2.currentY -= 180;
                    state.player2.hasRun = true;
                }

            } else if (state.player1.currentX > state.player2.currentX) {
                state.player1.currentAnimationImg = state.player1.fighter.uppercutRight;
                hitbox.uppercutHitbox();

                if (state.player2.currentAction.equals("got uppercut") && !state.player2.hasRun) {
                    state.player2.currentAnimationImg = state.player2.fighter.staggerLeft;
                    state.player2.currentX -= 60;
                    state.player2.currentY -= 180;
                    state.player2.hasRun = true;
                }

            }
        }

        if (state.currentPlayer.equals(state.player1) && state.player2.isAttacking && state.player2.currentAction.equals("uppercut")) {
            //TODO: Fix
            if (state.player1.currentX <= state.player2.currentX) {
                state.player2.currentAnimationImg = state.player2.fighter.uppercutRight;
                hitbox.uppercutHitbox();

                if (state.player1.currentAction.equals("got uppercut") && !state.player1.hasRun) {
                    state.player1.currentAnimationImg = state.player1.fighter.staggerLeft;
                    state.player1.currentX -= 60;
                    state.player1.currentY -= 180;
                    state.player1.hasRun = true;
                    //attackTimer("uppercut");
                }

            } else if (state.player1.currentX > state.player2.currentX) {
                state.player2.currentAnimationImg = state.player2.fighter.uppercutLeft;
                hitbox.uppercutHitbox();

                if (state.player1.currentAction.equals("got uppercut") && !state.player1.hasRun) {
                    state.player1.currentAnimationImg = state.player1.fighter.staggerRight;
                    state.player1.currentX += 60;
                    state.player1.currentY -= 180;
                    state.player1.hasRun = true;
                }
            }
        }

        //TODO: Special Moves Animations
        //Ice Ball (in progress):
        if (state.player1.fighter.name.equals("Subzero")) {
            animateIceBall(g, iceBall1);
        } 
        if (state.player2.fighter.name.equals("Subzero")) {
            animateIceBall(g, iceBall2);
        }

        //Update movement (we'll see if this needs to be moved all the way to the top)
        g.drawImage(state.player1.currentAnimationImg, state.player1.currentX, state.player1.currentY, this); //Player 1 (Host)
        g.drawImage(state.player2.currentAnimationImg, state.player2.currentX, state.player2.currentY, this); // Client

        //Refresh to default frame settings for other player after an attack
        state.player1.movementDisabled = false;
        state.player2.movementDisabled = false;
        //state.player1.currentAction = "Idle";
        //state.player2.currentAction = "Idle";   
         
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == timer) {
            if (state.player1 == null || state.player2 == null) { return; }

            if (state.player1.fighter.name.equals("Subzero") && state.player1.fighter.isSpecialBeingUsed == true) {
                iceBall1.iceBallX += 50;
            } 
            if (state.player2.fighter.name.equals("Subzero") && state.player2.fighter.isSpecialBeingUsed == true) {
                iceBall2.iceBallX += 50;
            }

            forceFrame();

            this.repaint();
        }
    }    

    //Force animations to be within frame
    private void forceFrame() {
        if (state.player1.currentX < 0) {
            state.player1.currentX = 0;
        } else if (state.player1.currentX > 1280 - state.player1.fighter.WIDTH) {
            state.player1.currentX = 1280 - state.player1.fighter.WIDTH;
        }

        if (state.player2.currentX < 0) {
            state.player2.currentX = 0;
        } else if (state.player2.currentX > 1280 - state.player2.fighter.WIDTH) {
            state.player2.currentX = 1280 - state.player2.fighter.WIDTH;
        }

        if (state.player1.currentY < 500 - state.player1.fighter.HEIGHT - 180) { //Ensure jump doesn't overshoot
            state.player1.currentY = 500 - state.player1.fighter.HEIGHT; 
        } else if (state.player1.currentY > 680 - state.player1.fighter.HEIGHT - 180) { //Ensure landing doesn't overshoot
            state.player1.currentY = 680 - state.player1.fighter.HEIGHT;
        }

        if (state.player2.currentY < 500 - state.player2.fighter.HEIGHT - 180) { //Ensure jump doesn't overshoot
            state.player2.currentY = 500 - state.player2.fighter.HEIGHT; 
        } else if (state.player2.currentY > 680 - state.player2.fighter.HEIGHT - 180) { //Ensure landing doesn't overshoot
            state.player2.currentY = 680 - state.player2.fighter.HEIGHT;
        }
    }

    //Animation functionalities:
    private void jumpTimer() {
        Timer jumpTimer = new Timer(500, new ActionListener() { //Dynamic timer puts a delay and then performs an action afterwards
            @Override
            public void actionPerformed(ActionEvent e) {
                state.currentPlayer.currentY += 180;
                state.ssm.sendText((state.currentPlayer.equals(state.player1) ? "host," : "client,") +state.currentPlayer.currentX+","+state.currentPlayer.currentY+","+state.currentPlayer.isAttacking+",finished jump,"+state.currentPlayer.movementDisabled);
            }
        });
        jumpTimer.setRepeats(false);
        jumpTimer.start();
    }

    private void attackTimer(String attack) {
        int delay = 0; //Default
        if (attack.equals("punch")) {
            delay = 250;
        } else if (attack.equals("kick")) {
            delay = 500;
        } else if (attack.equals("uppercut")) {
            delay = 1000;
        }
        System.out.println("starting timer");

        Timer attackTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("ending timer");
                if (attack.equals("uppercut")) {
                    if (state.player1.currentAction.equals("got uppercut")) {
                        state.player1.currentY = 680 - state.player1.fighter.HEIGHT;

                        if (state.player1.currentX <= state.player2.currentX) {
                            state.player1.currentX -= 50;
                        } else if (state.player1.currentX < state.player2.currentX) {
                            state.player1.currentX += 50;
                        }
                    } 
                    
                    if (state.player2.currentAction.equals("got uppercut")) {
                        state.player2.currentY = 680 - state.player2.fighter.HEIGHT;
                        
                        if (state.player1.currentX <= state.player2.currentX) {
                            state.player2.currentX += 50;
                        } else if (state.player1.currentX < state.player2.currentX) {
                            state.player2.currentX -= 50;
                        }

                    }
                }

                state.currentPlayer.isAttacking = false;
                state.player1.movementDisabled = false;
                state.player2.movementDisabled = false; //Ensures both characters can move
                state.player1.currentAction = "Idle";
                state.player2.currentAction = "Idle";  
                state.player1.hasRun = false; 
                state.player2.hasRun = false;

                state.ssm.sendText("host,"+state.player1.currentX+","+state.player1.currentY+","+state.player1.isAttacking+","+state.player1.currentAction+","+state.player1.movementDisabled+","+state.player1.hasRun);
                state.ssm.sendText("client,"+state.player2.currentX+","+state.player2.currentY+","+state.player2.isAttacking+","+state.player2.currentAction+","+state.player2.movementDisabled+","+state.player2.hasRun);
                //state.ssm.sendText((state.currentPlayer.equals(state.player1) ? "host," : "client,") +state.currentPlayer.currentX+","+state.currentPlayer.currentY+","+state.currentPlayer.isAttacking+","+state.currentPlayer.currentAction+","+state.currentPlayer.movementDisabled);
            }
        });
        attackTimer.setRepeats(false);
        attackTimer.start();
    }

    private void animateScorpionSpear(Graphics g, int x, int y) {
        //TODO: animate scorpion's spear throw -- changes based on which side
        

    }

    //TODO: Create IceBall animations for both players in paintComponent
    private void animateIceBall(Graphics g, IceBall iceBall) {
        //TODO: animate Sub Zero's ice ball -- freezes opponent for set amount of time
        //While ice ball is being thrown, do we want it to be stuck in that pose/image?
        if (state.player1.fighter.name.equals("Subzero") && state.player1.fighter.isSpecialBeingUsed && !state.player1.currentAction.equals("jump")) {
            
            //If target is reached (regardless of block or not):
            if (iceBall.iceBallX >= state.player2.currentX && iceBall.iceBallX <= state.player2.currentX+state.player2.fighter.WIDTH) {
                state.player1.fighter.isSpecialBeingUsed = false;
                iceBall.iceBallX = 0; //Revert to default settings
            }

            //Movement based on side:
            if (state.player1.currentX <= state.player2.currentX) { //Left
                g.drawImage(iceBall.IceBallLeft, state.player1.currentX + state.player1.fighter.WIDTH + iceBall.iceBallX, 680 - state.player1.fighter.HEIGHT/2, null);
                //TODO: Ice Ball Hitbox
            } else if (state.player1.currentX > state.player2.currentX) { //Right
                g.drawImage(iceBall.IceBallRight, state.player1.currentX - iceBall.iceBallX, 680 - state.player1.fighter.HEIGHT/3, null);
            }
        }

        if (state.player2.fighter.name.equals("Subzero") && state.player2.fighter.isSpecialBeingUsed && !state.player2.currentAction.equals("jump")) {
            
            //If target is reached (regardless of block or not):
            if (iceBall.iceBallX >= state.player2.currentX && iceBall.iceBallX <= state.player2.currentX+state.player2.fighter.WIDTH) {
                state.player2.fighter.isSpecialBeingUsed = false;
                iceBall.iceBallX = 0; //Revert to default settings
            }

            //Movement based on side:
            if (state.player1.currentX <= state.player2.currentX) { //Right
                g.drawImage(iceBall.IceBallRight, state.player2.currentX - iceBall.iceBallX, 680 - state.player2.fighter.HEIGHT/3, null);
                //TODO: Ice Ball Hitbox
            } else if (state.player1.currentX > state.player2.currentX) { //Left
                g.drawImage(iceBall.IceBallLeft, state.player2.currentX + state.player2.fighter.WIDTH + iceBall.iceBallX, 680 - state.player2.fighter.HEIGHT/2, null);
            }
        }

    }    

    //Constructor
    public GameView(GameState state) {
        super();
        this.state = state;
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1280, 720));
        hitbox = new Hitbox(this.state);

        //JLabels
        player1NameLabel.setFont(new Font("Cambria", Font.PLAIN, 20));
        player1NameLabel.setForeground(Color.BLACK);
        player1NameLabel.setSize(100, 30);
        player1NameLabel.setLocation(40, 20);
        player1NameLabel.setVerticalAlignment(SwingConstants.CENTER);
        this.add(player1NameLabel);

        player2NameLabel.setFont(new Font("Cambria", Font.PLAIN, 20));
        player2NameLabel.setForeground(Color.BLACK);
        player2NameLabel.setSize(100, 30);
        player2NameLabel.setLocation(1120, 20);
        player2NameLabel.setVerticalAlignment(SwingConstants.CENTER);
        this.add(player2NameLabel);

        player1HPCount.setFont(new Font("Cambria", Font.PLAIN, 18));
        player1HPCount.setForeground(Color.GREEN);
        player1HPCount.setSize(100, 30);
        player1HPCount.setLocation(760 + 10, 21);
        player1HPCount.setVerticalAlignment(SwingConstants.CENTER);
        this.add(player1HPCount);

        player2HPCount.setFont(new Font("Cambria", Font.PLAIN, 18));
        player2HPCount.setForeground(Color.GREEN);
        player2HPCount.setSize(100, 30);
        player2HPCount.setLocation(420, 21);
        player2HPCount.setVerticalAlignment(SwingConstants.CENTER);
        this.add(player2HPCount);

        //Create new Ice Ball Instances for Sub-Zero
        iceBall1 = new IceBall();
        iceBall2 = new IceBall();

        //Loading the fight background:
        //Try to read the image from both the jar file and local drive
        InputStream backgroundClass = this.getClass().getResourceAsStream("src/assets/FightBackground.jpg");
    
        if (backgroundClass != null) {
            try {
                imgBackground = ImageIO.read(backgroundClass);
            } catch (IOException e) {
                System.out.println("Unable to read/load image from jar");
                e.printStackTrace();
            }
        } else { //If it can't be found on the jar, search it locally
            try {
                imgBackground = ImageIO.read(new File("src/assets/FightBackground.jpg"));
            } catch (IOException e) {
                System.out.println("Unable to read/load image");
                e.printStackTrace();
            }
        }

        //Start the timer:
        timer.start();

        //Key Bindings for Movement
        //Key Pressed Equivalent:
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("D"), "right"); //Moving right
        this.getActionMap().put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!state.currentPlayer.movementDisabled) {
                    state.currentPlayer.currentX += 10;
                    forceFrame();
                    System.out.println((state.currentPlayer.equals(state.player1) ? "host," : "client,") +state.currentPlayer.currentX+","+state.currentPlayer.currentY+","+state.currentPlayer.isAttacking+",moved right,"+state.currentPlayer.movementDisabled);
                    state.ssm.sendText((state.currentPlayer.equals(state.player1) ? "host," : "client,") +state.currentPlayer.currentX+","+state.currentPlayer.currentY+","+state.currentPlayer.isAttacking+",moved right,"+state.currentPlayer.movementDisabled);
                }
            }
        });

        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("A"), "left"); //Moving left
        this.getActionMap().put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!state.currentPlayer.movementDisabled) {
                    state.currentPlayer.currentX -= 10;
                    forceFrame();
                    System.out.println((state.currentPlayer.equals(state.player1) ? "host," : "client,") +state.currentPlayer.currentX+","+state.currentPlayer.currentY+","+state.currentPlayer.isAttacking+",moved left,"+state.currentPlayer.movementDisabled);
                    state.ssm.sendText((state.currentPlayer.equals(state.player1) ? "host," : "client,") +state.currentPlayer.currentX+","+state.currentPlayer.currentY+","+state.currentPlayer.isAttacking+",moved left,"+state.currentPlayer.movementDisabled);
                }
            }
        });

        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("W"), "jump"); //Jumping
        this.getActionMap().put("jump", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                if (!state.currentPlayer.movementDisabled) {
                    state.currentPlayer.currentY -= 180;
                    forceFrame();
                    state.ssm.sendText((state.currentPlayer.equals(state.player1) ? "host," : "client,") +state.currentPlayer.currentX+","+state.currentPlayer.currentY+","+state.currentPlayer.isAttacking+",jump,"+state.currentPlayer.movementDisabled);
                    jumpTimer();
                }
            }
        });

        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("J"), "punch"); //Punching
        this.getActionMap().put("punch", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {                 
                if (state.currentPlayer.equals(state.player1) && !state.player1.movementDisabled) {
                    state.currentPlayer.punch(state.player2);
                    state.ssm.sendText("host,"+state.currentPlayer.currentX+","+state.currentPlayer.currentY+","+state.currentPlayer.isAttacking+",punch,"+state.currentPlayer.movementDisabled);
                    String result = hitbox.punchHitbox();
                    forceFrame();

                    if (result.equals("Left punch hit")) {
                        state.player2.currentAnimationImg = state.player2.fighter.staggerRight;
                        state.player2.currentX += 50;
                    } else if (result.equals("Right punch hit")) {
                        state.player2.currentAnimationImg = state.player2.fighter.staggerLeft;
                        state.player2.currentX -= 50;
                    }

                    attackTimer("punch");

                } else if (state.currentPlayer.equals(state.player2) && !state.player2.movementDisabled) {
                    state.currentPlayer.punch(state.player1);
                    state.ssm.sendText("client,"+state.currentPlayer.currentX+","+state.currentPlayer.currentY+","+state.currentPlayer.isAttacking+",punch,"+state.currentPlayer.movementDisabled);
                    String result = hitbox.punchHitbox();
                    forceFrame();

                    if (result.equals("Left punch hit")) {
                        state.player1.currentAnimationImg = state.player1.fighter.staggerRight;
                        System.out.println("animation img correct? "+state.player1.currentAnimationImg.equals(state.player1.fighter.staggerRight));
                        state.player1.currentX += 50;
                    } else if (result.equals("Right punch hit")) {
                        state.player1.currentAnimationImg = state.player1.fighter.staggerLeft;
                        state.player1.currentX -= 50;
                    }
                    attackTimer("punch");

                }
            }
        });

        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("L"), "kick"); //Kicking
        this.getActionMap().put("kick", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {                 
                if (state.currentPlayer.equals(state.player1) && !state.player1.movementDisabled) {
                    state.currentPlayer.kick(state.player2);
                    state.ssm.sendText("host,"+state.currentPlayer.currentX+","+state.currentPlayer.currentY+","+state.currentPlayer.isAttacking+",kick,"+state.currentPlayer.movementDisabled);

                    String result = hitbox.kickHitbox();
                    forceFrame();
                    if (result.equals("Left kick hit")) {
                        state.player2.currentAnimationImg = state.player2.fighter.staggerRight;
                        //System.out.println("animation img correct? "+state.player1.currentAnimationImg.equals(state.player1.fighter.staggerRight));
                        state.player2.currentX += 80;
                    } else if (result.equals("Right kick hit")) {
                        state.player2.currentAnimationImg = state.player2.fighter.staggerLeft;
                        state.player2.currentX -= 80;
                    }
                    attackTimer("kick");

                } else if (state.currentPlayer.equals(state.player2) && !state.player2.movementDisabled) {
                    state.currentPlayer.kick(state.player1);
                    state.ssm.sendText("client,"+state.currentPlayer.currentX+","+state.currentPlayer.currentY+","+state.currentPlayer.isAttacking+",kick,"+state.currentPlayer.movementDisabled);
                    String result = hitbox.kickHitbox();
                    forceFrame();

                    if (result.equals("Left kick hit")) {
                        state.player1.currentAnimationImg = state.player1.fighter.staggerRight;
                        //System.out.println("animation img correct? "+state.player1.currentAnimationImg.equals(state.player1.fighter.staggerRight));
                        state.player1.currentX += 80;
                    } else if (result.equals("Right kick hit")) {
                        state.player1.currentAnimationImg = state.player1.fighter.staggerLeft;
                        state.player1.currentX -= 80;
                    }
                    attackTimer("kick");
                }
            }
        });

        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("K"), "uppercut"); //Uppercut
        this.getActionMap().put("uppercut", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {                 
                if (state.currentPlayer.equals(state.player1) && !state.player1.movementDisabled) {
                    state.currentPlayer.uppercut(state.player2);
                    state.ssm.sendText("host,"+state.currentPlayer.currentX+","+state.currentPlayer.currentY+","+state.currentPlayer.isAttacking+",uppercut,"+state.currentPlayer.movementDisabled);

                    String result = hitbox.uppercutHitbox();
                    forceFrame();
                    if (result.equals("Left uppercut hit")) {
                        state.player2.currentAnimationImg = state.player2.fighter.staggerRight;
                        //System.out.println("animation img correct? "+state.player1.currentAnimationImg.equals(state.player1.fighter.staggerRight));
                        state.player2.currentX += 60;
                        state.player2.currentY -= 180;
                    } else if (result.equals("Right uppercut hit")) {
                        state.player2.currentAnimationImg = state.player2.fighter.staggerLeft;
                        state.player2.currentX -= 60;
                        state.player2.currentY -= 180;
                    }
                    attackTimer("uppercut");

                } else if (state.currentPlayer.equals(state.player2) && !state.player2.movementDisabled) {
                    state.currentPlayer.uppercut(state.player1);
                    state.ssm.sendText("client,"+state.currentPlayer.currentX+","+state.currentPlayer.currentY+","+state.currentPlayer.isAttacking+",uppercut,"+state.currentPlayer.movementDisabled);
                    String result = hitbox.uppercutHitbox();
                    forceFrame();

                    if (result.equals("Left uppercut hit")) {
                        state.player1.currentAnimationImg = state.player1.fighter.staggerRight;
                        //System.out.println("animation img correct? "+state.player1.currentAnimationImg.equals(state.player1.fighter.staggerRight));
                        state.player1.currentX += 60;
                        state.player1.currentY -= 180;
                    } else if (result.equals("Right uppercut hit")) {
                        state.player1.currentAnimationImg = state.player1.fighter.staggerLeft;
                        state.player1.currentX -= 60;
                        state.player1.currentY -= 180;
                    }
                    attackTimer("uppercut");
                }
            }
        });

        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("I"), "special"); //Special Move
        this.getActionMap().put("special", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {                 
                if (state.currentPlayer.equals(state.player1) && !state.player1.movementDisabled) {
                    state.currentPlayer.specialMove(state.player2);
                    state.currentPlayer.fighter.isSpecialBeingUsed = true;
                    forceFrame();
                    state.ssm.sendText("host,"+state.currentPlayer.currentX+","+state.currentPlayer.currentY+","+state.currentPlayer.isAttacking+",special,"+state.currentPlayer.movementDisabled+","+state.currentPlayer.hasRun);
                    //attackTimer();
                } else if (state.currentPlayer.equals(state.player2) && !state.player2.movementDisabled) {
                    state.currentPlayer.specialMove(state.player1);
                    state.currentPlayer.fighter.isSpecialBeingUsed = true;
                    forceFrame();
                    state.ssm.sendText("client,"+state.currentPlayer.currentX+","+state.currentPlayer.currentY+","+state.currentPlayer.isAttacking+",special,"+state.currentPlayer.movementDisabled+","+state.currentPlayer.hasRun);
                    //attackTimer();
                }
            }
        });

    }
}
package model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**Class to store Scorpion's attributes*/
public class ScorpionFighter extends Fighter {
    // Properties

    /**Name of the Scorpion Fighter*/
    public String name = "Scorpion";

    /**Constructor to initialize Scorpion's images and dimensions*/
    public ScorpionFighter() {
        this.loadData(this.name);

        //Set width and height of generic fighter class
        WIDTH = 128; //(px) of idle image
        HEIGHT = 158; //(px) of idle image

        //Try to read the image from both the jar file and local drive
        InputStream idleLeftClass = this.getClass().getResourceAsStream("src/assets/scorpionIdleLeft.png");
        InputStream idleRightClass = this.getClass().getResourceAsStream("src/assets/scorpionIdleRight.png");
        InputStream punchLeftClass = this.getClass().getResourceAsStream("src/assets/scorpionPunchLeft.png");
        InputStream punchRightClass = this.getClass().getResourceAsStream("src/assets/scorpionPunchRight.png");
        InputStream kickLeftClass = this.getClass().getResourceAsStream("src/assets/scorpionKickLeft.png");
        InputStream kickRightClass = this.getClass().getResourceAsStream("src/assets/scorpionKickRight.png");
        InputStream uppercutLeftClass = this.getClass().getResourceAsStream("src/assets/scorpionUppercutLeft.png");
        InputStream uppercutRightClass = this.getClass().getResourceAsStream("src/assets/scorpionUppercutRight.png");
        InputStream staggerLeftClass = this.getClass().getResourceAsStream("src/assets/scorpionStaggerLeft.png");
        InputStream staggerRightClass = this.getClass().getResourceAsStream("src/assets/scorpionStaggerRight.png");
        InputStream blockLeftClass = this.getClass().getResourceAsStream("src/assets/scorpionBlockLeft.png");
        InputStream blockRightClass = this.getClass().getResourceAsStream("src/assets/scorpionBlockRight.png");  
        InputStream specialLeftClass = this.getClass().getResourceAsStream("src/assets/scorpionSpecialLeft.png");
        InputStream specialRightClass = this.getClass().getResourceAsStream("src/assets/scorpionSpecialRight.png");               

        if (punchLeftClass != null && punchRightClass != null && kickLeftClass != null && kickRightClass != null && uppercutLeftClass != null && uppercutRightClass != null && specialLeftClass != null && specialRightClass != null && staggerLeftClass != null && staggerRightClass != null) {
            try {
                idleLeft = ImageIO.read(idleLeftClass);
                idleRight = ImageIO.read(idleRightClass);
                punchLeft = ImageIO.read(punchLeftClass);
                punchRight = ImageIO.read(punchRightClass);
                kickLeft = ImageIO.read(kickLeftClass);
                kickRight = ImageIO.read(kickRightClass);
                uppercutLeft = ImageIO.read(uppercutLeftClass);
                uppercutRight = ImageIO.read(uppercutRightClass);
                staggerLeft = ImageIO.read(staggerLeftClass);
                staggerRight = ImageIO.read(staggerRightClass);
                blockLeft = ImageIO.read(blockLeftClass);
                blockRight = ImageIO.read(blockRightClass);
                specialLeft = ImageIO.read(specialLeftClass);
                specialRight = ImageIO.read(specialRightClass);                
                
            } catch (IOException e) {
                System.out.println("Unable to read/load image from jar");
                e.printStackTrace();
            }
        } else { //If it can't be found on the jar, search it locally
            try {
                idleLeft = ImageIO.read(new File("src/assets/scorpionIdleLeft.png"));
                idleRight = ImageIO.read(new File("src/assets/scorpionIdleRight.png"));
                punchLeft = ImageIO.read(new File("src/assets/scorpionPunchLeft.png"));
                punchRight = ImageIO.read(new File("src/assets/scorpionPunchRight.png"));
                kickLeft = ImageIO.read(new File("src/assets/scorpionKickLeft.png"));
                kickRight = ImageIO.read(new File("src/assets/scorpionKickRight.png"));
                uppercutLeft = ImageIO.read(new File("src/assets/scorpionUppercutLeft.png"));
                uppercutRight = ImageIO.read(new File("src/assets/scorpionUppercutRight.png"));
                staggerLeft = ImageIO.read(new File("src/assets/scorpionStaggerLeft.png"));
                staggerRight = ImageIO.read(new File("src/assets/scorpionStaggerRight.png"));
                blockLeft = ImageIO.read(new File("src/assets/scorpionBlockLeft.png"));
                blockRight = ImageIO.read(new File("src/assets/scorpionBlockRight.png"));
                specialLeft = ImageIO.read(new File("src/assets/scorpionSpecialLeft.png"));
                specialRight = ImageIO.read(new File("src/assets/scorpionSpecialRight.png"));    
                  
            } catch (IOException e) {
                System.out.println("Unable to read/load image");
                e.printStackTrace();
            }
        }

    }
}

package main;

import entities.Player;
import gfx.Screen;
import gfx.SpriteSheet;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import level.InputHandler;
import level.LevelHandler;

/* 
 * @author Colin King
 * @date Jun 23, 2013
 */
public class CargoForge extends Canvas implements Runnable {
    
    private JFrame frame;
    
    /* size of the screen, before scaling up */
    private final int SCREENWIDTH = 16 * 20;
    private final int SCREENHEIGHT = 16 * 16;
    
    /* increases the size of the displayed screen */
    private final int SCALE = 2;
    
    private final String NAME = "OreMiner v0.3";
    
    private BufferedImage image = new BufferedImage(SCREENWIDTH, SCREENHEIGHT, BufferedImage.TYPE_INT_RGB);
    /*pixels is a reference to the above image's pixel data -> change pixels to change image */
    private int[] actualPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    
    /* other classes used */
    private SpriteSheet sprites;
    private Screen screen;
    private LevelHandler level;
    private InputHandler input;
    private Player miner;
    
    private CargoForge() {
        frame = new JFrame(NAME);
        
        this.setPreferredSize(new Dimension(SCALE * SCREENWIDTH, SCALE * SCREENHEIGHT));
        
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.toFront();
        this.requestFocus();
    }
    
    public static void main(String[] args) {
        new CargoForge().init();
    }
    
    private void init() {
        //initialize things
        sprites = new SpriteSheet();
        try {
            frame.setIconImage(ImageIO.read(new File("images/CFicon.png")));
        } catch (IOException ex) {
            Logger.getLogger(LevelHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        screen = new Screen(sprites, SCREENHEIGHT, SCREENWIDTH);
        level = new LevelHandler(screen, SCREENHEIGHT, SCREENWIDTH);
        input = new InputHandler(this);
        miner = new Player(input, level);
        //new thread that runs the "run" method below
        new Thread(this).start(); 
    }
    
    @Override
    public void run() {
        long lastTick = System.nanoTime();
        double deltaTime = 0;
        double timeBetwTicks = 1000000000.0 / 60.0; //nanoseconds/ ticks (50 ticks per sec)
        long fpsTimer = System.currentTimeMillis(); //second timer to keep track of fps
        
        int fps = 0;
        
        while(true) {
            long now = System.nanoTime();
            deltaTime += now - lastTick;
            lastTick = now;
            
            while(deltaTime >= timeBetwTicks) {
                deltaTime-=timeBetwTicks;
                tick();
            }
            
            render();
            fps++;
            
            if(System.currentTimeMillis() - fpsTimer >= 1000) {
                frame.setTitle(NAME + " --"
                        + " FPS: " + fps);
                fps = 0;
                fpsTimer += 1000;
            }
        }
    }
    
    private void tick() {
        //call tick methods
        //tick the level, which will tick the entities etc.
        //updates the player
        miner.tick();
    }
    
    private void render() {
        //offset of the window (changes as the player moves across the level
        int xOffset = miner.getX() - SCREENWIDTH/2;
        int yOffset = miner.getY() - SCREENHEIGHT/2;
        //draws all the tiles & background
        level.renderTiles(xOffset, yOffset);
        //draws the player
//        miner.drawMineAnimation(screen);
        miner.render(screen);
        /* transfers the pixels saved in the screen class into the images pixel data array */
        System.arraycopy(screen.screenPixels, 0, actualPixels, 0, actualPixels.length);
        
        Graphics g = this.getGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
    }
}
package gfx;

import java.awt.Image;
import java.awt.image.BufferedImage;
import static java.awt.image.ImageObserver.WIDTH;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/* 
 * @author Colin King
 * @date Jun 23, 2013
 */
public class SpriteSheet {
    
    private final String imgLoc = "images/Sprites16x.png";
    
    /* pixel width & height of the sprite sheet */
    protected int width, height;
    
    /* stores the values of the pixels in the sprite sheet */
    public int[] spritePixels;
    BufferedImage image;
    
    public SpriteSheet() {
        try {
            image = ImageIO.read(new File(imgLoc));
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException ex) {
            Logger.getLogger(SpriteSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
        spritePixels = new int[width * height];
        /* stores the RGB values of each pixel from the spritesheet in the format of "0xAARRGGBB" (hexadecimal) */
        spritePixels = image.getRGB(0, 0, width, height, null, 0, width);
    }
    
    public Image getSpriteImage(int tileIndex) {
        int xTile = tileIndex % 10;
        int yTile = tileIndex / 10;
        return image.getSubimage(16 * xTile, 16 * yTile, 16, 16);
    }
}

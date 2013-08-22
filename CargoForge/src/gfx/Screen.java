package gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import main.CargoForge;

/* 
 * @author Colin King
 * @date Jun 24, 2013
 */
public class Screen {

    /* pixel array where everything is rendered to, before being copied to the actualPixels array (and thus shown to the user) */
    public int[] screenPixels;
    /* pixel array containing pixel info of the background */
    private int[] bgPixels;
    private final String bg_sky = "images/Background.png";
    private BufferedImage bgImage;
    private int bgHeight, bgWidth;
    private SpriteSheet sprites;
    private int screenHeight, screenWidth;
    private int xOffset, yOffset;

    public void setOffsets(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public Screen(SpriteSheet sprites, int screenHeight, int screenWidth) {
        screenPixels = new int[screenHeight * screenWidth];
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.sprites = sprites;
        try {
            bgImage = ImageIO.read(new File(bg_sky));
            bgWidth = bgImage.getWidth();
            bgHeight = bgImage.getHeight();
            bgPixels = new int[bgWidth * bgHeight];
            bgPixels = bgImage.getRGB(0, 0, bgWidth, bgHeight, null, 0, bgWidth);
        } catch (IOException ex) {
            Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void render(int xPos, int yPos, int spriteTile) {
        render(xPos, yPos, spriteTile, false, false);
    }

    /*
     * @params xPos = pixels from the left-most boundary of the map to whatever is being rendered
     *         yPos = pixels from the upper-most boundary of the map to whatever is being rendered
     *         spriteTile = which tile from the sprite sheet is being drawn
     *         flipHor = whether the tile should be flipped over the y-axis
     *         flipVert = whether the tile should be flipped over the x-axis
     */
    public void render(int xPos, int yPos, int spriteTile, boolean flipHor, boolean flipVert) {

        xPos -= xOffset;
        yPos -= yOffset;

        int xTile = spriteTile % 10;
        int yTile = spriteTile / 10;
        //number of pixels on the sprite sheet from (0, 0) to the first pixel of the tile being rendered 
        int pixelOffset = (yTile * sprites.width + xTile) * 16;
        //cycles through each pixel within one tile (which is 8 pixels by 8 pixels)
        for (int y = 0; y < 16; y++) {
            int ySheet = y;
            if (flipVert) {
                ySheet = 15 - y;
            }
            int yPixel = ySheet + yPos;
            if (yPixel < 0 || yPixel >= screenHeight) {
                continue;
            }
            for (int x = 0; x < 16; x++) {
                int xSheet = x;
                if (flipHor) {
                    xSheet = 15 - x;
                }
                int xPixel = xSheet + xPos;
                if (xPixel < 0 || xPixel >= screenWidth) {
                    continue;
                }
                //gives RGB color
                int pixelColor = (sprites.spritePixels[x + y * sprites.width + pixelOffset]);
                //only draw if alpha channel is 100% opacity (the first 8 bits equal to 11111111 or 0xff)
                if (((pixelColor >> 24) & 0xff) == 0xff) {
                    screenPixels[xPixel + yPixel * screenWidth] = pixelColor;
                } else if (spriteTile == 5) { //air
                    //paint background
                    if (yOffset + yPos + y < 16 * 17 + 2) { //17 blocks down from the zero coord plus 2 extra pixels so that the blocks don't align
                        screenPixels[xPixel + yPixel * screenWidth] = bgPixels[xPixel + yPixel * screenWidth];
                    } else {
                        //draws from the background sprite (the "(y + 16)" bit selectes the bg tile which is one row down)
                        screenPixels[xPixel + yPixel * screenWidth] = (sprites.spritePixels[x + (y + 16) * sprites.width]);
                    }
                }
            }
        }
    }
    /*
     * Draws a pixel from the background color at a specified coord
     * Used to be used to make mining animation 
     */
//    public void renderBGPixel(int xPos, int yPos) {
//
//        xPos -= xOffset;
//        yPos -= yOffset;
//        
//        if (yPos < 0 || yPos >= screenHeight)
//            return;
//        if (xPos < 0 || xPos >= screenWidth)
//            return;
//        //only draw if alpha channel is 100% opacity (the first 8 bits equal to 11111111 or 0xff)
//        if (yOffset + yPos < 16 * 17 + 2) { //17 blocks down plus 2 extra pixels so that the blocks don't align
//            screenPixels[xPos + yPos * screenWidth] = bgPixels[xPos + yPos * screenWidth];
//        } else {
//            screenPixels[xPos + yPos * screenWidth] = (sprites.spritePixels[sprites.width * 16]);
//        }
//    }
}

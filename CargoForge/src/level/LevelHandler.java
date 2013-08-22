package level;

import gfx.Screen;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import level.tiles.Tile;

/*
 * STORED IDEA:
 * somehow look at the sprites to the cardinal directions, draw part of the other sprite over top at the corners
 * to create the curved block effect
 */

/* 
 * @author Colin King
 * @date Jun 25, 2013
 */
public class LevelHandler {
    
    private final String levelLoc = "images/Simple Level3.png";
    
    /* height and width of the MAP */
    public int mapHeight,  mapWidth;
    /* height and width of the SCREEN */
    private int screenHeight, screenWidth;
    private BufferedImage image;
    
    /* stores the RGB value of each pixel from the level diagram image, each pixel color represents a tile */
    private byte[] tiles;
    
    private Screen screen;
    
    public LevelHandler(Screen screen, int screenHeight, int screenWidth) {
        try {
            image = ImageIO.read(new File(levelLoc));
            mapHeight = image.getHeight();
            mapWidth = image.getWidth();
        } catch (IOException ex) {
            Logger.getLogger(LevelHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        tiles = new byte[mapWidth * mapHeight];
        //stores the RGB value of each pixel from the level drawing
        int[] levelPixelColors = image.getRGB(0, 0,  mapWidth, mapHeight, null, 0,  mapWidth);
        //translates the RGB pixel values of each tile into the index of the corresponding tile
        for(int i = 0; i < levelPixelColors.length; i++) {
            for(Tile t : Tile.tileTypes) {
                if(t.getLevelColor() == levelPixelColors[i]) {
                    tiles[i] = (byte) Tile.tileTypes.indexOf(t);
                }
            }
        }
        this.screen = screen;
    }
    /*
     * Renders tiles from the tiles[] array
     */
    public void renderTiles(int xOffset, int yOffset) {
        //keeps the window from moving out of bounds
        if(xOffset < 0) 
            xOffset = 0;
        if(xOffset > mapWidth * 16 - screenWidth)
            xOffset = mapWidth * 16 - screenWidth;
        if(yOffset > mapHeight * 16 - screenHeight)
            yOffset = mapHeight * 16 - screenHeight;
        screen.setOffsets(xOffset, yOffset);
        for(int y = (int) Math.floor((yOffset / 16.0)); y <= Math.ceil((yOffset + screenHeight) / 16.0); y++)
            for(int x = (int) Math.floor((xOffset / 16.0)); x <= Math.ceil(((xOffset + screenWidth) / 16.0)); x++)
                //gets a reference to a tile matching the index stored in the tiles[] array. then renders it
                getTile(x, y).render(screen, x * 16, y * 16);
    }
    
    public Tile getTile(int x, int y) {
        if(y < 0)
            return Tile.AIR;
        else if(x < 0 || x >= mapWidth || y >= mapHeight)
            return Tile.EMPTY;
        else
            return Tile.tileTypes.get(tiles[x + y * mapWidth]);
    }
    
    public void mineTile(int x, int y, int movingDir) {
        if(movingDir % 2 == 0) { //if mining horizontally, 
            x = (int)Math.floor((x + 8) / 16.0) - (movingDir - 3);
            y = (int)Math.floor((y + 3) / 16.0);
        }else {//if mining vertically
            x = (int)Math.floor((x + 8) / 16.0);
            y = (int)Math.floor((y + 3) / 16.0) + (movingDir - 2);
        }
        tiles[x + y * mapWidth] = (byte) Tile.tileTypes.indexOf(Tile.AIR);
    }
}

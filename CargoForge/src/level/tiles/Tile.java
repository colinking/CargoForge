package level.tiles;

import gfx.Screen;
import java.util.ArrayList;

/* 
 * @author Colin King
 * @date Jun 25, 2013
 */
public class Tile {
    
    /* AL that holds references to each tile type*/
    public static ArrayList<Tile> tileTypes = new ArrayList<>(7);
    
    /* game tiles */
    public static final Tile AIR = new Tile(5, 0xFFFFFFFF, false, "AIR");
    public static final Tile DIRT = new Tile(1, 0xFF888800, true, "DIRT", 10);
    public static final Tile STONE = new Tile(2, 0xFFAAAAAA, true, "STONE", 15);
    public static final Tile COAL = new Tile(3, 0xFF000000, true, "COAL", 20);
    public static final Tile DIAMOND = new Tile(4, 0xFF0055FF, true, "DIAMOND", 10);
    public static final Tile GRASS = new Tile(0, 0xFF00FF00, true, "GRASS", 10);
    public static final Tile EMPTY = new Tile(79, 0x00000000, true, "EMPTY"); //all tiles outside of the level
    
    protected int spriteTileIndex;
    protected int levelColor;
    protected boolean isSolid;
    protected int mineSpeed;
    protected String tileName;

    @Override
    public String toString() {
        return tileName;
    }
    
    public int getMineSpeed() {
        return mineSpeed;
    }
    
    public int getLevelColor() {
        return levelColor;
    }

    public boolean isSolid() {
        return isSolid;
    }
    
    /*
     * @params spriteTileIndex = index on the sprite sheet for the sprite that this tile uses
     *         levelColor = what color represents this tile in a level drawing
     */
    private Tile(int spriteTileIndex, int levelColor, boolean isSolid, String tileName, int mineSpeed) {
        this.spriteTileIndex = spriteTileIndex;
        this.levelColor = levelColor;
        this.isSolid = isSolid;
        this.tileName = tileName;
        this.mineSpeed = mineSpeed;
        tileTypes.add(this);
    }
    
    private Tile(int spriteTileIndex, int levelColor, boolean isSolid, String tileName) {
        this(spriteTileIndex, levelColor, isSolid, tileName, 10000);
    }
    public void render(Screen screen, int x, int y) {
        screen.render(x, y, spriteTileIndex);
    }
}

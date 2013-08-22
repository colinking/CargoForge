package entities;

import gfx.Screen;
import level.InputHandler;
import level.LevelHandler;
import level.tiles.Tile;

/* 
 * @author Colin King
 * @date Jun 28, 2013
 * 
 * bugs--:
 * fix background -> the underground BG isn't painted according to the map, so it can seem odd
 * mining while falling
 * moving in a direction, after mining a block won't mine other blocks
 * delete parts of a block as it is mined -- each 1/16 of the minespeed, one line of pixels disappears
 */
public class Player {

    private LevelHandler level;
    private InputHandler input;
    private int x, y;
    private int movingDir;
    private static final int tile = 40;
    private int stepCount;
    private int step; //which miner to draw 
    private boolean isFlying, movingDown;
    private static int min = 3, max = 12; //bounding box
    public int mineStep; //how many ticks the miner has been mining a block for
    private int lastMovDir;
    private final int mineDelay = 5; //delay before mining
    private int mineSpeed;
    public Tile miningTile; //the tile being mined
    private boolean didShake = false;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Player(InputHandler input, LevelHandler level) {
        this.level = level;
        this.input = input;
        x = 152;
        y = 200;
        movingDir = 2; //right
        mineSpeed = 1;
    }

    /*
     * 
     * @params xa = x acceleration
     *         ya = y acceleration
     */
    public void move(int xa, int ya) {
        //the order that it sets direction is important, 
        //allows the sprite to turn while falling, forces upward sprite when going up
        if (ya > 0) {
            movingDir = 3;
        }
        if (xa < 0) {
            movingDir = 4;
        }
        if (xa > 0) {
            movingDir = 2;
        }
        if (ya < 0) {
            movingDir = 1;
        }
//        if(hasCollidedX(1) && hasCollidedX(-1)) {
//            System.out.println("STUCK -- X");
//            x++;
//        }
//        if(hasCollidedY(1) && hasCollidedY(-1)) {
//            System.out.println("STUCK -- Y");
//            y++;
//        }
        mine(xa, ya);
        if (!isFlying && ya == 0) {
            ya++;
        }
        if (!hasCollidedX(xa)) {
            x += xa;
        }
        if (!hasCollidedY(ya)) {
            y += ya;
        }
    }

    public boolean hasCollidedX(int xa) {
        for (int x = min; x < max; x++) {
            if (isSolidTile(xa, 0, x, min) || isSolidTile(xa, 0, x, max)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCollidedY(int ya) {
        for (int y = min; y < max; y++) {
            if (isSolidTile(0, ya, min, y) || isSolidTile(0, ya, max, y)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSolidTile(int xa, int ya, int x, int y) {
        Tile nextTile = level.getTile((int) Math.floor((this.x + x + 2 * xa) / 16.0), (int) Math.floor((this.y + y + 2 * ya) / 16.0));
//        Tile lastTile = level.getTile((int)Math.floor((this.x + x) / 16.0), (int)Math.floor((this.y + y) / 16.0));
//        if(nextTile.isSolid())
//            return true;
//        return false;
        return nextTile.isSolid();
    }

    private void mine(int xa, int ya) {
        if (movingDir == lastMovDir) {
            switch (movingDir) {
                case 1:
                    if (hasCollidedY(-1) && input.UP.isPressed()) {
                        mineStep++;
                    } else {
                        mineStep = 0;
                    }
                    break;
                case 2:
                    if (hasCollidedX(1) && input.RIGHT.isPressed()) {
                        mineStep++;
                    } else {
                        mineStep = 0;
                    }
                    break;
                case 3:
                    if (hasCollidedY(1) && input.DOWN.isPressed()) {
                        mineStep++;
                    } else {
                        mineStep = 0;
                    }
                    break;
                case 4:
                    if (hasCollidedX(-1) && input.LEFT.isPressed()) {
                        mineStep++;
                    } else {
                        mineStep = 0;
                    }
                    break;
            }
            //find nearest block
            if (mineStep == mineDelay) {
                //calculates mining direction
                if (movingDir % 2 == 0) //if mining horizontally, 
                {
                    miningTile = level.getTile((int) Math.floor((x + 8) / 16.0) - (movingDir - 3), (int) Math.floor((y + 3) / 16.0));
                } else //if mining vertically
                {
                    miningTile = level.getTile((int) Math.floor((x + 8) / 16.0), (int) Math.floor((y + 3) / 16.0) + (movingDir - 2));
                }
//                System.out.println(miningTile);
            } else if (mineStep > mineDelay) {
                //moves miner over when mining up and down, also shakes the screen when going any direction :D
                int toMove;
                switch (movingDir) {
                    case 1:
                    case 3: //moves up or down depending on distance from center of tile
                        toMove = x / 16 * 16;
                        if (toMove + 8 < x) {
                            x++;
                        } else {
                            x--;
                        }
                        break;
                    case 2:
                    case 4: //alternates moving up or down
                        if (didShake) {
                            didShake = false;
                            y--;
                        } else {
                            didShake = true;
                            y++;
                        }
                }
                if (mineStep >= mineDelay + miningTile.getMineSpeed()) {
//                    System.out.println("Mined (Speed: " + miningTile.getMineSpeed() + ")");
                    level.mineTile(x, y, movingDir);
                    mineStep = 0;
                    miningTile = null;
                }
            }
        } else {
            mineStep = 0;
        }
    }
/*
 * Scratched because it didn't look quite right, wanted to keep just in case
 */
//    public void drawMineAnimation(Screen screen) {
//        if (miningTile == null) {
//            return;
//        }
//
//        int portionToMine = (int) Math.ceil((double) (mineStep - mineDelay) / miningTile.getMineSpeed() * 16);
//        int xToDraw = x / 16 * 16;
//        int yToDraw = y / 16 * 16;
//        switch (movingDir) {
//            case 1:
//                break;
//            case 2:
//                for (int xPixel = 0; xPixel < portionToMine; xPixel++) {
//                    for (int yPixel = 0; yPixel < 16; yPixel++) {
//                        screen.renderBGPixel(xToDraw + 16 + xPixel, yToDraw + yPixel);
//                    }
//                }
//                break;
//            case 3:
//                break;
//            case 4:
//                break;
//        }
//    }

    public void tick() {
        lastMovDir = movingDir;
//        System.out.println(x + " " + y);
        //handles switching sprites to make drill + tread moving effect
        stepCount++;
        //every 5 ticks, rotates sprites
        if (stepCount > 5) {
            stepCount = 0;
            if (step == 2) {
                step = 0;
            } else {
                step++;
            }
        }
        int xa = 0, ya = 0;
        if (input.UP.isPressed()) {
            ya--;
            isFlying = true;
        } else {
            isFlying = false;
        }
        if (input.DOWN.isPressed()) {
            ya++;
            movingDown = true;
        } else {
            movingDown = false;
        }
        if (input.LEFT.isPressed()) {
            xa--;
        }
        if (input.RIGHT.isPressed()) {
            xa++;
        }
        move(xa, ya);
    }

    public void render(Screen screen) {
        //the +/-2 's make it look like the miner turns around the middle of the machine
        switch (movingDir) {
            case 1: //up
                screen.render(x, y - 2, tile + step, false, true);
                if (isFlying) {
                    screen.render(x, y + 16 - 2, 60 + step, false, false); //draws fire below the miner
                }
                break;
            case 2: //right
                screen.render(x + 2, y, tile + step + 10, false, true); // the +10 switches sprites to the sideways one
                break;
            case 3: //down
                screen.render(x, y + 2, tile + step, false, false);
                break;
            case 4: //left
                screen.render(x - 2, y, tile + step + 10, true, true);
                break;
        }
    }
}

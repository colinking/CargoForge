package level;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.CargoForge;

/* 
 * @author Colin King
 * @date Jun 28, 2013
 */
public class InputHandler implements KeyListener {

    public Key UP = new Key();
    public Key DOWN = new Key();
    public Key LEFT = new Key();
    public Key RIGHT = new Key();
    
    public InputHandler(CargoForge o) {
        o.addKeyListener(this);
    }
    
    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                UP.setPressed(true);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                DOWN.setPressed(true);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                LEFT.setPressed(true);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                RIGHT.setPressed(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                UP.setPressed(false);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                DOWN.setPressed(false);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                LEFT.setPressed(false);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                RIGHT.setPressed(false);
                break;
        }
    }
    
    
}

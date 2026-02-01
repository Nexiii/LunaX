package io.github.nx.LunaX.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener {

    private GameContainer gc;
    
    private boolean[] keys = new boolean[1024]; 
    private boolean[] keysLast = new boolean[1024];

    private boolean[] buttons = new boolean[5];
    private boolean[] buttonsLast = new boolean[5];

    private int mouseX, mouseY;
    private int scroll = 0;

    public Input(GameContainer gc) {
        this.gc = gc;
        mouseX = 0;
        mouseY = 0;
        scroll = 0;

        gc.getWindow().getCanvas().addKeyListener(this);
        gc.getWindow().getCanvas().addMouseListener(this);
        gc.getWindow().getCanvas().addMouseMotionListener(this);
    }

    public void update() {
        scroll = 0;
        for (int i = 0; i < keys.length; i++) {
            keysLast[i] = keys[i];
        }
        for (int i = 0; i < buttons.length; i++) {
            buttonsLast[i] = buttons[i];
        }
    }

    public boolean isKey(int keyCode) {
        if (keyCode < 0 || keyCode >= keys.length) return false;
        return keys[keyCode] && !keysLast[keyCode];
    }

    public boolean isKeyDown(int keyCode) {
        if (keyCode < 0 || keyCode >= keys.length) return false;
        return keys[keyCode];
    }

    public boolean isKeyUp(int keyCode) {
        if (keyCode < 0 || keyCode >= keys.length) return false;
        return !keys[keyCode] && keysLast[keyCode];
    }

    public boolean isButton(int button) {
        if (button < 0 || button >= buttons.length) return false;
        return buttons[button] && !buttonsLast[button];
    }

    public boolean isButtonDown(int button) {
        if (button < 0 || button >= buttons.length) return false;
        return buttons[button];
    }

    public boolean isButtonUp(int button) {
        if (button < 0 || button >= buttons.length) return false;
        return !buttons[button] && buttonsLast[button];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code >= 0 && code < keys.length) {
            keys[code] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code >= 0 && code < keys.length) {
            keys[code] = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = (int)(e.getX() / gc.getScale());
        mouseY = (int)(e.getY() / gc.getScale());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = (int)(e.getX() / gc.getScale());
        mouseY = (int)(e.getY() / gc.getScale());
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton();
        if (button >= 0 && button < buttons.length) {
            buttons[button] = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int button = e.getButton();
        if (button >= 0 && button < buttons.length) {
            buttons[button] = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public int getMouseX() { return mouseX; }
    public int getMouseY() { return mouseY; }
    public int getScroll() { return scroll; }
}
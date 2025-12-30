package io.github.nx.exampleGame;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import io.github.nx.LunaX.engine.GameContainer;
import io.github.nx.LunaX.engine.Renderer;
import io.github.nx.LunaX.engine.gfx.Image;

public class Level {

    private Image levelImage;
    private BufferedImage colData;
    private int width, height;

    public Level(String path) {
        try {
            colData = ImageIO.read(getClass().getResourceAsStream(path));
            width = colData.getWidth();
            height = colData.getHeight();
            
            levelImage = new Image(colData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render(GameContainer gc, Renderer r, Camera cam) {
        if (levelImage == null) return;

        int camX = (int) cam.getX();
        int camY = (int) cam.getY();
        
        int screenW = gc.getWidth();
        int screenH = gc.getHeight();

        int srcX = camX;
        int srcY = camY;
        
        int drawW = screenW;
        int drawH = screenH;

        int destX = 0;
        int destY = 0;

        if (srcX < 0) { 
            destX = -srcX;
            drawW += srcX;
            srcX = 0;
        }
        if (srcY < 0) {
            destY = -srcY;
            drawH += srcY;
            srcY = 0;
        }

        if (srcX + drawW > width) {
            drawW = width - srcX;
        }
        if (srcY + drawH > height) {
            drawH = height - srcY;
        }

        if (drawW <= 0 || drawH <= 0) return;

        r.drawImagePart(levelImage, destX, destY, drawW, drawH, srcX, srcY);
    }
    
    public boolean isSolid(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return false;
        int alpha = (colData.getRGB(x, y) >> 24) & 0xFF;
        return alpha > 10;
    }
}
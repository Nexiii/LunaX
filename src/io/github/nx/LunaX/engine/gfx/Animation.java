package io.github.nx.LunaX.engine.gfx;

import java.awt.image.BufferedImage;

public class Animation {

    private Image[] frames;
    private int currentFrame = 0;
    
    private float timer = 0;
    private float delay;
    private boolean loop = true;
    private boolean isPlaying = true;

    public Animation(Image sheet, int tileW, int tileH, float speed) {
        this.delay = speed;
        
        BufferedImage bImg = sheet.getBufferedImage();
        
        int cols = bImg.getWidth() / tileW;
        frames = new Image[cols];

        for (int i = 0; i < cols; i++) {
            BufferedImage subImage = bImg.getSubimage(i * tileW, 0, tileW, tileH);
            frames[i] = new Image(subImage);
        }
    }
    
    public Animation(Image[] frames, float speed) {
        this.frames = frames;
        this.delay = speed;
    }

    public void update(float dt) {
        if (!isPlaying) return;

        timer += dt;

        if (timer >= delay) {
            timer = 0;
            currentFrame++;

            if (currentFrame >= frames.length) {
                if (loop) {
                    currentFrame = 0;
                } else {
                    currentFrame = frames.length - 1;
                    isPlaying = false;
                }
            }
        }
    }

    public Image getCurrentFrame() {
        return frames[currentFrame];
    }
    
    public void play() { isPlaying = true; }
    public void stop() { isPlaying = false; currentFrame = 0; }
    public void setLoop(boolean loop) { this.loop = loop; }
    public boolean isFinished() { return !loop && currentFrame == frames.length - 1; }
}
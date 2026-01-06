package io.github.nx.exampleGame;

import io.github.nx.LunaX.engine.Renderer;
import io.github.nx.LunaX.engine.gfx.Image;

public class Enemy {
    
    public float x, y;
    private float startX;
    public int width = 32;
    public int height = 32;
    private float speed = 50;
    public boolean remove = false;

    private int type;

	private Image img;      
    private float shootTimer = 0; 

    public Enemy(float x, float y, Image img, int type) {
        this.x = x;
        this.startX = x;
        this.y = y;
        this.img = img;
        this.type = type;

        if (img != null) {
            this.width = img.getWidth();
            this.height = img.getHeight();
        }

        if (type == 1) this.speed = 70;
        if (type == 2) this.speed = 70;
    }

    public void update(float dt, SpaceGame game) {
        y += speed * dt;

        if (type == 1) {
            x = startX + (float) Math.sin(y * 0.02f) * 50;
        }

        if (type == 2) {
            Player p = game.getPlayer();
            
            float chaseSpeed = 100f;
            
            if (p.x > this.x) {
                this.x += chaseSpeed * dt;
            }
            else if (p.x < this.x) {
                this.x -= chaseSpeed * dt;
            }
            shootTimer += dt;
            if (shootTimer > .5f) {
                game.spawnEnemyBullet(x + width/2 - 3, y + height);
                shootTimer = 0;
            }
        }

        if (y > 450) remove = true;
    }

    public void render(Renderer r) {
        if (img != null) {
            r.drawImage(img, (int) x, (int) y, false);
        } else {
            int color = 0xff00ff00; 
            if (type == 1) color = 0xffff0000; 
            if (type == 2) color = 0xffffff00; 
            
            r.drawRectFill((int) x, (int) y, width, height, color);
        }
    }
    
    public int getType() {
		return type;
	}
    
    public int[] getPixels() {
        if (img != null) return img.getPixels();
        return new int[0]; // Fallback
    }
}
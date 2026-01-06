package io.github.nx.exampleGame;

import io.github.nx.LunaX.engine.Renderer;

public class EnemyBullet {
    public float x, y;
    public int width = 6;
    public int height = 12;
    private float speed = 250;
    public boolean remove = false;

    public EnemyBullet(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update(float dt) {
        y += speed * dt;
        if (y > 350) remove = true;
    }

    public void render(Renderer r) {
        r.drawRectFill((int)x, (int)y, width, height, 0xffff0000); 
    }
}
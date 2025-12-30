package io.github.nx.exampleGame;

import io.github.nx.LunaX.engine.GameContainer;
import io.github.nx.exampleGame.Entities.Player;

public class Camera {

    private float x, y;
    private Player target;
    private float smoothSpeed = 0.05f;

    public Camera(Player target, GameContainer gc) {
        this.target = target;

        this.x = (target.getX() + 25) - (gc.getWidth() / 2.0f);
        this.y = (target.getY() + 18) - (gc.getHeight() / 2.0f);
    }

    public void update(GameContainer gc) {
        float targetX = (target.getX() + 25) - (gc.getWidth() / 2.0f);
        float targetY = (target.getY() + 18) - (gc.getHeight() / 2.0f);

        x += (targetX - x) * smoothSpeed;
        y += (targetY - y) * smoothSpeed;
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
package io.github.nx.exampleGame;

import io.github.nx.LunaX.engine.Renderer;

public class Bullet {
	public float x, y;
	public int width = 4;
	public int height = 10;
	private float speed = 300;
	public boolean remove = false;

	public Bullet(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void update(float dt) {
		y -= speed * dt;
		if (y < -20)
			remove = true;
	}

	public void render(Renderer r) {
		r.drawRectFill((int) x, (int) y, width, height, 0xffffdd00);
	}
}
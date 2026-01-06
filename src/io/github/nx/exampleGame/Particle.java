package io.github.nx.exampleGame;

import io.github.nx.LunaX.engine.Renderer;

public class Particle {
	public float x, y;
	private float dx, dy;
	private float life = 1.0f;
	private int color;

	public Particle(float x, float y, int color) {
		this.x = x;
		this.y = y;
		this.color = color;

		this.dx = (float) (Math.random() * 200 - 100);
		this.dy = (float) (Math.random() * 200 - 100);

		this.life = (float) (Math.random() * 0.5f + 0.5f);
	}

	public boolean update(float dt) {
		x += dx * dt;
		y += dy * dt;
		life -= dt * 2.0f;

		return life > 0;
	}

	public void render(Renderer r) {
		if (life > 0) {
			r.setPixel((int) x, (int) y, color);
		}
	}
}
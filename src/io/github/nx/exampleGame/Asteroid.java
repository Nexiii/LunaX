package io.github.nx.exampleGame;

import io.github.nx.LunaX.engine.Renderer;
import io.github.nx.LunaX.engine.gfx.Image;

public class Asteroid {
	public float x, y;
	public int width, height;
	public float speed;
	public boolean remove = false;

	private Image img;

	public Asteroid(float x, float y, Image img) {
		this.x = x;
		this.y = y;

		this.img = img;

		this.width = (int) (Math.random() * 20 + 30);
		this.height = this.width;

		this.speed = (float) (Math.random() * 100 + 50);
	}

	public void update(float dt) {
		y += speed * dt;

		if (y > 300)
			remove = true;
	}

	public void render(Renderer r) {
		if (img != null) {
			r.drawImage(img, (int) x, (int) y, false);
		} else {
			r.drawRectFill((int) x, (int) y, width, height, 0xff888888);
			r.drawRectFill((int) x + 5, (int) y + 5, width - 10, height - 10, 0xff666666);
		}
	}
}
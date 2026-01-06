package io.github.nx.exampleGame;

import java.awt.event.KeyEvent;
import io.github.nx.LunaX.engine.GameContainer;
import io.github.nx.LunaX.engine.Renderer;
import io.github.nx.LunaX.engine.gfx.Image;

public class Player {

	private Image player;

	private Image normal;
	private Image gold;
	private Image platin;

	public float x, y;
	public int width = 34;
	public int height = 34;
	private float speed = 200;

	private float shootCooldown = 0.3f;

	private float shootTimer = 0;

	public Player(int startX, int startY, SpaceGame game) {
		normal = new Image("/sprites/spaceship.png");
		gold = new Image("/sprites/golden_spaceship.png");
		platin = new Image("/sprites/platin_spaceship.png");

		if (game.getHighscore() >= 750 && game.getHighscore() <= 1499) {
			player = gold;
			speed = 275;
		} else if (game.getHighscore() >= 1500) {
			player = platin;
			speed = 300;
		} else {
			player = normal;
			speed = 250;
		}

		this.x = startX;
		this.y = startY;
	}

	public void update(GameContainer gc, SpaceGame game, float dt) {
		if (gc.getInput().isKey(KeyEvent.VK_A) || gc.getInput().isKey(KeyEvent.VK_LEFT)) {
			x -= speed * dt;
		}
		if (gc.getInput().isKey(KeyEvent.VK_D) || gc.getInput().isKey(KeyEvent.VK_RIGHT)) {
			x += speed * dt;
		}

		if (x < 0)
			x = 0;
		if (x > gc.getWidth() - width)
			x = gc.getWidth() - width;

		if (game.getHighscore() >= 125) {
			shootCooldown = 0.15f;
		}

		shootTimer += dt;
		if (gc.getInput().isKey(KeyEvent.VK_SPACE) && shootTimer > shootCooldown) {
			if (game.getHighscore() >= 500) {
				game.spawnBullet(x, y);
				game.spawnBullet(x + width - 4, y);
			} else {
				game.spawnBullet(x + width / 2 - 2, y);
			}

			shootTimer = 0;
		}
	}

	public void render(Renderer r, SpaceGame game) {
		r.drawImage(player, (int) x, (int) y, false);
	}
	
	public Image getImage() {
		return player;
	}
}
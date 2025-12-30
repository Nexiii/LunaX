package io.github.nx.exampleGame.Entities;

import java.awt.event.KeyEvent;

import io.github.nx.LunaX.engine.GameContainer;
import io.github.nx.LunaX.engine.Input;
import io.github.nx.LunaX.engine.Renderer;
import io.github.nx.LunaX.engine.audio.SoundClip;
import io.github.nx.LunaX.engine.gfx.Animation;
import io.github.nx.LunaX.engine.gfx.Color;
import io.github.nx.LunaX.engine.gfx.Font;
import io.github.nx.LunaX.engine.gfx.Image;
import io.github.nx.LunaX.engine.serialization.Storage;
import io.github.nx.exampleGame.Camera;

public class Player extends Entity {

	private int deaths;
	private int sheetW = 50;
	private int sheetH = 37;
	private boolean flipX = false;

	private float exactX;
	private float exactY;
	
	private int defaultX, defaultY;
	
	private SoundClip deathClip;
	
	private Animation currentAnim;
	private Animation idleAnim;
	private Animation runAnim;
	
	private Camera camera;

	public Player(GameContainer gc) {
		
		idleAnim = new Animation(new Image("/sheets/player_idle.png"), sheetW, sheetH, 0.25f);
		runAnim = new Animation(new Image("/sheets/player_run.png"), sheetW, sheetH, 0.15f);

		currentAnim = idleAnim;
		
		deathClip = new SoundClip("/sounds/sound.wav");
		deathClip.setVolume(0.5f);
		
		defaultX = 243;
		defaultY = 283;

		setX(Storage.read("player_x", defaultX));
		setY(Storage.read("player_y", defaultY));
		setHealth(Storage.read("player_health", 100));
		this.deaths = Storage.read("player_deaths", 0);
		setSpeed(2);
		
		this.camera = new Camera(this, gc);
	}

	@Override
    public void render(GameContainer gc, Renderer r) {
        int camX = (int) camera.getX();
        int camY = (int) camera.getY();

        r.drawImage(currentAnim.getCurrentFrame(), x - camX, y - camY, flipX);

        r.drawString("Health: " + health, x - camX, (y - 20) - camY, Color.RGB(255, 0, 0), Font.ARIAL);
        r.drawString("Deaths: " + deaths, x - camX, (y - 8) - camY, Color.RGB(255, 0, 0), Font.ARIAL);
    }

	public void update(GameContainer gc, Input input, float dt) {
		currentAnim.update(dt);

		if (health <= 0) {
			deathClip.play();
			Reset(gc);
		}

		float moveX = 0;
		float moveY = 0;

		if (input.isKey(KeyEvent.VK_W)) moveY -= 1;
		if (input.isKey(KeyEvent.VK_S)) moveY += 1;

		if (input.isKey(KeyEvent.VK_A)) {
			moveX -= 1;
			flipX = true;
		}
		if (input.isKey(KeyEvent.VK_D)) {
			moveX += 1;
			flipX = false;
		}

		if (moveX != 0 || moveY != 0) {
			if (currentAnim != runAnim) {
				currentAnim = runAnim;
			}

			float length = (float) Math.sqrt(moveX * moveX + moveY * moveY);
			moveX /= length;
			moveY /= length;

			moveX *= speed;
			moveY *= speed;

			exactX += moveX;
			exactY += moveY;

			x = Math.round(exactX);
			y = Math.round(exactY);
		} else {
			if (currentAnim != idleAnim) {
				currentAnim = idleAnim;
			}
		}

		if (input.isKeyDown(KeyEvent.VK_MINUS)) {
			if (health >= 1)
				health -= 5;
		}

		if (input.isKeyDown(KeyEvent.VK_SHIFT)) {
			speed = 3;
		} else if (input.isKeyUp(KeyEvent.VK_SHIFT)) {
			speed = 2;
		}
		
		camera.update(gc);
		saveData();
	}

	public void Reset(GameContainer gc) {
		setX(defaultX);
		setY(defaultY);
		setHealth(100);

		deaths++;
		saveData();
	}

	public void saveData() {
		Storage.add("player_x", x);
		Storage.add("player_y", y);
		Storage.add("player_health", health);
		Storage.add("player_deaths", deaths);
		
		Storage.save();
	}

	public Camera getCamera() { return camera; }
	
	public int getX() { return x; }
	public void setX(int x) { this.x = x; this.exactX = x; }

	public int getY() { return y; }
	public void setY(int y) { this.y = y; this.exactY = y; }
	
	public int getHealth() { return health; }
	public void setHealth(int health) { this.health = health; }
	
	public float getSpeed() { return speed; }
	public void setSpeed(float speed) { this.speed = speed; }
	
	public int getDeaths() { return deaths; }
	public void setDeaths(int deaths) { this.deaths = deaths; }
}
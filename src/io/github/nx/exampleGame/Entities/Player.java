package io.github.nx.exampleGame.Entities;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import io.github.nx.LunaX.engine.GameContainer;
import io.github.nx.LunaX.engine.Input;
import io.github.nx.LunaX.engine.Renderer;
import io.github.nx.LunaX.engine.gfx.Color;
import io.github.nx.LunaX.engine.gfx.Image;
import io.github.nx.LunaX.engine.serialization.SaveManager;

public class Player extends Entity {

	private int deaths;
	private Image img;
	public SaveManager saveMan;

	public Player(GameContainer gc) {
		img = new Image("/sprites/player.png");
		saveMan = new SaveManager(gc);
		// Setting player to the middle of the Screen \\
		String directory = System.getProperty("user.dir")+"/saves/";
		if(new File(directory+"/player/").exists()) {
			try {
				setX(saveMan.loadInt("player", "xPos"));
				setY(saveMan.loadInt("player", "yPos"));
				setHealth(saveMan.loadInt("player", "health"));
				setDeaths(saveMan.loadInt("player", "deaths"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			setX((gc.getWidth() / 2) - (img.getWidth() / 2));
			setY((gc.getHeight() / 2) - (img.getHeight() / 2));
			setHealth(100);
		}
		setSpeed(2);
	}

	public void render(GameContainer gc, Renderer r) {
		r.drawImage(img, x, y);
		r.drawString("Health: " + health, x-img.getWidth(), y-37, Color.RGB(255, 0, 0));
		r.drawString("Deaths: " + deaths, x-img.getWidth(), y-25, Color.RGB(255, 0, 0));
	}

	public void update(GameContainer gc, Input input, float dt) {
		saveMan.saveInt("player", "xPos", getX());
		saveMan.saveInt("player", "yPos", getY());
		saveMan.saveInt("player", "health", getHealth());
		saveMan.saveInt("player", "deaths", getDeaths());
		
		if(health == 0) {
			Reset(gc);
		}
		
		if (input.isKey(KeyEvent.VK_W)) {
			y = (int) (y - speed);
		}
		if (input.isKey(KeyEvent.VK_S)) {
			y = (int) (y + speed);
		}

		if (input.isKey(KeyEvent.VK_A)) {
			x = (int) (x - speed);
		}
		if (input.isKey(KeyEvent.VK_D)) {
			x = (int) (x + speed);
		}

		if(input.isKeyDown(KeyEvent.VK_MINUS)) {
			if(health >= 1) {
				health -= 5;
			}
		}
		
		if (input.isKeyDown(KeyEvent.VK_SHIFT)) {
			speed = 3;
		} else if (input.isKeyUp(KeyEvent.VK_SHIFT)) {
			speed = 2;
		}
	}

	public void Reset(GameContainer gc) {
		setX((gc.getWidth() / 2) - (img.getWidth() / 2));
		setY((gc.getHeight() / 2) - (img.getHeight() / 2));
		setHealth(100);
		deaths++;
	}
	
	// GETTERS & SETTERS
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
}

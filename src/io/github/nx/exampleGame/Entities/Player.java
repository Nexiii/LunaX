package io.github.nx.exampleGame.Entities;

import java.awt.event.KeyEvent;

import io.github.nx.LunaX.engine.GameContainer;
import io.github.nx.LunaX.engine.Input;
import io.github.nx.LunaX.engine.Renderer;
import io.github.nx.LunaX.engine.gfx.Image;

public class Player{

	private int health;
	private float speed;
	private int x,y;
	private Image img;

	public Player(GameContainer gc) {
		img = new Image("/sprites/point.png");
		setX((gc.getWidth()/2)-(img.getWidth()/2));
		setY((gc.getHeight()/2)-(img.getHeight()/2));
		setHealth(100);
		setSpeed(2);
	}

	public void render(GameContainer gc, Renderer r) {
		r.drawImage(img, x, y);
	}
	
	public void update(GameContainer gc, Input input, float dt) {
		
		if(input.isKey(KeyEvent.VK_W)) {
			y=(int) (y-speed);
		}
		if(input.isKey(KeyEvent.VK_S)) {
			y=(int) (y+speed);
		}
		
		if(input.isKey(KeyEvent.VK_A)) {
			x=(int) (x-speed);
		}
		if(input.isKey(KeyEvent.VK_D)) {
			x=(int) (x+speed);
		}
		
		if(input.isKeyDown(KeyEvent.VK_SHIFT)) {
			speed = 3;
		} else if(input.isKeyUp(KeyEvent.VK_SHIFT)) {
			speed = 2;
		}
	}
	
	
	//GETTERS & SETTERS
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
}

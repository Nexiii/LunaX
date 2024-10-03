package io.github.nx.exampleGame;

import io.github.nx.LunaX.engine.AbstractGame;
import io.github.nx.LunaX.engine.GameContainer;
import io.github.nx.LunaX.engine.Renderer;
import io.github.nx.LunaX.engine.gfx.Color;
import io.github.nx.LunaX.engine.gfx.Image;
import io.github.nx.exampleGame.Entities.Player;

public class GameManager extends AbstractGame {

	public GameManager() { }
	
	public Player player;
	
	@Override
	public void init(GameContainer gc) {
		player = new Player(gc);
		
	}

	@Override
	public void update(GameContainer gc, float dt) {
		player.update(gc, gc.getInput(), dt);
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		player.render(gc, r);
		r.drawString("WASD to Move | Shift to Sprint", 0, 26, Color.RGB(255, 255, 255));
		if(gc.getInput().isButton(1)) {
			Image img = new Image("/sprites/point.png");
			r.drawImage(img, gc.getInput().getMouseX()-(img.getWidth()/2), gc.getInput().getMouseY()-(img.getHeight()/2));
		}
	}
	
	public static void main(String[] args) {
		GameContainer gc = new GameContainer(new GameManager());
		gc.setWidth(640);
		gc.setHeight(480);
		gc.setScale(1.5f);
		gc.setIcoPath("/icons/game.png");
		gc.setTitle("LunaX | Example Game");
		gc.start();
	}
}

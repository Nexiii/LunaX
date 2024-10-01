package io.nx.LunaX.game;

import io.nx.LunaX.engine.AbstractGame;
import io.nx.LunaX.engine.GameContainer;
import io.nx.LunaX.engine.Renderer;
import io.nx.LunaX.engine.gfx.Image;

public class GameManager extends AbstractGame {

	public GameManager() { }
	
	@Override
	public void init(GameContainer gc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		Image player = new Image("/sprites/player.png");
		r.drawImage(player, gc.getWidth()/2, gc.getHeight()/2);
	}
	
	public static void main(String[] args) {
		GameContainer gc = new GameContainer(new GameManager());
		gc.setWidth(640);
		gc.setHeight(480);
		gc.setScale(1.5f);
		gc.setIcoPath("/icons/game.png");
		gc.setTitle("LunaX | Example");
		gc.start();
	}
}

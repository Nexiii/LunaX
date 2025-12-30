package io.github.nx.exampleGame;

import java.text.DecimalFormat;

import io.github.nx.LunaX.engine.AbstractGame;
import io.github.nx.LunaX.engine.GameContainer;
import io.github.nx.LunaX.engine.Input;
import io.github.nx.LunaX.engine.Renderer;
import io.github.nx.LunaX.engine.audio.SoundClip;
import io.github.nx.LunaX.engine.gfx.Color;
import io.github.nx.LunaX.engine.gfx.Font;
import io.github.nx.LunaX.engine.gfx.Image;
import io.github.nx.LunaX.engine.serialization.Storage;
import io.github.nx.exampleGame.Entities.Player;

public class GameManager extends AbstractGame {

	public Player player;
	public Level lvl1;
	SoundClip clip;

	public GameManager() { }

	@Override
	public void init(GameContainer gc) {
		lvl1 = new Level("/levels/level1.png");
		player = new Player(gc);
		clip = new SoundClip("/sounds/jump.wav");
		clip.setVolume(0.5f);
	}

	@Override
	public void update(GameContainer gc, float dt) {		
		player.update(gc, gc.getInput(), dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.clear();
		
		lvl1.render(gc, r, player.getCamera());
		player.render(gc, r);
		Input input = gc.getInput();
		
		float fps = gc.getFps();
		DecimalFormat df = new DecimalFormat("#.###");
		float ms = 0;
		if(gc.getFps() != 0) { ms = (1*1000)/fps; }
		String formMS = df.format(ms);
		
		
		r.drawString("FPS: " + gc.getFps() + " | " + formMS+"ms" , 0, 0, Color.RGB(191, 0, 255), Font.ARIAL);
		
		r.drawString("MouseX: "+ input.getMouseX() + " MouseY: "+ input.getMouseY() + " | X:" + player.getX() + " Y: " + player.getY(), 0, 13, Color.RGB(191, 0, 255), Font.ARIAL);
		
		r.drawString("WASD to Move | Shift to Sprint | Mouse1 for a sound | - to take Damage", 0, 26, Color.RGB(191, 0, 255), Font.ARIAL);
		
		Image img = new Image("/sprites/point.png");
		r.drawImage(img, gc.getInput().getMouseX() - (img.getWidth() / 2), gc.getInput().getMouseY() - (img.getHeight() / 2), false);
		if (gc.getInput().isButton(1)) {
			if (!clip.isRunning()) {
				clip.play();
			}
		}
	}

	public static void main(String[] args) {
		Storage.init("save.bin");
		
		GameContainer gc = new GameContainer(new GameManager());
		gc.setWidth(640);
		gc.setHeight(360);
		gc.setScale(2f);
		gc.setIcoPath("/icons/LunaX.png");
		gc.setTitle("LunaX | Example Game");
		
		gc.start();
	}
}

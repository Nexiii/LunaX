package io.github.nx.exampleGame;

import java.text.DecimalFormat;

import io.github.nx.LunaX.engine.AbstractGame;
import io.github.nx.LunaX.engine.GameContainer;
import io.github.nx.LunaX.engine.Input;
import io.github.nx.LunaX.engine.Renderer;
import io.github.nx.LunaX.engine.audio.SoundClip;
import io.github.nx.LunaX.engine.gfx.Color;
import io.github.nx.LunaX.engine.gfx.Image;
import io.github.nx.LunaX.engine.serialization.SaveManager;
import io.github.nx.exampleGame.Entities.Player;

public class GameManager extends AbstractGame {

	public Player player;
	public SaveManager saveMan;
	SoundClip clip = new SoundClip("/sounds/jump.wav");

	public GameManager() {
		
	}

	@Override
	public void init(GameContainer gc) {
		player = new Player(gc);
		saveMan = new SaveManager(gc);
	}

	@Override
	public void update(GameContainer gc, float dt) {		
		player.update(gc, gc.getInput(), dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.clear();
		
		player.render(gc, r);
		Input input = gc.getInput();
		
		float fps = gc.getFps();
		DecimalFormat df = new DecimalFormat("#.###");
		float ms = 0;
		if(gc.getFps() != 0) { ms = (1*1000)/fps; }
		String formMS = df.format(ms);
		
		
		r.drawString("FPS: " + gc.getFps() + " | " + formMS+"ms" , 0, 0, Color.RGB(191, 0, 255));
		
		r.drawString("MouseX: "+ input.getMouseX()+" MouseY: "+input.getMouseY() + " | X:" + player.getX() + " Y: " +player.getY(), 0, 13, Color.RGB(191, 0, 255));
		
		r.drawString("WASD to Move | Shift to Sprint | Mouse1 for a sound | - to take Damage", 0, 26, Color.RGB(191, 0, 255));
		
		
		Image img = new Image("/sprites/point.png");
		r.drawImage(img, gc.getInput().getMouseX() - (img.getWidth() / 2), gc.getInput().getMouseY() - (img.getHeight() / 2));
		
		if (gc.getInput().isButton(1)) {
			clip.setVolume(0.5f);
			if (!clip.isRunning()) { //fixing that sounds isn't playing 1000 times a second
				clip.play();
			}
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

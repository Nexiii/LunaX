package io.github.nx.exampleGame.Entities;

import io.github.nx.LunaX.engine.GameContainer;
import io.github.nx.LunaX.engine.Input;
import io.github.nx.LunaX.engine.Renderer;

public abstract class Entity {

	public int x, y;
	public int health;
	public float speed;
	
	public abstract void render(GameContainer gc, Renderer r);
	
	public abstract void update(GameContainer gc, Input input, float dt);
	
}

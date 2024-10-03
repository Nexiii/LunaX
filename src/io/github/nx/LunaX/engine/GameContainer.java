package io.github.nx.LunaX.engine;

import io.github.nx.LunaX.engine.gfx.Color;

public class GameContainer implements Runnable {

	private Thread thread;
	private Window window;
	private Renderer renderer;
	private Input input;
	private AbstractGame game;

	private boolean running = false;
	private final double UPDATE_CAP = 1.0 / 60.0;

	private int defaultW = 320, defaultH = 240;
	private float defaultScl = 4f;
	private int width = defaultW, height = defaultH;
	private float scale = defaultScl;
	private String title = "LunaX | v.0.7A";
	private String IcoImagePath;

	// CONSTRUCTOR
	public GameContainer(AbstractGame game) {
		this.game = game;
	}

	// START
	public void start() {
		window = new Window(this);
		renderer = new Renderer(this);
		input = new Input(this);

		thread = new Thread(this);
		thread.run();
	}

	// STOP
	public void stop() {

	}

	// RUN
	public void run() {
		running = true;

		boolean render = false;
		double firstTime = 0;
		double lastTime = System.nanoTime() / 1000000000.0;
		double passedTime = 0;
		double unprocessedTime = 0;

		double frameTime = 0;
		int frames = 0;
		int fps = 0;

		game.init(this);

		while (running) {
			render = true;

			firstTime = System.nanoTime() / 1000000000.0;
			passedTime = firstTime - lastTime;
			lastTime = firstTime;

			unprocessedTime += passedTime;
			frameTime += passedTime;

			// Handles game updates
			while (unprocessedTime >= UPDATE_CAP) {
				unprocessedTime -= UPDATE_CAP;
				render = true;

				game.update(this, (float) UPDATE_CAP);
				input.update();

				if (frameTime >= 1.0) {
					frameTime = 0;
					fps = frames;
					frames = 0;
				}
			}

			if (render) {
				renderer.clear();
				game.render(this, renderer);
				renderer.process();
				renderer.setCamX(0);
				renderer.setCamY(0);
				renderer.drawString("FPS:" + fps, 0, 0, Color.RGB(255, 255, 255));
				renderer.drawString("MouseX: "+ input.getMouseX()+" MouseY: "+input.getMouseY(), 0, 13, Color.RGB(255, 255, 255));
				window.update();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		dispose();
	}

	// DISPOSE
	private void dispose() {

	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Window getWindow() {
		return window;
	}

	public Input getInput() {
		return input;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public String getIcoPath() {
		return IcoImagePath;
	}

	public void setIcoPath(String icoImagePath) {
		IcoImagePath = icoImagePath;
	}
}

package io.github.nx.LunaX.engine;

import java.awt.Canvas;

import javax.swing.JFrame;

public class GameContainer implements Runnable {

	private Thread thread;
	private Window window;
	private Renderer renderer;
	private Input input;
	private AbstractGame game;

	private boolean running = false;
	private final double UPDATE_CAP = 1.0 / 60.0;

	int fps;

	private int defaultW = 640, defaultH = 480;
	private float defaultScl = 1.5f;
	private int width = defaultW, height = defaultH;
	private float scale = defaultScl;
	private String version = "v.0.8.3A";
	private String title = "LunaX | " + version;
	private String IcoImagePath;

	public GameContainer(AbstractGame game) {
		this.game = game;
	}

	public void start() {
		window = new Window(this);
		renderer = new Renderer(this);
		input = new Input(this);

		thread = new Thread(this);
		thread.run();
	}

	public void stop() {

	}

	public void run() {
		running = true;

		boolean render = false;
		double firstTime = 0;
		double lastTime = System.nanoTime() / 1000000000.0;
		double passedTime = 0;
		double unprocessedTime = 0;

		double frameTime = 0;
		int frames = 0;
		fps = 0;

		game.init(this);

		while (running) {
			render = true;

			firstTime = System.nanoTime() / 1000000000.0;
			passedTime = firstTime - lastTime;
			lastTime = firstTime;

			unprocessedTime += passedTime;
			frameTime += passedTime;

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

	private void dispose() {

	}
	
	public void toggleFullscreen() {
		Canvas canvas = window.getCanvas();
		JFrame window = this.window.getFrame();
		window.remove(canvas);
	    window.dispose();
	    
	    boolean isFullscreen = window.isUndecorated();
	    if (!isFullscreen) {
	        window.setUndecorated(true);
	        window.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
	    } else {
	        window.setUndecorated(false);
	        window.setExtendedState(javax.swing.JFrame.NORMAL);
	        window.setSize((int)(width * scale), (int)(height * scale));
	        window.setLocationRelativeTo(null);
	    }
	    
	    window.add(canvas);
	    window.setVisible(true);
	    
	    canvas.createBufferStrategy(2);

	    canvas.requestFocus();
	}

	public String getVersion() {
		return version;
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

	public int getFps() {
		return fps;
	}
}

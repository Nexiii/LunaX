package io.github.nx.exampleGame;

import java.util.ArrayList;
import java.util.List;

import io.github.nx.LunaX.engine.GameContainer;
import io.github.nx.LunaX.engine.Renderer;
import io.github.nx.LunaX.engine.gfx.Font;

public class Console {

	private boolean open = false;
	private List<String> history = new ArrayList<>();

	private float debugTimer = 0;
	private String ramText = "MEMORY: 0MB";

	public Console(GameContainer gc) {
		log("LunaX " + gc.getVersion() + " started");
	}

	public void toggle() {
		open = !open;
	}

	public boolean isOpen() {
		return open;
	}

	public void log(String text) {
		history.add(text);
		if (history.size() > 15) {
			history.remove(0);
		}
	}

	public void update(GameContainer gc, float dt) {
		debugTimer += dt;
		if (debugTimer >= 1.0f) {
			debugTimer = 0;

			Runtime rt = Runtime.getRuntime();
			long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1048576L;

			ramText = "MEMORY: " + usedMB + "MB";
		}
	}

	public void render(Renderer r, GameContainer gc) {
		if (!open)
			return;

		int h = 200;
		int w = gc.getWidth();

		r.drawRectFill(0, 0, w, h, 0xaa000000);

		r.drawRect(0, h, w, 1, 0xffffffff);

		r.drawString("SYSTEM LOG", 5, 5, 0xff00ff00, Font.ARIAL, 1.0f);

		r.drawString(ramText, 5, 21, 0xff00ff00, Font.ARIAL, 1.0f);
		int y = h - 20;

		for (int i = history.size() - 1; i >= 0; i--) {
			r.drawString(history.get(i), 10, y, 0xffcccccc, Font.ARIAL, 1.0f);
			y -= 12;
		}
	}
}
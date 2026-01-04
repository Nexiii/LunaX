package io.github.nx.LunaX.engine.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Image {
	protected int w;
	protected int h;
	protected int[] p; // pixel data
	private boolean alpha = true;
	private int lightBlock = Light.NONE;

	public Image(String path) {
		BufferedImage image = null;

		try {
			var resource = Image.class.getResourceAsStream(path);
			if (resource == null) {
				throw new IOException("Bild nicht gefunden: " + path);
			}
			image = ImageIO.read(resource);
		} catch (IOException e) {
			e.printStackTrace();
			this.w = 0;
			this.h = 0;
			this.p = new int[0];
			return; 
		}
		this.w = image.getWidth();
		this.h = image.getHeight();
		this.p = image.getRGB(0, 0, w, h, null, 0, w);

		image.flush();
	}
	public Image(int[] p, int w, int h) {
		this.p = p;
		this.w = w;
		this.h = h;
	}
	public Image(BufferedImage image) {
		this.w = image.getWidth();
		this.h = image.getHeight();
		this.p = image.getRGB(0, 0, w, h, null, 0, w);
	}
	public BufferedImage getBufferedImage() {
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		bi.setRGB(0, 0, w, h, p, 0, w);
		return bi;
	}
	
	public int getWidth() {
		return w;
	}

	public void setWidth(int w) {
		this.w = w;
	}

	public int getHeight() {
		return h;
	}

	public void setHeight(int h) {
		this.h = h;
	}

	public int[] getPixels() {
		return p;
	}

	public void setPixels(int[] p) {
		this.p = p;
	}

	public boolean isAlpha() {
		return alpha;
	}

	public void setAlpha(boolean alpha) {
		this.alpha = alpha;
	}

	public int getLightBlock() {
		return lightBlock;
	}

	public void setLightBlock(int lightBlock) {
		this.lightBlock = lightBlock;
	}
}
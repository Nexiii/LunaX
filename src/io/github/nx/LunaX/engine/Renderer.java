package io.github.nx.LunaX.engine;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import io.github.nx.LunaX.engine.gfx.Color;
import io.github.nx.LunaX.engine.gfx.Font;
import io.github.nx.LunaX.engine.gfx.Image;
import io.github.nx.LunaX.engine.gfx.ImageRequest;
import io.github.nx.LunaX.engine.gfx.ImageTile;
import io.github.nx.LunaX.engine.gfx.Light;
import io.github.nx.LunaX.engine.gfx.LightRequest;

public class Renderer {

	private GameContainer gc;
	
	private ArrayList<ImageRequest> imageRequest = new ArrayList<ImageRequest>();
	private ArrayList<LightRequest> lightRequest = new ArrayList<LightRequest>();

	private int pW, pH;
	private int[] p; // pixels
	private int[] zBuffer;
	private int[] lightMap;
	private int[] lightBlock;

	private int clearColor = Color.RGB(0, 0, 0);
	
	private int ambientColor = Color.RGB(255, 255, 255);
	private int zDepth = 0;
	private boolean processing = false;

	private int offX = 0;
    private int offY = 0;

	public Renderer(GameContainer gc) {
		this.gc = gc;
		pW = gc.getWidth();
		pH = gc.getHeight();
		p = ((DataBufferInt) gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
		zBuffer = new int[p.length];
		lightMap = new int[p.length];
		lightBlock = new int[p.length];
	}

	public void clear() {
	    Arrays.fill(p, clearColor); 

	    for (int i = 0; i < p.length; i++) {
	        
	        zBuffer[i] = 0;
	        lightMap[i] = ambientColor;
	        lightBlock[i] = 0;
	    }
	}

	public void process() {
	    processing = true;

	    Collections.sort(imageRequest, new Comparator<ImageRequest>() {
	        @Override
	        public int compare(ImageRequest i0, ImageRequest i1) {
	            if (i0.zDepth < i1.zDepth) return -1;
	            if (i0.zDepth > i1.zDepth) return 1;
	            return 0;
	        }
	    });

	    for (int i = 0; i < imageRequest.size(); i++) {
	        ImageRequest ir = imageRequest.get(i);
	        setzDepth(ir.zDepth);
	        
	        drawImage(ir.image, ir.offX, ir.offY, ir.flipX); 
	    }

	    for (int i = 0; i < lightRequest.size(); i++) {
	        LightRequest lr = lightRequest.get(i);
	        drawLightRequest(lr.light, lr.locX, lr.locY);
	    }

	    for (int i = 0; i < p.length; i++) {
	        float red = ((lightMap[i] >> 16) & 0xff) / 255f;
	        float green = ((lightMap[i] >> 8) & 0xff) / 255f;
	        float blue = (lightMap[i] & 0xff) / 255f;

	        p[i] = ((int) (((p[i] >> 16) & 0xff) * red) << 16 | 
	                (int) (((p[i] >> 8) & 0xff) * green) << 8 | 
	                (int) ((p[i] & 0xff) * blue));
	    }

	    imageRequest.clear();
	    lightRequest.clear();
	    processing = false;
	}

	public void setPixel(int x, int y, int value) {
		int alpha = ((value >> 24) & 0xff); // alpha values go up to 255

		if ((x < 0 || x >= pW || y < 0 || y >= pH) || alpha == 0 || value == Color.RGB(255, 0, 255))
			return;

		int index = x + y * pW;

		if (zBuffer[index] > zDepth)
			return;

		zBuffer[index] = zDepth;
		if (alpha == 255) {
			p[index] = value;
		} else {
			// Alpha blending
			int pixelColor = p[index];

			int newRed = ((pixelColor >> 16) & 0xff)
					- (int) ((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
			int newGreen = ((pixelColor >> 8) & 0xff)
					- (int) ((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
			int newBlue = (pixelColor & 0xff0 - (int) (((pixelColor & 0xff) - (value & 0xff)) * (alpha / 255f)));

			p[index] = (newRed << 16 | newGreen << 8 | newBlue);
		}
	}

	public void setLightMap(int x, int y, int value) {
		if (x < 0 || x >= pW || y < 0 || y >= pH)
			return;

		int baseColor = lightMap[x + y * pW];

		int maxRed = Math.max((baseColor >> 16) & 0xff, (value >> 16) & 0xff);
		int maxGreen = Math.max((baseColor >> 8) & 0xff, (value >> 8) & 0xff);
		int maxBlue = Math.max(baseColor & 0xff, value & 0xff);

		lightMap[x + y * pW] = (maxRed << 16 | maxGreen << 8 | maxBlue);
	}

	public void setLightBlock(int x, int y, int value) {
		if (x < 0 || x >= pW || y < 0 || y >= pH)
			return;

		if (zBuffer[x + y * pW] > zDepth)
			return;

		lightBlock[x + y * pW] = value;
	}

	public void drawString(String text, int offX, int offY, int color, Font font, float scale) {
	    Image fontImage = font.getFontImage();
	    int[] fontPixels = fontImage.getPixels();
	    int fontW = fontImage.getWidth();
	    int fontH = fontImage.getHeight();
	    
	    int canvasWidth = gc.getWidth(); 

	    int textR = (color >> 16) & 0xff;
	    int textG = (color >> 8) & 0xff;
	    int textB = color & 0xff;

	    int offset = 0;

	    for (int i = 0; i < text.length(); i++) {
	        int unicode = text.codePointAt(i);
	        if (unicode < 0 || unicode >= font.getWidths().length) continue;

	        int charWidth = font.getWidths()[unicode];
	        int charOffset = font.getOffsets()[unicode];

	        int destHeight = (int) (fontH * scale);
	        int destWidth = (int) (charWidth * scale);

	        for (int y = 0; y < destHeight; y++) {
	            for (int x = 0; x < destWidth; x++) {
	                int srcX = (int) (x / scale);
	                int srcY = (int) (y / scale);
	                
	                if (srcX >= charWidth || srcY >= fontH) continue;

	                int fontPixel = fontPixels[(srcX + charOffset) + srcY * fontW];
	                int alpha = (fontPixel >> 24) & 0xff;

	                if (alpha == 0) continue;

	                int screenX = x + offX + offset;
	                int screenY = y + offY;

	                if (screenX < 0 || screenX >= canvasWidth || screenY < 0 || screenY >= gc.getHeight()) continue;

	                if (alpha == 255) {
	                    setPixel(screenX, screenY, color);
	                } else {
	                    int bgIndex = screenX + screenY * canvasWidth;
	                    int bgColor = p[bgIndex];

	                    int bgR = (bgColor >> 16) & 0xff;
	                    int bgG = (bgColor >> 8) & 0xff;
	                    int bgB = bgColor & 0xff;

	                    float a = alpha / 255.0f;
	                    float invA = 1.0f - a;

	                    int r = (int)(bgR * invA + textR * a);
	                    int g = (int)(bgG * invA + textG * a);
	                    int b = (int)(bgB * invA + textB * a);

	                    int finalPixel = (255 << 24) | (r << 16) | (g << 8) | b;
	                    
	                    p[bgIndex] = finalPixel; 
	                }
	            }
	        }
	        offset += destWidth;
	    }
	}

	public void drawImage(Image img, int offX, int offY, boolean flipX) {
	    if (offX < -img.getWidth() || offY < -img.getHeight() || offX >= pW || offY >= pH) {
	        return;
	    }

	    int newX = 0;
	    int newY = 0;
	    int newWidth = img.getWidth();
	    int newHeight = img.getHeight();

	    if (offX < 0) newX -= offX;
	    if (offY < 0) newY -= offY;
	    if (newWidth + offX >= pW) newWidth -= (newWidth + offX) - pW;
	    if (newHeight + offY >= pH) newHeight -= (newHeight + offY) - pH;

	    for (int y = newY; y < newHeight; y++) {
	        for (int x = newX; x < newWidth; x++) {
	        	int sourceX = flipX ? (img.getWidth() - 1 - x) : x;
	            
	            int colorValue = img.getPixels()[sourceX + y * img.getWidth()];

	            setPixel(x + offX, y + offY, colorValue);
	        }
	    }
	}

	public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY, boolean flipX) {
		offX -= this.offX;
		offY -= this.offY;

		if (image.isAlpha() && !processing) {
		    int id = 0;
			imageRequest.add(new ImageRequest(image.getTileImage(id), zDepth, offX, offY, flipX));
		    return;
		}

		if (offX < -image.getTileW())
			return;
		if (offY < -image.getTileH())
			return;
		if (offX >= pW)
			return;
		if (offY >= pH)
			return;

		int newX = 0;
		int newY = 0;
		int newWidth = image.getTileW();
		int newHeight = image.getTileH();

		if (offX < 0)
			newX -= offX;
		if (offY < 0)
			newY -= offY;
		if (newWidth + offX >= pW)
			newWidth -= newWidth + offX - pW;
		if (newHeight + offY >= pH)
			newHeight -= newHeight + offY - pH;

		for (int y = newY; y < newHeight; y++) {
			for (int x = newX; x < newWidth; x++) {
				setPixel(x + offX, y + offY,
						image.getPixels()[(x + tileX * image.getTileW()) + (y + tileY * image.getTileH()) * image.getWidth()]);
				setLightBlock(x + offX, y + offY, image.getLightBlock());
			}
		}
	}

	public void drawImagePart(Image image, int offX, int offY, int width, int height, int srcX, int srcY) {

	    if (offX >= pW || offY >= pH || offX + width <= 0 || offY + height <= 0) {
	        return;
	    }

	    int startX = 0;
	    int startY = 0;

	    if (offX < 0) {
	        startX = -offX;
	        width -= startX;
	        offX = 0;
	    }
	    
	    if (offY < 0) {
	        startY = -offY;
	        height -= startY;
	        offY = 0;
	    }

	    if (offX + width > pW) {
	        width = pW - offX;
	    }
	    
	    if (offY + height > pH) {
	        height = pH - offY;
	    }

	    if (width <= 0 || height <= 0) return;

	    int imgW = image.getWidth();
	    int[] srcPixels = image.getPixels();
	    int[] destPixels = p;

	    int srcIndex = (srcY + startY) * imgW + (srcX + startX);
	    
	    int destIndex = offY * pW + offX; 

	    int srcStep = imgW - width;
	    int destStep = pW - width;

	    for (int y = 0; y < height; y++) {
	        for (int x = 0; x < width; x++) {
	            
	            int color = srcPixels[srcIndex];

	            if ((color & 0xFF000000) != 0) {
	                destPixels[destIndex] = color;
	            }

	            srcIndex++;
	            destIndex++;
	        }
	        
	        srcIndex += srcStep;
	        destIndex += destStep;
	    }
	}
	
	public void drawRect(int offX, int offY, int width, int height, int color) {
		offX -= this.offX;
		offY -= this.offY;

		for (int y = 0; y <= height; y++) {
			setPixel(offX, y + offY, color);
			setPixel(offX + width, y + offY, color);
		}
		for (int x = 0; x <= width; x++) {
			setPixel(x + offX, offY, color);
			setPixel(x + offX, offY + height, color);
		}
	}

	public void drawFillRect(int offX, int offY, int width, int height, int color) {
		offX -= this.offX;
		offY -= this.offY;

		// Stops rendering
		if (offX < -width)
			return;
		if (offY < -height)
			return;
		if (offX >= pW)
			return;
		if (offY >= pH)
			return;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				setPixel(x + offX, y + offY, color);
			}
		}
	}

	public void drawLight(Light light, int offX, int offY) {
		lightRequest.add(new LightRequest(light, offX, offY));
	}

	private void drawLightRequest(Light light, int offX, int offY) {
		offX -= this.offX;
		offY -= this.offY;

		for (int i = 0; i <= light.getDiameter(); i++) {
			drawLightLine(light, light.getRadius(), light.getRadius(), i, 0, offX, offY); // top
			drawLightLine(light, light.getRadius(), light.getRadius(), i, light.getDiameter(), offX, offY); // bottom
			drawLightLine(light, light.getRadius(), light.getRadius(), 0, i, offX, offY); // left
			drawLightLine(light, light.getRadius(), light.getRadius(), light.getDiameter(), i, offX, offY); // right
		}
	}

	private void drawLightLine(Light light, int x0, int y0, int x1, int y1, int offX, int offY) {
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);

		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;

		int err = dx - dy;
		int err2;

		while (true) {
			int screenX = x0 - light.getRadius() + offX;
			int screenY = y0 - light.getRadius() + offY;

			if (screenX < 0 || screenX >= pW || screenY < 0 || screenY >= pH)
				return;

			int lightColor = light.getLightValue(x0, y0);
			if (lightColor == 0)
				return;

			if (lightBlock[screenX + screenY * pW] == Light.FULL)
				return;

			setLightMap(screenX, screenY, lightColor);

			if (x0 == x1 && y0 == y1)
				break;

			err2 = 2 * err;

			if (err2 > -1 * dy) {
				err -= dy;
				x0 += sx;
			}
			if (err2 < dx) {
				err += dx;
				y0 += sy;
			}
		}
	}

	public int getOffX() { return offX; }
    public int getOffY() { return offY; }
	
	public int getzDepth() { return zDepth; }

	public void setzDepth(int zDepth) {
		this.zDepth = zDepth;
	}

	public int getAmbientColor() {
		return ambientColor;
	}

	public void setAmbientColor(int ambientColor) {
		this.ambientColor = ambientColor;
	}
	
	public void setBackgroundColor(int color) {
	    this.clearColor = color;
	}
}

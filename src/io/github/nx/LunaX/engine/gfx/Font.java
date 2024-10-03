package io.github.nx.LunaX.engine.gfx;

public class Font {
	public static final Font COMIC_SANS = new Font("/fonts/comicsans.png");

	private Image fontImage;
	private int[] offsets;
	private int[] widths;

	private int characters = 256; // unicode things

	public Font(String path) {
		fontImage = new Image(path);
		offsets = new int[characters];
		widths = new int[characters];

		int unicode = 0;

		for (int i = 0; i < fontImage.getWidth(); i++) {
			if (fontImage.getPixels()[i] == 0xff0000ff) {
				offsets[unicode] = i;
			}
			if (fontImage.getPixels()[i] == 0xffffff00) {
				widths[unicode] = i - offsets[unicode];
				unicode++;
			}
		}
	}

	public Image getFontImage() {
		return fontImage;
	}

	public void setFontImage(Image fontImage) {
		this.fontImage = fontImage;
	}

	public int[] getOffsets() {
		return offsets;
	}

	public void setOffsets(int[] offsets) {
		this.offsets = offsets;
	}

	public int[] getWidths() {
		return widths;
	}

	public void setWidths(int[] widths) {
		this.widths = widths;
	}
}

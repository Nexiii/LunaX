package io.github.nx.LunaX.engine.gfx;

public class Font {
    public static final Font ARIAL = new Font("/fonts/arial.png");
    public static final Font COMICSANS = new Font("/fonts/comicsans.png");

    private Image fontImage;
    private int[] offsets;
    private int[] widths;

    private int characters = 256;

    public Font(String path) {
        fontImage = new Image(path);
        offsets = new int[characters];
        widths = new int[characters];

        int unicode = 0;
        int[] p = fontImage.getPixels();
        int w = fontImage.getWidth();

        for (int i = 0; i < w; i++) {
            if (p[i] == 0xff0000ff) {
                offsets[unicode] = i;
            }
            if (p[i] == 0xffffff00) {
                widths[unicode] = (i - offsets[unicode]) + 1;
                unicode++;
                
                if(unicode >= characters) break;
            }
        }
        for (int i = 0; i < w; i++) {
            if (p[i] == 0xff0000ff || p[i] == 0xffffff00) {
                p[i] = 0;
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
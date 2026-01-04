package io.github.nx.LunaX.engine.gfx;

import java.util.ArrayList;
import java.util.List;

public class ImageTile extends Image {

    private int tileW, tileH;
    // ArrayList ist sicherer als reines Array
    private List<Image> tileImages = new ArrayList<>();

    public ImageTile(String path, int tileW, int tileH) {
        super(path);
        this.tileW = tileW;
        this.tileH = tileH;

        // Wenn das Bild nicht geladen wurde, abbrechen
        if (this.w == 0) return;

        processTiles();
    }

    private void processTiles() {
        tileImages.clear();
        int cols = w / tileW;
        int rows = h / tileH;

        // Wir schneiden das große Bild in kleine Stücke
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                int[] pixels = new int[tileW * tileH];
                
                for (int ty = 0; ty < tileH; ty++) {
                    for (int tx = 0; tx < tileW; tx++) {
                        int srcX = (x * tileW) + tx;
                        int srcY = (y * tileH) + ty;
                        pixels[tx + ty * tileW] = this.p[srcX + srcY * w];
                    }
                }
                tileImages.add(new Image(pixels, tileW, tileH));
            }
        }
    }

    public Image getTileImage(int index) {
        // SICHERHEITS-CHECK 1: Ist die Liste leer?
        if (tileImages.isEmpty()) return this; // Fallback auf das ganze Bild

        // SICHERHEITS-CHECK 2: Ist der Index gültig?
        if (index < 0 || index >= tileImages.size()) {
            // Um den Crash zu verhindern, geben wir Tile 0 zurück 
            // oder null (dann wird nichts gemalt)
            System.err.println("Warnung: Tile-ID " + index + " existiert nicht! (Max: " + (tileImages.size()-1) + ")");
            return tileImages.get(0); 
        }

        return tileImages.get(index);
    }
    
    public int getTileW() { return tileW; }
    public int getTileH() { return tileH; }
}
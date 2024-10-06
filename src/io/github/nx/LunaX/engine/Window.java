package io.github.nx.LunaX.engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Window {
	private JFrame frame;
	private BufferedImage image;
	private Canvas canvas;
	private BufferStrategy bs;
	private Graphics g;

	public Window(GameContainer gc) {
		image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);

		canvas = new Canvas();
		Dimension s = new Dimension((int) (gc.getWidth() * gc.getScale()), (int) (gc.getHeight() * gc.getScale()));
		canvas.setPreferredSize(s);
		canvas.setMaximumSize(s); // locks canvas to 1 size
		canvas.setMinimumSize(s);

		frame = new JFrame(gc.getTitle());
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(gc.getIcoPath())));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // makes sure program closes - DO NOT FORGET THIS LINE
		frame.setLayout(new BorderLayout());
		
		// Creating a blank Cursor\\
		// You can delete this \\
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		cursorImg, new Point(0, 0), "BLANK");
		
		frame.getContentPane().setCursor(blankCursor);
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack(); // sets frame to canvas size
		frame.setLocationRelativeTo(null);
		frame.setResizable(false); // allows frame to be resized (disabled)
		frame.setVisible(true);

		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		g = bs.getDrawGraphics();
	}

	public void update() {
		g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		bs.show();
	}

	// ----- GETTERS & SETTERS -----
	public BufferedImage getImage() {
		return image;
	}

	public Canvas getCanvas() {
		return canvas;
	}

}

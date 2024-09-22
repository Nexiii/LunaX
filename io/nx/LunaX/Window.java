package io.nx.LunaX;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Window {

	private JFrame frame;
	private BufferedImage img;
	private Canvas canvas;
	private BufferStrategy bs;
	private Graphics gfx;
	
	public Window(GameContainer gc) {
		img = new BufferedImage(gc.getWidth(), gc.getHeight() ,BufferedImage.TYPE_INT_RGB);
		
		canvas = new Canvas();
		Dimension s = new Dimension((int)(gc.getWidth()*gc.getScale()),(int)(gc.getHeight()*gc.getScale()));
		canvas.setPreferredSize(s);
		canvas.setMinimumSize(s);
		canvas.setMaximumSize(s);
		
		frame = new JFrame(gc.getTitle());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		gfx = bs.getDrawGraphics();
	}
	
	public void update() {
		gfx.drawImage(img, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		bs.show();
	}
	
}

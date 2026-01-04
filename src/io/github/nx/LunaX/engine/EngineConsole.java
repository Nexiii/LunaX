package io.github.nx.LunaX.engine;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class EngineConsole {
    private JFrame frame;
    private JTextPane textPane;

    public EngineConsole(String title, GameContainer gc) {
        frame = new JFrame(title);
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(gc.getIcoPath()));
            frame.setIconImage(icon.getImage());
        } catch (Exception e) {
            gc.getLogger().logError("Icon not found");
        }
        
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        textPane = new JTextPane();
        textPane.setBackground(new Color(28, 28, 28));
        textPane.setEditable(false);
        textPane.setFont(new Font("Segoe UI Variable", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(null);
        frame.add(scrollPane);
        
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
    }

    public void log(String text, Color color) {
        String time = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
        String message = "[" + time + "] " + text + "\n";

        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setForeground(attrs, color);

        try {
            StyledDocument doc = textPane.getStyledDocument();
            doc.insertString(doc.getLength(), message, attrs);
            
            textPane.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}
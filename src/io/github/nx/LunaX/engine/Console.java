package io.github.nx.LunaX.engine;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import io.github.nx.LunaX.engine.gfx.Font;

public class Console {

    private boolean open = false;
    private String currentInput = "";
    private List<String> lines = new ArrayList<>();
    private HashMap<String, Consumer<String[]>> commands = new HashMap<>();
    
    private boolean enterBlock = false;
    private boolean backspaceBlock = false;

    public Console(GameContainer gc) {
        registerCommand("clear", (args) -> lines.clear());
        registerCommand("exit", (args) -> System.exit(0));
    }

    public void registerCommand(String name, Consumer<String[]> action) {
        commands.put(name.toLowerCase(), action);
    }

    public void log(String text) {
        lines.add(text);
        if (lines.size() > 15) {
            lines.remove(0);
        }
        System.out.println("[CONSOLE] " + text);
    }

    public void toggle() {
        open = !open;
        currentInput = "";
    }

    public boolean isOpen() {
        return open;
    }

    public void update(GameContainer gc, float dt) {
        if (!open) return;

        Input input = gc.getInput();

        if (input.isKeyDown(KeyEvent.VK_BACK_SPACE)) {
            if (!backspaceBlock && currentInput.length() > 0) {
                currentInput = currentInput.substring(0, currentInput.length() - 1);
                backspaceBlock = true;
            }
        } else {
            backspaceBlock = false;
        }

        if (input.isKeyDown(KeyEvent.VK_ENTER)) {
            if (!enterBlock) {
                execute();
                enterBlock = true;
            }
        } else {
            enterBlock = false;
        }

        checkChar(input, KeyEvent.VK_H, 'h');
        checkChar(input, KeyEvent.VK_O, 'o');
        checkChar(input, KeyEvent.VK_S, 's');
        checkChar(input, KeyEvent.VK_T, 't');
        checkChar(input, KeyEvent.VK_J, 'j');
        checkChar(input, KeyEvent.VK_I, 'i');
        checkChar(input, KeyEvent.VK_N, 'n');
        checkChar(input, KeyEvent.VK_SPACE, ' ');
        checkChar(input, KeyEvent.VK_PERIOD, '.');
        checkChar(input, KeyEvent.VK_L, 'l');
        checkChar(input, KeyEvent.VK_C, 'c');
        checkChar(input, KeyEvent.VK_A, 'a');
        
        for(int i = 0; i <= 9; i++) {
            checkChar(input, KeyEvent.VK_0 + i, (char)('0' + i));
        }
    }
    
    private boolean[] keyState = new boolean[1000];
    
    private void checkChar(Input input, int keyCode, char c) {
        if (input.isKeyDown(keyCode)) {
            if (!keyState[keyCode]) {
                currentInput += c;
                keyState[keyCode] = true;
            }
        } else {
            keyState[keyCode] = false;
        }
    }

    private void execute() {
        if (currentInput.trim().isEmpty()) return;
        
        log("> " + currentInput);
        
        String[] parts = currentInput.split(" ");
        String cmd = parts[0].toLowerCase();
        
        if (commands.containsKey(cmd)) {
            String[] args = new String[parts.length - 1];
            System.arraycopy(parts, 1, args, 0, args.length);
            
            try {
                commands.get(cmd).accept(args);
            } catch (Exception e) {
                log("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            log("Unknown command: " + cmd);
        }
        
        currentInput = "";
    }

    public void render(Renderer r, GameContainer gc) {
        if (!open) return;

        int h = gc.getHeight() / 2;
        int w = gc.getWidth();

        for(int y=0; y<h; y+=2) {
             r.drawFillRect(0, y, w, 1, 0xff222222);
        }

        r.drawFillRect(0, h, w, 2, 0xff00ff00);

        int yOff = h - 20;
        for (int i = lines.size() - 1; i >= 0; i--) {
            r.drawString(lines.get(i), 5, yOff, 0xffaaaaaa, Font.ARIAL, 1f);
            yOff -= 12;
        }

        r.drawString("> " + currentInput + "_", 5, h - 8, 0xffffffff, Font.ARIAL, 1f);
    }
}
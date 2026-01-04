package io.github.nx.LunaX.engine;

import java.awt.Color;

public class Logger {

    public EngineConsole console;
    
    private boolean useConsole;
    
    private final Color COLOR_LOG = new Color(230, 230, 230);
    private final Color COLOR_WARN = new Color(255, 170, 0);
    private final Color COLOR_ERROR = new Color(255, 80, 80);

    public Logger(GameContainer gc) {
    	useConsole = gc.getUseConsole();
        if(gc.getUseConsole()) {
            this.console = new EngineConsole("LunaX | Debug Terminal", gc);
        }
    }
    
    public void log(String text) {
        if(useConsole && console != null) {
            console.log("[LOG]: " + text, COLOR_LOG);
        }
    }
    
    public void logWarn(String text) {
        if(useConsole && console != null) {
            console.log("[WARN]: " + text, COLOR_WARN);
        }
    }
    
    public void logError(String text) {
        if(useConsole && console != null) {
            console.log("[ERROR]: " + text, COLOR_ERROR);
        }
    }
}
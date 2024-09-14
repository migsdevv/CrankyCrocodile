package me.miguelbuccat;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import me.miguelbuccat.state.MainMenuState;

public class Main extends SimpleApplication {
    public static void main(String[] args) {
        Main app = new Main();

        AppSettings settings = new AppSettings(true);
        settings.setTitle("Cranky Crocodile");
        
        app.setSettings(settings);
        
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        stateManager.attach(new MainMenuState(this));
        
        GuiGlobals.initialize(this);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        
    }
}
package me.miguelbuccat;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import me.miguelbuccat.state.MainMenuState;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Main extends SimpleApplication {
    public static void main(String[] args) {
        Main app = new Main();

        AppSettings settings = new AppSettings(true);
        settings.setTitle("Cranky Crocodile");

        try {
            BufferedImage[] icons = new BufferedImage[] {
                    ImageIO.read(Objects.requireNonNull(Main.class.getResourceAsStream("/Icons/icon16.png"))),
                    ImageIO.read(Objects.requireNonNull(Main.class.getResourceAsStream("/Icons/icon32.png"))),
                    ImageIO.read(Objects.requireNonNull(Main.class.getResourceAsStream("/Icons/icon64.png")))
            };

            // Set the icons for the application
            settings.setIcons(icons);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        app.setSettings(settings);
        settings.setResolution(1280,720);
        app.setShowSettings(false);
        
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        assetManager.registerLocator(null, com.jme3.asset.plugins.ClasspathLocator.class);
        stateManager.attach(new MainMenuState(this));
        
        GuiGlobals.initialize(this);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        sun.setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        rootNode.addLight(sun);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        
    }
}
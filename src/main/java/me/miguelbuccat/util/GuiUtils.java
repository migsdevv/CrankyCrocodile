package me.miguelbuccat.util;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.QuadBackgroundComponent;

public class GuiUtils {
    public static Label createImgLabel(AssetManager assetManager, String path, int w, int h) {
        Label imageLabel = new Label("");

        Texture texture = assetManager.loadTexture(path);
        QuadBackgroundComponent imageBackground = new QuadBackgroundComponent(texture);
        imageLabel.setBackground(imageBackground);

        imageLabel.setPreferredSize(new Vector3f(w, h, 0));

        return imageLabel;
    }
    
    public static Button createButton(String s, Command<Button> callback) {
        Button button = new Button(s);
        
        button.setFontSize(20f);
        button.setTextHAlignment(HAlignment.Center);
        
        button.addClickCommands(callback);
        return button;
    }
    
    public static void centerGUIContainer(Application app, Container c) {
        int screenWidth = app.getContext().getSettings().getWidth();
        int screenHeight = app.getContext().getSettings().getHeight();
        
        float windowWidth = c.getPreferredSize().x;
        float windowHeight = c.getPreferredSize().y;

        float xPos = (screenWidth - windowWidth) / 2;
        float yPos = (screenHeight + windowHeight) / 2;

        c.setLocalTranslation(xPos, yPos, 0);
    }
} 

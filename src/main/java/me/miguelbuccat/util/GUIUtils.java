package me.miguelbuccat.util;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.QuadBackgroundComponent;

public class GUIUtils {
    private final AssetManager assetManager;
    
    public GUIUtils(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    public Label createImgLabel(String path, int w, int h) {
        Label imageLabel = new Label("");

        Texture texture = assetManager.loadTexture(path);
        QuadBackgroundComponent imageBackground = new QuadBackgroundComponent(texture);
        imageLabel.setBackground(imageBackground);

        imageLabel.setPreferredSize(new Vector3f(w, h, 0));

        return imageLabel;
    }
    
    public Button createButton(String s, Command<Button> callback) {
        Button button = new Button(s);
        
        button.setFontSize(20f);
        button.setTextHAlignment(HAlignment.Center);
        
        button.addClickCommands(callback);
        return button;
    }
}

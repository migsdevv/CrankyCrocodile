package me.miguelbuccat.util;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

public class EnvironmentUtils {
    // Helper function for creating the sky
    public static Spatial createSky(AssetManager assetManager) {
        Texture west = assetManager.loadTexture("Textures/Sky/left.png");
        Texture east = assetManager.loadTexture("Textures/Sky/right.png");
        Texture north = assetManager.loadTexture("Textures/Sky/front.png");
        Texture south = assetManager.loadTexture("Textures/Sky/back.png");
        Texture up = assetManager.loadTexture("Textures/Sky/up.png");
        Texture down = assetManager.loadTexture("Textures/Sky/down.png");

        return SkyFactory.createSky(assetManager, west, east, north, south, up, down);
    }
}

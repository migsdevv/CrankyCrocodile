package me.miguelbuccat.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import me.miguelbuccat.util.EnvironmentUtils;

public class GameState extends AbstractAppState {
    private final Node rootNode;
    private final Node gameNode = new Node("Game");

    private final AssetManager assetManager;
    
    public GameState(SimpleApplication app) {
        rootNode = app.getRootNode();
        assetManager = app.getAssetManager();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        // Attach state note to root node
        rootNode.attachChild(gameNode);

        // Create sky
        gameNode.attachChild(EnvironmentUtils.createSky(assetManager));
    }

    @Override
    public void update(float tpf) {
        
    }

    @Override
    public void cleanup() {
        rootNode.detachChild(gameNode);

        super.cleanup();
    }
}
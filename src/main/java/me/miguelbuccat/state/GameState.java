package me.miguelbuccat.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;

public class GameState extends AbstractAppState {
    private final Node rootNode;
    private final Node gameNode = new Node("Game");
    
    public GameState(SimpleApplication app) {
        rootNode = app.getRootNode();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        // Attach state note to root node
        rootNode.attachChild(gameNode);
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
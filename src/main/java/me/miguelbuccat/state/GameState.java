package me.miguelbuccat.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

public class GameState extends AbstractAppState implements ActionListener {
    private final Node rootNode;
    
    private final Node gameNode = new Node("Game");
    
    private final AssetManager assetManager;

    private final FlyByCamera flyByCamera;
    private final Camera camera;
    
    private final InputManager inputManager;

    private Geometry crocodile;
    
    private CrocodileState crocodileState = CrocodileState.Normal;
    
    public GameState(SimpleApplication app) {
        rootNode = app.getRootNode();
        
        assetManager = app.getAssetManager();

        flyByCamera = app.getFlyByCamera();
        camera = app.getCamera();

        inputManager = app.getInputManager();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        this.flyByCamera.setEnabled(false);

        rootNode.attachChild(gameNode);

        Geometry water = new Geometry("Water", new Box(3, 1, 100));
        Material waterMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        waterMat.setColor("Color", ColorRGBA.Blue);
        water.setMaterial(waterMat);
        water.setLocalTranslation(0, 0, 0);
        gameNode.attachChild(water);

        crocodile = new Geometry("Crocodile", new Box(0.5f, 0.8f, 1.5f));
        Material crocMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        crocMat.setColor("Color", ColorRGBA.DarkGray);
        crocodile.setLocalTranslation(0, 1, 5);
        crocodile.setMaterial(crocMat);
        gameNode.attachChild(crocodile);

        Vector3f cameraLocation = new Vector3f(-2.8473775f, 5.749816f, 10.062765f);
        camera.setLocation(cameraLocation);

        Quaternion cameraRotation = new Quaternion(0.038307413f, 0.9584965f, -0.2356789f, 0.15579629f);
        camera.setRotation(cameraRotation);

        float angleInRadians = (float) Math.toRadians(-15);
        Quaternion rotation = new Quaternion().fromAngleAxis(angleInRadians, Vector3f.UNIT_Y);
        crocodile.setLocalRotation(rotation);

        createSky();
        
        //map controls

        inputManager.addMapping("TurnLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("TurnRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(this, "TurnLeft", "TurnRight");
    }

    public void createSky() {
        Texture west = assetManager.loadTexture("Textures/Sky/left.png");
        Texture east = assetManager.loadTexture("Textures/Sky/right.png");
        Texture north = assetManager.loadTexture("Textures/Sky/front.png");
        Texture south = assetManager.loadTexture("Textures/Sky/back.png");
        Texture up = assetManager.loadTexture("Textures/Sky/up.png");
        Texture down = assetManager.loadTexture("Textures/Sky/down.png");

        Spatial sky = SkyFactory.createSky(assetManager, west, east, north, south, up, down);

        gameNode.attachChild(sky);
    }

    @Override
    public void update(float tpf) {
        switch (crocodileState) {
            case CrocodileState.Normal:
                crocodile.setLocalRotation(new Quaternion().fromAngleAxis((float) Math.toRadians(0), Vector3f.UNIT_Y));
                break;
            case CrocodileState.TurnLeft:
                crocodile.setLocalRotation(new Quaternion().fromAngleAxis((float) Math.toRadians(15), Vector3f.UNIT_Y));
                if (crocodile.getLocalTranslation().x > -2.5f) {
                    crocodile.move(-2f * tpf, 0, 0);
                }
                break; 
            case CrocodileState.TurnRight:
                crocodile.setLocalRotation(new Quaternion().fromAngleAxis((float) Math.toRadians(-15), Vector3f.UNIT_Y));
                if (crocodile.getLocalTranslation().x < 2.5f) {
                    crocodile.move(2f * tpf, 0, 0);
                }
                break;
        }
        
        System.out.println(crocodile.getLocalTranslation().x);
    }

    @Override
    public void cleanup() {
        rootNode.detachChild(gameNode);

        super.cleanup();
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("TurnLeft") && isPressed) {
            if(crocodileState == CrocodileState.TurnRight) {
                crocodileState = CrocodileState.Normal;
            } else {
                crocodileState = CrocodileState.TurnLeft;
            }
        } else if (name.equals("TurnRight") && isPressed) {
            if(crocodileState == CrocodileState.TurnLeft) {
                crocodileState = CrocodileState.Normal;
            } else {
                crocodileState = CrocodileState.TurnRight;
            }
        }
    }

    private enum CrocodileState {
        TurnLeft,
        Normal,
        TurnRight
    }
}
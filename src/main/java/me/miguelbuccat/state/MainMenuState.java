package me.miguelbuccat.state;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.input.FlyByCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.renderer.Camera;
import com.jme3.app.Application;
import com.jme3.math.Vector3f;

import com.jme3.scene.Spatial;
import com.jme3.math.FastMath;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.*;
import me.miguelbuccat.util.GUIUtils;

public class MainMenuState extends AbstractAppState {
    private final Node rootNode;
    private final Node guiNode;
    
    private final AssetManager assetManager;

    private final Node menuNode = new Node("Main Menu");

    private final FlyByCamera flyByCamera;
    private final Camera camera;
    
    
    //Containers here
    Container mainMenu;
    Container contributorsMenu;
    Container attributionsMenu;

    public MainMenuState(SimpleApplication app) {
        rootNode = app.getRootNode();
        guiNode = app.getGuiNode();
        
        assetManager = app.getAssetManager();

        flyByCamera = app.getFlyByCamera();
        
        camera = app.getCamera();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        this.flyByCamera.setEnabled(false);

        rootNode.attachChild(menuNode);
        
        //menu music
        AudioNode menuMusic = new AudioNode(assetManager, "Sounds/menu.ogg", AudioData.DataType.Stream);
        menuMusic.setLooping(true);
        menuMusic.setPositional(false);
        menuMusic.setVolume(3);
        menuNode.attachChild(menuMusic);
        menuMusic.play();

        Geometry water = new Geometry("Water", new Box(200, 1, 200));
        Material waterMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); 
        waterMat.setColor("Color", ColorRGBA.Blue);
        water.setMaterial(waterMat);
        water.setLocalTranslation(0, -20, 0);
        menuNode.attachChild(water);

        Quaternion initialTilt = new Quaternion();
        initialTilt.fromAngleAxis(FastMath.DEG_TO_RAD * 15, Vector3f.UNIT_X);

        camera.setRotation(initialTilt);

        //Create sky
        createSky();
        
        int screenWidth = app.getContext().getSettings().getWidth();
        int screenHeight = app.getContext().getSettings().getHeight();
        
        //Create the menu containers
        mainMenu = new Container();
        contributorsMenu = new Container();
        attributionsMenu = new Container();
        createMainMenu(screenWidth, screenHeight);
        createContribsMenu(screenWidth, screenHeight);
        
        guiNode.attachChild(mainMenu);
    }
    
    public void createSky() {
        Texture west = assetManager.loadTexture("Textures/Sky/left.png");
        Texture east = assetManager.loadTexture("Textures/Sky/right.png");
        Texture north = assetManager.loadTexture("Textures/Sky/front.png");
        Texture south = assetManager.loadTexture("Textures/Sky/back.png");
        Texture up = assetManager.loadTexture("Textures/Sky/up.png");
        Texture down = assetManager.loadTexture("Textures/Sky/down.png");
        
        Spatial sky = SkyFactory.createSky(assetManager, west, east, north, south, up, down);
        
        menuNode.attachChild(sky);
    }
    
    public void createMainMenu(float screenWidth, float screenHeight) {
        mainMenu.addChild(new GUIUtils(assetManager).createImgLabel("Textures/logo.png", 364, 200));
        mainMenu.addChild(new GUIUtils(assetManager).createButton("Play", new Command<Button>() {
            @Override
            public void execute(Button button) {
                
            }
        }));
        mainMenu.addChild(new GUIUtils(assetManager).createButton("Contributions", new Command<Button>() {
            @Override
            public void execute(Button button) {
                guiNode.attachChild(contributorsMenu);
                guiNode.detachChild(mainMenu);
            }
        }));
        mainMenu.addChild(new GUIUtils(assetManager).createButton("Attributions", new Command<Button>() {
            @Override
            public void execute(Button button) {

            }
        }));
        mainMenu.addChild(new GUIUtils(assetManager).createButton("Exit", new Command<Button>() {
            @Override
            public void execute(Button button) {
                System.exit(0);
            }
        }));

        float windowWidth = mainMenu.getPreferredSize().x;
        float windowHeight = mainMenu.getPreferredSize().y;

        float xPos = (screenWidth - windowWidth) / 2;
        float yPos = (screenHeight + windowHeight) / 2;

        mainMenu.setLocalTranslation(xPos, yPos, 0);
    }

    public void createContribsMenu(float screenWidth, float screenHeight) {
        contributorsMenu.addChild(new GUIUtils(assetManager).createImgLabel("Textures/Menu/contribs.png", 364, 382));
        contributorsMenu.addChild(new GUIUtils(assetManager).createButton("Back", new Command<Button>() {
            @Override
            public void execute(Button button) {
                guiNode.attachChild(mainMenu);
                guiNode.detachChild(contributorsMenu);
            }
        }));

        float windowWidth = contributorsMenu.getPreferredSize().x;
        float windowHeight = contributorsMenu.getPreferredSize().y;

        float xPos = (screenWidth - windowWidth) / 2;
        float yPos = (screenHeight + windowHeight) / 2;
 
        contributorsMenu.setLocalTranslation(xPos, yPos, 0);
    }
    
    @Override
    public void update(float tpf) {
        float rotationSpeed = FastMath.TWO_PI / 20;

        float angle = rotationSpeed * tpf;

        Quaternion rot = new Quaternion();
        rot.fromAngleAxis(angle, Vector3f.UNIT_Y);
        camera.setRotation(camera.getRotation().mult(rot));
    }

    @Override
    public void cleanup() {
        rootNode.detachChild(menuNode);

        super.cleanup();
    }
}

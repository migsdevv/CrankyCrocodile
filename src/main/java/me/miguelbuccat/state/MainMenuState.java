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
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.renderer.Camera;
import com.jme3.app.Application;
import com.jme3.math.Vector3f;

import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.*;
import me.miguelbuccat.util.EnvironmentUtils;
import me.miguelbuccat.util.GuiUtils;

public class MainMenuState extends AbstractAppState {
    private final SimpleApplication app;
    
    private final Node rootNode;
    private final Node guiNode;
    private final Node menuNode = new Node("Main Menu");
    private final AssetManager assetManager;
    private final AppStateManager stateManager;
    private final FlyByCamera flyByCamera;
    private final Camera camera;
    private AudioNode menuMusic;

    //Containers here
    private Container mainMenu;
    private Container contributorsMenu;
    private Container attributionsMenu;

    public MainMenuState(SimpleApplication app) {
        this.app = app;
        rootNode = app.getRootNode();
        guiNode = app.getGuiNode();
        assetManager = app.getAssetManager();
        stateManager = app.getStateManager();
        flyByCamera = app.getFlyByCamera();
        camera = app.getCamera();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        // Disable flyby camera
        this.flyByCamera.setEnabled(false);

        // Tilt camera by 15 degrees
        camera.setRotation((new Quaternion()).fromAngleAxis(FastMath.DEG_TO_RAD * 15, Vector3f.UNIT_X));

        rootNode.attachChild(menuNode);
        
        // Handle menu music
        menuMusic = new AudioNode(assetManager, "Sounds/menu.ogg", AudioData.DataType.Stream);
        menuMusic.setLooping(true);
        menuMusic.setPositional(false);
        menuMusic.setVolume(3);
        menuNode.attachChild(menuMusic);
        menuMusic.play();

        Spatial menuScene = assetManager.loadModel("3D/menu.glb");
        menuNode.attachChild(menuScene);

        // Create sky
        menuNode.attachChild(EnvironmentUtils.createSky(assetManager));
        
        int screenWidth = app.getContext().getSettings().getWidth();
        int screenHeight = app.getContext().getSettings().getHeight();
        
        // Create the menu containers
        mainMenu = new Container();
        contributorsMenu = new Container();
        attributionsMenu = new Container();
        
        // Populate menu containers
        createMainMenu();
        createContributionsMenu(screenWidth, screenHeight);
        createAttribsMenu(screenWidth, screenHeight);
        
        // Initially attach the main menu container
        guiNode.attachChild(mainMenu);
    }
    
    public void createMainMenu() {
        // Import logo
        mainMenu.addChild(GuiUtils.createImgLabel(assetManager, "Textures/logo.png", 364, 200));
        
        // Create buttons
        mainMenu.addChild(GuiUtils.createButton("Play", new Command<Button>() {
            @Override
            public void execute(Button button) {
                switchToGameState();
            }
        }));
        
        mainMenu.addChild(GuiUtils.createButton("Contributions", new Command<Button>() {
            @Override
            public void execute(Button button) {
                guiNode.attachChild(contributorsMenu);
                guiNode.detachChild(mainMenu);
            }
        }));
        
        mainMenu.addChild(GuiUtils.createButton("Attributions", new Command<Button>() {
            @Override
            public void execute(Button button) {
                guiNode.attachChild(attributionsMenu);
                guiNode.detachChild(mainMenu);
            }
        }));
        
        mainMenu.addChild(GuiUtils.createButton("Exit", new Command<Button>() {
            @Override
            public void execute(Button button) {
                System.exit(0);
            }
        }));

        // Center Main menu
        GuiUtils.centerGUIContainer(app, mainMenu);
    }

    public void createContributionsMenu(float screenWidth, float screenHeight) {
        // Import contributions
        contributorsMenu.addChild(GuiUtils.createImgLabel(assetManager, "Textures/Menu/contribs.png", 364, 382));
        
        // Create back button
        contributorsMenu.addChild(GuiUtils.createButton("Back", new Command<Button>() {
            @Override
            public void execute(Button button) {
                guiNode.attachChild(mainMenu);
                guiNode.detachChild(contributorsMenu);
            }
        }));

        // Center Contributions menu
        GuiUtils.centerGUIContainer(app, contributorsMenu);
    }

    public void createAttribsMenu(float screenWidth, float screenHeight) {
        // Import contributions
        attributionsMenu.addChild(GuiUtils.createImgLabel(assetManager, "Textures/Menu/attribs.png", 364, 382));

        // Create back button
        attributionsMenu.addChild(GuiUtils.createButton("Back", new Command<Button>() {
            @Override
            public void execute(Button button) {
                guiNode.attachChild(mainMenu);
                guiNode.detachChild(attributionsMenu);
            }
        }));

        // Center Contributions menu
        GuiUtils.centerGUIContainer(app, attributionsMenu);
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
        mainMenu.removeFromParent();
        contributorsMenu.removeFromParent();
        attributionsMenu.removeFromParent();
        
        menuMusic.stop();
    }
    
    public void switchToGameState() {
        stateManager.detach(this);
        
        stateManager.attach(new GameState(app));
    }
}

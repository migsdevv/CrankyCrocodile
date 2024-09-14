package me.miguelbuccat.state;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.FlyByCamera;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.renderer.Camera;
import com.jme3.app.Application;

import com.jme3.scene.control.CameraControl;
import com.jme3.texture.Texture;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.QuadBackgroundComponent;

import java.nio.file.Path;

public class MainMenuState extends AbstractAppState {
    private final Node rootNode;
    private final Node guiNode;
    
    private final AssetManager assetManager;

    private final Node menuNode = new Node("Main Menu");

    private final FlyByCamera flyByCamera;
    private final Camera camera;

    /**
     * Main menu states (i dont have braincells left to code so im doing this)
     * 0 = main menu
     * 1 = contributors menu
     * 2 = attributions menu
     */
    private int menuStates = 0;
    
    //Containers here
    Container mainMenu;
    Container contributorsMenu;
    Container attributionsMenu;

    Container backgroundContainer;

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

        CameraNode cameraNode = new CameraNode("Main Camera", camera);
        cameraNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        menuNode.attachChild(cameraNode);
        
        //Create full-screen container
        backgroundContainer = new Container();
        int screenWidth = app.getContext().getSettings().getWidth();
        int screenHeight = app.getContext().getSettings().getHeight();
        backgroundContainer.setPreferredSize(new Vector3f(screenWidth, screenHeight, 0));
        backgroundContainer.setLocalTranslation(0, screenHeight, 0);
        backgroundContainer.setBackground(new QuadBackgroundComponent(new ColorRGBA(0.188f, 0.565f, 1.0f, 1.0f)));
        guiNode.attachChild(backgroundContainer);
        
        //Create the menu containers
        mainMenu = new Container();
        contributorsMenu = new Container();
        attributionsMenu = new Container();
        createMainMenu(screenWidth, screenHeight);
    }
    
    public void createMainMenu(float screenWidth, float screenHeight) {

        mainMenu.addChild(new Label("Hello, World."));
        Button clickMe = mainMenu.addChild(new Button("Click Me"));
        clickMe.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
            }
        });

        float windowWidth = mainMenu.getPreferredSize().x;
        float windowHeight = mainMenu.getPreferredSize().y;

        float xPos = (screenWidth - windowWidth) / 2;
        float yPos = (screenHeight + windowHeight) / 2;

        mainMenu.setLocalTranslation(xPos, yPos, 0);
    }
    
    @Override
    public void update(float tpf) {
        switch (menuStates) {
            case 0:
                if(guiNode.hasChild(contributorsMenu)) guiNode.detachChild(contributorsMenu);
                if(guiNode.hasChild(attributionsMenu)) guiNode.detachChild(attributionsMenu);
                if(!guiNode.hasChild(mainMenu)) guiNode.attachChild(mainMenu);
                break;
            case 1:
                if(guiNode.hasChild(mainMenu)) guiNode.detachChild(mainMenu);
                if(guiNode.hasChild(attributionsMenu)) guiNode.detachChild(attributionsMenu);
                if(!guiNode.hasChild(contributorsMenu)) guiNode.attachChild(contributorsMenu);
            case 2:
                if(guiNode.hasChild(mainMenu)) guiNode.detachChild(mainMenu);
                if(guiNode.hasChild(contributorsMenu)) guiNode.detachChild(contributorsMenu);
                if(!guiNode.hasChild(attributionsMenu)) guiNode.attachChild(attributionsMenu);
        }
    }

    @Override
    public void cleanup() {
        rootNode.detachChild(menuNode);

        super.cleanup();
    }
    
    // helper function because im stupid
    public Button createImageButton(String path, int w, int h, Command<Button> callback) {
        Button imageButton = new Button("");
        Texture texture = assetManager.loadTexture(path);

        QuadBackgroundComponent imageBackground = new QuadBackgroundComponent(texture);
        imageButton.setBackground(imageBackground);
        
        imageButton.setPreferredSize(new Vector3f(w, h, 0));

        imageButton.addClickCommands(callback);
        
        return imageButton;
    }
}

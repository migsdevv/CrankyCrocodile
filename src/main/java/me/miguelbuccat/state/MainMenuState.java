package me.miguelbuccat.state;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.FlyByCamera;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.renderer.Camera;
import com.jme3.app.Application;

import com.jme3.scene.control.CameraControl;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.QuadBackgroundComponent;

public class MainMenuState extends AbstractAppState {
    private final Node rootNode;
    private final Node guiNode;

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
        backgroundContainer.setBackground(new QuadBackgroundComponent(ColorRGBA.Blue));
        guiNode.attachChild(backgroundContainer);
        
        //Create the menu containers
        mainMenu = new Container("menu-panel");
        contributorsMenu = new Container();
        attributionsMenu = new Container();

        mainMenu.addChild(new Label("Hello, World."));
        Button clickMe = mainMenu.addChild(new Button("Click Me"));
        clickMe.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                System.out.println("The world is yours.");
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
}

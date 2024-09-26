package me.miguelbuccat.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.simsilica.lemur.*;
import me.miguelbuccat.util.EnvironmentUtils;
import me.miguelbuccat.util.GuiUtils;

import java.util.Random;

public class GameState extends AbstractAppState {
    private final Node rootNode;
    private final Node gameNode = new Node("Game");
    private final Node fishNode = new Node("Fish");
    private final Node badFishNode = new Node("BadFish");
    private final Node guiNode;
    
    private final SimpleApplication app;

    private final AssetManager assetManager;
    
    private final Camera camera;
    private final FlyByCamera flyByCamera;
    private final InputManager inputManager;
    
    private Spatial crocodile;

    private final float spawnInterval = 5f; // 5 seconds interval for spawning fish
    private float timeSinceLastSpawn = 0f;
    private final Random random = new Random();
    
    private float speed = 4f;

    private final float speedIncreaseRate = 0.5f;

    private int score = 0;
    private Label scoreLabel;
    
    private int health = 10;
    private Label healthLabel;

    private boolean shouldContinue = true;
    private Container gameOverPanel = new Container();
    
    public GameState(SimpleApplication app) {
        rootNode = app.getRootNode();
        this.app = app;
        guiNode = app.getGuiNode();
        assetManager = app.getAssetManager();
        
        camera = app.getCamera();
        flyByCamera = app.getFlyByCamera();
        inputManager = app.getInputManager();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        camera.setLocation(new Vector3f(3.3682368f, 4.6428804f, -7.1625094f));
        camera.setRotation(new Quaternion(0.14035198f, -0.103739336f, 0.014788608f, 0.9845409f));

        
        this.flyByCamera.setEnabled(false);
        
        // Attach state note to root node
        rootNode.attachChild(gameNode);

        // Create sky
        gameNode.attachChild(EnvironmentUtils.createSky(assetManager));
        
        //Create scene
        Spatial scene = assetManager.loadModel("3D/scene.glb");
        gameNode.attachChild(scene);

        crocodile = ((Node) scene).getChild("Crocodile");

        Material crocColor = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        crocColor.setColor("Color", ColorRGBA.Brown);
        crocodile.setMaterial(crocColor);

        inputManager.addMapping("MoveLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("MoveRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(moveListener, "MoveLeft", "MoveRight");
        
        gameNode.attachChild(fishNode);
        gameNode.attachChild(badFishNode);

        scoreLabel = new Label("SCORE: ");

        // Set the size and alignment for the label
        scoreLabel.setFontSize(24);  // Optional: Change the size of the text

        // Position the label in the top-left corner
        // Using setLocalTranslation to place the label explicitly
        scoreLabel.setLocalTranslation(10, camera.getHeight() - 10, 0);

        // Attach the label to the GUI node (for 2D rendering)
        guiNode.attachChild(scoreLabel);

        healthLabel = new Label("HEALTH: 10/10");

        // Set the size and alignment for the label
        healthLabel.setFontSize(24);  // Optional: Change the size of the text

        // Position the label in the top-right corner
        // Using setLocalTranslation to place the label explicitly
        healthLabel.setLocalTranslation(camera.getWidth() - 200, camera.getHeight() - 10, 0);  // Adjust for margin

        // Attach the label to the GUI node (for 2D rendering)
        guiNode.attachChild(healthLabel);
        
        spawnFish();
        spawnBadFish();
    }

    private final AnalogListener moveListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            if(!shouldContinue) return;
            if (crocodile != null) {
                if (name.equals("MoveLeft")) {
                    // Move the spatial along +X when 'A' is pressed
                    crocodile.move(5f * tpf, 0, 0);
                } else if (name.equals("MoveRight")) {
                    // Move the spatial along -X when 'D' is pressed
                    crocodile.move(-5f * tpf, 0, 0);
                }
            }
        }
    };

    private void spawnFish() {
        // Generate a random number of fish to spawn (between 3 and 5)
        int fishCount = 3 + random.nextInt(3); // 3, 4, or 5 fish
        float baseZ = 24f; // Starting Z position
        float zSpacing = 5f; // Minimum spacing between fish in the Z direction

        for (int i = 0; i < fishCount; i++) {
            // Load the fish model
            Spatial fish = assetManager.loadModel("3D/fish.glb");
            

            // Set the random X position between -5 and 5
            float randomX = -5 + random.nextFloat() * (5 - (-5)); // Random X between -5 and 5
            float zPosition = baseZ + i * zSpacing + random.nextFloat();
            fish.setLocalTranslation(new Vector3f(randomX, 0f, zPosition)); // Spawn at random X, fixed Z

            // Scale the fish down
            fish.setLocalScale(new Vector3f(0.01f, 0.01f, 0.01f));

            // Apply a green material to the fish
            Material greenFish = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            greenFish.setColor("Color", ColorRGBA.Green);
            fish.setMaterial(greenFish);

            // Attach the fish to the fish node
            fishNode.attachChild(fish);
        }
    }

    private void spawnBadFish() {
        // Generate a random number of fish to spawn (between 3 and 5)
        int fishCount = 2 + random.nextInt(3); // 3, 4, or 5 fish
        float baseZ = 24f; // Starting Z position
        float zSpacing = 5f; // Minimum spacing between fish in the Z direction

        for (int i = 0; i < fishCount; i++) {
            // Load the fish model
            Spatial fish = assetManager.loadModel("3D/fish.glb");


            // Set the random X position between -5 and 5
            float randomX = -5 + random.nextFloat() * (5 - (-5)); // Random X between -5 and 5
            float zPosition = baseZ + i * zSpacing + random.nextFloat();
            fish.setLocalTranslation(new Vector3f(randomX, 0f, zPosition)); // Spawn at random X, fixed Z

            // Scale the fish down
            fish.setLocalScale(new Vector3f(0.01f, 0.01f, 0.01f));

            // Apply a green material to the fish
            Material redFish = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            redFish.setColor("Color", ColorRGBA.Red);
            fish.setMaterial(redFish);

            // Attach the fish to the fish node
            badFishNode.attachChild(fish);
        }
    }

    public void gameOver() {
        shouldContinue = false;

        Label gameOverLabel = new Label("GAME OVER!");
        gameOverLabel.setFontSize(48f);

        Label scoreLabel = new Label("SCORE: " + score);
        scoreLabel.setFontSize(24f);

        gameOverPanel.addChild(gameOverLabel);
        gameOverPanel.addChild(scoreLabel);
        guiNode.attachChild(gameOverPanel);

        GuiUtils.centerGUIContainer(app, gameOverPanel);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if(!shouldContinue) return;

        timeSinceLastSpawn += tpf;
        
        if (timeSinceLastSpawn >= spawnInterval) {
            spawnFish();
            spawnBadFish();
            timeSinceLastSpawn = 0f;
        }

        for (Spatial fish : fishNode.getChildren()) {
            if (fish != null) {
                // Move fish along negative X-axis based on the current speed
                fish.move(0, 0, -speed * tpf);
                if (crocodile != null && fish.getWorldBound().intersects(crocodile.getWorldBound())) {
                    fishNode.detachChild(fish);
                    score += 100;
                    scoreLabel.setText("SCORE: " + score);
                }
            }
        }

        for (Spatial fish : badFishNode.getChildren()) {
            if (fish != null) {
                // Move fish along negative X-axis based on the current speed
                fish.move(0, 0, -speed * tpf);
                if (crocodile != null && fish.getWorldBound().intersects(crocodile.getWorldBound())) {
                    badFishNode.detachChild(fish);
                    health -= 1;
                    healthLabel.setText("HEALTH: " + health + "/10");
                    if(health == 0) {
                        if(!shouldContinue) return;
                        shouldContinue = false;
                        gameOver();
                    }
                }
            }
        }

        // Increase speed as time passes
        speed += speedIncreaseRate * tpf;
    }

    @Override
    public void cleanup() {
        rootNode.detachChild(gameNode);

        super.cleanup();
    }
}
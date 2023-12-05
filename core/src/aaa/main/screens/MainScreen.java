package aaa.main.screens;

import aaa.main.AntGame;
import aaa.main.game.map.Colony;
import aaa.main.game.map.FoodSource;
import aaa.main.game.input.PlayerInputProcessor;
import aaa.main.game.input.CameraInputProcessor;
import aaa.main.game.input.MenusInputProcessor;
import aaa.main.game.map.MapManager;
import aaa.main.game.map.MapObjectHandler;
import aaa.main.stages.PauseMenu;
import aaa.main.stages.PurchaseMenu;
import aaa.main.util.ColonyUtils;
import aaa.main.util.RenderUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

import static aaa.main.util.Constants.*;

public class MainScreen extends ScreenAdapter {
    private final AntGame game;
    private final PauseMenu pauseMenu;

    private final PurchaseMenu purchaseMenu;
    private final Stage stage;
    private boolean pauseOpened = false;
    public boolean purchaseOpened = false;

    private boolean DEBUG = false;
    private Box2DDebugRenderer b2dr;
    public World world;

    private MapManager mapManager;
    private MapObjectHandler moh;
    public Body player;
    private final float SCALE = 2.0f;

    private float time = 0;

    boolean cameraLock = false;

    public OrthographicCamera camera;
    CameraInputProcessor cameraInputProcessor;
    PlayerInputProcessor playerInputProcessor;

    MenusInputProcessor menusInputProcessor;
    InputMultiplexer inputMultiplexer;
    public ArrayList<FoodSource> forage = new ArrayList<FoodSource>();
    public ArrayList<FoodSource> candy = new ArrayList<FoodSource>();

    //List for storing all colonies
    public ArrayList<Colony> colonies = new ArrayList<Colony>();

    public MainScreen(final AntGame game) {
        inputMultiplexer = new InputMultiplexer();

        this.game = game;
        mapManager = new MapManager();
        stage = new Stage();

        //Camera initilization
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH / SCALE, SCREEN_HEIGHT / SCALE);
//        camera.setToOrtho(false, 32, 32);
        this.cameraInputProcessor = new CameraInputProcessor(camera);

        //World/debug renderer initialization
        world = new World(new Vector2(0, 0), false);
        mapManager.setup(game.batch, world, 0, 1, 30, 5);
        b2dr = new Box2DDebugRenderer();
        b2dr.setDrawBodies(true);
        b2dr.setDrawVelocities(true);
        b2dr.setDrawJoints(true);

        player = RenderUtils.createBox(0,0,32,32,true, world);

        cameraInputProcessor.selectedObject = player;


        //Colony creation testing
//        ColonyUtils.createColony("test1", false, 100, 100, 10,  1, 1, camera,this);

        moh = new MapObjectHandler(mapManager.getMap(), this);
        moh.setup();
        world.setContactListener(moh);

        for (Colony c : colonies) {
            ColonyUtils.addAnt(c, "Worker", camera, world, moh);
            ColonyUtils.addAnt(c, "Worker", camera, world, moh);
        }


        //print all colonies and their world positions
        for (Colony c : colonies) {
            System.out.println("Colony " + c.getName() + " at " + c.getColonyBody().getPosition().x*PPM + ", " + c.getColonyBody().getPosition().y*PPM );
        }

        //this will eventually be removed
        setPlayerInputProcessor(player);
        menusInputProcessor = new MenusInputProcessor(game.gameState, this);


        inputMultiplexer.addProcessor(playerInputProcessor);
        inputMultiplexer.addProcessor(cameraInputProcessor);
        inputMultiplexer.addProcessor(menusInputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);

        //set camera position to the center of the box
        camera.position.set(player.getPosition().x * PPM, player.getPosition().y * PPM, 0);

        pauseMenu = new PauseMenu(game);
        purchaseMenu = new PurchaseMenu(game, this);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Matrix4 originalProjection = game.batch.getProjectionMatrix();
//        Matrix4 originalTransform = game.batch.getTransformMatrix();
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cameraUpdate(delta);
        game.batch.setProjectionMatrix(camera.combined);


        if (game.gameState.paused && !pauseOpened) {
            pauseMenu.setInput();
            pauseOpened = true;
        } else if (!game.gameState.paused && pauseOpened) {
            Gdx.input.setInputProcessor(inputMultiplexer);
            pauseOpened = false;
        }

        if (game.gameState.purchaseMenuOpen && !purchaseOpened) {
            purchaseMenu.setInput();
            purchaseOpened = true;
        } else if (!game.gameState.purchaseMenuOpen && purchaseOpened) {
            Gdx.input.setInputProcessor(inputMultiplexer);
            purchaseOpened = false;
        }

        game.batch.begin();
        stage.draw();
        game.batch.end();

        // this will fix it. trust
        // it didnt. it's so joever
//        game.batch.setProjectionMatrix(new Matrix4());

        game.batch.setProjectionMatrix(originalProjection);
//        game.batch.setTransformMatrix(originalTransform);

        mapManager.render(camera);

        //for future use when debugging is needed
        b2dr.render(world, camera.combined.scl(PPM));

        for (FoodSource basicFood : forage) {
            basicFood.render();
        }
        for (FoodSource candyFood : candy) {
            candyFood.render();
        }

        for (Colony colony : colonies) {
            colony.render();
        }

        if (game.gameState.paused) {
            this.pauseMenu.draw(delta, world, camera.combined.scl(PPM));
        }

        if (game.gameState.purchaseMenuOpen) {
            this.purchaseMenu.draw(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        camera.viewportHeight = height / SCALE;
        camera.viewportWidth = width / SCALE;
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        mapManager.dispose();
        world.dispose();
        b2dr.dispose();
        pauseMenu.dispose();
        purchaseMenu.dispose();
        for (Colony colony : colonies) {
            colony.dispose();
        }
    }

    //Non tick based renderings
    public void update(float delta) {
        world.step(1/60f, 6, 2);
        moh.handleAI(delta);
        moh.update(delta);
        inputUpdate(delta);
        cameraUpdate(delta);

        //Update colony stuff
        //change constants for health and recourses consumption rate
        //make to so colony update happens every 5 seconds
        time += delta;
        if (time > COLONY_UPDATE_INTERVAL) {
            updateColony(delta);
            System.out.println("Colony updated");
            time = 0;
        }
        isGameOver(this);
        //updateColony(delta);
        //isGameOver(this);


        // temporary (proven works)
//        int dummyNumber = game.gameState.currentGame.getDummyNumber() + 1;
//        System.out.println(dummyNumber);
//        if (dummyNumber > 100) {
//            dummyNumber = 0;
//        }
//        game.gameState.currentGame.setDummyNumber(dummyNumber);
    }

    public void inputUpdate(float delta) {
        // do not process camera if paused
        if (game.gameState.paused) {
            return;
        }
        if (game.gameState.purchaseMenuOpen) {
            return;
        }

        //if = is pressed, use action from cameraInputProcessor
        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.EQUALS)) {
            cameraInputProcessor.keyDown(Input.Keys.EQUALS);
        }
        //if - is pressed, use action from cameraInputProcessor
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.MINUS)) {
            cameraInputProcessor.keyDown(Input.Keys.MINUS);
        }

        //camera controlls with arrow keys
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            cameraInputProcessor.keyDown(Input.Keys.LEFT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            cameraInputProcessor.keyDown(Input.Keys.RIGHT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) {
            cameraInputProcessor.keyDown(Input.Keys.UP);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            cameraInputProcessor.keyDown(Input.Keys.DOWN);
        }

        //if 2 keys pressed at the same time, move diagonally (camera)
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cameraInputProcessor.keyDown2(Input.Keys.LEFT, CAMERA_DIAGONAL_MOVE_SPEED_MODIFIER);
            cameraInputProcessor.keyDown2(Input.Keys.UP, CAMERA_DIAGONAL_MOVE_SPEED_MODIFIER);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cameraInputProcessor.keyDown2(Input.Keys.RIGHT, CAMERA_DIAGONAL_MOVE_SPEED_MODIFIER);
            cameraInputProcessor.keyDown2(Input.Keys.UP, CAMERA_DIAGONAL_MOVE_SPEED_MODIFIER);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cameraInputProcessor.keyDown2(Input.Keys.LEFT, CAMERA_DIAGONAL_MOVE_SPEED_MODIFIER);
            cameraInputProcessor.keyDown2(Input.Keys.DOWN, CAMERA_DIAGONAL_MOVE_SPEED_MODIFIER);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cameraInputProcessor.keyDown2(Input.Keys.RIGHT, CAMERA_DIAGONAL_MOVE_SPEED_MODIFIER);
            cameraInputProcessor.keyDown2(Input.Keys.DOWN, CAMERA_DIAGONAL_MOVE_SPEED_MODIFIER);
        }

        //if c is pressed, lock camera to selected object
        if (Gdx.input.isKeyPressed(Input.Keys.C) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.C)) {
            cameraInputProcessor.keyDown(Input.Keys.C);
            setCameraLock(false);
            //set all colonies to unselected (since not focused anymore)
            for (Colony c : colonies) {
                c.setSelected(false);
            }
        }

        //if p is pressed, purchase ant
        if (Gdx.input.isKeyPressed(Input.Keys.P) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.P)) {
            cameraInputProcessor.keyDown(Input.Keys.P);
        }

        //Player inputs
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
            playerInputProcessor.keyDown(Input.Keys.A);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
            playerInputProcessor.keyDown(Input.Keys.D);
        } else if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
            playerInputProcessor.keyDown(Input.Keys.W);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
            playerInputProcessor.keyDown(Input.Keys.S);
        }

        //if 2 keys pressed at the same time, move diagonally (player)
        if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerInputProcessor.keyDown2(Input.Keys.A, Input.Keys.W, PLAYER_DIAGONAL_MOVE_SPEED_MODIFIER);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerInputProcessor.keyDown2(Input.Keys.D, Input.Keys.W, PLAYER_DIAGONAL_MOVE_SPEED_MODIFIER);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerInputProcessor.keyDown2(Input.Keys.A, Input.Keys.S, PLAYER_DIAGONAL_MOVE_SPEED_MODIFIER);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerInputProcessor.keyDown2(Input.Keys.D, Input.Keys.S, PLAYER_DIAGONAL_MOVE_SPEED_MODIFIER);
        }

        //if mouse is clicked call camerainputprocessor touchdown with click position
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            cameraInputProcessor.touchDown(Gdx.input.getX(), Gdx.input.getY(), 0, 0, this);
        }

        //reset player velocity if no keys are pressed
        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D) &&
                !Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.setLinearVelocity(0, 0);
        }





    }
    public void cameraUpdate(float delta) {
        if (cameraLock) {
            Vector3 position = camera.position;
            position.x = player.getPosition().x * PPM;
            position.y = player.getPosition().y * PPM;
            camera.position.set(position);
        }
        camera.update();
    }

    public void setCameraLock(boolean val) {
        this.cameraLock = val;
    }

    public void setPlayerInputProcessor(Body body) {
        this.playerInputProcessor = new PlayerInputProcessor(body);
    }

    public void focusCameraOnColony(Body colony) {
        player = colony;
        camera.position.set(colony.getPosition().x * PPM, colony.getPosition().y * PPM, 0);
    }

    public void focusCameraOnAnt(Body ant) {
        player = ant;
        camera.position.set(ant.getPosition().x * PPM, ant.getPosition().y * PPM, 0);
    }

    public MapObjectHandler getMapObjectHandler() {
        return moh;
    }


    //check if the game is over, game is over when the player owned colony as 0 resources and 0 ants, or the player colony has 0 health
    public static boolean isGameOver(MainScreen screen) {
        for (Colony colony : screen.colonies) {
            if (colony.isPlayerOwned()) {
                if (colony.getResources() <= 0 && colony.getAntsAlive() <= 0) {
                    System.out.println("Game over!");
                    return true;
                }
                if (colony.getHealth() <= 0) {
                    System.out.println("Game over!");
                    return true;
                }
            }
        }
        return false;
    }

    //update colony (per tick)
    public void updateColony(float delta) {
        for (Colony colony : colonies) {
            colony.consumeResources(COLONY_RESOURCE_CONSUMPTION_LOSS);
            System.out.println("Colony " + colony.getName() + " has " + colony.getResources() + " resources");
            if (colony.getResources() <= 0) {
                colony.decreaseHealth(COLONY_STARVATION_HEALTH_LOSS);
                System.out.println("Colony " + colony.getName() + " is starving!");
            }
        }
    }
}

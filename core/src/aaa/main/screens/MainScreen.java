package aaa.main.screens;

import aaa.main.AntGame;
import aaa.main.game.input.CameraInputProcessor;
import aaa.main.game.input.PlayerInputProcessor;
import aaa.main.game.map.Colony;
import aaa.main.game.map.MapManager;
import aaa.main.game.map.MapObjectHandler;
import aaa.main.stages.PauseMenu;
import aaa.main.util.ColonyUtils;
import aaa.main.util.RenderUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

import static aaa.main.util.Constants.*;

public class MainScreen extends ScreenAdapter {
    private final AntGame game;
    private final PauseMenu pauseMenu;
    private final Stage stage;
    private boolean pauseOpened = false;

    private boolean DEBUG = false;
    private Box2DDebugRenderer b2dr;
    public World world;

    private MapManager mapManager;
    private MapObjectHandler moh;
    private Body player;
    private final float SCALE = 2.0f;

    boolean cameraLock = false;

    public OrthographicCamera camera;
    CameraInputProcessor cameraInputProcessor;
    PlayerInputProcessor playerInputProcessor;
    InputMultiplexer inputMultiplexer;

    //List for storing all colonies
    public ArrayList<Colony> colonies = new ArrayList<Colony>();

    public MainScreen(final AntGame game) {
        inputMultiplexer = new InputMultiplexer();

        this.game = game;
        mapManager = new MapManager();
        mapManager.setup(game.batch, 0, 3);
        stage = new Stage();

        //Camera initilization
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH / SCALE, SCREEN_HEIGHT / SCALE);
//        camera.setToOrtho(false, 32, 32);
        this.cameraInputProcessor = new CameraInputProcessor(camera);

        //World/debug renderer initialization
        world = new World(new Vector2(0, 0), false);
        b2dr = new Box2DDebugRenderer();

        player = RenderUtils.createBox(0,0,32,32,false, world);


        //Colony creation testing
//        ColonyUtils.createColony("test1", false, 100, 100, 10,  1, 1, camera,this);

        moh = new MapObjectHandler(mapManager.getMap(), this);
        moh.setup();

        for (Colony c : colonies) {
            ColonyUtils.addAnt(c, "Worker", camera, world, moh);
            ColonyUtils.addAnt(c, "Worker", camera, world, moh);
        }

        playerInputProcessor = new PlayerInputProcessor(player);

        inputMultiplexer.addProcessor(playerInputProcessor);
        inputMultiplexer.addProcessor(cameraInputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);

        //set camera position to the center of the box
        camera.position.set(player.getPosition().x * PPM, player.getPosition().y * PPM, 0);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent ev, int keycode) {
                if (keycode == com.badlogic.gdx.Input.Keys.ESCAPE) {
                    game.gameState.paused = true;
                }
                return true;
            }
        });
        pauseMenu = new PauseMenu(game);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);

        if (game.gameState.paused && !pauseOpened) {
            pauseMenu.setInput();
            pauseOpened = true;
        } else if (!game.gameState.paused && pauseOpened) {
            Gdx.input.setInputProcessor(stage);
            pauseOpened = false;
        }
        game.batch.begin();
        stage.draw();
        game.batch.end();

        game.batch = new SpriteBatch();

        mapManager.render(camera);

        //for future use when debugging is needed
        b2dr.render(world, camera.combined.scl(PPM));

        for (Colony colony : colonies) {
            colony.render(game.batch);
        }

        if (game.gameState.paused) {
            this.pauseMenu.draw(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
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
        for (Colony colony : colonies) {
            colony.dispose();
        }
    }

    //Non tick based renderings
    public void update(float delta) {
        world.step(1/60f, 6, 2);
        inputUpdate(delta);
        cameraUpdate(delta);

    }

    public void inputUpdate(float delta) {
        // do not process camera if paused
        if (game.gameState.paused) {
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

}

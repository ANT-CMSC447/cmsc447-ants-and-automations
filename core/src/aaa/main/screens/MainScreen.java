package aaa.main.screens;

import aaa.main.AntGame;
import aaa.main.game.Colony;
import aaa.main.game.PlayerInputProcessor;
import aaa.main.stages.PauseMenu;
import aaa.main.game.CameraInputProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import static aaa.main.util.Constants.*;

public class MainScreen extends ScreenAdapter {
    private final AntGame game;
    private final PauseMenu pauseMenu;
    private final Stage stage;
    private boolean pauseOpened = false;


    private boolean DEBUG = false;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera camera;
    private World world;
    private Body player ,borderUP, borderDOWN, borderLEFT, borderRIGHT, circle;
    private final float SCALE = 2.0f;

    boolean cameraLock = false;

    OrthographicCamera camera;
    CameraInputProcessor cameraInputProcessor;
    PlayerInputProcessor playerInputProcessor;
    InputMultiplexer inputMultiplexer;
    private final Colony[] antColonies = new Colony[GLOBAL_MAX_COLONY];
    public MainScreen(final AntGame game) {
        inputMultiplexer = new InputMultiplexer();

        this.game = game;
        stage = new Stage();

        //Camera initilization
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH / SCALE , SCREEN_HEIGHT / SCALE);

        Gdx.input.setInputProcessor(cameraInputProcessor);
        this.cameraInputProcessor = new CameraInputProcessor(camera);

        //World/debug renderer initialization
        world = new World(new Vector2(0, 0), false);
        b2dr = new Box2DDebugRenderer();

        player = createBox(0,0,32,32,false);

        playerInputProcessor = new PlayerInputProcessor(player);

        inputMultiplexer.addProcessor(playerInputProcessor);
        inputMultiplexer.addProcessor(cameraInputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);

        //World border definition
        borderUP = createBox(0, (float) BORDER_HEIGHT /2, BORDER_WIDTH, 1, true);
        borderDOWN = createBox(0, (float) -BORDER_HEIGHT /2, BORDER_WIDTH, 1, true);
        borderLEFT = createBox((float) -BORDER_WIDTH /2, 0, 1, BORDER_HEIGHT, true);
        borderRIGHT = createBox((float) BORDER_WIDTH /2, 0, 1, BORDER_HEIGHT, true);

        //set camera position to the center of the box
        camera.position.set(player.getPosition().x * PPM, player.getPosition().y * PPM, 0);

//        Label.LabelStyle labelStyle = new Label.LabelStyle();
//        labelStyle.fontColor = Color.BLUE;
//        labelStyle.font = game.font;
//        Label mainText = new Label("Main Screen", labelStyle);
//        mainText.setPosition(450f, 400f);
//
//        stage.addActor(mainText);

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

        //for future use when debugging is needed
        b2dr.render(world, camera.combined.scl(PPM));

        if (game.gameState.paused && !pauseOpened) {
            pauseMenu.setInput();
            pauseOpened = true;
        } else if (!game.gameState.paused && pauseOpened) {
            Gdx.input.setInputProcessor(stage);
            pauseOpened = false;
        }
        game.batch.begin();
        stage.draw();
        antColonies[0].render(game.batch);
        if (game.gameState.paused) {
            this.pauseMenu.draw(delta);
        }
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, (float) width / SCALE, (float) height / SCALE);
//        viewport.update(width, height);
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
        world.dispose();
        b2dr.dispose();
        pauseMenu.dispose();
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

        //Player inputs
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            playerInputProcessor.keyDown(Input.Keys.LEFT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            playerInputProcessor.keyDown(Input.Keys.RIGHT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) {
            playerInputProcessor.keyDown(Input.Keys.UP);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            playerInputProcessor.keyDown(Input.Keys.DOWN);
        } else {
            playerInputProcessor.resetVelocity();
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

    //helper function for creating a box.
    public Body createBox(float x, float y, float width, float height, boolean isStatic) {
        BodyDef def = new BodyDef();

        if (isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        Body pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();

        return pBody;
    }

    public void setCameraLock(boolean val) {
        this.cameraLock = val;
    }
}

package aaa.main.screens;

import aaa.main.AntGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen extends ScreenAdapter {
    public static final int SCREEN_WIDTH = 1080;
    public static final int SCREEN_HEIGHT = 720;
//    OrthographicCamera camera;
//    Viewport viewport;

    private AntGame game;
    private Texture img;

    public MainScreen(AntGame game) {
        this.game = game;
        img = new Texture("ant.png");
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == com.badlogic.gdx.Input.Keys.ESCAPE) {
                    //show pause menu
                    //game.setScreen(new MenuScreen(game));
                }
                if (keycode == com.badlogic.gdx.Input.Keys.SPACE) {
                    game.setScreen(new MenuScreen(game));
                }
                return true;
            }
        });
    }

//    public void initializeViewport() {
//        camera = new OrthographicCamera();
//        viewport = new ScalingViewport(Scaling.fit, SCREEN_WIDTH, SCREEN_HEIGHT, camera);
//    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.font.setColor(Color.RED);
        game.batch.draw(img, 0, 0, 300, 300);
        game.font.draw(game.batch, "Main Game", 310, 310);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
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
        img.dispose();
    }
}

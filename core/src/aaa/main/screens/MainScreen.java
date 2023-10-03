package aaa.main.screens;

import aaa.main.AntGame;
import aaa.main.stages.PauseMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class MainScreen extends ScreenAdapter {
    public static final int SCREEN_WIDTH = 1080;
    public static final int SCREEN_HEIGHT = 720;

    private final AntGame game;
    private final PauseMenu pauseMenu;
    private final Stage stage;
    private final Texture tex;

    private boolean pauseOpened = false;

    public MainScreen(final AntGame game) {
        this.game = game;
        tex = new Texture("ant.png");
        stage = new Stage();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.BLUE;
        labelStyle.font = game.font;
        Label mainText = new Label("Main Screen", labelStyle);
        mainText.setPosition(450f, 400f);
        
        Image image = new Image(tex);
        image.setSize(300f, 300f);
        
        stage.addActor(mainText);
        stage.addActor(image);

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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (game.gameState.paused && !pauseOpened) {
            Gdx.input.setInputProcessor(pauseMenu.getStage());
            pauseOpened = true;
        } else if (!game.gameState.paused && pauseOpened) {
            Gdx.input.setInputProcessor(stage);
            pauseOpened = false;
        }
        game.batch.begin();
        stage.draw();
        if (game.gameState.paused) {
            this.pauseMenu.getStage().draw();
        }
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
        tex.dispose();
    }
}

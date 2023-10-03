package aaa.main.screens;

import aaa.main.AntGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuScreen extends ScreenAdapter {

    private AntGame game;
    private Stage stage;
    private Table mainTable;

    public MenuScreen(final AntGame game) {
        this.game = game;
        stage = new Stage();
        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        VerticalGroup subTable = new VerticalGroup();


        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.BLUE;
        labelStyle.font = game.font;
        Label menuText = new Label("Menu Screen", labelStyle);

        TextButton newGame = new TextButton("New Game", game.buttonStyle);
        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                game.setScreen(new MainScreen(game));
            }
        });

        TextButton loadGame = new TextButton("Load Game", game.buttonStyle);
        loadGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                game.setScreen(new MainScreen(game));
            }
        });

        TextButton exit = new TextButton("Quit", game.buttonStyle);
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                game.quit();
            }
        });


        subTable.addActor(menuText);
        subTable.addActor(newGame);
        subTable.addActor(loadGame);
        subTable.addActor(exit);


        mainTable.add(subTable);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        stage.act();
        stage.draw();
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

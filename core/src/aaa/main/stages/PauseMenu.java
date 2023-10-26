package aaa.main.stages;

import aaa.main.AntGame;
import aaa.main.screens.MainScreen;
import aaa.main.screens.MenuScreen;
import aaa.main.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PauseMenu {
    private AntGame game;
    private Stage stage;

    private Pixmap pauseBg;
    private Texture pauseTex;
    private TextureRegion pauseTexReg;
    private TextureRegionDrawable pauseTexRegDraw;

    public PauseMenu(final AntGame game) {
        this.game = game;
        stage = new Stage();
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == com.badlogic.gdx.Input.Keys.ESCAPE) {
                    System.out.println("ESC Pressed");
                    game.gameState.paused = false;
                }
                return true;
            }
        });

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        pauseBg = new Pixmap(1, 1, Pixmap.Format.RGBA4444);
        pauseBg.setColor(0f, 0f, 0f, 0.6f);
        pauseBg.fill();
        pauseTex = new Texture(pauseBg);
        pauseTexReg = new TextureRegion(pauseTex);
        pauseTexRegDraw = new TextureRegionDrawable(pauseTexReg);
        mainTable.setBackground(pauseTexRegDraw);

        TextButton resumeGame = new TextButton("Resume Game", game.buttonStyle);
        resumeGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                game.gameState.paused = false;
            }
        });

        TextButton exitToMenu = new TextButton("Exit to Menu", game.buttonStyle);
        exitToMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                game.gameState.paused = false;
                game.setScreen(new MenuScreen(game));
            }
        });

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.ORANGE;
        labelStyle.font = game.font;
        Label pauseText = new Label("Pause Menu", labelStyle);
//        pauseText.setPosition(450f, 350f);

        VerticalGroup verticalGroup = new VerticalGroup();
        // this needs to be set because for some reason, this vertical group
        // does not behave the same as the one in MenuScreen - its position is set to 0,0
        // instead of the center of the screen.
        verticalGroup.setPosition(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 2);
        verticalGroup.addActor(pauseText);
        verticalGroup.addActor(resumeGame);
        verticalGroup.addActor(exitToMenu);
        mainTable.addActor(verticalGroup);

//        stage.addActor(pauseText);
//        stage.setDebugAll(true);
    }

    public void setInput() {
        Gdx.input.setInputProcessor(stage);
    }

    public void draw(float delta) {
        stage.draw();
    }

    public void update() {

    }

    public void dispose() {
        pauseBg.dispose();
        pauseTex.dispose();
        stage.dispose();
    }
}

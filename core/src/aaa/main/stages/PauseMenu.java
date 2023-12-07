package aaa.main.stages;

import aaa.main.AntGame;
import aaa.main.screens.MenuScreen;
import aaa.main.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PauseMenu {
    private AntGame game;
    private Stage stage;
    private Pixmap pauseBg;
    private Texture pauseTex;
    private TextureRegion pauseTexReg;
    private TextureRegionDrawable pauseTexRegDraw;
    private Table mainTable;

    public PauseMenu(final AntGame game) {
        this.game = game;
        stage = new Stage();

        mainTable = new Table();
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

        TextButton saveAndExit = new TextButton("Save and Exit", game.ExitbuttonStyle);
        saveAndExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                game.saveState(); // Save the game state
                game.gameState.paused = false; // The game should not be paused on next load
                game.setScreen(new MenuScreen(game)); // Return to the main menu
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
        verticalGroup.addActor(saveAndExit);
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

    public void resize(int width, int height) {
        if (mainTable != null) {
            // Set the position of the table manually
            mainTable.setPosition(width / 2f - mainTable.getWidth() / 2f, height / 2f - mainTable.getHeight() / 2f);

            // Update the layout
            mainTable.invalidateHierarchy();
        }
    }

    public void dispose() {
        pauseBg.dispose();
        pauseTex.dispose();
        stage.dispose();
    }
}

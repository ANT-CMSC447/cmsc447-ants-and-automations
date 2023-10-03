package aaa.main.stages;

import aaa.main.AntGame;
import aaa.main.screens.MainScreen;
import aaa.main.screens.MenuScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PauseMenu {
    private AntGame game;
    private Stage stage;

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
        mainTable.setColor(0,0,0,0.2f);

        TextButton resumeGame = new TextButton("Resume Game", game.buttonStyle);
        resumeGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                game.gameState.paused = false;
            }
        });

        TextButton exit = new TextButton("Exit to Menu", game.buttonStyle);
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                game.gameState.paused = false;
                game.setScreen(new MenuScreen(game));
            }
        });

        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.addActor(resumeGame);
        verticalGroup.addActor(exit);
        mainTable.addActor(verticalGroup);
    }

    public Stage getStage() {
        return stage;
    }
}

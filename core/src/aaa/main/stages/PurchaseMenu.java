package aaa.main.stages;

import aaa.main.AntGame;
import aaa.main.game.map.Colony;
import aaa.main.screens.MainScreen;
import aaa.main.screens.MenuScreen;
import aaa.main.util.ColonyUtils;
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

public class PurchaseMenu {
    private AntGame game;
    private Stage stage;
    private Pixmap pauseBg;
    private Texture pauseTex;
    private TextureRegion pauseTexReg;
    private TextureRegionDrawable pauseTexRegDraw;
    private Table mainTable;

    private MainScreen screen;

    public PurchaseMenu(final AntGame game, MainScreen Screen) {
        this.screen = Screen;
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


        TextButton PurchaseBasicAnt = new TextButton("Purchase Basic Ant", game.buttonStyle);
        PurchaseBasicAnt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                game.gameState.paused = false;

                //get the selected colony
                Colony selectedColony = null;
                for (Colony c : screen.colonies) {
                    if (c.isSelected()) {
                        selectedColony = c;
                        System.out.println("Found selected colony: " + c.getName());
                    }
                }

                if (selectedColony != null) {
                    //Purchase ant
                    ColonyUtils.purchaseAnt(selectedColony, screen);
                }
            }
        });

        TextButton PurchaseWorkerAnt = new TextButton("Purchase Worker Ant", game.buttonStyle);
        PurchaseWorkerAnt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                game.gameState.paused = false;

                //get the selected colony
                Colony selectedColony = null;
                for (Colony c : screen.colonies) {
                    if (c.isSelected()) {
                        selectedColony = c;
                        System.out.println("Found selected colony: " + c.getName());
                    }
                }

                if (selectedColony != null) {
                    //Purchase ant
                    ColonyUtils.purchaseWorkerAnt(selectedColony, screen);
                }
            }
        });

        TextButton PurchaseSoldierAnt = new TextButton("Purchase Soldier Ant", game.buttonStyle);
        PurchaseSoldierAnt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                game.gameState.paused = false;

                //get the selected colony
                Colony selectedColony = null;
                for (Colony c : screen.colonies) {
                    if (c.isSelected()) {
                        selectedColony = c;
                        System.out.println("Found selected colony: " + c.getName());
                    }
                }

                if (selectedColony != null) {
                    //Purchase ant
                    ColonyUtils.purchaseSoldierAnt(selectedColony, screen);
                }
            }
        });

        TextButton ExitButton = new TextButton("Exit", game.ExitbuttonStyle);
        ExitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                game.saveState(); // Save the game state
                game.gameState.purchaseMenuOpen = false; // The game should not be paused on next load
            }
        });

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.ORANGE;
        labelStyle.font = game.font;
        Label pauseText = new Label("Purchase Menu", labelStyle);
//        pauseText.setPosition(450f, 350f);

        VerticalGroup verticalGroup = new VerticalGroup();
        // this needs to be set because for some reason, this vertical group
        // does not behave the same as the one in MenuScreen - its position is set to 0,0
        // instead of the center of the screen.
        verticalGroup.setPosition(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 2);
        verticalGroup.addActor(pauseText);
        verticalGroup.addActor(PurchaseBasicAnt);
        verticalGroup.addActor(PurchaseWorkerAnt);
        verticalGroup.addActor(PurchaseSoldierAnt);
        verticalGroup.addActor(ExitButton);
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

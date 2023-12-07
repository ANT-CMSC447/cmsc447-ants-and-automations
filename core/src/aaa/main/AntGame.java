package aaa.main;

import aaa.main.game.state.GameState;
import aaa.main.screens.MenuScreen;
import aaa.main.stages.PauseMenu;
import aaa.main.util.Constants;
import aaa.main.util.GameSave;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class AntGame extends Game {
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public BitmapFont font;
	public GameState gameState;
	public TextButton.TextButtonStyle buttonStyle;
	public TextButton.TextButtonStyle ExitbuttonStyle;
	private PauseMenu pauseMenu;
	@Override
	public void create () {
//		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode()); // enable fullscreen ?
		gameState = new GameState(); // don't load unless the load button is pressed
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
		buttonStyle = new TextButton.TextButtonStyle();
		buttonStyle.font = this.font;
		buttonStyle.fontColor = Color.WHITE;

		ExitbuttonStyle = new TextButton.TextButtonStyle();
		ExitbuttonStyle.font = this.font;
		ExitbuttonStyle.fontColor = Color.RED;
		// set the screen size properly
		Gdx.graphics.setWindowedMode(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		setScreen(new MenuScreen(this));
		pauseMenu = new PauseMenu(this);
	}

	public GameState loadState() {
		return GameSave.load();
	}

	// Save game state to JSON file
	public void saveState() {
		GameSave.save(gameState);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		pauseMenu.resize(width, height);
	}

	@Override
	public void pause() {
		saveState();
	}

	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
	}

	public void saveAndQuit() {
		saveState(); // Save the game state
		Gdx.app.exit(); // Terminate the game
	}
}

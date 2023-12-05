package aaa.main;

import aaa.main.game.GameState;
import aaa.main.screens.MainScreen;
import aaa.main.screens.MenuScreen;
import aaa.main.stages.PauseMenu;
import aaa.main.util.Constants;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;

public class AntGame extends Game {
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public BitmapFont font;
	public GameState gameState;
	public TextButton.TextButtonStyle buttonStyle;
	private PauseMenu pauseMenu;
	@Override
	public void create () {
//		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode()); // enable fullscreen ?
		gameState = loadState();
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
		buttonStyle = new TextButton.TextButtonStyle();
		buttonStyle.font = this.font;
		buttonStyle.fontColor = Color.RED;
		// set the screen size properly
		Gdx.graphics.setWindowedMode(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		setScreen(new MenuScreen(this));
		pauseMenu = new PauseMenu(this);
	}

	public GameState loadState() {
		// eventually handle save state loading here
		Json json = new Json();
		String filePath = Gdx.files.getLocalStoragePath() + "save.json";

		// Read JSON data from the file
		FileHandle file = Gdx.files.local(filePath);
		if (file.exists()) {
			String jsonData = file.readString();
			return json.fromJson(GameState.class, jsonData);
		} else {
			return new GameState();
		}
	}

	// Save game state to JSON file
	public void saveState() {
		Json json = new Json();
		String jsonData = json.toJson(gameState);

		// Determine file location based on the operating system
		String filePath;
		if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
			// Desktop (Linux, Windows, Mac)
			filePath = Gdx.files.getLocalStoragePath() + "save.json";
		} else if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
			// HTML
			filePath = Gdx.files.getLocalStoragePath() + "save.json";
		} else {
			// Handle other platforms if needed
			filePath = "save.json";
		}

		// Write JSON data to the file
		FileHandle file = Gdx.files.local(filePath);
		file.writeString(jsonData, false);
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

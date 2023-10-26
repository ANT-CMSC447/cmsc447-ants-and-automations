package aaa.main;

import aaa.main.game.GameState;
import aaa.main.screens.MainScreen;
import aaa.main.screens.MenuScreen;
import aaa.main.util.Constants;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;

public class AntGame extends Game {
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public BitmapFont font;

	public GameState gameState;

	public TextButton.TextButtonStyle buttonStyle;
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
	}

	public GameState loadState() {
		// eventually handle save state loading here
		return new GameState();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}

	public void quit() {
		this.pause();
		this.dispose();
		Gdx.app.exit();
	}
}

package aaa.main;

import aaa.main.screens.MainScreen;
import aaa.main.screens.MenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class AntGame extends Game {
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public BitmapFont font;
	@Override
	public void create () {
//		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode()); // enable fullscreen ?
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
		setScreen(new MenuScreen(this));
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}

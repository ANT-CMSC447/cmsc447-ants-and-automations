package aaa.main.game;

import aaa.main.screens.MainScreen;
import aaa.main.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;


public class CameraInputProcessor implements InputProcessor {
    private OrthographicCamera camera;

    public CameraInputProcessor(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {


        //Camera zoom controls
        if (keycode == Input.Keys.MINUS && camera.zoom <= Constants.MAX_ZOOM) {
            //zoom out
            camera.zoom += 0.02f;
        } else if (keycode == Input.Keys.EQUALS && camera.zoom >= Constants.MIN_ZOOM) {
            //zoom in
            camera.zoom -= 0.02f;
        }

        //move camera based on arrow keys (doesnt work when set to ortho)
        //test msg
        /*
        if (keycode == Input.Keys.LEFT) {
            //move left
            camera.translate(-32, 0);
        } else if (keycode == Input.Keys.RIGHT) {
            //move right
            camera.translate(32, 0);
        } else if (keycode == Input.Keys.UP) {
            //move up
            camera.translate(0, 32);
        } else if (keycode == Input.Keys.DOWN) {
            //move down
            camera.translate(0, -32);
        }
        */

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}

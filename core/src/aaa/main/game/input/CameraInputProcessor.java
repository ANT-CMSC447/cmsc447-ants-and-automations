package aaa.main.game.input;

import aaa.main.screens.MainScreen;
import aaa.main.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import aaa.main.screens.MainScreen;

import static aaa.main.util.Constants.CAMERA_MOVE_SPEED;


public class CameraInputProcessor implements InputProcessor {
    private OrthographicCamera camera;
    private float lastX, lastY;

    public CameraInputProcessor(OrthographicCamera camera) {
        this.camera = camera;
    }

    //handler for horizontal/vertical movement
    @Override
    public boolean keyDown(int keycode) {
        //Camera zoom controls
        if (keycode == Input.Keys.MINUS) {
            //zoom out
            if (camera.zoom + 0.02f <= Constants.MAX_ZOOM) {
                camera.zoom += 0.02f;
            } else {
                camera.zoom = Constants.MAX_ZOOM;
            }
        } else if (keycode == Input.Keys.EQUALS) {
            //zoom in
            if (camera.zoom - 0.02f >= Constants.MIN_ZOOM) {
                camera.zoom -= 0.02f;
            } else {
                camera.zoom = Constants.MIN_ZOOM;
            }
        }

        //Camera movement controls
        if (keycode == Input.Keys.LEFT) {
            //move left
            camera.translate(-CAMERA_MOVE_SPEED, 0);
        }
        if (keycode == Input.Keys.RIGHT) {
            //move right
            camera.translate(CAMERA_MOVE_SPEED, 0);
        }
        if (keycode == Input.Keys.UP) {
            //move up
            camera.translate(0, CAMERA_MOVE_SPEED);
        }
        if (keycode == Input.Keys.DOWN) {
            //move down
            camera.translate(0, -CAMERA_MOVE_SPEED);
        }
        return false;
    }

    //handler for diagonal movement
    public boolean keyDown2(int keycode, float modifier) {
        if (keycode == Input.Keys.LEFT) {
            //move left
            camera.translate(-CAMERA_MOVE_SPEED/modifier, 0);
        }
        if (keycode == Input.Keys.RIGHT) {
            //move right
            camera.translate(CAMERA_MOVE_SPEED/modifier, 0);
        }
        if (keycode == Input.Keys.UP) {
            //move up
            camera.translate(0, CAMERA_MOVE_SPEED/modifier);
        }
        if (keycode == Input.Keys.DOWN) {
            //move down
            camera.translate(0, -CAMERA_MOVE_SPEED/modifier);
        }
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
        //logic for switching player context should be to check if position clicked is on a object
        //checks would have to be in world coordinates, not screen coordinates
        //also would need to check for clicks on UI buttons, should be in screen coordinates then
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

package aaa.main.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerInputProcessor implements InputProcessor {
    private float horizontalVelocity;
    private float verticalVelocity;

    private Body player;
    public PlayerInputProcessor(Body player) {
        this.player = player;
    }
    @Override
    public boolean keyDown(int keycode) {

        horizontalVelocity = 0;
        verticalVelocity = 0;

        if (keycode == Input.Keys.LEFT) {
            //move left
            horizontalVelocity = -5;
        } else if (keycode == Input.Keys.RIGHT) {
            //move right
            horizontalVelocity = 5;
        } else if (keycode == Input.Keys.UP) {
            //move up
            verticalVelocity = 5;
        } else if (keycode == Input.Keys.DOWN) {
            //move down
            verticalVelocity = -5;
        }

        player.setLinearVelocity(horizontalVelocity, verticalVelocity);

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

    //reset player velocity when key is released
    public void resetVelocity() {
        player.setLinearVelocity(0, 0);
    }

}

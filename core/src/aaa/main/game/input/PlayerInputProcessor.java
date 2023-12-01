package aaa.main.game.input;

import aaa.main.util.ColonyUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import static aaa.main.util.Constants.PLAYER_MOVE_SPEED;

public class PlayerInputProcessor implements InputProcessor {
    private float horizontalVelocity;
    private float verticalVelocity;

    private Body player;
    public PlayerInputProcessor(Body player) {
        this.player = player;
    }

    //handler for horizontal/vertical movement
    @Override
    public boolean keyDown(int keycode) {

        horizontalVelocity = 0;
        verticalVelocity = 0;

        if (keycode == Input.Keys.A) {
            //move left
            horizontalVelocity = -PLAYER_MOVE_SPEED;
        }
        if (keycode == Input.Keys.D) {
            //move right
            horizontalVelocity = PLAYER_MOVE_SPEED;
        }
        if (keycode == Input.Keys.W) {
            //move up
            verticalVelocity = PLAYER_MOVE_SPEED;
        }
        if (keycode == Input.Keys.S) {
            //move down
            verticalVelocity = -PLAYER_MOVE_SPEED;
        }

        player.setLinearVelocity(horizontalVelocity, verticalVelocity);
        Vector2 ppos = player.getPosition();
        System.out.println("ppos: " + ppos.x + ", " + ppos.y);
        Vector2 mpos = ColonyUtils.getMapCoordinates(ppos);
        System.out.println("mpos: " + mpos.x + ", " + mpos.y);
        return false;
    }

    //handler for diagonal movement
    public boolean keyDown2(int keycode1, int keycode2, float speedModifier) {

        horizontalVelocity = 0;
        verticalVelocity = 0;

        if (keycode1 == Input.Keys.A && keycode2 == Input.Keys.W) {
            horizontalVelocity = -PLAYER_MOVE_SPEED * speedModifier;
            verticalVelocity = PLAYER_MOVE_SPEED * speedModifier;
        }
        if (keycode1 == Input.Keys.A && keycode2 == Input.Keys.S) {
            horizontalVelocity = -PLAYER_MOVE_SPEED * speedModifier;
            verticalVelocity = -PLAYER_MOVE_SPEED * speedModifier;
        }
        if (keycode1 == Input.Keys.D && keycode2 == Input.Keys.W) {
            horizontalVelocity = PLAYER_MOVE_SPEED * speedModifier;
            verticalVelocity = PLAYER_MOVE_SPEED * speedModifier;
        }
        if (keycode1 == Input.Keys.D && keycode2 == Input.Keys.S) {
            horizontalVelocity = PLAYER_MOVE_SPEED * speedModifier;
            verticalVelocity = -PLAYER_MOVE_SPEED * speedModifier;
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

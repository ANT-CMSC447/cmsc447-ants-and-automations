package aaa.main.game.input;

import aaa.main.game.state.GameState;
import aaa.main.screens.MainScreen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class MenusInputProcessor implements InputProcessor {

    private final GameState state;
    private MainScreen screen;

    public MenusInputProcessor(GameState state, MainScreen screen) {
        this.screen = screen;
        this.state = state;
    }
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            if (this.state.purchaseMenuOpen) {
                this.state.purchaseMenuOpen = false;
            } else {
                this.state.paused = !this.state.paused;
            }
        }
        if (keycode == Input.Keys.P) {
            //if any colony is selected then allow opening the purchase menu
            for (int i = 0; i < screen.colonies.size(); i++) {
                if (screen.colonies.get(i).isSelected()) {
                    this.state.purchaseMenuOpen = !this.state.purchaseMenuOpen;
                }
            }
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

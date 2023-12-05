package aaa.main.util;

import aaa.main.game.state.GameState;
import aaa.main.game.state.SerializableGameState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class GameSave {
    public static void save(GameState state) {
        FileHandle file;
        if (Gdx.files.isExternalStorageAvailable()) {
            file = Gdx.files.external(".antgame/save.json");
        } else if (Gdx.files.isLocalStorageAvailable()) {
            file = Gdx.files.local("save.json");
        } else {
            return;
        }

        if (!file.exists()) {
            file.parent().mkdirs();
        }

        Json json = new Json();
        String text = json.toJson(state.currentGame);
        System.out.println(text);
        file.writeString(text, false);
    }

    public static GameState load() {
        FileHandle file;
        GameState state = new GameState();
        if (Gdx.files.isExternalStorageAvailable()) {
            file = Gdx.files.external(".antgame/save.json");
        } else if (Gdx.files.isLocalStorageAvailable()) {
            file = Gdx.files.local("save.json");
        } else {
            return state;
        }

        if (!file.exists()) {
            return state;
        } else {
            String text = file.readString();
            Json json = new Json();
            SerializableGameState stateInner = json.fromJson(SerializableGameState.class, text);
            state.currentGame = stateInner;
            return state;
        }
    }
}

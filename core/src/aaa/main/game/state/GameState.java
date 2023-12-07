package aaa.main.game.state;

import java.util.HashMap;
import java.util.Random;

public class GameState {
    public GameState() {
        this.currentGame = new SerializableGameState();
        /*
         * VV THESE ARE DEFAULT VALUES VV
         */
        this.currentGame.colonies = new HashMap<>();
        this.currentGame.seed = new Random().nextLong();
        /*
         * ^^ SET HERE ^^
         */
    }
    public GameState(SerializableGameState state) {
        this.currentGame = state;
    }

    // current game variables which cannot be saved
    /// variables with defaults:
    public boolean paused = false;
    public boolean purchaseMenuOpen = false;
    /// other game variables:

    /// save game:
    public SerializableGameState currentGame;
}
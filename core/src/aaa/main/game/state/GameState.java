package aaa.main.game.state;

public class GameState {
    public GameState() {
        this.currentGame = new SerializableGameState();
        /*
         * VV THESE ARE DEFAULT VALUES VV
         */
        this.currentGame.dummyVariable = true;
        this.currentGame.dummyNumber = 42;
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
    /// other game variables:

    /// save game:
    public SerializableGameState currentGame;
}
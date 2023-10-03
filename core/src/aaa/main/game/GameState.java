package aaa.main.game;

public class GameState {
    // variables that can be saved to a save file
    private class SerializableGameState {
        //
    }

    // current game variables which cannot be saved
    /// variables with defaults:
    public boolean paused = false;
    /// other game variables:

    /// save game:
    public SerializableGameState currentGame;
}

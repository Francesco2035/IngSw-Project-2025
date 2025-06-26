package org.example.galaxy_trucker.ClientServer.Messages;

/**
 * An interface used for handling game finish events.
 * Listeners implementing this interface can define actions
 * to be executed when a game ends.
 */
public interface FinishListener {
    /**
     * Handles the end-of-game event, providing details about the game's result,
     * a unique identifier, a message, and associated scoreboard data.
     *
     * @param success a boolean indicating whether the game ended successfully
     * @param id a String representing a unique identifier for the game or event
     * @param message a descriptive message related to the end of the game
     * @param event a ScoreboardEvent instance containing score and player data related to the game
     */
    void onEndGame(boolean success, String id, String message, ScoreboardEvent event);

}

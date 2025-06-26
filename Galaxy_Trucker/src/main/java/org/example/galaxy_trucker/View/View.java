package org.example.galaxy_trucker.View;

import org.example.galaxy_trucker.Controller.Messages.*;
import org.example.galaxy_trucker.Controller.Messages.TileSets.*;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;

import java.io.IOException;

/**
 * The View interface represents a contract for the user interface component of the application.
 * It defines methods for updating the state of the game on the interface, interacting with the user,
 * and handling various game and system events.
 */
public interface View {

    /**
     * Updates the board with the details of a new TileEvent.
     *
     * @param tileEvent The TileEvent containing information about the tile update, including its position,
     *                  characteristics, and other associated data. This event represents a change on
     *                  a specific part of the game board.
     */
    // Metodo per aggiornare la board con un nuovo TileEvent
    void updateBoard(TileEvent tileEvent);


    /**
     * Displays a message to the user interface, typically used for notifications or error messages.
     *
     * @param message The message to be displayed on the view.
     */
    // Metodo per mostrare un messaggio alla vista (per esempio, per notifiche o errori)
    void showMessage(String message);

    /**
     * Prompts the user for input by displaying the given message. This method
     * can be used for interaction with the user when specific input is required.
     *
     * @param message The prompt message to be displayed to the user, requesting input.
     * @return A string containing the user's input as entered in response to the prompt.
     */
    // Metodo per chiedere input all'utente, se necessario
    String askInput(String message);

    /**
     * Updates the player's hand with the specified event data.
     * This method is typically used to synchronize the view or client-side
     * representation of the hand with the server-side data.
     *
     * @param event The HandEvent containing information about the player's hand,
     *              including the unique identifier and a list of connectors.
     */
    void updateHand(HandEvent event);

    /**
     * Updates the gameboard display based on the provided game board event.
     * This method is intended to reflect the state or changes of the gameboard
     * on the user interface as specified by the event details.
     *
     * @param event The GameBoardEvent containing the information about the state or
     *              change to be applied to the gameboard.
     */
    void updateGameboard(GameBoardEvent event);

    /**
     * Updates the state of the covered tiles set in the view based on the provided event.
     * This method processes the provided {{@link CoveredTileSetEvent}} instance to update
     * relevant information in the user interface, reflecting changes in the covered tiles set.
     *
     * @param event the event containing information related to the covered tiles set state,
     *              such as the updated size of the set.
     */
    void updateCoveredTilesSet(CoveredTileSetEvent event);

    /**
     * Updates the view with information about the uncovered tiles set based on the provided event.
     * The method is responsible for processing an UncoverdTileSetEvent which contains details
     * about a set of tiles that have been uncovered, including their unique identifiers and
     * associated connectors.
     *
     * @param event The UncoverdTileSetEvent object containing details about the uncovered tiles set.
     *              It includes the unique identifier of the event and a list of connectors
     *              associated with the uncovered tiles.
     */
    void updateUncoveredTilesSet(UncoverdTileSetEvent event);

    /**
     * Displays the current state of the deck provided in the event.
     *
     * @param event the DeckEvent containing the data (e.g., card IDs) for the deck to be displayed
     */
    void showDeck(DeckEvent event);

//    void updateGameBoard(GameBoardEvent event);

    /**
     * Displays the card information on the view based on the details provided in the event.
     *
     * @param event The CardEvent containing details of the card to be displayed.
     */
    void showCard(CardEvent event);

    /**
     * Disconnects the view from the current session or connection.
     * This method is typically invoked when a client needs to terminate
     * its association with the view, such as during a connection switch
     * or when ending a session.
     *
     * This action ensures any resources or references tied to the current
     * connection are properly released, preparing the view for potential
     * reconnection or shutdown.
     */
    void disconnect();

    /**
     * Establishes a connection between the view and its corresponding
     * client or server infrastructure. This method should be called to
     * initiate the necessary communication channels for the view to function.
     *
     * @throws IOException if an I/O error occurs while trying to connect.
     */
    void connect() throws IOException;

    /**
     * Updates the gameboard state to reflect the specified level.
     *
     * @param lv The level of the game to be displayed on the gameboard. This
     *           parameter specifies the complexity or stage of the game to be
     *           set in the interface.
     */
    public void setGameboard(int lv);

    /**
     * Displays the lobby interface to the user based on the information
     * provided by the LobbyEvent parameter. The method is responsible for
     * visualizing the game's lobby state, including details about the game ID,
     * level, maximum players, and current players in the lobby.
     *
     * @param event A LobbyEvent object containing details such as the
     *              game ID, level, maximum number of players, and the
     *              list of players currently in the lobby.
     */
    void showLobby(LobbyEvent event);

    /**
     * Displays the state of the lobby game to the user interface.
     * This method is used to update the view to reflect the current players in the lobby
     * and their readiness status.
     *
     * @param event the GameLobbyEvent containing the list of players and their readiness states.
     */
    void showLobbyGame(GameLobbyEvent event);

    /**
     * Notifies the view that rewards have been updated or changed as a result of a specific event.
     *
     * @param event The RewardsEvent containing information about the updated rewards.
     */
    void rewardsChanged(RewardsEvent event);

    /**
     * Handles the event triggered when the game phase changes. This method is used
     * to update the view to reflect the new phase of the game as defined in the
     * received PhaseEvent.
     *
     * @param event the PhaseEvent containing details about the new game phase.
     */
    void phaseChanged(PhaseEvent event);

    /**
     * Handles an exception event that occurred during the application's execution.
     * This method processes and manages the provided exception details from the event.
     *
     * @param exceptionEvent The event containing the exception information to handle.
     *                       It contains a message or other details about the exception.
     */
    void exceptionOccurred(ExceptionEvent exceptionEvent);

    /**
     * Updates the progress bar or graphical interface elements related to other players
     * based on the provided player's tile event data.
     *
     * @param playerTileEvent an instance of PlayerTileEvent containing information
     *                        about the player's tile, such as its id, position, cargo,
     *                        crew details, and tile-specific metadata. This parameter
     *                        provides necessary data for updating the view.
     */
    void updateOthersPB(PlayerTileEvent playerTileEvent);

    /**
     * Displays the current state of the boards for all players in the game.
     * This method is used to provide a visual representation or overview of
     * the boards, enabling the user to review or compare them. The specific
     * implementation may vary depending on the context in which it is invoked.
     */
    void seeBoards();

    /**
     * Refreshes the view interface, ensuring that it reflects the current state of the game
     * or application. This method is generally used to trigger a full update or redraw
     * of the user interface elements after significant state changes. Implementations may vary
     * based on the specific needs of the view or game phase.
     */
    void refresh();

    /**
     * Applies the effect of a card based on the provided LogEvent. This method is
     * expected to be used for handling events that involve card effects, updating
     * the view accordingly to reflect the result of the event.
     *
     * @param event The LogEvent instance containing information about the card effect,
     *              including its type, position, direction, and description of the effect.
     */
    void effectCard(LogEvent event);

    /**
     * Updates the Player Board information on the user interface based on the provided event.
     * This method is responsible for reflecting the current state of the player's board by processing
     * the details included in the PBInfoEvent. It may update the displayed resources, damage levels,
     * crew members, and other related data.
     *
     * @param event The PBInfoEvent object containing the updated information about the player's board,
     *              including attributes such as credits, damage, energy, alien status, and other statistics.
     */
    void updatePBInfo(PBInfoEvent event);

    /**
     * Updates the state of the hourglass in the view based on the provided event.
     *
     * @param event The HourglassEvent containing information about the hourglass state,
     *              including a message and whether the hourglass should start.
     */
    void updateHourglass(HourglassEvent event);

    /**
     * Logs or displays the activity history or relevant logs to the user interface.
     * This method can be used to review gameplay logs, interaction history, or system events
     * for better transparency and user understanding.
     */
    void seeLog();

    /**
     * Displays the outcome of the game to the user, based on the provided event details.
     *
     * @param event The event containing the result of the game, including whether the user won
     *              or lost and an accompanying message.
     */
    void showOutcome(FinishGameEvent event);

    /**
     * Handles the re-initialization of the view upon a reconnection event.
     * This method allows the user interface to adjust and synchronize with
     * the current game state after a user reconnects to the application.
     *
     * @param event The event containing the reconnection details, including
     *              the player's token, game ID, player ID, and game level.
     */
    void reconnect(ReconnectedEvent event);

    /**
     * Processes a TokenEvent. This method is responsible for handling the specified TokenEvent
     * and performing the appropriate actions or updates based on its contents.
     *
     * @param tokenEvent the TokenEvent to be processed, containing information about a token
     */
    void Token(TokenEvent tokenEvent);

    /**
     * Displays the scores of players based on the given ScoreboardEvent.
     *
     * @param event The ScoreboardEvent containing the mapping of player names to their scores.
     */
    void showScore(ScoreboardEvent event);


    /**
     * Sets the background processing or attributes for a component or application.
     * This method is intended to handle configurations or functionalities
     * related to the background within the context it is implemented.
     * Specific implementations or effects are determined by the subclass or concrete usage.
     */
    void background();
}

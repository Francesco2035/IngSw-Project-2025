package org.example.galaxy_trucker.ClientServer.Messages;

import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.*;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.*;
//import org.example.galaxy_trucker.Messages.TileSets.*;

/**
 * This interface defines a visitor for processing various types of events
 * within a system using the visitor design pattern. Each method within the
 * interface corresponds to a specific event type, allowing the visitor to
 * handle events differently based on their concrete type.
 *
 * The EventVisitor interface is typically implemented by classes that need
 * to perform specific actions or computations depending on the event type.
 * This approach decouples event handling logic from the events themselves.
 */
public interface EventVisitor {


    /**
     * Processes a DeckEvent instance using the visitor design pattern.
     *
     * This method is called when a DeckEvent is passed to an EventVisitor, enabling
     * specific behavior to be implemented for events of this type.
     *
     * @param event the DeckEvent instance to be processed by the visitor
     */
    void visit(DeckEvent event);

    /**
     * Processes a CardEvent by invoking the appropriate logic given by the visitor implementation.
     * This method is part of the visitor design pattern, allowing different actions or behaviors
     * to be executed based on the specific type of the Event subclass.
     *
     * @param event the CardEvent instance to be processed by the visitor
     */
    void visit(CardEvent event);

    /**
     * Processes the provided GameLobbyEvent using the visitor design pattern.
     * The implementation of this method defines how the visitor handles
     * specific logic related to the state of the game lobby, such as
     * the list of players and their readiness statuses.
     *
     * @param event the GameLobbyEvent instance representing the game lobby state
     *              with its list of players and their readiness statuses
     */
    void visit(GameLobbyEvent event);

    /**
     * Processes a HandEvent using the visitor design pattern.
     *
     * This method is called when a HandEvent instance is visited by an
     * implementation of the EventVisitor interface. It allows for specific
     * processing or handling of the event within the visitor implementation.
     *
     * @param event the HandEvent instance being processed
     */
    void visit(HandEvent event);

    /**
     * Processes a {@link VoidEvent} using the visitor design pattern.
     * This method performs an operation defined by the implementation
     * of the {@code EventVisitor} on the provided {@code VoidEvent}.
     *
     * @param event the {@code VoidEvent} to be processed by this visitor
     */
    void visit(VoidEvent event);

    /**
     * Processes a {@link TileEvent}, allowing the visitor to perform specific
     * actions or computations based on the details of the TileEvent.
     *
     * @param event the TileEvent to be processed, which contains details
     *              such as its ID, position, cargo, and attribute information
     *              like alien presence, humans, batteries, and connectors.
     */
    void visit(TileEvent event);

    /**
     * Processes the provided UncoverdTileSetEvent instance within the system.
     * This method is part of an implementation of the visitor design pattern,
     * allowing specific logic to be executed for UncoverdTileSetEvent objects.
     *
     * @param event the UncoverdTileSetEvent to be processed, containing data
     *              such as the identifier and a list of connectors related to the event
     */
    void visit(UncoverdTileSetEvent event);

    /**
     * Processes the specified CoveredTileSetEvent. This method is part of
     * the visitor design pattern and allows implementing classes to define
     * specific behaviors when encountering this type of event.
     *
     * @param event the CoveredTileSetEvent instance to be processed
     */
    void visit(CoveredTileSetEvent event);

    /**
     * Handles the processing of a GameBoardEvent.
     *
     * This method is part of the visitor design pattern, enabling different implementations
     * of the EventVisitor interface to define specific behaviors for handling GameBoardEvent instances.
     * GameBoardEvent represents an event that occurs on the game board, typically linked
     * to a specific position and player action or state.
     *
     * @param gameBoardEvent the GameBoardEvent instance to be processed
     */
    void visit(GameBoardEvent gameBoardEvent);

    /**
     * Visits the specified {@link LobbyEvent} instance and processes it using the visitor's logic.
     * This method is part of the Visitor design pattern, allowing the implementation
     * of customized logic for different event types.
     *
     * @param lobbyEvent the {@link LobbyEvent} object to be processed
     */
    void visit(LobbyEvent lobbyEvent);

    /**
     * Handles the processing of a {@link PhaseEvent}.
     *
     * This method is called to perform specific actions or computations
     * when a {@link PhaseEvent} is encountered. The implementation of this
     * method allows the visitor to handle the {@link PhaseEvent} according
     * to its functionality within the system.
     *
     * @param phaseEvent the {@link PhaseEvent} object to be processed,
     *                   which encapsulates information about the player's
     *                   state during a specific game phase.
     */
    void visit(PhaseEvent phaseEvent);

    /**
     * Processes a RewardsEvent using the visitor design pattern.
     * This method allows a visitor to perform specific logic
     * based on the contents of the RewardsEvent.
     *
     * @param rewardsEvent the RewardsEvent instance to be processed by the visitor
     */
    void visit(RewardsEvent rewardsEvent);

    /**
     * Processes an ExceptionEvent instance.
     *
     * This method is responsible for handling events of type ExceptionEvent.
     * ExceptionEvent represents an event that contains information about
     * an exception occurring in the application. The visitor implementing
     * this method can define specific logic to handle or respond to such
     * events, such as logging, error handling, or notification tasks.
     *
     * @param exceptionEvent the ExceptionEvent instance providing details about
     *                       the exception that occurred
     */
    void visit(ExceptionEvent exceptionEvent);

    /**
     * Processes the given PlayerTileEvent using the visitor design pattern.
     * This method is intended to handle events related to player tiles
     * and may perform specific actions based on the event details.
     *
     * @param playerTileEvent the PlayerTileEvent to process, containing information
     *                        about a player's tile, including its position, cargo,
     *                        players, aliens, and other attributes.
     */
    void visit(PlayerTileEvent playerTileEvent);

    /**
     * Processes a {@code LogEvent} that represents a logging-related event in the system.
     * The {@link LogEvent} contains information such as effect, coordinates, direction,
     * and type, which can be utilized by the visitor implementation to execute specific logic.
     *
     * @param event the {@link LogEvent} instance to be processed
     */
    void visit(LogEvent event);

    /**
     * Processes a ConnectionRefusedEvent using the visitor design pattern.
     * This method allows the visitor to perform operations specific to the
     * ConnectionRefusedEvent type. The ConnectionRefusedEvent is typically
     * used to represent scenarios where a connection attempt was unsuccessful.
     *
     * @param connectionRefusedEvent the ConnectionRefusedEvent instance to be processed by the visitor
     */
    void visit(ConnectionRefusedEvent connectionRefusedEvent);

    /**
     * Processes a PBInfoEvent, allowing specific logic to be executed
     * based on the properties and context of the event.
     *
     * @param pbInfoEvent the PBInfoEvent instance to be processed by this method
     */
    void visit(PBInfoEvent pbInfoEvent);

    /**
     * Processes the given {@code QuitEvent}.
     *
     * This method is called when a {@code QuitEvent} occurs and is intended
     * to handle logic specific to this type of event. Implementations of the
     * {@code EventVisitor} interface can provide custom behavior for dealing
     * with a participant quitting the system or application.
     *
     * @param quitEvent the {@code QuitEvent} to be processed
     */
    void visit(QuitEvent quitEvent);

    /**
     * Processes an HourglassEvent instance using the visitor design pattern.
     *
     * @param hourglassEvent the HourglassEvent instance to be processed.
     *                        This event contains details such as a message and a flag
     *                        indicating whether it represents the start of a process or action.
     */
    void visit(HourglassEvent hourglassEvent);

    /**
     * Processes a FinishGameEvent using the visitor design pattern.
     * This method is specifically responsible for handling events indicating
     * the end of a game, including details about the outcome such as whether
     * it was a win or not and additional context about the game's conclusion.
     *
     * @param finishGameEvent the FinishGameEvent to be processed, containing
     *                        information about the game's conclusion.
     */
    void visit(FinishGameEvent finishGameEvent);

    /**
     * Processes the ReconnectedEvent instance using the visitor design pattern.
     *
     * @param reconnectedEvent the event triggered when a player reconnects, containing
     *                         information such as the player's authentication token,
     *                         game ID, player ID, and level.
     */
    void visit(ReconnectedEvent reconnectedEvent);

    /**
     * Processes a {@link TokenEvent} within the context of the visitor design pattern.
     * This method is invoked to handle logic specific to the {@code TokenEvent}.
     *
     * @param tokenEvent the {@link TokenEvent} instance to be processed
     */
    void visit(TokenEvent tokenEvent);

    /**
     * Processes a {@code ScoreboardEvent} within the visitor design pattern.
     * This method is intended to allow specific behavior or operations to be
     * defined when a {@code ScoreboardEvent} is encountered.
     *
     * @param scoreboardEvent the {@code ScoreboardEvent} instance containing the
     *                         scores and player information to be processed
     */
    void visit(ScoreboardEvent scoreboardEvent);


    //public void visit(GameBoardEvent event);
}

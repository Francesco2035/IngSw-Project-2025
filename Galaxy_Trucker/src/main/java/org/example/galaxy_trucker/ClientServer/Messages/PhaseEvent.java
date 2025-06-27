package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.View.ClientModel.States.PlayerStateClient;

/**
 * The PhaseEvent class represents an event in the system that encapsulates a specific phase
 * within the game. It contains information about the player's current state during this phase
 * and facilitates interaction with event visitors.
 *
 * This class implements the Event interface and serves as part of the event-driven architecture
 * designed to notify and handle different game states through visitor-based processing.
 *
 * Features:
 * - Stores a PlayerStateClient object representing the player's state in the game phase.
 * - Supports JSON serialization and deserialization using Jackson annotations for easy
 *   data exchange.
 * - Implements the Event interface to allow visitor-based processing and message retrieval.
 *
 * Responsibilities:
 * - Encapsulate game phase information.
 * - Enable visitor-based operations to handle the event appropriately.
 */
public class PhaseEvent implements Event{

    /**
     * Represents the specific state of a player during a phase of the game.
     *
     * This variable encapsulates the player's behavior and available actions
     * within the context of the current game phase. It is an instance of the
     * PlayerStateClient class or one of its subclasses, which allows for
     * polymorphic operations dependent on the specific game state.
     *
     * The stateClient can handle various functionalities such as rendering
     * game views, providing available commands, and managing game-specific
     * player interactions. It is utilized within the PhaseEvent to model
     * phase-oriented player behaviors and actions in the game logic.
     */
    public PlayerStateClient stateClient;

    /**
     * Constructs a new PhaseEvent instance.
     *
     * This no-argument constructor initializes a PhaseEvent object
     * with default values for its properties. It is primarily used
     * for deserialization and scenarios where the event's properties
     * will be set or configured later.
     */
    public PhaseEvent() {

    }

    /**
     * Constructs a PhaseEvent with the specified PlayerStateClient object representing the player's
     * state during a specific game phase.
     *
     * @param stateClient the PlayerStateClient object encapsulating the player's current state
     *                    during the phase. This is used to represent phase-specific behavior
     *                    in the game architecture.
     */
    @JsonCreator
    public PhaseEvent(@JsonProperty("phase") PlayerStateClient stateClient) {
        this.stateClient = stateClient;
    }


    /**
     * Accepts a visitor for processing this {@code PhaseEvent}.
     *
     * @param visitor the {@link EventVisitor} instance that will handle this event
     */
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Retrieves the message associated with this event.
     *
     * This method is intended to return a descriptive message
     * relevant to the specific event. For this implementation,
     * the method returns an empty string.
     *
     * @return a string representing the message of the event, or an
     *         empty string if no specific message is provided.
     */
    @Override
    public String message() {
        return "";
    }

    /**
     * Retrieves the player's current state in the game phase.
     * The returned object encapsulates the state-specific behavior and attributes
     * related to the player during a specific phase of the game.
     *
     * @return a PlayerStateClient object representing the current state of the player
     *         in the game phase. This object provides methods and data specific to
     *         the player's state within the context of the current phase.
     */
    @JsonProperty("phase")
    public PlayerStateClient getStateClient() {
        return stateClient;
    }
}

package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

/**
 * The AcceptCommand class is a specific implementation of the {@link Command} class that handles
 * acceptance-related logic during game execution. This command determines whether a specific
 * game action should be accepted or declined.
 *
 * This class is annotated with {@link JsonProperty} to map
 * the 'accepting' field for serialization and deserialization purposes.
 *
 * Constructors of this class allow initialization with or without parameters, inheriting specific
 * attributes like gameId, playerId, level (lv), title, and token from the parent {@link Command}.
 *
 * Methods:
 * - {@code execute(Player player)}: Overrides the abstract method in {@link Command}. This method triggers
 *   the continuation of a player's current card, based on whether the action is accepted or not.
 * - {@code allowedIn(PlayerState playerState)}: Determines if the command is permissible for a given
 *   player state at a specific point in the game.
 *
 * This class contributes to the game's command pattern infrastructure, ensuring that player actions
 * are validated and executed under defined rules and states.
 */
public class AcceptCommand extends Command implements Serializable {

    /**
     * Indicates whether a specific action or command is being accepted in the current context.
     *
     * This boolean variable is used to represent the state of acceptance for a game-related
     * action, typically within the scope of an AcceptCommand. Its value can determine the
     * behavior or outcome of the command execution.
     *
     * The variable is serialized and deserialized using the `@JsonProperty` annotation for
     * compatibility with JSON-based data formats.
     */
    @JsonProperty("accepting")
    boolean accepting;

    /**
     * Default constructor for the AcceptCommand class.
     *
     * This constructor initializes an instance of AcceptCommand without any specific parameters.
     * It can be used when no contextual information is required during instantiation.
     */
    public AcceptCommand() {}

    /**
     * Constructs an instance of the AcceptCommand class, representing a command
     * that determines whether a specific game action should be accepted.
     *
     * @param gameId the unique identifier for the game.
     * @param playerId the unique identifier for the player executing the command.
     * @param lv the level or stage of the game where the command is being issued.
     * @param title the title or description associated with the command.
     * @param accepting boolean indicating whether the action is being accepted or not.
     * @param token authentication token associated with the player or game session.
     */
    public AcceptCommand(String gameId, String playerId, int lv, String title,boolean accepting, String token) {
        super(gameId, playerId, lv, title, token, -1);
        this.accepting = accepting;
    }

    /**
     * Executes the accept command for the given player. This method determines whether the
     * player's current card should continue processing based on the 'accepting' state.
     *
     * @param player the player who will execute this command. The player's current card will
     *               proceed based on the 'accepting' value associated with this command.
     */
    @Override
    public void execute(Player player) {
        player.getCurrentCard().continueCard(accepting);
    }

    /**
     * Determines if the {@link AcceptCommand} is allowed in the specified {@link PlayerState}.
     *
     * @param playerState the current state of the player used to evaluate the command allowance
     * @return true if the command is allowed in the given player state, false otherwise
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }
}

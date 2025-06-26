package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.AddCrewAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

/**
 * Represents a command to add crew members to a player's game board.
 * This command allows a player to specify the number of human crew members,
 * types of alien crew members, and a coordinate for placement.
 *
 * AddCrewCommand is serialized to JSON and utilizes Jackson annotations
 * for property mapping. It overrides behavior from its superclass to implement
 * specific logic for adding crew in the game.
 */
public class AddCrewCommand extends Command implements Serializable {

    /**
     * Represents the number of human crew members to be added in the AddCrewCommand.
     *
     * This variable specifies how many human crew members a player wishes to place
     * on a specific tile of their game board during the execution of an AddCrewCommand.
     *
     * It is a critical parameter for initializing and executing the command and is
     * serialized/deserialized using the Jackson library for data transmission.
     */
    @JsonProperty("numHumans")
    int numHumans;
    /**
     * Indicates whether a purple alien crew member is included in the AddCrewCommand.
     *
     * This field is used to specify the inclusion of a purple alien when adding crew members
     * to a specific tile on the player's game board. Its value determines whether the command
     * will consider the presence of a purple alien in the action. This field is serialized
     * and deserialized using the `@JsonProperty` annotation to ensure proper handling
     * during data exchange.
     */
    @JsonProperty("purpleAlien")
    boolean purpleAlien;
    /**
     * Indicates whether a brown alien crew member is included in the AddCrewCommand.
     *
     * This variable is part of the crew composition details, helping specify if a
     * brown alien is to be added to the player's game board. It is serialized and
     * deserialized using Jackson annotations for communication with external systems.
     */
    @JsonProperty("brownAlien")
    boolean brownAlien;
    /**
     * Represents the coordinate of a tile on the game board where crew members
     * will be placed. The coordinate consists of an integer pair, representing
     * the x and y positions of the tile. This value is used to identify the
     * exact location of the action within the game.
     *
     * The coordinate is serialized and deserialized using Jackson annotations,
     * allowing it to be part of the AddCrewCommand when transmitted or stored.
     */
    @JsonProperty("coordinate")
    IntegerPair coordinate;

    /**
     * Represents the type of command being executed.
     *
     * The `commandType` variable is used to identify the specific command as
     * "AddCrewCommand". This field is immutable and consistently reflects the
     * command type for instances of the AddCrewCommand class.
     *
     * This property is primarily utilized during serialization and deserialization
     * processes to ensure the correct command type is recognized in JSON structures.
     */
    @JsonProperty("commandType")
    private final String commandType = "AddCrewCommand";




    /**
     * Constructs an AddCrewCommand, which initiates the process to add crew members
     * to a specific tile on the player's game board.
     *
     * @param numHumans     the number of human crew members to be added
     * @param purpleAlien   whether a purple alien crew member is included
     * @param brownAlien    whether a brown alien crew member is included
     * @param coordinate    the coordinate of the tile where crew members should be placed
     * @param gameId        the identifier of the game
     * @param playerId      the identifier of the player issuing the command
     * @param lv            the level of the command
     * @param title         the title of the command
     * @param token         the authentication token for the command
     */
    public AddCrewCommand(int numHumans, boolean purpleAlien, boolean brownAlien, IntegerPair coordinate, String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.numHumans = numHumans;
        this.purpleAlien = purpleAlien;
        this.brownAlien = brownAlien;
        this.coordinate = coordinate;
    }

    /**
     * Executes the AddCrewCommand for the given player. This method ensures
     * that the command is valid in the current game state and performs the
     * necessary actions to add the specified crew members to the designated
     * tile on the player's game board.
     *
     * @param player the player executing the command. The method operates on
     *               this player's board and game state to carry out the action.
     *               It throws an exception if the command violates game rules
     *               or cannot be performed due to the current game state.
     */
    @Override
    public void execute(Player player) {
        if (player.getCommonBoard().getLevel() != 2 && (purpleAlien || brownAlien)) {
            throw new InvalidInput("");
        }
        PlayerBoard playerBoard = player.getmyPlayerBoard();
        AddCrewAction action = new AddCrewAction(numHumans,purpleAlien,brownAlien, playerBoard);
        playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent(),action, player.getPlayerState());
    }

    /**
     * Determines whether the command is allowed to be executed in the given player state.
     *
     * @param playerState the current state of the player
     * @return true if this command is allowed in the given player state, false otherwise
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    /**
     * Default constructor for the AddCrewCommand class.
     *
     * This constructor initializes an instance of AddCrewCommand
     * with no pre-defined values. It is primarily used for deserialization
     * or scenarios where the command properties are defined after initialization.
     */
    public AddCrewCommand(){}

}

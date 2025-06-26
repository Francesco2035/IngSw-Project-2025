package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("numHumans")
    int numHumans;
    @JsonProperty("purpleAlien")
    boolean purpleAlien;
    @JsonProperty("brownAlien")
    boolean brownAlien;
    @JsonProperty("coordinate")
    IntegerPair coordinate;

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
     * Executes the AddCrewCommand for the specified player, applying the action to the player's game board.
     * This method creates an AddCrewAction based on the provided parameters (number of humans,
     * alien types, and coordinate) and performs the action on the associated tile of the player's board.
     *
     * @param player the player for whom the command is being executed. This includes the player's game board
     *               and state, which are used to perform the action.
     */
    @Override
    public void execute(Player player) {
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

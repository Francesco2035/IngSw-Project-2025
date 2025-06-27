package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Objects;

/**
 * BuildingCommand represents a specific type of command that is used for managing
 * and executing actions related to building operations in the game. It extends the
 * generic Command class and implements the Serializable interface.
 *
 * This class encapsulates data about the building operation, such as coordinates,
 * rotation, and position, along with the game and player context. It also defines
 * the logic for executing the command based on the building-related operation specified.
 *
 * The command type for this class is defined as "BuildingCommand".
 */
public class BuildingCommand extends Command implements Serializable {


    /**
     * Represents the type of command being executed, specifically designated
     * as a "BuildingCommand" for this implementation.
     *
     * This variable is used to identify and classify the command as related to
     * building actions within the game's command framework.
     *
     * The value of this field is constant and set to "BuildingCommand".
     */
    @JsonProperty("commandType")
    private final String commandType = "BuildingCommand";


    /**
     * Represents the x-coordinate for the building placement in the game command.
     *
     * This field is used to define the horizontal position for a building or other
     * object in the game's coordinate system. It is initialized by default to -1
     * and can be updated when constructing or executing a BuildingCommand.
     *
     * Annotated with `@JsonProperty("x")` to enable serialization and deserialization
     * via JSON during communication or data persistence.
     */
    @JsonProperty("x")
    private int x = -1;
    /**
     * Represents the y-coordinate for the placement of a building in the game.
     * This variable defines the vertical position where the building command
     * will be executed.
     *
     * The default value is -1, indicating that the y-coordinate has not been
     * explicitly set or initialized.
     */
    @JsonProperty("y")
    private int y = -1;
    /**
     * Represents the rotation value for a building in a game scenario.
     *
     * The rotation variable determines the orientation of a building on the game board.
     * It is typically an integer value that defines the angle or direction in which
     * the building is placed.
     *
     * This property is serialized and deserialized using Jackson annotations to ensure
     * proper data handling in JSON-based communication.
     */
    @JsonProperty("rotation")
    int rotation;
    /**
     * Represents the position or index related to the building or action
     * within the command.
     *
     * This field is utilized to determine the specific location or order
     * where an operation is performed, such as placing a building or
     * executing an action at a defined point.
     *
     * It is annotated with @JsonProperty to facilitate serialization
     * and deserialization when interacting with external systems.
     */
    @JsonProperty("position")
    int position;


    /**
     * Constructs a new BuildingCommand instance with default parameters.
     * This constructor initializes a BuildingCommand object without setting
     * specific values for its fields.
     *
     * The BuildingCommand class represents a command in the game that allows
     * players to construct or place a building at designated coordinates with
     * a specified rotation and position. This command is part of the game's
     * command system and extends the Command base class.
     *
     * This default constructor may serve as a placeholder or be used in scenarios
     * where a BuildingCommand object needs to be initialized prior to being configured
     * with specific details.
     */
    public BuildingCommand(){}

    /**
     * Constructs a new BuildingCommand instance, initializing its parameters.
     *
     * @param x the x-coordinate for the building placement
     * @param y the y-coordinate for the building placement
     * @param rotation the rotation of the building
     * @param position the position or index of the building
     * @param gameId the unique identifier for the game
     * @param playerId the unique identifier for the player issuing the command
     * @param lv the level or stage of the game
     * @param title the title or name of the command
     * @param token a unique token for authentication or tracking
     */
    public BuildingCommand(int x, int y, int rotation, int position, String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.x = x;
        this.y = y;
        this.gameId = gameId;
        this.playerId = playerId;
        this.rotation = rotation;
        this.title = title;
        this.position = position;
    }


    /**
     * Executes the building command for the given player based on the specific action title.
     *
     * @param player The player on which the command is executed.
     * @throws RemoteException If a remote invocation error occurs.
     * @throws JsonProcessingException If there is an error processing JSON data.
     * @throws IllegalStateException If the command is executed in an invalid state.
     */
    @Override
    public void execute(Player player) throws RemoteException, JsonProcessingException, IllegalStateException {

        try{
            if(!player.GetReady() || (Objects.equals(title, "HOURGLASS"))){
                switch (title) {

                    case "SEEDECK": {
                        player.getCommonBoard().getCardStack().notify(playerId, x);
                        break;
                    }

                    case "INSERTTILE": {
                        Tile tile = player.getCurrentTile();
                        int temp = 0;
                        int rotations = (rotation % 360) / 90;
                        for (int i = 0; i < rotations; i++) {
                            tile.RotateDx();
                            temp+= 90;
                        }
                        tile.setRotation(temp);

                        player.getmyPlayerBoard().insertTile(tile, x, y, true);
                        player.setCurrentTile(null);
                        break;
                    }
                    case "TOBUFFER": {
                        player.PlaceInBuffer();
                        break;
                    }
                    case "FROMBUFFER": {
                        player.SelectFromBuffer(position);
                        break;
                    }
                    case "PICKTILE": {
                        player.PickNewTile(position);
                        break;
                    }
                    case "DISCARD": {
                        player.DiscardTile();
                        break;
                    }
                    case "HOURGLASS": {
                        try {
                            player.StartTimer();
                        } catch (RuntimeException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                }
            }
        }catch (Exception e){
            Tile tile = player.getCurrentTile();
            if (tile!=null){
                int temp = 360;
                int rotations = (rotation % 360) / 90;
                for (int i = 0; i < rotations; i++) {
                    tile.RotateSx();
                    temp-=90;
                }

                temp = rotation % 360;
                tile.setRotation(temp);
                tile.clearRotation();
            }
            throw new InvalidInput(e.getMessage());
        }




    }

    /**
     * Retrieves the title associated with this command.
     *
     * @return the title of the command as a String
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves the ID of the player associated with this command.
     *
     * @return the player ID as a String
     */
    @Override
    public String getPlayerId() {
        return playerId;
    }

    /**
     * Retrieves the unique identifier for the game associated with this command.
     *
     * @return the game ID as a string.
     */
    @Override
    public String getGameId() {
        return gameId;
    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }
}

package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
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


    @JsonProperty("commandType")
    private final String commandType = "BuildingCommand";


    @JsonProperty("x")
    private int x = -1;
    @JsonProperty("y")
    private int y = -1;
    @JsonProperty("rotation")
    int rotation;
    @JsonProperty("position")
    int position;


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

        if(!player.GetReady() || (Objects.equals(title, "HOURGLASS"))){
            switch (title) {

                    case "SEEDECK": {
                        player.getCommonBoard().getCardStack().notify(playerId, x);
                        break;
                    }

                    case "INSERTTILE": {
                        Tile tile = player.getCurrentTile();
                        int rotations = (rotation % 360) / 90;
                        for (int i = 0; i < rotations; i++) {
                            tile.RotateDx();
                        }
                        tile.setRotation(rotation);

                        player.getmyPlayerBoard().insertTile(tile, x, y, true);
                        player.setCurrentTile(null);
                        break;
                    }
                    case "TOBUFFER": {
                        Tile tile = player.getCurrentTile();
                        int rotations = (rotation % 360) / 90;
                        for (int i = 0; i < rotations; i++) {
                            tile.RotateDx();
                        }
                        tile.setRotation(rotation);
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
                        System.out.println(player.GetID()+" sta scartando");
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
        return playerState.allows(this); //TODO: aggiungere caso per playerstate == null => eccezione
    }
}

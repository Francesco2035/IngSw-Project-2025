package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Objects;

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

    public BuildingCommand(int x, int y, int rotation, int position, String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token);
        this.x = x;
        this.y = y;
        this.gameId = gameId;
        this.playerId = playerId;
        this.rotation = rotation;
        this.title = title;
        this.position = position;
    }


    @Override
    public void execute(Player player) throws RemoteException, JsonProcessingException {

        if(!player.GetReady() || (Objects.equals(title, "Hourglass"))) {
            try {

                switch (title) {

                    case "SEEDECK": {
                        player.getCommonBoard().getCardStack().notify(playerId, x);
                    }

                    case "INSERTTILE": {
                        Tile tile = player.getCurrentTile();
                        int rotations = (rotation % 360) / 90;
                        for (int i = 0; i < rotations; i++) {
                            tile.RotateDx();
                        }
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
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getPlayerId() {
        return playerId;
    }

    @Override
    public String getGameId() {
        return gameId;
    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }
}

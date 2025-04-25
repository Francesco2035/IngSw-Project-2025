package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.rmi.RemoteException;

public class BuildingCommand extends Command{


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

    public BuildingCommand(int x, int y, int rotation, int position, String gameId, String playerId, int lv, String title) {
        super(gameId, playerId, lv, title);
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
        switch (title){

            case "SeeDeck": {
                player.getCommonBoard().getCardStack().notify(playerId, x);
            }

            case "InsertTile": {
                Tile tile = player.getCurrentTile();
                int rotations = (rotation % 360) / 90;
                for (int i = 0; i < rotations; i++) {
                    tile.RotateDx();
                }
                player.getmyPlayerBoard().insertTile(tile, x, y);
                player.setCurrentTile(null);
                break;
            }
            case "ToBuffer" :{
                Tile tile = player.getCurrentTile();
                int rotations = (rotation % 360) / 90;
                for (int i = 0; i < rotations; i++) {
                    tile.RotateDx();
                }
                player.PlaceInBuffer();
                break;
            }
            case "FromBuffer" :{

                player.SelectFromBuffer(position);
                break;
            }
            case "PickTile":{
                player.PickNewTile(position);
                break;
            }
            case "Discard":{
                player.DiscardTile();
                break;
            }
            case "Hourglass":{
                    try {
                        player.StartTimer();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

            }break;
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

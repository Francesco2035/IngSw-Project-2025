package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.Boards.Actions.GetPlasmaDrillPower;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;
import java.util.ArrayList;

public class GiveAttackCommand extends Command implements Serializable {

    private ArrayList<IntegerPair> coordinates;
    private Player player;
    public GiveAttackCommand( ArrayList<IntegerPair> coordinates,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.coordinates = coordinates;

    }

    @Override
    public void execute(Player player) {
        if (coordinates!= null){
            PlayerBoard playerBoard = player.getmyPlayerBoard();
            GetPlasmaDrillPower action = new GetPlasmaDrillPower(playerBoard.getEnginePower());
            for (IntegerPair coordinate : coordinates) {
                playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent(),
                        action ,player.getPlayerState());
            }

            try {
                player.getCurrentCard().checkPower(action.getPower() ,action.getCountDoublePlasmaDrills());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            PlayerBoard playerBoard = player.getmyPlayerBoard();
            GetPlasmaDrillPower action = new GetPlasmaDrillPower(playerBoard.getPlasmaDrillsPower());
            try {
                player.getCurrentCard().checkPower(action.getPower() ,action.getCountDoublePlasmaDrills());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

}

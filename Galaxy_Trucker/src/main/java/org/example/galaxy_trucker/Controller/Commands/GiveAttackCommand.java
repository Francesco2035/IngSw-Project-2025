package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Boards.Actions.GetPlasmaDrillPower;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

import java.util.ArrayList;

public class GiveAttackCommand implements Command{

    private Card card;
    private ArrayList<IntegerPair> coordinates;
    private Player player;
    public GiveAttackCommand(Card card, ArrayList<IntegerPair> coordinates, Player player) {
        this.coordinates = coordinates;
        this.card = card;
    }

    @Override
    public void execute() {
        PlayerBoard playerBoard = player.getmyPlayerBoard();
        GetPlasmaDrillPower action = new GetPlasmaDrillPower(playerBoard.getEnginePower());
        for (IntegerPair coordinate : coordinates) {
            playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent(),
                    action ,player.getPlayerState());
        }

        card.checkPower(action.getPower() ,action.getCountDoublePlasmaDrills());
    }

}

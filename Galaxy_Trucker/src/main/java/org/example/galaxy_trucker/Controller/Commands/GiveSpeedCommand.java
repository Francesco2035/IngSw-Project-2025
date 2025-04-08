package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Boards.Actions.GetEnginePower;
import org.example.galaxy_trucker.Model.Boards.Actions.GetPlasmaDrillPower;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

import java.util.ArrayList;

public class GiveSpeedCommand implements Command {
    private Card card;
    private ArrayList<IntegerPair> coordinates;
    private Player player;
    public GiveSpeedCommand(Card card, ArrayList<IntegerPair> coordinates, Player player) {
        this.coordinates = coordinates;
        this.card = card;
    }

    @Override
    public void execute() {
        PlayerBoard playerBoard = player.getmyPlayerBoard();
        GetEnginePower action = new GetEnginePower(playerBoard.getEnginePower());
        for (IntegerPair coordinate : coordinates) {
            playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent(),
                    action ,player.getPlayerState());
        }
        //AGGIUNGERE STATO GIVENREGY
        card.continueCard(action.getPower(),action.getCountDoubleEngine());
    }
}

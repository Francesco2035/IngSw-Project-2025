package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.Optional;

public class BuildingShip extends PlayerState {

    private int x;
    private int y;
    private Tile tile;
    private PlayerBoard playerBoard;
    private String command;

    //deserializzi il json
    public BuildingShip() {
//        this.x = x;
//        this.y = y;
//        this.tile = tile;
//        this.playerBoard = player.getmyPlayerBoard();
//        this.command = command;
    }

    @Override
    public Command PlayerAction(String json, Player player, Optional<Card> card){
//        switch (command){
//            case "add": return new AddTileAction(tile,playerBoard,x,y);
//            case "take":
//        }
        return null;
    }



}

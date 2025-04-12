package org.example.galaxy_trucker.Controller.Commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

public class SelectChunkCommand implements Command {

    Player player;
    IntegerPair chunk;

    public SelectChunkCommand(Player player, IntegerPair chunk) {
        this.player = player;
        this.chunk = chunk;
    }

    @Override
    public void execute() {
        PlayerBoard playerBoard = player.getmyPlayerBoard();
        playerBoard.modifyPlayerBoard(playerBoard.choosePlayerBoard(chunk));
        player.getCurrentCard().keepGoing();
    }
}

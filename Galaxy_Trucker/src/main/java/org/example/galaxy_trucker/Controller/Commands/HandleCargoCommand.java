package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Boards.Actions.AddGoodAction;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Boards.Player_IntegerPair;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;

import java.util.ArrayList;

public class HandleCargoCommand implements Command {

    String title;
    int position;
    IntegerPair coordinate;
    Player player;

    public HandleCargoCommand(String title, int position, IntegerPair coordinate, Player player) {
        this.title = title;
        this.position = position;
        this.coordinate = coordinate;
        this.player = player;
    }



    @Override
    public void execute() {


        PlayerBoard playerBoard = player.getmyPlayerBoard();
        Goods temp;
        switch (title) {
            case "GetFromRewards": {
                playerBoard.AddGoodInBuffer(playerBoard.getFromRewards(position));
            }
            case "PutInStorage":{
                int x = coordinate.getFirst();
                int y = coordinate.getSecond();
                temp = playerBoard.pullFromBufferGoods(position);
                playerBoard.performAction(playerBoard.getTile(x, y).getComponent()
                , new AddGoodAction(temp, playerBoard,x,y),player.getPlayerState());
            }
            case "GetFromStorage": {
                int x = coordinate.getFirst();
                int y = coordinate.getSecond();
                GetGoodAction action = new GetGoodAction(position,playerBoard,x,y);
                playerBoard.performAction(playerBoard.getTile(x, y).getComponent(),action,player.getPlayerState());
                playerBoard.AddGoodInBuffer(action.getGood());
            }
            case "Finish": {
                playerBoard.getRewards().clear();
                playerBoard.getBufferGoods().clear();
                player.setState(new BaseState());
            }
        }


    }
}

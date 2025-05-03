package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.Boards.Actions.AddGoodAction;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

public class HandleCargoCommand extends Command implements Serializable {

    String title;
    int position;
    IntegerPair coordinate;


    public HandleCargoCommand(int position, IntegerPair coordinate,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token);
        this.title = title;
        this.position = position;
        this.coordinate = coordinate;

    }



    @Override
    public void execute(Player player) {


        PlayerBoard playerBoard = player.getmyPlayerBoard();
        Goods temp;
        switch (title) {
            case "GetFromRewards": {
                playerBoard.AddGoodInBuffer(playerBoard.getFromRewards(position));
                break;
            }
            case "PutInStorage":{
                int x = coordinate.getFirst();
                int y = coordinate.getSecond();
                temp = playerBoard.pullFromBufferGoods(position);
                playerBoard.performAction(playerBoard.getTile(x, y).getComponent()
                , new AddGoodAction(temp, playerBoard,x,y),player.getPlayerState());
                break;

            }
            case "GetFromStorage": {
                int x = coordinate.getFirst();
                int y = coordinate.getSecond();
                GetGoodAction action = new GetGoodAction(position,playerBoard,x,y);
                playerBoard.performAction(playerBoard.getTile(x, y).getComponent(),action,player.getPlayerState());
                playerBoard.AddGoodInBuffer(action.getGood());
                break;

            }
            case "Finish": {
                playerBoard.getRewards().clear();
                playerBoard.getBufferGoods().clear();
                player.setState(new BaseState());
                break;

            }
        }
    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }
}

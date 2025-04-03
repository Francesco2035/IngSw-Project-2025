package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.Storage;

public class GetGoodAction extends ComponentActionVisitor{

    private int position;
    private Goods good;
    private PlayerBoard playerBoard;
    private int x;
    private int y;
    public GetGoodAction(int position, PlayerBoard playerBoard, int x, int y) {
        this.position = position;
        this.playerBoard = playerBoard;
        this.x = x;
        this.y = y;
    }

    @Override
    public void visit(Storage storage, PlayerStates State){
        if (!State.equals(PlayerStates.RemoveCargo))      {
            throw new IllegalStateException("Player state is not RemoveCargo");
        }
        good = storage.removeGood(position);
        playerBoard.getStoredGoods().get(good.getValue()).remove(new IntegerPair(x,y));
        if (playerBoard.getStoredGoods().get(good.getValue()).isEmpty()){
            playerBoard.getStoredGoods().remove(good.getValue());
        }
    }

    public Goods getGood() {
        return good;
    }


}

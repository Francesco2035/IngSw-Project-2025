package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.Storage;

public class GetGoodAction extends ComponentAction {

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
    public void visit(Storage storage, PlayerState playerState) {
        if (!playerState.allows(this)){
            throw new IllegalStateException("illegal state");
        }

        good = storage.removeGood(position);
        if (good!=null){
            playerBoard.getStoredGoods().get(good.getValue()).remove(new IntegerPair(x,y));
            if (playerBoard.getStoredGoods().get(good.getValue()).isEmpty()){
                playerBoard.getStoredGoods().remove(good.getValue());
            }
        }
    }

    public Goods getGood() {
        return good;
    }


}

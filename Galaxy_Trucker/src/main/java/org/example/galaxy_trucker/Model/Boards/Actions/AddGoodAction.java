package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.Storage;

import java.util.ArrayList;
import java.util.HashMap;

public class AddGoodAction extends ComponentAction {

    private Goods good;
    private PlayerBoard playerBoard;
    int x;
    int y;
    public AddGoodAction(Goods good,PlayerBoard playerBoard, int x, int y) {
        this.good = good;
        this.playerBoard = playerBoard;
        this.x = x;
        this.y = y;
    }

    @Override
    public void visit(Storage storage, PlayerState playerState) {
        if (!playerState.allows(this)){
            throw new IllegalStateException("You are not allowed to perform this action in this state");
        }
        storage.addGood(good);
//        if(good==null){
//            return;
//        }
//        HashMap<Integer, ArrayList<IntegerPair>> storedgoods = playerBoard.getStoredGoods();
//        if (storedgoods.containsKey(good.getValue())){
//            storedgoods.get(good.getValue()).add(new IntegerPair(x, y));
//        }
//        else {
//            storedgoods.put(good.getValue(),new ArrayList<>());
//            storedgoods.get(good.getValue()).add(new IntegerPair(x, y));
//        }



    }

}

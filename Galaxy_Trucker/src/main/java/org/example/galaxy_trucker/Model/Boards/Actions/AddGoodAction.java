package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.Storage;

public class AddGoodAction extends ComponentActionVisitor{

    private Goods good;
    private PlayerBoard playerBoard;

    public AddGoodAction(Goods good,PlayerBoard playerBoard) {
        this.good = good;
        this.playerBoard = playerBoard;
    }

    @Override
    public void visit(Storage storage, PlayerStates State){
        if (!State.equals(PlayerStates.AddCargo)){
            throw new IllegalStateException("ivalid state");
        }
        storage.addGood(good);
    }

}

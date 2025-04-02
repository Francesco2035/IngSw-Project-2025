package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.Storage;

public class GetGoodAction extends ComponentActionVisitor{

    private int position;
    private Goods good;
    private PlayerBoard playerBoard;
    public GetGoodAction(int position, PlayerBoard playerBoard) {
        this.position = position;
        this.playerBoard = playerBoard;
    }

    @Override
    public void visit(Storage storage, PlayerStates State){
        if (!State.equals(PlayerStates.RemoveCargo))      {
            throw new IllegalStateException("Player state is not RemoveCargo");
        }
        good = storage.removeGood(position);

    }

    public Goods getGood() {
        return good;
    }


}

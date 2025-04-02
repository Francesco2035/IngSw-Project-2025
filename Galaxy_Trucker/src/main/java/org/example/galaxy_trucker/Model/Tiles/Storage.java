package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Actions.ComponentActionVisitor;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.State;
import org.example.galaxy_trucker.Model.Goods.Goods;

import java.util.ArrayList;
import java.util.Comparator;

public class Storage extends Component{


    ArrayList<Goods> goods;

    @Override
    public void rotate(Boolean direction) {
    }

    @Override
    public void accept(ComponentActionVisitor visitor, PlayerStates State) {
        if (!(State.equals(PlayerStates.AddCargo) || State.equals(PlayerStates.RemoveCargo))){
            throw new IllegalStateException("Invalid state ");
        }
        visitor.visit(this, State);
    }

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    public  Goods removeGood(int i){
        return null;
    }

    public void addGood(Goods good){

    }



    @Override
    public void insert(PlayerBoard playerBoard) {
    }

    @Override
    public void remove(PlayerBoard playerBoard) {
    }

    private void orderGoods() {
        this.goods.sort(Comparator.comparingInt(Goods::getValue));
    }

}

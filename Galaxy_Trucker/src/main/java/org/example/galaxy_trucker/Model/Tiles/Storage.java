package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.util.ArrayList;
import java.util.Comparator;

public abstract class Storage extends Component{


    ArrayList<Goods> goods;

    @Override
    public void rotate(Boolean direction) {
    }

    @Override
    public void accept(ComponentAction visitor, PlayerState State) {
        visitor.visit(this, State);
    }

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
    }

    @Override
    public void remove(PlayerBoard playerBoard) {

    }

    public  Goods removeGood(int i){
        return null;
    }

    public void addGood(Goods good){

    }


    private void orderGoods() {
        this.goods.sort(Comparator.comparingInt(Goods::getValue));
    }

}

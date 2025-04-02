package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;

import java.util.ArrayList;
import java.util.Comparator;

public class Storage extends Component{


    ArrayList<Goods> goods;


    @Override
    public void rotate(Boolean direction) {
    }

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
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

package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RemoveTileEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;

import java.lang.reflect.InaccessibleObjectException;
import java.util.ArrayList;
import java.util.Comparator;

public class SpecialStorageCompartment extends Storage{

    private ArrayList<Goods> goods;



    public ArrayList<Goods> getGoods() {
        return goods;
    }
    public void setGoods(ArrayList<Goods> goods) {
        this.goods = goods;
    }
    private void orderGoods() {
          this.goods.sort(Comparator.comparingInt(Goods::getValue));
    }


    @Override
    public void rotate(Boolean direction) {}


    @Override
    public Goods removeGood(int position){
        if (position >= goods.size() || position < 0){
            throw new InvalidInput("Cannot remove a good because it is out of bounds");
        }
        tile.sendUpdates(goods,0, false, false, 0, "SpecialStorageCompartment");
        return goods.remove(position);



    }


    @Override
    public void addGood(Goods good) {
        if (goods.size() == type){
            throw new InvalidInput("StorageCompartment is full!");
        }
        goods.add(good);
        tile.sendUpdates(goods,0, false, false, 0, "SpecialStorageCompartment");

    }


    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
        playerBoard.getStorages().add(this);
        goods = new ArrayList<>();
        tile.sendUpdates(goods,0, false, false, 0, "SpecialStorageCompartment");

    }


    @Override
    public void remove(PlayerBoard playerBoard) {
        playerBoard.getStorages().remove(this);
        tile.sendUpdates(new RemoveTileEvent());

    }

    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        SpecialStorageCompartment clone = new SpecialStorageCompartment();
        clone.goods = new ArrayList<>(goods);
        clone.type = this.type;
        return clone;
    }

}

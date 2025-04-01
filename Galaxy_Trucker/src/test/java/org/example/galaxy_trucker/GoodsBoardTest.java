package org.example.galaxy_trucker;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.BLUE;
import org.example.galaxy_trucker.Model.Goods.GREEN;
import org.example.galaxy_trucker.Model.Tiles.SpecialStorageCompartment;
import org.example.galaxy_trucker.Model.Tiles.StorageCompartment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GoodsBoardTest {

    @Test
    void pullGoods() {
    }

    @Test
    void putGoods() {
    }

    @Test
    void pullFromBuffer() {
    }

    @Test
    void removeGood() {
        StorageCompartment storage1 = new StorageCompartment();
        SpecialStorageCompartment storage2 = new SpecialStorageCompartment();
        storage1.setMaxNumGoods(3);
//        storage1.setAbility(new BLUE(), true);
//        storage1.setAbility(new GREEN(), true);
//        Assertions.assertEquals(new GREEN(), storage1.getGoods().get(0));

        PlayerBoard testboard= new PlayerBoard(2);
        //testboard.insertTile();
    }
}
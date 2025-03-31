package org.example.galaxy_trucker.Model.GetterHandler;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Exceptions.StorageCompartmentEmptyException;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.Tiles.SpecialStorageCompartment;
import org.example.galaxy_trucker.Model.Tiles.StorageCompartment;

import java.util.ArrayList;
import java.util.Map;

public class StorageComparmentGetter implements PlayerBoardGetters{


    private int i;
    private IntegerPair coordinate;
    private PlayerBoard playerBoard;

    public StorageComparmentGetter(PlayerBoard playerBoard, IntegerPair coordinate, int i){
        this.playerBoard = playerBoard;
        this.coordinate = coordinate;
        this.i = i;
    }



    /**
     * Method get removes a good from the specified StorageCompartment at the given position.
     *
     * @return the good removed.
     * @throws InvalidInput If the coordinates are out of bounds, the tile is invalid, or the selected tile is not a storage compartment.
     * @throws StorageCompartmentEmptyException If the storage compartment is empty and there are no goods to remove.
     */

    public Object get(){
        Tile[][] pb = playerBoard.getPlayerBoard();
        int[][] ValidPlayerBoard = playerBoard.getValidPlayerBoard();
        int x = coordinate.getFirst();
        int y = coordinate.getSecond();

        if (x < 0 || x >= 10 || y < 0 || y >= 10 || ValidPlayerBoard[x][y] == -1) {
            throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile.");
        }

        if (!checkExistence(playerBoard.getClassifiedTiles(),coordinate, StorageCompartment.class, SpecialStorageCompartment.class)){
            throw new InvalidInput("The following tile is not a storageCompartment");
        }

//        if (pb[x][y].getComponent().getAbility() == 0){
//            throw new StorageCompartmentEmptyException("The following StorageCompartment is Empty: " + x + "," + y);
//        }


//        Goods good = pb[x][y].getComponent().getAbility(null).remove(i);
//        playerBoard.getStoredGoods().get(good.getClass()).remove(new IntegerPair(x,y));
//        playerBoard.setTotalValue(-good.getValue());
//        return good;


        return null;
    }


    public boolean checkExistence(Map<Class<?>, ArrayList<IntegerPair>> classifiedTiles, IntegerPair tiles, Class<?> type1, Class<?> type2){

        return  (classifiedTiles.containsKey(type1) &&
                classifiedTiles.get(type1).contains(tiles) ) ||
                (classifiedTiles.containsKey(type2) &&
                        classifiedTiles.get(type2).contains(tiles))
                ;

    }


}

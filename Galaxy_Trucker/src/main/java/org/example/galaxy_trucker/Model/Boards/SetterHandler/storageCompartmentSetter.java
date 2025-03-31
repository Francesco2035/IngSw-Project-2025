package org.example.galaxy_trucker.Model.Boards.SetterHandler;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Exceptions.StorageCompartmentFullException;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.Tiles.SpecialStorageCompartment;

import java.util.ArrayList;
import java.util.Map;

public class storageCompartmentSetter implements PlayerBoardSetters{
    PlayerBoard playerBoard;
    IntegerPair coordinate;
    Goods good;

    public storageCompartmentSetter(PlayerBoard playerBoard, IntegerPair coordinate, Goods good){
        this.playerBoard = playerBoard;
        this.coordinate = coordinate;
        this.good = good;
    }


    //SET
    /**
     * Method putGoods adds one good to a storageCompartment.
     *
     * @throws InvalidInput If the coordinates are out of bounds, the tile is not a storage compartment.
     * @throws StorageCompartmentFullException If the storage compartment is full and cannot accept more goods.
     * @throws IllegalArgumentException If the good being added is not allowed in a standard storage compartment.
     */
    @Override
    public void set(){

        int x = coordinate.getFirst();
        int y = coordinate.getSecond();
        int[][] ValidPlayerBoard = playerBoard.getValidPlayerBoard();
        Tile[][] pb = playerBoard.getPlayerBoard();


        if (x < 0 || x >= 10 || y < 0 || y >= 10 || ValidPlayerBoard[x][y] == -1) {
            throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile.");
        }

        Map<Class<?>, ArrayList<IntegerPair>>  classifiedTiles = playerBoard.getClassifiedTiles();
        if (!checkExistence(classifiedTiles,coordinate, storageCompartmentSetter.class, SpecialStorageCompartment.class)){
            throw new InvalidInput("The following tile is not a storageCompartment");
        }

//        if (pb[x][y].getComponent().getAbility(null).size() + 1 > pb[x][y].getComponent().getAbility()){
//            throw new StorageCompartmentFullException("The following StorageCompartment is full: " + x + "," + y);
//        }

//        pb[coordinate.getFirst()][coordinate.getSecond()].getComponent().setAbility(good, true);
//        playerBoard.getStoredGoods().computeIfAbsent(good.getClass(), k -> new ArrayList<>()).add(coordinate);
//        playerBoard.setTotalValue(good.getValue());

    }




    public boolean checkExistence(Map<Class<?>, ArrayList<IntegerPair>> classifiedTiles, IntegerPair tiles, Class<?> type1, Class<?> type2){

        return  (classifiedTiles.containsKey(type1) &&
                classifiedTiles.get(type1).contains(tiles) ) ||
                (classifiedTiles.containsKey(type2) &&
                        classifiedTiles.get(type2).contains(tiles))
                ;

    }




}

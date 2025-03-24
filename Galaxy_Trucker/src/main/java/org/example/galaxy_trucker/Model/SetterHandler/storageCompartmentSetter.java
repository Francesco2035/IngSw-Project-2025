package org.example.galaxy_trucker.Model.SetterHandler;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Tiles.specialStorageCompartment;
import org.intellij.lang.annotations.Pattern;

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
    @Override
    public void set(){
        Map<Class<?>, ArrayList<IntegerPair>>  classifiedTiles = playerBoard.getClassifiedTiles();
        if (!checkExistence(classifiedTiles,coordinate, storageCompartmentSetter.class, specialStorageCompartment.class)){
            throw new InvalidInput("pietro");
        }

        playerBoard.getPlayerBoard()[coordinate.getFirst()][coordinate.getSecond()].getComponent().setAbility(good, true);
        playerBoard.getStoredGoods().computeIfAbsent(good.getClass(), k -> new ArrayList<>()).add(coordinate);

    }




    public boolean checkExistence(Map<Class<?>, ArrayList<IntegerPair>> classifiedTiles, IntegerPair tiles, Class<?> type1, Class<?> type2){

        return  (classifiedTiles.containsKey(type1) &&
                classifiedTiles.get(type1).contains(tiles) ) ||
                (classifiedTiles.containsKey(type2) &&
                        classifiedTiles.get(type2).contains(tiles))
                ;

    }




}

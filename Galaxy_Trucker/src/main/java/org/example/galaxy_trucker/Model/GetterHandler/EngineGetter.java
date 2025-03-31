package org.example.galaxy_trucker.Model.GetterHandler;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Tiles.ComponentGetters.EnginePowerGetter;
import org.example.galaxy_trucker.Model.Tiles.HotWaterHeater;

import java.util.ArrayList;
import java.util.Map;

public class EngineGetter implements PlayerBoardGetters{


    private ArrayList<IntegerPair> chosenHotWaterHeaters;
    PlayerBoard playerBoard;
    public EngineGetter(PlayerBoard pb, ArrayList<IntegerPair> chosenHotWaterHeaters){
        this.playerBoard = pb;

        this.chosenHotWaterHeaters = chosenHotWaterHeaters;

    }




    /**
     * Method getEnginePower calculates the instantaneous EnginePower of the ship also based on the player's choices
     *
     * @return the EnginePower of the ship.
     * @throws NullPointerException if chosenHotWaterHeaters is null.
     * @throws InvalidInput if at least one engine is invalid.
     */
    @Override
    public Object get() {
        Map<Class<?>, ArrayList<IntegerPair>> classifiedTiles = playerBoard.getClassifiedTiles();

        if (chosenHotWaterHeaters == null) {
            throw new NullPointerException("chosenHotWaterHeaters cannot be null.");
        }
        if (!checkExistence(classifiedTiles,chosenHotWaterHeaters, HotWaterHeater.class)) {
            throw new InvalidInput("Invalid input: at least one of the selected tils isn't an hotWaterHeater.");
        }

        int power = 0;
        for (IntegerPair engine : chosenHotWaterHeaters){

            if ((int)playerBoard.getPlayerBoard()[engine.getFirst()][engine.getSecond()].getComponent().get(new EnginePowerGetter(playerBoard.getPlayerBoard()[engine.getFirst()][engine.getSecond()].getComponent())) == 1){
                power += 1;
            }
            else{
                power += 2;
            }

        }

        if (playerBoard.getBrownAlien()){
            power += 2;
        }
        return power;
    }


    public boolean checkExistence(Map<Class<?>, ArrayList<IntegerPair>> classifiedTiles,ArrayList<IntegerPair> tiles, Class<?> type){
        return  classifiedTiles.containsKey(type) &&
                classifiedTiles.get(type).containsAll(tiles);
    }

}

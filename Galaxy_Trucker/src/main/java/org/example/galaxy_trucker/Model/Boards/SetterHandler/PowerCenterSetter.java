package org.example.galaxy_trucker.Model.Boards.SetterHandler;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Exceptions.powerCenterEmptyException;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Tiles.ComponentGetters.EnergyGetter;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.Tiles.PowerCenter;

import java.util.ArrayList;
import java.util.Map;

public class PowerCenterSetter implements PlayerBoardSetters{


    private ArrayList<IntegerPair> chosenEnergyTiles;
    PlayerBoard playerBoard;

    public PowerCenterSetter(PlayerBoard pb , ArrayList<IntegerPair> chosenEnergyTiles){
        this.playerBoard = pb;
        this.chosenEnergyTiles = chosenEnergyTiles;

    }

    /**
     * Method useEnergy reduces the energy of the tiles in the array by 1 - the position of the EnergyTiles chosen by the player,
     *                          there may be duplicates if the user wants to consume more energy from the same tile .
     *
     * @throws NullPointerException If the provided list is null.
     * @throws InvalidInput If at least one of the chosen energy tiles is invalid or does not exist in the player's energyTiles list.
     * @throws powerCenterEmptyException If any of the selected power centers are empty and cannot provide energy.
     */
    @Override
    public void set() {

        Map<Class<?>, ArrayList<IntegerPair>> classifiedTiles = playerBoard.getClassifiedTiles();
        Tile[][] pb = playerBoard.getPlayerBoard();
        if (chosenEnergyTiles == null) {
            throw new NullPointerException("chosenEnergyTiles cannot be null.");
        }
        if (!checkExistence(classifiedTiles,chosenEnergyTiles, PowerCenter.class)) {
            throw new InvalidInput("Invalid choice, at least one energy is invalid");
        }
        for (IntegerPair energy : chosenEnergyTiles){
            pb[energy.getFirst()][energy.getSecond()].getComponent().setComponentGetter(new EnergyGetter(pb[energy.getFirst()][energy.getSecond()].getComponent()));
            if ((int) pb[energy.getFirst()][energy.getSecond()].getComponent().getComponentGetter().get() == 0){
                throw new powerCenterEmptyException("PowerCenter is empty");
            }
            pb[energy.getFirst()][energy.getSecond()].getComponent().getComponentSetter().set();
        }
    }



    public boolean checkExistence(Map<Class<?>, ArrayList<IntegerPair>> classifiedTiles,ArrayList<IntegerPair> tiles, Class<?> type){
        return  classifiedTiles.containsKey(type) &&
                classifiedTiles.get(type).containsAll(tiles);
    }

}

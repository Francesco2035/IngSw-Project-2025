package org.example.galaxy_trucker.Model.Boards.GetterHandler;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Connectors.CANNON;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Tiles.ComponentGetters.CannonPowerGetter;
import org.example.galaxy_trucker.Model.Tiles.PlasmaDrill;

import java.util.ArrayList;
import java.util.Map;

public class PlasmaDrillsGetter implements PlayerBoardGetters{

    private ArrayList<IntegerPair> chosenPlasmaDrills;
    PlayerBoard playerBoard;
    public PlasmaDrillsGetter(PlayerBoard pb , ArrayList<IntegerPair> chosenPlasmaDrills){
        this.playerBoard = pb;

        this.chosenPlasmaDrills = chosenPlasmaDrills;

    }




    /**
     * Method get calculates the instantaneous power of the ship also based on the player's choices
     *
     * @return the Power of the ship.
     * @throws NullPointerException if chosenPlasmaDrills is null.
     */
    @Override
    public Object get() {
        Map<Class<?>, ArrayList<IntegerPair>> classifiedTiles = playerBoard.getClassifiedTiles();

        if (chosenPlasmaDrills == null) {
            throw new NullPointerException("chosenPlasmaDrills cannot be null.");
        }
        if (!checkExistence(classifiedTiles,chosenPlasmaDrills, PlasmaDrill.class)) {
            throw new InvalidInput("Invalid input: at least one of the selected tils isn't a plasmaDrill.");
        }

        double power = 0;
        for (IntegerPair cannon : chosenPlasmaDrills){
            if (playerBoard.getPlayerBoard()[cannon.getFirst()][cannon.getSecond()].getConnectors().get(1).equals(new CANNON())){
                if (((int) playerBoard.getPlayerBoard()[cannon.getFirst()][cannon.getSecond()].getComponent()
                        .get(new CannonPowerGetter(playerBoard.getPlayerBoard()[cannon.getFirst()][cannon.getSecond()].getComponent()))) == 1){
                    power += 1;
                }
                else{
                    power += 2;
                }
            }
            else{
                if (((int) playerBoard.getPlayerBoard()[cannon.getFirst()][cannon.getSecond()].getComponent().
                        get(new CannonPowerGetter(playerBoard.getPlayerBoard()[cannon.getFirst()][cannon.getSecond()].getComponent()))) == 1){
                    power += 0.5;
                }
                else{
                    power += 1;
                }
            }
        }
        if (playerBoard.getPurpleAlien()){
            power += 2;
        }
        return power;

    }


    public boolean checkExistence(Map<Class<?>, ArrayList<IntegerPair>> classifiedTiles,ArrayList<IntegerPair> tiles, Class<?> type){
        return  classifiedTiles.containsKey(type) &&
                classifiedTiles.get(type).containsAll(tiles);
    }


}

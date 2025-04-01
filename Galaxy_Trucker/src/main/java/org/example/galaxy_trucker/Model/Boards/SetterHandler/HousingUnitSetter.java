package org.example.galaxy_trucker.Model.Boards.SetterHandler;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Exceptions.StorageCompartmentFullException;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Tiles.ComponentGetters.HousingAlienGetter;
import org.example.galaxy_trucker.Model.Tiles.ComponentGetters.HousingHumanGetter;
import org.example.galaxy_trucker.Model.Tiles.ComponentGetters.NearAddonsGetter;
import org.example.galaxy_trucker.Model.Tiles.ComponentSetters.HousingSetter;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.ModularHousingUnit;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;
import java.util.Map;

public class HousingUnitSetter implements PlayerBoardSetters{

    PlayerBoard playerBoard;
    IntegerPair coordinate;
    private int humans;
    boolean purpleAlien;
    boolean brownAlien;

    public HousingUnitSetter(PlayerBoard playerBoard, IntegerPair coordinate, int humans, boolean purpleAlien, boolean brownAlien){
        this.playerBoard = playerBoard;
        this.coordinate = coordinate;
        this.humans = humans;
        this.brownAlien = brownAlien;
        this.purpleAlien = purpleAlien;
    }


    /**
     * Method set populates a housing unit with humans or aliens.
     *
     * @throws InvalidInput If the coordinates are out of bounds, the tile is invalid, the tile is not a housing unit,
     *                      aliens are added to the MainCockpit, aliens are added , or the combination of occupants is not allowed.
     * @throws StorageCompartmentFullException If the housing unit is already full and cannot accommodate more occupants.
     */

    @Override
    public void set(){


        Tile[][] pb = playerBoard.getPlayerBoard();
        int[][] ValidPlayerBoard = playerBoard.getValidPlayerBoard();
        int x = coordinate.getFirst();
        int y = coordinate.getSecond();

        if (x < 0 || x >= 10 || y < 0 || y >= 10 || ValidPlayerBoard[x][y] == -1) {
            throw new InvalidInput(x, y, "Invalid input: coordinates out of bounds or invalid tile.");
        }

        if (!checkExistence(playerBoard.getClassifiedTiles(),coordinate, ModularHousingUnit.class, MainCockpitComp.class)){
            throw new InvalidInput("The following tile is not a modularHousingUnit");
        }

        if (purpleAlien && brownAlien){
            throw new InvalidInput("Invalid input: only one alien can be added");
        }

        if (playerBoard.getPurpleAlien() && purpleAlien){
            throw new InvalidInput("Invalid input: only one purple alien can be added");
        }

        if (playerBoard.getBrownAlien() && brownAlien){
            throw new InvalidInput("Invalid input: only one brown alien can be added");
        }

        if (x == 6 && y == 6 && (purpleAlien || brownAlien)){
            throw new InvalidInput("Invalid input: aliens cannot be added to the MainCockpit");
        }

        if((purpleAlien && !(Boolean) pb[x][y].getComponent().get(new NearAddonsGetter((ModularHousingUnit) pb[x][y].getComponent(), true))) ||
                (brownAlien && !(Boolean) pb[x][y].getComponent().get(new NearAddonsGetter((ModularHousingUnit) pb[x][y].getComponent(), false)))){
            throw new InvalidInput("Invalid input: aliens cannot be added without specific Addons.");
        }

        if (((int) pb[x][y].getComponent().get(new HousingHumanGetter(pb[x][y].getComponent()))) == 2 && humans > 0){
            throw new StorageCompartmentFullException("The following StorageCompartment is Full: " + x + "," + y);
        }

        if (((int) pb[x][y].getComponent().get(new HousingHumanGetter(pb[x][y].getComponent()))) != 0 && (purpleAlien || brownAlien)){
            throw new InvalidInput("Invalid input: aliens cannot be added if humans are already present");
        }

        if ((((boolean) pb[x][y].getComponent().get(new HousingAlienGetter(pb[x][y].getComponent(), false))) || (((boolean) pb[x][y].getComponent().get(new HousingAlienGetter(pb[x][y].getComponent(), true))))) && humans > 0){
            throw new InvalidInput("Invalid input: humans cannot be added if an alien is already present");
        }

        if ((purpleAlien && ((boolean) pb[x][y].getComponent().get(new HousingAlienGetter(pb[x][y].getComponent(), false)))) || (brownAlien && (((boolean) pb[x][y].getComponent().get(new HousingAlienGetter(pb[x][y].getComponent(), true)))))){
            throw new InvalidInput("Invalid input: there is already an alien of the other color present");
        }

        if (purpleAlien){
            playerBoard.setPurpleAlien(true);
        }
        if (brownAlien){
            playerBoard.setBrownAlien(true);
        }
        playerBoard.getPlayerBoard()[x][y].getComponent().set(new HousingSetter(playerBoard.getPlayerBoard()[x][y].getComponent(), humans, purpleAlien, brownAlien));

    }


    public boolean checkExistence(Map<Class<?>, ArrayList<IntegerPair>> classifiedTiles, IntegerPair tiles, Class<?> type1, Class<?> type2){

        return  (classifiedTiles.containsKey(type1) &&
                classifiedTiles.get(type1).contains(tiles) ) ||
                (classifiedTiles.containsKey(type2) &&
                        classifiedTiles.get(type2).contains(tiles))
                ;

    }
}

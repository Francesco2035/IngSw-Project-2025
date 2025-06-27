package org.example.galaxy_trucker.Model.Tiles;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.util.ArrayList;

/**
 * Represents a modular housing unit within the game, designed to accommodate humans and alien entities.
 * This class determines interactions and connections with nearby housing units, alien addons, and other components.
 * It also manages the internal state of the modular unit such as the number of humans, alien occupants,
 * and adjacency to alien addons.
 *
 * The behavior of the modular housing unit includes validity checks for connections, managing its occupancy,
 * and handling the consequences of invalid configurations such as killing its occupants.
 * It also integrates with the game board's state and updates relevant connections and units accordingly.
 */
public class ModularHousingUnit extends HousingUnit {

    /**
     * Indicates whether the component is connected to others within
     * the ModularHousingUnit structure. A value of {@code true} means
     * the connection is established, while {@code false} indicates it is not.
     */
    private boolean connected = false;

    /**
     * Represents the number of humans currently associated with a
     * specific modular housing unit. This value is used to track the
     * human crew members present in the corresponding housing unit.
     */
    protected int numHumans;
    /**
     * Indicates whether the housing unit contains a purple alien.
     * This variable is used to determine the presence of a purple alien
     * in the associated modular housing unit.
     */
    protected boolean purpleAlien;
    /**
     * Indicates the presence of brown aliens in the housing unit.
     * This variable is used to track whether brown aliens are occupying
     * the specific modular housing unit in the game.
     *
     * A value of {@code true} signifies that brown aliens are present,
     * while a value of {@code false} indicates their absence.
     */
    protected boolean brownAlien;
    /**
     * Indicates whether the modular housing unit is located near a purple addon.
     * This flag is used to determine proximity to a purple addon,
     * which may trigger certain behaviors or functionalities within the modular housing unit.
     */
    protected boolean nearPurpleAddon;
    /**
     * Indicates whether the modular housing unit is located near a brown addon.
     * This variable acts as a flag to determine adjacency-based conditions
     * related to brown addons in the game logic. It may influence the behavior
     * of the housing unit during gameplay, such as interactions with brown alien
     * species or augmentations provided by the addon.
     */
    protected boolean nearBrownAddon;
    /**
     * Represents the player's board associated with this ModularHousingUnit.
     * This variable tracks the PlayerBoard instance that the unit belongs to
     * and is used to manage interactions or operations related to the player.
     */
    private PlayerBoard playerBoard;

    /**
     * Retrieves the number of humans currently associated with this modular housing unit.
     *
     * @return the number of humans in this unit
     */
    @Override
    public int getNumHumans() {
        return this.numHumans;
    }

    /**
     * Sets the number of humans for the housing unit.
     *
     * @param numHumans the number of humans to be set for the housing unit
     */
    @Override
    public void setNumHumans(int numHumans) {
        this.numHumans = numHumans;
    }

    /**
     * Determines if the current unit is a purple alien.
     *
     * @return true if the unit is identified as a purple alien, false otherwise.
     */
    @Override
    public boolean isPurpleAlien() {
        return this.purpleAlien;
    }

    /**
     * Sets the purpleAlien status for the modular housing unit.
     *
     * @param purpleAlien a boolean value indicating whether the modular housing unit contains a purple alien.
     */
    @Override
    public void setPurpleAlien(boolean purpleAlien) {
        this.purpleAlien = purpleAlien;
    }

    /**
     * Determines whether the current unit contains a brown alien.
     *
     * @return true if the unit contains a brown alien, false otherwise.
     */
    @Override
    public boolean isBrownAlien() {
        return this.brownAlien;
    }

    /**
     * Updates the state of the brownAlien field of the ModularHousingUnit.
     *
     * @param brownAlien a boolean value indicating whether a brown alien is
     *                   present in the modular housing unit.
     */
    @Override
    public void setBrownAlien(boolean brownAlien) {
        this.brownAlien = brownAlien;
    }

    /**
     * Determines whether this modular housing unit is near a purple addon.
     *
     * @return true if the modular housing unit is near a purple addon, false otherwise.
     */
    @Override
    public boolean isNearPurpleAddon() {
        return this.nearPurpleAddon;
    }

    /**
     * Sets the state of whether a "near purple addon" is present for the modular housing unit.
     *
     * @param nearPurpleAddon a boolean value indicating whether the housing unit is near a purple addon.
     */
    @Override
    public void setNearPurpleAddon(boolean nearPurpleAddon) {
        this.nearPurpleAddon = nearPurpleAddon;
    }

    /**
     * Determines if the modular housing unit is near a brown addon.
     *
     * @return true if the modular housing unit is near a brown addon, false otherwise.
     */
    @Override
    public boolean isNearBrownAddon() {
        return this.nearBrownAddon;
    }

    /**
     * Sets whether the modular housing unit is near a brown addon.
     *
     * @param nearBrownAddon a boolean indicating if the modular housing unit is near a brown addon.
     */
    @Override
    public void setNearBrownAddon(boolean nearBrownAddon) {
        this.nearBrownAddon = nearBrownAddon;
    }




    /**
     * Controls the validity of the current modular housing unit based on its position on the player's board.
     * It checks connections between adjacent tiles, verifies nearby alien addons, and updates the status
     * of connected housing units and entity attributes accordingly. If certain conditions are not met,
     * the housing unit may be eliminated.
     *
     * @param pb The player's board on which the modular housing unit is being placed or assessed.
     * @param x The x-coordinate of the tile being checked on the player's board.
     * @param y The y-coordinate of the tile being checked on the player's board.
     * @return A boolean indicating whether the modular housing unit's placement and connections are valid.
     */
    @Override
    //@SuppressWarnings("SuspiciousMethodCalls")
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        this.playerBoard = pb;
        Tile tile = playerBoard.getTile(x,y);
        nearBrownAddon = false;
        nearPurpleAddon = false;
        connected = false;

        int[][] validPlayerBoard = playerBoard.getValidPlayerBoard();
        int index = 0;


        //playerBoard.getConnectedHousingUnits().remove(this);
        ArrayList<HousingUnit> nearbyunits = getNearbyHousingUnits();

        if (validPlayerBoard[x-1][y] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(1), pb.getTile(x-1,y).getConnectors().get(3)) && playerBoard.getHousingUnits().contains(pb.getTile(x-1,y).getComponent())) {
            HousingUnit u = pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x-1,y).getComponent())));
            nearbyunits.remove(u);
            nearbyunits.add(u);
        }
        if (validPlayerBoard[x+1][y] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(3), pb.getTile(x+1,y).getConnectors().get(1)) && playerBoard.getHousingUnits().contains( pb.getTile(x+1,y).getComponent())) {
            HousingUnit u = pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x+1,y).getComponent())));
            nearbyunits.remove(u);
            nearbyunits.add(u);
        }
        if (validPlayerBoard[x][y-1] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(0), pb.getTile(x,y-1).getConnectors().get(2)) && playerBoard.getHousingUnits().contains( pb.getTile(x,y-1).getComponent())) {
            HousingUnit u = pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y-1).getComponent())));
            nearbyunits.remove(u);
            nearbyunits.add(u);
        }
        if (y < 9 && validPlayerBoard[x][y+1] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(2), pb.getTile(x,y+1).getConnectors().get(0)) && playerBoard.getHousingUnits().contains( pb.getTile(x,y+1).getComponent())) {
            HousingUnit u = pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y+1).getComponent())));
            nearbyunits.remove(u);
            nearbyunits.add(u);
        }


        if(validPlayerBoard[x][y-1] == 1 && playerBoard.getAlienAddons().contains(playerBoard.getTile(x,y-1).getComponent())){
            index = playerBoard.getAlienAddons().indexOf(playerBoard.getTile(x,y-1).getComponent());
            if (tile.getConnectors().get(0).checkAdjacent(playerBoard.getTile(x,y-1).getConnectors().get(2))){

                if (validPlayerBoard[x][y-1] == 1 && playerBoard.getAlienAddons().get(index).type == 1 ){
                    nearPurpleAddon = true;
                }
                else {
                    nearBrownAddon = true;
                }
            }
        }

        if(validPlayerBoard[x-1][y] == 1 && playerBoard.getAlienAddons().contains(playerBoard.getTile(x-1,y).getComponent())){
            index = playerBoard.getAlienAddons().indexOf(playerBoard.getTile(x-1,y).getComponent());
            if (tile.getConnectors().get(1).checkAdjacent(playerBoard.getTile(x-1,y).getConnectors().get(3))){

                if (validPlayerBoard[x-1][y] == 1 && playerBoard.getAlienAddons().get(index).type == 1 ){
                    nearPurpleAddon = true;
                }
                else {
                    nearBrownAddon = true;
                }
            }
        }

        if(y < 9 && validPlayerBoard[x][y+1] == 1 && playerBoard.getAlienAddons().contains(playerBoard.getTile(x,y+1).getComponent())){
            index = playerBoard.getAlienAddons().indexOf(playerBoard.getTile(x,y+1).getComponent());
            if (tile.getConnectors().get(2).checkAdjacent(playerBoard.getTile(x,y+1).getConnectors().get(0))){

                if (validPlayerBoard[x][y+1] == 1 && playerBoard.getAlienAddons().get(index).type == 1 ){
                    nearPurpleAddon = true;
                }
                else {
                    nearBrownAddon = true;
                }
            }
        }
        // Safe: getComponent() returns Component, and list contains Component
        if(validPlayerBoard[x+1][y] == 1 && playerBoard.getAlienAddons().contains(playerBoard.getTile(x +1,y).getComponent())){
            index = playerBoard.getAlienAddons().indexOf(playerBoard.getTile(x+1,y).getComponent());
            //System.out.println("dovrei entrare qui "+index);
            if (tile.getConnectors().get(3).checkAdjacent(playerBoard.getTile(x+1,y).getConnectors().get(1))){

                if (validPlayerBoard[x+1][y] == 1 && playerBoard.getAlienAddons().get(index).type == 1 ){
                    nearPurpleAddon = true;
                }
                else {
                    nearBrownAddon = true;
                }
            }

        }

        if(numHumans > 0 || isBrownAlien() || isPurpleAlien()){
            for (HousingUnit unit : getNearbyHousingUnits()){
                if(unit.getNumHumans() > 0 || unit.isBrownAlien() || unit.isPurpleAlien()){
                    playerBoard.getConnectedHousingUnits().remove(unit);
                    playerBoard.getConnectedHousingUnits().add(unit);
                }
            }
        }

        if((!nearPurpleAddon && purpleAlien) || (brownAlien && !nearBrownAddon)){
            kill();
        }

        return true;
    }



    /**
     * Accepts a visitor and allows it to perform an action on this component
     * using the visitor pattern. The visitor will execute its specific logic
     * for handling this component and interact with the provided player state.
     *
     * @param visitor the ComponentAction visitor that is performing an operation
     *                on this component
     * @param state the PlayerState representing the current state of the player
     *              interacting with this component
     */
    @Override
    public void accept(ComponentAction visitor, PlayerState state) {
        visitor.visit(this, state);
    }

    /**
     * Executes the kill operation on the current modular housing unit. Depending on the state of the unit,
     * it decreases the count of humans, removes aliens, and updates any nearby housing units.
     * If the housing unit is empty and does not contain any aliens, an exception is thrown.
     *
     * @return an integer indicating the type of occupant that was removed:
     *         - 2 if a human was removed,
     *         - 1 if a purple alien was removed,
     *         - 0 if a brown alien was removed,
     *         - throws InvalidInput if the unit is empty with no occupants.
     */
    @Override
    public int kill(){
        if (numHumans == 0 && !purpleAlien && !brownAlien){
            throw new InvalidInput("This ModularHousingUnit is empty! "+getX()+" "+getY());
        }
        if (numHumans != 0){
            numHumans--;
            System.out.println("killed a human in "+getX()+" "+getY());
            if (numHumans == 0){
                playerBoard.getConnectedHousingUnits().remove(this);
            }
            if(numHumans == 0 && !getNearbyHousingUnits().isEmpty()){
                playerBoard.getConnectedHousingUnits().remove(this);
                for (HousingUnit housingUnit : getNearbyHousingUnits()) {
                    housingUnit.notifyUnit(false, this);
                }
            }
            tile.sendUpdates(null, numHumans,purpleAlien,brownAlien,0);
            return 2;
        }
        else if (purpleAlien){
            purpleAlien = false;
            System.out.println("killed a purple alien in "+getX()+" "+getY());
            playerBoard.getConnectedHousingUnits().remove(this);
            if(!getNearbyHousingUnits().isEmpty()){
                for (HousingUnit housingUnit : getNearbyHousingUnits()) {
                    housingUnit.notifyUnit(false, this);
                }
            }
            tile.sendUpdates(null, numHumans,purpleAlien,brownAlien,0);


            return 1;

        }
        else {
            brownAlien = false;
            System.out.println("killed a brown alien in "+getX()+" "+getY());
            playerBoard.getConnectedHousingUnits().remove(this);
            if(!getNearbyHousingUnits().isEmpty()){
                for (HousingUnit housingUnit : getNearbyHousingUnits()) {
                    housingUnit.notifyUnit(false, this);
                }
            }
            tile.sendUpdates(null, numHumans,purpleAlien,brownAlien,0);

            return 0;
        }

    }

    /**
     * Adds a crew to the modular housing unit. The crew can consist of humans or aliens of specific types (purple or brown).
     * Validates the input and ensures the addition complies with housing unit rules.
     *
     * @param humans the number of human crew members to add. Must be between 0 and 2, inclusive.
     * @param purple indicates if a purple alien is to be added. True if adding a purple alien, false otherwise.
     * @param brown indicates if a brown alien is to be added. True if adding a brown alien, false otherwise.
     * @throws InvalidInput if there are violations with housing unit rules such as:
     *                      - Adding humans when aliens are present.
     *                      - Adding both humans and aliens in a single call.
     *                      - Adding more than 2 humans in total to the unit.
     *                      - Adding an alien when another alien is already present.
     *                      - Adding an alien without a nearby applicable addon.
     */
    @Override
    public void addCrew(int humans, boolean purple, boolean brown){

        if ((purple && !nearPurpleAddon ) || (brown && !nearBrownAddon)){
            throw new InvalidInput("There isn't a nearby addon");
        }

        if (humans != 0 && (brownAlien || purpleAlien)){
            throw new InvalidInput("You can't add humans, there is an alien!");
        }

        if (humans > 2){
            throw new InvalidInput("Input Human amount is greater than 2");
        }
        if (humans > 0 && (purple || brown)){
            throw new InvalidInput("Is possible to add only one type of alien or humans");
        }
        if (numHumans + humans > 2){
            throw new InvalidInput("Human amount is greater than 2");
        }
        if ((purple && brownAlien) || (brown && purpleAlien) || (purple && purpleAlien) || (brown && brownAlien)){
            throw new InvalidInput("There is already an alien");
        }
        numHumans += humans;
        purpleAlien = purple;
        brownAlien = brown;
        if(!getNearbyHousingUnits().isEmpty()){
            for (HousingUnit housingUnit : getNearbyHousingUnits()) {
                housingUnit.notifyUnit(true, this);
            }
        }


        tile.sendUpdates(null,numHumans,purpleAlien,brownAlien,0);
    }


    /**
     * Creates and returns a deep copy of this ModularHousingUnit instance.
     * The cloned instance will have the same properties as the original,
     * but it will reference a new PlayerBoard object provided as input.
     *
     * @param clonedPlayerBoard the PlayerBoard instance to be associated with the cloned object
     * @return a new Component instance that is a clone of this ModularHousingUnit
     */
    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        ModularHousingUnit clone = new ModularHousingUnit();
        clone.setBrownAlien(brownAlien);
        clone.setPurpleAlien(purpleAlien);
        clone.setNearPurpleAddon(nearPurpleAddon);
        clone.setNearBrownAddon(nearBrownAddon);
        clone.setNumHumans(numHumans);
        clone.setConnected(connected);
        clone.setPlayerBoard(clonedPlayerBoard);
        clone.setX(this.getX());
        clone.setY(this.getY());
        if (connected && this.playerBoard.getConnectedHousingUnits().contains(this)){
            clonedPlayerBoard.getConnectedHousingUnits().add(clone);
        }
        return clone;
    }

    /**
     * Sets the connected state of the ModularHousingUnit.
     *
     * @param connected a boolean indicating whether the ModularHousingUnit is connected (true) or disconnected (false)
     */
    public void setConnected(boolean connected){
        this.connected = connected;
    }

    /**
     * Sets the PlayerBoard for the modular housing unit.
     *
     * @param playerBoard the PlayerBoard instance to be associated with this modular housing unit
     */
    public void setPlayerBoard(PlayerBoard playerBoard){
        this.playerBoard = playerBoard;
    }


    /**
     * Notifies the housing unit about changes based on the specified type.
     *
     * @param type a boolean indicating the type of notification. If true,
     *             processes the connection logic for the housing unit. If false,
     *             removes the specified unit and updates the nearby housing units.
     * @param unit the housing unit to be notified or updated as part of the operation.
     */
    @Override
    public void notifyUnit(boolean type, HousingUnit unit){
        if (type){
            if (numHumans > 0 || purpleAlien || brownAlien){
                playerBoard.getConnectedHousingUnits().remove(unit);
                playerBoard.getConnectedHousingUnits().add(unit);
                playerBoard.getConnectedHousingUnits().remove(this);
                playerBoard.getConnectedHousingUnits().add(this);
            }
        }
        else {
            getNearbyHousingUnits().remove(unit);
            if(getNearbyHousingUnits().isEmpty()){
                playerBoard.getConnectedHousingUnits().remove(this);
            }

        }
    }



}





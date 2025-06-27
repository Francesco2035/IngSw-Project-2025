package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.util.ArrayList;

/**
 * Represents an abstract housing unit within a tile-based game board.
 * HousingUnit serves as a component that can hold crew members,
 * alien types, and support connectivity with nearby housing units.
 *
 * The HousingUnit manages state and behavior related to its position
 * on a player board, the presence of humans/aliens, adjacent units,
 * and interactions with other game components.
 */
public abstract class HousingUnit extends Component{


    /**
     * Represents the x-coordinate of the housing unit's position
     * on the player board or grid.
     *
     * This variable is used to store and manage the horizontal position of
     * the housing unit, which is important for spatial and logical game operations.
     */
    private int x;
    /**
     * Represents the Y-coordinate of the housing unit.
     * This variable is used to track the vertical position of the unit in the context of the game or system logic.
     */
    private int y;
    /**
     * A list that holds references to housing units located nearby the current housing unit.
     * This can be used to manage and interact with adjacent or surrounding housing units, allowing
     * spatial relationships and interactions such as proximity checks or group actions.
     */
    private ArrayList<HousingUnit> nearbyHousingUnits = new ArrayList<>();
    /**
     * A list that holds references to the housing units that are observing or listening
     * to the current housing unit. These listeners are typically neighboring units or
     * units that are contextually related to the current unit.
     */
    private ArrayList<HousingUnit> unitsListeners = new ArrayList<>();

    /**
     * Retrieves the number of humans associated with this housing unit.
     *
     * @return the number of humans present in the housing unit.
     */
    public abstract int getNumHumans();
    /**
     * Sets the number of humans in the housing unit.
     *
     * @param numHumans the number of humans to set
     */
    public abstract void setNumHumans(int numHumans);

    /**
     * Determines whether the housing unit is inhabited by a purple alien.
     *
     * @return true if the housing unit contains a purple alien, false otherwise.
     */
    public abstract boolean isPurpleAlien();
    /**
     * Sets whether the HousingUnit is associated with a purple alien.
     *
     * @param purpleAlien true if the HousingUnit should be marked as having a purple alien, false otherwise
     */
    public abstract void setPurpleAlien(boolean purpleAlien);
    /**
     * Determines if the current housing unit contains a brown alien.
     *
     * @return true if the housing unit contains a brown alien, false otherwise.
     */
    public abstract boolean isBrownAlien();
    /**
     * Sets the brownAlien state for the housing unit.
     *
     * @param brownAlien a boolean value indicating whether the housing unit contains a brown alien (true) or not (false)
     */
    public abstract void setBrownAlien(boolean brownAlien);

    /**
     * Determines whether the current housing unit is near a purple addon.
     *
     * @return true if the housing unit is near a purple addon, false otherwise.
     */
    public abstract boolean isNearPurpleAddon();
    /**
     * Sets the status of whether this housing unit is near a "Purple Addon."
     *
     * @param nearPurpleAddon a boolean indicating if the housing unit is near a Purple Addon.
     *                        Pass true if it is near, false otherwise.
     */
    public abstract void setNearPurpleAddon(boolean nearPurpleAddon);
    /**
     * Determines whether the current housing unit is located near a "Brown Addon."
     *
     * @return true if the unit is near a "Brown Addon," false otherwise.
     */
    public abstract boolean isNearBrownAddon();
    /**
     * Sets whether the housing unit is near a brown addon.
     *
     * @param nearBrownAddon a boolean value indicating if the housing unit is near a brown addon.
     *                        {@code true} if near a brown addon; {@code false} otherwise.
     */
    public abstract void setNearBrownAddon(boolean nearBrownAddon);




    /**
     * Rotates the component of the housing unit in the specified direction.
     *
     * @param direction if true, rotates the component clockwise;
     *                  if false, rotates the component counterclockwise.
     */
    @Override
    public void rotate(Boolean direction){}

    /**
     * Retrieves the list of nearby housing units associated with the housing unit.
     *
     * @return an ArrayList of HousingUnit objects representing the nearby housing units.
     */
    public ArrayList<HousingUnit> getNearbyHousingUnits() {
        return nearbyHousingUnits;
    }

//    public ArrayList<HousingUnit> getUnitsListeners() {
//        return unitsListeners;
//    }
//
//    public void setUnitsListeners(HousingUnit unitListener) {
//        unitsListeners.add(unitListener);
//    }

    /**
     * Notifies a specific housing unit of a certain type of event or update.
     *
     * @param type A boolean indicating the type of notification.
     *             If true, it signifies a certain action (e.g., adding a crew).
     * @param unit The HousingUnit object that is being notified.
     */
    //true addcrew
    public abstract void notifyUnit(boolean type, HousingUnit unit);

    /**
     * Abstract method to kill a specified target or entity associated with the housing unit.
     *
     * @return an integer representing the result or outcome of the kill operation,
     *         which could denote success, remaining units, or other implementation-specific values.
     */
    public abstract int kill();

    /**
     * Accepts a visitor object to perform an operation on the HousingUnit component
     * following the visitor design pattern. This method enables external operations
     * to be performed on the component without modifying its structure, facilitating
     * flexibility and modularity.
     *
     * @param visitor the ComponentAction visitor that performs a specific action on this component
     * @param State the current PlayerState representing the state of the player interacting with the component
     */
    public abstract void accept(ComponentAction visitor, PlayerState State);

    /**
     * Adds crew members to the housing unit.
     *
     * @param humans the number of human crew members to add
     * @param purple indicates if a purple alien crew member should be added
     * @param brown indicates if a brown alien crew member should be added
     */
    public abstract void addCrew(int humans, boolean purple, boolean brown);

    /**
     * Inserts the current housing unit into the specified player board at the given coordinates.
     *
     * @param playerBoard the player board into which the housing unit will be inserted
     * @param x the x-coordinate where the housing unit should be placed
     * @param y the y-coordinate where the housing unit should be placed
     */
    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
        //System.out.println("HousingUnit insert called, i'm here: " + x + ", " + y);
        //playerBoard.getHousingUnits().remove(this);
        playerBoard.getHousingUnits().add(this);
        playerBoard.setNumHumans(getNumHumans());
        this.x = x;
        this.y = y;
        tile.sendUpdates(null,getNumHumans(),isPurpleAlien(),isBrownAlien(),0);
    }

    /**
     * Removes the current housing unit from the player's board and updates
     * associated states and connections.
     *
     * @param playerBoard The player's board from which this housing unit will be removed.
     */
    @Override
    public void remove(PlayerBoard playerBoard)  {
        playerBoard.getHousingUnits().remove(this);
        playerBoard.getConnectedHousingUnits().remove(this);
        playerBoard.setNumHumans(-getNumHumans());
        if (isBrownAlien()){
            playerBoard.setBrownAlien(false);
        }
        if (isPurpleAlien()){
            playerBoard.setPurpleAlien(false);
        }
        for (HousingUnit unit : nearbyHousingUnits){
            unit.notifyUnit(false, this);
        }
        tile.sendUpdates();
    }

    /**
     * Retrieves the x-coordinate of the housing unit.
     *
     * @return the x-coordinate as an integer.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of this housing unit.
     *
     * @return the y-coordinate of the housing unit as an integer
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the value of the x property.
     *
     * @param x the integer value to set for x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the value of the y-coordinate.
     *
     * @param y the new value to set for the y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }


    /**
     * Determines whether the housing unit is populated based on the presence of humans
     * or specific alien species.
     *
     * @return true if the housing unit contains at least one human, a brown alien,
     *         or a purple alien; false otherwise.
     */
    public boolean isPopulated(){
        return getNumHumans() > 0 || isBrownAlien() || isPurpleAlien();
    }

    /**
     * Checks for nearby housing units that are populated and establishes connections
     * to these units if certain conditions are met. The method considers the player's
     * board setup and evaluates whether nearby tiles are connected or contain valid
     * housing units.
     *
     * @param pb the player board object that contains housing units, their configurations,
     *           and methods to check connections between tiles
     */
    public void checkNearbyUnits(PlayerBoard pb){
        if(getNumHumans() > 0 || isBrownAlien() || isPurpleAlien()){
            int x = getX();
            int y = getY();
            int[][] validPlayerBoard = pb.getValidPlayerBoard();
            int index = 0;
            pb.getConnectedHousingUnits().remove(this);
            ArrayList<HousingUnit> nearbyunits = getNearbyHousingUnits();

            if (validPlayerBoard[x-1][y] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(1), pb.getTile(x-1,y).getConnectors().get(3)) && pb.getHousingUnits().contains(pb.getTile(x-1,y).getComponent())) {
                if (pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x-1,y).getComponent()))).isPopulated()){
                    nearbyunits.add(pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x-1,y).getComponent()))));
                }
            }
            if (validPlayerBoard[x+1][y] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(3), pb.getTile(x+1,y).getConnectors().get(1)) && pb.getHousingUnits().contains( pb.getTile(x+1,y).getComponent())) {
                if (pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x+1,y).getComponent()))).isPopulated()){
                    nearbyunits.add(pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x+1,y).getComponent()))));
                }
            }
            if (validPlayerBoard[x][y-1] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(0), pb.getTile(x,y-1).getConnectors().get(2)) && pb.getHousingUnits().contains( pb.getTile(x,y-1).getComponent())) {
                if (pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y-1).getComponent()))).isPopulated()){
                    nearbyunits.add(pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y-1).getComponent()))));
                }
            }
            if (y < 9 && validPlayerBoard[x][y+1] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(2), pb.getTile(x,y+1).getConnectors().get(0)) && pb.getHousingUnits().contains( pb.getTile(x,y+1).getComponent())) {
                if (pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y+1).getComponent()))).isPopulated()){
                    nearbyunits.add(pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y+1).getComponent()))));
                }
            }

            if(!nearbyunits.isEmpty()){
                pb.getConnectedHousingUnits().remove(this);
                pb.getConnectedHousingUnits().add(this);
            }
        }
    }

}

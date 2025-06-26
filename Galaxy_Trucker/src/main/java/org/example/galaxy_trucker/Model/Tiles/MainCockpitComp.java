package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.util.ArrayList;

/**
 * The MainCockpitComp class represents the main cockpit component
 * of a housing unit. It is responsible for managing human crew members,
 * ensuring connections to other components, and enforcing specific rules
 * about the presence of aliens or addons. This class extends the HousingUnit
 * base class, inheriting its core functionality, while also implementing additional
 * features specific to the main cockpit.
 */
public class MainCockpitComp extends HousingUnit {

    /**
     * Represents the number of human crew members currently present.
     *
     * This field indicates the quantity of humans associated with the specific
     * component or context in which it is used. It may be utilized in operations such
     * as crew management, game mechanics, or other relevant logic.
     */
    private int numHumans;
    /**
     * Represents the connection status of the component.
     * The variable indicates whether the component is currently connected within the system or not.
     * It is initialized to {@code false} by default, implying a disconnected state.
     */
    private boolean connected = false;
    /**
     * Represents the player's game board in the context of the MainCockpitComp class.
     * This variable holds the current state and configuration of the player's board,
     * including all associated elements and methods for interaction during gameplay.
     * It is used to manage and control relevant game mechanics tied to the player's board.
     */
    private PlayerBoard playerBoard;


    /**
     * Retrieves the number of human crew members associated with this component.
     *
     * @return the number of humans currently assigned to this component.
     */
    @Override
    public int getNumHumans() {
        return this.numHumans;
    }

    /**
     * Sets the number of humans associated with the component.
     *
     * @param numHumans the number of humans to be set
     */
    public void setNumHumans(int numHumans) {
        this.numHumans = numHumans;
    }

    /**
     * Determines if the current entity is classified as a purple alien.
     *
     * @return {@code true} if the entity is a purple alien; {@code false} otherwise.
     */
    @Override
    public boolean isPurpleAlien() {
        return false;
    }

    /**
     * Sets whether the MainCockpit component contains a purple alien.
     *
     * @param purpleAlien a boolean indicating the presence of a purple alien.
     *                    If true, an IllegalArgumentException is thrown as the
     *                    MainCockpit cannot have aliens.
     * @throws IllegalArgumentException if the parameter purpleAlien is true.
     */
    @Override
    public void setPurpleAlien(boolean purpleAlien) throws IllegalArgumentException {
        if (purpleAlien) {
            throw new IllegalArgumentException("MainCockpit can't have aliens");
        }

    }

    /**
     * Determines if the current component represents a brown alien.
     *
     * @return true if the component is a brown alien, otherwise false.
     */
    @Override
    public boolean isBrownAlien() {
        return false;
    }

    /**
     * Sets the brown alien attribute for the MainCockpit component.
     *
     * @param brownAlien a boolean indicating whether a brown alien is present.
     *                   If set to true, an IllegalArgumentException will be thrown, as MainCockpit cannot have aliens.
     * @throws IllegalArgumentException if the brownAlien parameter is true.
     */
    @Override
    public void setBrownAlien(boolean brownAlien) {
        if (brownAlien) {
            throw new IllegalArgumentException("MainCockpit can't have aliens");
        }
    }

    /**
     * Checks whether the current component is near a purple addon.
     *
     * @return true if the component is near a purple addon, otherwise false.
     */
    @Override
    public boolean isNearPurpleAddon() {
        return false;
    }

    /**
     * Sets the state of whether the component is near a purple addon.
     *
     * @param nearPurpleAddon a boolean indicating if the component is near a purple addon.
     *                        Throws an IllegalArgumentException if the condition involves aliens.
     */
    @Override
    public void setNearPurpleAddon(boolean nearPurpleAddon) {
        throw new IllegalArgumentException("can't have aliens");
    }

    /**
     * Checks whether the current object is near a brown addon.
     *
     * @return {@code true} if the object is near a brown addon, otherwise {@code false}.
     */
    @Override
    public boolean isNearBrownAddon() {
        return false;
    }

    /**
     * Sets the state of the near brown addon for the current component.
     *
     * @param nearBrownAddon a boolean indicating whether the near brown addon is active.
     * @throws IllegalArgumentException if the method invocation violates the component's rules.
     */
    @Override
    public void setNearBrownAddon(boolean nearBrownAddon) {
        throw new IllegalArgumentException("can't have aliens");
    }

    /**
     * Notifies a housing unit with a specified type of action or status.
     *
     * @param type a boolean indicating the type of notification (e.g., true for one type, false for another)
     * @param unit the housing unit to be notified
     */
    @Override
    public void notifyUnit(boolean type, HousingUnit unit) {}


    /**
     * Validates and updates the connection status of the housing units on the player board
     * based on the specified coordinates and surrounding tiles.
     *
     * @param pb the player board on which to perform the validity check
     * @param x the x-coordinate of the tile to validate
     * @param y the y-coordinate of the tile to validate
     * @return true if the connections and nearby housing units are valid, false otherwise
     */
    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        this.playerBoard = pb;
        this.connected = false;
        //playerBoard.getConnectedHousingUnits().remove(this);
        ArrayList<HousingUnit> nearbyunits = getNearbyHousingUnits();

        int[][] validPlayerBoard = pb.getValidPlayerBoard();
        if (validPlayerBoard[x-1][y] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(1), pb.getTile(x-1,y).getConnectors().get(3)) && playerBoard.getHousingUnits().contains(pb.getTile(x-1,y).getComponent())) {
            HousingUnit u = pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x-1,y).getComponent())));
            nearbyunits.remove(u);
            nearbyunits.add(u);
        }
        if (validPlayerBoard[x+1][y] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(3), pb.getTile(x+1,y).getConnectors().get(1))&& playerBoard.getHousingUnits().contains(pb.getTile(x+1,y).getComponent())) {
            HousingUnit u = pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x+1,y).getComponent())));
            nearbyunits.remove(u);
            nearbyunits.add(u);
        }
        if (validPlayerBoard[x][y-1] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(0), pb.getTile(x,y-1).getConnectors().get(2))&& playerBoard.getHousingUnits().contains(pb.getTile(x,y-1).getComponent())) {
            HousingUnit u = pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y-1).getComponent())));
            nearbyunits.remove(u);
            nearbyunits.add(u);
        }
        if (y < 9 && validPlayerBoard[x][y+1] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(2), pb.getTile(x,y+1).getConnectors().get(0))&& playerBoard.getHousingUnits().contains(pb.getTile(x,y+1).getComponent())) {
            HousingUnit u = pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y+1).getComponent())));
            nearbyunits.remove(u);
            nearbyunits.add(u);
        }

        return true;
    }



    /**
     * Accepts a visitor implementing a specific action on the component and player state.
     * This method enables the use of the visitor pattern, allowing interaction between
     * the current component and a specified action. The visitor performs its logic on the
     * component based on the provided player state.
     *
     * @param visitor the ComponentAction visitor that defines the action to be performed
     * @param State the current state of the player, which may influence the action's logic
     */
    @Override
    public void accept(ComponentAction visitor, PlayerState State) {

        visitor.visit(this, State);
    }


    /**
     * Decreases the number of humans present in the MainCockPitComp. If the count of humans
     * reaches zero, the component is removed from connected housing units in the player board
     * and notifies nearby housing units. Finally, it sends updates to the associated tile.
     *
     * @return an integer value of 2 to indicate the completion of the operation.
     * @throws InvalidInput if there are no humans present in the MainCockPitComp.
     */
    @Override
    public int kill(){
        if (numHumans == 0){
            throw new InvalidInput("MainCockPit is empty!");
        }
            numHumans--;
            if (numHumans == 0){
                playerBoard.getConnectedHousingUnits().remove(this);
            }
            if(numHumans == 0 &&!getNearbyHousingUnits().isEmpty()){
                for (HousingUnit unit : getNearbyHousingUnits()){
                    unit.notifyUnit(false, this);
                }
            }
            tile.sendUpdates(null, numHumans, false, false, 0);

            return 2;


    }



    /**
     * Adds crew members to the main cockpit, ensuring that the specified number of humans
     * and alien types adhere to the prescribed constraints. Notifies nearby housing units
     * and sends updates regarding the crew addition.
     *
     * @param humans the number of human crew members to add
     * @param purple specifies if a purple alien is being added
     * @param brown specifies if a brown alien is being added
     * @throws InvalidInput if attempts are made to add aliens (purple or brown) or if
     *                      the total number of humans exceeds the allowed limit of 2
     */
    @Override
    public void addCrew(int humans, boolean purple, boolean brown){
        if (purple || brown){
            throw new InvalidInput("This is the mainCockpit you can't add aliens");
        }

        if (numHumans + humans > 2){
            throw new InvalidInput("Human amount is greater than 2");
        }

        numHumans += humans;
        if(!getNearbyHousingUnits().isEmpty()){
            for (HousingUnit unit : getNearbyHousingUnits()){
                unit.notifyUnit(true, this);
            }
        }

        tile.sendUpdates(null, numHumans, false, false, 0);

    }

    /**
     * Sets the connection state of the component.
     *
     * @param connected the new connection state to set; {@code true} if the component is connected, {@code false} otherwise
     */
    public void setConnected(boolean connected){
        this.connected = connected;
    }


    /**
     * Creates and returns a deep copy of this MainCockpitComp instance while associating it with a
     * specified player board. Copies the state of relevant properties and adjusts references to
     * maintain consistency in the cloned context.
     *
     * @param clonedPlayerBoard the target PlayerBoard to which the cloned MainCockpitComp will belong
     * @return a new instance of MainCockpitComp containing the cloned state of the original object
     */
    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        MainCockpitComp clone = new MainCockpitComp();
        clone.setConnected(connected);
        clone.setNumHumans(numHumans);
        clone.setPlayerBoard(clonedPlayerBoard);
        clone.setX(6);
        clone.setY(6);
        if (connected && this.playerBoard.getConnectedHousingUnits().contains(this)){
            clonedPlayerBoard.getConnectedHousingUnits().add(clone);
        }

        return clone;
    }

    /**
     * Sets the player board for the current component.
     *
     * @param playerBoard the PlayerBoard to be assigned to this component
     */
    public void setPlayerBoard(PlayerBoard playerBoard){
        this.playerBoard = playerBoard;
    }





}


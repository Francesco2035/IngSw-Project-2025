package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Represents a Shield Generator component in the game.
 *
 * The ShieldGenerator provides directional protection based on its current configuration.
 * It can be rotated to change the directions it protects, validated for placement on a player board,
 * and cloned to create a copy with the same properties for a different player board.
 * The component interacts with the player's board and updates the game's state accordingly.
 */
public class ShieldGenerator extends Component{


    /**
     * Represents the directional protection configuration of the ShieldGenerator.
     *
     * The `protectedDirections` variable defines which directions are currently protected
     * by the ShieldGenerator. This is represented as an ArrayList of integers, where each
     * position corresponds to a cardinal direction (e.g., top, right, bottom, left),
     * and a value of 1 indicates that the direction is protected, while 0 means it is not.
     *
     * The default configuration protects the right and bottom directions, which is represented as [0, 1, 1, 0].
     * This list may be modified or rotated based on operations performed by the ShieldGenerator,
     * such as changing orientation during gameplay.
     */
    private ArrayList<Integer> protectedDirections = new ArrayList<>(Arrays.asList(0, 1, 1, 0));

    /**
     * Constructs a new instance of the ShieldGenerator.
     *
     * The ShieldGenerator is a game component that provides protection in specific
     * directions, determined by its configuration. It is used to enhance the defensive
     * capabilities of a player's ship. The generated shields are directional, meaning
     * they can be adjusted to cover different sides of the ship by rotating the component.
     */
    public ShieldGenerator() {}

    /**
     * Retrieves the list of protected directions for the ShieldGenerator.
     *
     * The returned list represents the directional protection configuration of the ShieldGenerator.
     * Each direction is represented as an integer, where 1 denotes a protected direction,
     * and 0 denotes an unprotected direction. The list typically contains four elements
     * corresponding to the four cardinal directions.
     *
     * @return an ArrayList of integers representing the protected directions of the ShieldGenerator.
     */
    public ArrayList<Integer> getProtectedDirections() {
        return protectedDirections;
    }


    /**
     * Rotates the protected directions of the shield generator either clockwise or counterclockwise.
     *
     * @param direction true for clockwise rotation, false for counterclockwise rotation
     */
    @Override
    public void rotate(Boolean direction) {
        if (direction){
            Collections.rotate(getProtectedDirections(), 1);}
        else {Collections.rotate(getProtectedDirections(), -1);}
    }


    /**
     * Validates the placement of a ShieldGenerator component on the player's board
     * while updating the player's shield configuration.
     *
     * @param pb the player's board where the component is being checked for validity and placement
     * @param x the x-coordinate of the placement on the player board
     * @param y the y-coordinate of the placement on the player board
     * @return true if the placement is valid and updates have been successfully performed
     */
    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        int[] pb_shield = pb.getShield();
        for (int i = 0; i < pb_shield.length; i++){
            pb_shield[i] += protectedDirections.get(i);
        }

        return true;
    }


    /**
     * Inserts the ShieldGenerator into the specified position on the player's board.
     *
     * Adds this ShieldGenerator to the player's board shield generator list and updates the game state.
     *
     * @param playerBoard the player's board where the ShieldGenerator is to be inserted
     * @param x the x-coordinate of the position where the ShieldGenerator is placed
     * @param y the y-coordinate of the position where the ShieldGenerator is placed
     */
    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
        playerBoard.getShieldGenerators().add(this);
        tile.sendUpdates(null,0, false, false, 0);

    }

    /**
     * Removes the current ShieldGenerator instance from the specified player board
     * and updates the game state to reflect this removal.
     *
     * @param playerBoard the player board from which this ShieldGenerator will be removed
     */
    @Override
    public void remove(PlayerBoard playerBoard) {
        playerBoard.getShieldGenerators().remove(this);
        tile.sendUpdates();

    }


    /**
     * Creates and returns a copy of this ShieldGenerator with the same properties,
     * associated with a specified PlayerBoard.
     *
     * @param clonedPlayerBoard the PlayerBoard instance for which the cloned ShieldGenerator is associated.
     * @return a new ShieldGenerator instance that is a copy of this component with the same protected directions.
     */
    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        ShieldGenerator clone = new ShieldGenerator();
        clone.protectedDirections = new ArrayList<>(this.protectedDirections);
        return clone;
    }



}



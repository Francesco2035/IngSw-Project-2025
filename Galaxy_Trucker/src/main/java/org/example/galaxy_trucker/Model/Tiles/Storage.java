package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents an abstract storage component that can hold goods in a structured game model.
 * This class is intended to be extended by specialized storage components and provides
 * basic functionality such as managing goods, interacting with the player board, and
 * implementing visitor-based actions.
 */
public abstract class Storage extends Component{


    /**
     * A collection that stores instances of {@link Goods} representing various types of goods
     * managed within the storage component. This list serves as the primary repository for
     * the goods held by a storage entity.
     *
     * The goods in this collection can be manipulated using available methods,
     * allowing for addition, removal, and retrieval based on specific requirements.
     *
     * @see #addGood(Goods)
     * @see #removeGood(int)
     * @see #getGoods()
     */
    ArrayList<Goods> goods;

    /**
     * Rotates the component of the storage in the specified direction.
     * The rotation behavior is determined by the implementation of the component.
     *
     * @param direction the direction to rotate; true for clockwise and false for counterclockwise
     */
    @Override
    public void rotate(Boolean direction) {}

    /**
     * Accepts a visitor that performs an action based on the current state of the player
     * and the specific type of this component. This method allows the visitor to execute
     * the appropriate logic for this component type using the visitor pattern.
     *
     * @param visitor the ComponentAction visitor implementing the logic to be executed
     * @param State the current state of the player, used to validate the action or determine its effects
     */
    @Override
    public void accept(ComponentAction visitor, PlayerState State) {
        visitor.visit(this, State);
    }

    /**
     * Determines if the specified coordinates (x, y) on the given PlayerBoard
     * pass the validity check for a specific operation or condition.
     *
     * @param pb the PlayerBoard on which the validity check is performed
     * @param x the x-coordinate on the PlayerBoard to be checked
     * @param y the y-coordinate on the PlayerBoard to be checked
     * @return true if the specified coordinates are valid according to the check;
     *         otherwise, false
     */
    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    /**
     * Inserts an element into the storage at the specified coordinates on the player's board.
     *
     * @param playerBoard the player's board where the element will be inserted
     * @param x the x-coordinate where the element will be inserted
     * @param y the y-coordinate where the element will be inserted
     */
    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {}

    /**
     * Removes a specific interaction or data associated with the given player board
     * from this storage component.
     *
     * @param playerBoard the player's board from which the removal operation is performed
     */
    @Override
    public void remove(PlayerBoard playerBoard) {}

    /**
     * Retrieves the value of the goods at the specified index in the storage.
     *
     * @param i the index of the goods item in the storage list
     * @return the integer value associated with the goods at the specified index
     */
    public int getValue(int i){
        return goods.get(i).getValue();
    }

    /**
     * Removes a good from the list of goods stored in the storage and returns it.
     *
     * @param i the index of the good to be removed
     * @return the good that was removed, or null if no good exists at the specified index
     */
    public Goods removeGood(int i){
        return null;
    }

    /**
     * Adds a good to the storage component.
     *
     * @param good the good to be added. Must be a valid instance of a class
     *             implementing the {@code Goods} interface.
     */
    public void addGood(Goods good){}

    /**
     * Retrieves the list of goods stored in the storage.
     *
     * @return an ArrayList containing Goods objects currently held in the storage
     */
    public ArrayList<Goods> getGoods() {
        return goods;
    }

    /**
     * Handles the communication of the current state of the storage component.
     * This method is a placeholder intended to be implemented by subclasses
     * or used for operations involving the state of the storage or its associated goods.
     *
     * Intended for scenarios where the state of the `Storage` needs to be transmitted,
     * logged, or otherwise used in interactions with other components, such as a
     * player board or external systems.
     */
    public void sendState(){}

//
//    private void orderGoods() {
//        this.goods.sort(Comparator.comparingInt(Goods::getValue));
//    }

}

package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.RemoveTileEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;

/**
 * Represents a special type of storage compartment designed to store goods.
 * This class extends the `Storage` class and provides additional functionality
 * for managing goods within a player's storage system on their player board.
 *
 * The `SpecialStorageCompartment` class stores a collection of `Goods` objects
 * and interacts with the player's board to maintain the total value of goods
 * and update their positions on the grid.
 */
public class SpecialStorageCompartment extends Storage{

    /**
     * Represents a container for storing goods within a special storage compartment.
     * Goods are managed as a collection of `Goods` objects, allowing operations such
     * as adding, removing, and retrieving goods.
     *
     * This field holds the goods associated with this storage compartment
     * and is intended to leverage various goods-related operations provided
     * within the class.
     */
    private ArrayList<Goods> goods;
    /**
     * Represents the player's board that stores the player's resources, goods,
     * and other details related to their gameplay progression.
     * It is used for managing the player's interactions with the special storage compartment
     * and facilitates adding, removing, and manipulating items or gameplay elements.
     */
    PlayerBoard playerBoard;



    /**
     * Retrieves the list of goods stored in this special storage compartment.
     * The goods are represented as a collection of objects implementing the Goods interface.
     *
     * @return an ArrayList of Goods currently stored in the compartment
     */
    @Override
    public ArrayList<Goods> getGoods() {
        return goods;
    }
    /**
     * Retrieves*/
    @Override
    public int getValue(int i){
        return goods.get(i).getValue();
    }

    /**
     * Sets the collection of goods for this storage compartment.
     *
     * @param goods the list of Goods to set, representing the items to be stored
     */
    public void setGoods(ArrayList<Goods> goods) {
        this.goods = goods;
    }
//    private void orderGoods() {
//          this.goods.sort(Comparator.comparingInt(Goods::getValue));
//    }


    /**
     * Removes a good from the specified position in the list of goods.
     * Updates the total value of the player's board and adjusts the stored goods map accordingly.
     * Sends updates to reflect the current state of the tile.
     *
     * @param position the index of the good to remove from the list
     * @return the removed {@code Goods} object if the position is valid, or {@code null} if the position is out of bounds
     */
    @Override
    public Goods removeGood(int position){
        if (position >= goods.size() || position < 0){
            return null;
            //throw new InvalidInput("Cannot remove a good because it is out of bounds");
        }
        Goods good = goods.remove(position);
        playerBoard.setTotalValue(-good.getValue());
        playerBoard.getStoredGoods().get(good.getValue()).remove(new IntegerPair(tile.x,tile.y));
        if (playerBoard.getStoredGoods().get(good.getValue()).isEmpty()){
            playerBoard.getStoredGoods().remove(good.getValue());
        }
        tile.sendUpdates(goods,0, false, false, 0);
        return good;

    }


    /**
     * Adds a new good to the storage compartment. If the compartment is full,
     * an {@code InvalidInput} exception is thrown. The total value of goods
     * in the player's board is updated accordingly. The good's location
     * is stored in the player board's storage tracking system, and updates
     * are sent to the corresponding tile.
     *
     * @param good the good to be added to the storage compartment; must not be null
     * @throws InvalidInput if the storage compartment is already full
     */
    @Override
    public void addGood(Goods good) {
        if(good == null){
            return;
        }
        if (goods.size() == type){
            throw new InvalidInput("SpecialStorageCompartment is full!");
        }
        goods.add(good);
        playerBoard.setTotalValue(good.getValue());
        if (playerBoard.getStoredGoods().containsKey(good.getValue())){
            System.out.println("Cargo add "+good.getValue()+ " in "+ tile.x+ " "+tile.y);
            playerBoard.getStoredGoods().get(good.getValue()).add(new IntegerPair(tile.x, tile.y));
        }
        else{
            ArrayList<IntegerPair> toAdd = new ArrayList<>();
            toAdd.add(new IntegerPair(tile.x, tile.y));
            System.out.println("Cargo add "+good.getValue()+ " in "+ tile.x+ " "+tile.y);
            playerBoard.getStoredGoods().put(good.getValue(), toAdd);

        }
        tile.sendUpdates(goods,0, false, false, 0);

    }


    /**
     * Inserts this SpecialStorageCompartment into the specified PlayerBoard at the given coordinates.
     * Updates the PlayerBoard's storage and maintains references to goods stored within the compartment.
     *
     * @param playerBoard The player's board where this SpecialStorageCompartment will be placed.
     * @param x The x-coordinate on the player's board where this storage compartment will be inserted.
     * @param y The y-coordinate on the player's board where this storage compartment will be inserted.
     */
    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
        this.playerBoard = playerBoard;
        playerBoard.getStorages().add(this);
        if (goods == null) {
            goods = new ArrayList<>();
        }
        else{
            ArrayList<IntegerPair> toAdd = new ArrayList<>();
            for (Goods good : goods){
                playerBoard.setTotalValue(good.getValue());
                if (playerBoard.getStoredGoods().containsKey(good.getValue())){
                    playerBoard.getStoredGoods().get(good.getValue()).add(new IntegerPair(tile.x, tile.y));
                }
                else{
                    toAdd.add(new IntegerPair(tile.x, tile.y));
                    playerBoard.getStoredGoods().put(good.getValue(), toAdd);
                    toAdd = new ArrayList<>();
                }
            }
        }
        tile.sendUpdates(goods,0, false, false, 0);

    }


    /**
     * Removes this storage compartment and its associated goods from the specified player's board.
     * Updates the player's total value and stored goods accordingly and notifies other components of the removal.
     *
     * @param playerBoard the player's board from which this storage compartment and its goods are to be removed
     */
    @Override
    public void remove(PlayerBoard playerBoard) {
        playerBoard.getStorages().remove(this);
        for (Goods good : goods){
            playerBoard.setTotalValue(-good.getValue());
            playerBoard.getStoredGoods().get(good.getValue()).remove(new IntegerPair(tile.x, tile.y));
            if (playerBoard.getStoredGoods().get(good.getValue()).isEmpty()){
                playerBoard.getStoredGoods().remove(good.getValue());
            }
        }
        tile.sendUpdates(new RemoveTileEvent());

    }

    /**
     * Creates and returns a deep copy of the current SpecialStorageCompartment
     * instance associated with the given player board. The cloned object is
     * independent of the original instance but maintains its state, including
     * its collection of goods and type.
     *
     * @param clonedPlayerBoard the player's board associated with the cloned component
     * @return a deep copy of the current SpecialStorageCompartment instance
     */
    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        SpecialStorageCompartment clone = new SpecialStorageCompartment();
        clone.goods = new ArrayList<>(goods);
        clone.type = this.type;
        for (Goods g : clone.getGoods()) {
            System.out.println("CLONED SPC "+g);
        }
        return clone;
    }

    /**
     * Sends the current state of the storage compartment using the associated tile's sendUpdates method.
     *
     * This method triggers an update of the tile and its associated properties,
     * such as the list of goods contained in this storage compartment. It calls
     * the tile's sendUpdates method with the following parameters:
     * - The list of goods in the storage compartment.
     * - Zero for the number of humans, assuming no humans are directly associated with this*/
    @Override
    public void sendState(){
        tile.sendUpdates(goods,0, false, false, 0);
    }

}

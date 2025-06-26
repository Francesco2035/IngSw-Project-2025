package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;

/**
 * Represents a specialized storage compartment for managing goods within a player's board.
 * The StorageCompartment class extends the abstract Storage class and implements
 * additional functionality specific to the compartment's behavior and constraints.
 * This includes adding and removing goods, tracking the total value of goods, and
 * synchronizing the state of the goods with the associated player board.
 */
public class StorageCompartment extends Storage{

    /**
     * A collection of Goods stored within the StorageCompartment.
     * This list holds the goods currently present, enabling operations like
     * adding, removing, or retrieving goods as part of inventory management.
     * Each element in the list represents a specific type of {@link Goods}.
     */
    private ArrayList<Goods> goods;
    /**
     * Represents the reference to the PlayerBoard associated with the storage compartment.
     * Used for managing interactions between the storage compartment and the player's board.
     */
    private PlayerBoard playerBoard;


//    private void orderGoods() {
//        this.goods.sort(Comparator.comparingInt(Goods::getValue));
//    }

    /**
     * Removes a good from the storage compartment at the specified index.
     * If the provided index is invalid (out of bounds), the method returns null.
     * Upon removal, the method updates the player's board and sends updates
     * to the tile to reflect the change.
     *
     * @param i the index of the good to be removed from the storage compartment
     * @return the removed {@code Goods} object, or {@code null} if the index is out of bounds
     */
    @Override
    public Goods removeGood(int i){
        if (i >= goods.size() || i<0){
            return null;
            //throw new IndexOutOfBoundsException("Cannot remove a good because it is out of bounds");
        }
        Goods good = goods.remove(i);
        tile.sendUpdates(goods,0, false, false, 0);
        playerBoard.setTotalValue(-good.getValue());
        playerBoard.getStoredGoods().get(good.getValue()).remove(new IntegerPair(tile.x,tile.y));
        if (playerBoard.getStoredGoods().get(good.getValue()).isEmpty()){
            playerBoard.getStoredGoods().remove(good.getValue());
        }
        return good;

    }

    /**
     * Adds a good to the storage compartment, updating associated game state and triggering relevant actions.
     * Validation is performed on input goods and constraints of the compartment.
     *
     * @param good the good to be added; must not be null and should satisfy specific criteria for addition:
     *             - The storage compartment must have enough capacity for the new good.
     *             - The value of the good must not equal 4, as special goods are not allowed.
     *             If these conditions are violated, an InvalidInput exception is thrown.
     */
    @Override
    public void addGood(Goods good) {
        if(good == null){
            return;
        }
        if (goods.size() == type){
            throw new InvalidInput("StorageCompartment is full!");
        }
        if (good.getValue() == 4){
            throw new InvalidInput("StorageCompartment cannot contain special Goods");
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
     * Inserts the storage compartment into the specified player's board at the given coordinates.
     * Updates the board's storage and goods information based on the current state of the storage compartment.
     *
     * @param playerBoard the player's board where the storage compartment is to be added
     * @param x the x-coordinate on the player's board where the compartment will be placed
     * @param y the y-coordinate on the player's board where the compartment will be placed
     */
    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
        this.playerBoard = playerBoard;
        playerBoard.getStorages().add(this);
        if (goods == null){
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
     * Removes this storage compartment from the specified player's board and updates the game state.
     *
     * @param playerBoard the player's board from which the storage compartment will be removed
     */
    @Override
    public void remove(PlayerBoard playerBoard) {
        playerBoard.getStorages().remove(this);
        tile.sendUpdates();
        for (Goods good : goods){
            playerBoard.setTotalValue(-good.getValue());
            playerBoard.getStoredGoods().get(good.getValue()).remove(new IntegerPair(tile.x, tile.y));
            if (playerBoard.getStoredGoods().get(good.getValue()).isEmpty()){
                playerBoard.getStoredGoods().remove(good.getValue());
            }
        }

    }

    /**
     * Creates and returns a deep copy of the current StorageCompartment associated with
     * the given player board. The cloned component maintains its state while being
     * independent of the original instance.
     *
     * @param clonedPlayerBoard the player's board associated with the cloned component
     * @return a deep copy of the current StorageCompartment
     */
    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        StorageCompartment clone = new StorageCompartment();
        clone.goods = new ArrayList<>(goods);
        clone.type = this.type;

        return clone;
    }


    /**
     * Retrieves the list of goods stored in the storage compartment.
     *
     * @return an ArrayList containing all the goods in the storage compartment
     */
    @Override
    public ArrayList<Goods> getGoods() {
        return goods;
    }

    /**
     * Retrieves the value of a specific goods item at the given index.
     *
     * @param i the index of the goods item in the internal list
     * @return the integer value of the goods item at the specified index
     */
    @Override
    public int getValue(int i){
        return goods.get(i).getValue();
    }

    /**
     * Sends the current state of the storage compartment to the associated player board.
     *
     * This method invokes the `sendUpdates` method of the associated tile with specific parameters:
     * - The list of goods contained in the compartment.
     * - A human count of 0.
     * - No purple aliens.
     * - No brown aliens.
     * - A battery count of 0.
     *
     * It facilitates synchronization of the storage compartment's state with the player board
     * by creating a `TileEvent` with the relevant parameters. This is useful for keeping
     * the player board updated on the state of the tile's content and configuration.
     */
    @Override
    public void sendState(){
        tile.sendUpdates(goods,0, false, false, 0);
    }

}

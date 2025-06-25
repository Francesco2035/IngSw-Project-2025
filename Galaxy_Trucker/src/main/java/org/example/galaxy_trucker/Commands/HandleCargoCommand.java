package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.AddGoodAction;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;
import org.example.galaxy_trucker.Model.Tiles.Storage;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * The HandleCargoCommand class represents a command in the game responsible
 * for handling various cargo-related actions. It is executed by a player
 * and performs actions on the player's board based on the specific command
 * type defined by the 'title' attribute.
 *
 * This class extends the Command class and implements Serializable interface
 * to allow for object serialization. It supports commands such as:
 * - Retrieving goods from rewards and placing them on a specific location.
 * - Clearing goods and rewards at the end of a cargo operation.
 * - Switching goods between two storage locations.
 * - Discarding goods from specified locations.
 *
 * The command ensures operations are only performed in a valid player state
 * and handles exceptions by restoring the player board to its previous state
 * and notifying affected storage areas.
 */
public class HandleCargoCommand extends Command implements Serializable {


    @JsonProperty("position")
    int position;
    @JsonProperty("coordinate")
    IntegerPair coordinate;
    @JsonProperty("position2")
    int position2;
    @JsonProperty("coordinate2")
    IntegerPair coordinate2;

    @JsonProperty("commandType")
    private final String commandType = "HandleCargoCommand";


    public HandleCargoCommand(){}


    /**
     * Constructs a HandleCargoCommand object with specified parameters.
     *
     * @param position    the position of the cargo in the first set
     * @param coordinate  the coordinates of the cargo in the first set as an IntegerPair
     * @param position2   the position of the cargo in the second set
     * @param coordinate2 the coordinates of the cargo in the second set as an IntegerPair
     * @param gameId      the unique identifier of the game
     * @param playerId    the unique identifier of the player
     * @param lv          the level associated with the command
     * @param title       the title or name of the command
     * @param token       the token used for authentication or validation
     */
    public HandleCargoCommand(int position, IntegerPair coordinate, int position2,IntegerPair coordinate2, String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.title = title;
        this.position = position;
        this.coordinate = coordinate;
        this.coordinate2 = coordinate2;
        this.position2 = position2;

    }



    /**
     * Executes the cargo handling command for the given player, based on the command type specified.
     * Commands include actions like retrieving goods from rewards, finishing cargo operations, switching goods
     * between storages, or discarding goods. In case of an error, it restores the previous state and propagates an exception.
     *
     * @param player the Player object on which the command will be executed. The player's current state, rewards, and storages are manipulated
     *               as per the logic of the specific command type.
     * @throws InvalidInput if any operation performed during execution of the command results in an invalid state or input being detected.
     */
    @Override
    public void execute(Player player) {

        PlayerBoard clone = player.getmyPlayerBoard().clone();
        PlayerBoard playerBoard = player.getmyPlayerBoard();
        Goods temp;
        ArrayList<Goods> rewards = null;
        if (playerBoard.getRewards() != null){
            rewards = new ArrayList<>(playerBoard.getRewards());
        }
        try{
            switch (title) {

                /// quando faccio la get from rewards faccio la remove prima di controllare che effettivamente faccia il command quindi quando ho errore consumo il good
                case "GetFromRewards": {
                    playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent(),
                            new AddGoodAction(
                                    (playerBoard.getFromRewards(position)), playerBoard, coordinate.getFirst(), coordinate.getSecond()),
                            player.getPlayerState()
                    );
                    break;
                }
                case "FinishCargo": {
                    playerBoard.getRewards().clear();
                    playerBoard.getBufferGoods().clear();
                    // non serve base state devi fare n'altra robaaaaa
                    //player.setState(new BaseState());

                    player.setState(new Waiting());
                    player.getCurrentCard().keepGoing();

                    break;
                }
                case "Switch":{
                    if (coordinate.getFirst() == coordinate2.getFirst() && coordinate.getSecond() == coordinate2.getSecond()) {
                        throw new InvalidInput("You can't switch within the same storage");
                    }
                    GetGoodAction action = new GetGoodAction(position,playerBoard,coordinate.getFirst(),coordinate.getSecond());
                    playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent()
                            , action, player.getPlayerState());
                    Goods good1 = action.getGood();
                    GetGoodAction action2 = new GetGoodAction(position2,playerBoard,coordinate2.getFirst(),coordinate2.getSecond());
                    playerBoard.performAction(playerBoard.getTile(coordinate2.getFirst(), coordinate2.getSecond()).getComponent()
                            , action2, player.getPlayerState());
                    Goods good2 = action2.getGood();
                    AddGoodAction action3 = new AddGoodAction(good2,playerBoard,coordinate.getFirst(), coordinate.getSecond());
                    playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent()
                            , action3, player.getPlayerState());
                    AddGoodAction action4 = new AddGoodAction(good1,playerBoard,coordinate2.getFirst(), coordinate2.getSecond());
                    playerBoard.performAction(playerBoard.getTile(coordinate2.getFirst(), coordinate2.getSecond()).getComponent()
                            , action4, player.getPlayerState());
                    break;
                }
                case "Discard":{
                    GetGoodAction action = new GetGoodAction(position,playerBoard,coordinate.getFirst(),coordinate.getSecond());
                    playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent()
                            , action, player.getPlayerState());
                    break;
                }
            }
        }
        catch (Exception e){
            Storage storage1;
            Storage storage2;
            ArrayList<Storage> storages = clone.getStorages();
//            storage1 = storages.get(storages.indexOf(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent()));
//            storage2 = storages.get(storages.indexOf(playerBoard.getTile(coordinate2.getFirst(), coordinate2.getSecond()).getComponent()));
//            if (storage1 != null){
//                storage1.sendState();
//            }
//            if (storage2 != null){
//                storage2.sendState();
//            }
            for (Storage storage : storages){
                storage.sendState();
            }
            e.printStackTrace();
            playerBoard.setRewards(rewards);
            throw new InvalidInput(e.getMessage());
        }

    }

    /**
     * Determines whether the current command is allowed in the specified player state.
     *
     * @param playerState the state of the player to check permissions against
     * @return true if the command is allowed in the given player state, false otherwise
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

}

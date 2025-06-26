package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The DebugShip class represents a specialized command in the game to debug and modify the player's
 * ship setup. It extends the Command class and implements Serializable. The class is responsible for
 * setting up specific debug configurations for a player's ship by inserting predefined tiles and adjustments
 * for testing purposes.
 *
 * This command interacts with the player's PlayerBoard and uses specific tiles from a tile deck to construct
 * a test configuration. The execute method carries out the modifications based on the `number` parameter
 * which determines the specific debug use case. Tile rotations, placements, and adjustments are explicitly
 * programmed for debugging purposes.
 *
 * Fields:
 * - `commandType`: Specifies the type of command as "DebugShip".
 * - `number`: Represents the debug scenario or configuration to be executed.
 *
 * Constructors:
 * - DebugShip(String gameId, String playerId, int lv, String title, String token, int number): Initializes
 *   the command with the provided game ID, player ID, title, and debug configuration number.
 * - DebugShip(): Default empty constructor.
 *
 * Methods:
 * - execute(Player player): Overrides the execute method to perform the debugging setup on the player's
 *   ship. The method retrieves the player's PlayerBoard, validates the tiles, and sets up a predefined
 *   debug configuration based on the `number` provided.
 */
public class DebugShip extends Command implements Serializable {


    @JsonProperty("commandType")
    private final String commandType = "DebugShip";
    @JsonProperty("number")
    private int number;

    public DebugShip() {}


    /**
     * Constructs a DebugShip object with the specified parameters.
     *
     * @param gameId   the unique identifier of the game
     * @param playerId the unique identifier of the player
     * @param lv       the level associated with the DebugShip command
     * @param title    the title or name associated with the DebugShip command
     * @param token    the token used for authentication or validation
     * @param number   the integer value representing the number associated with the DebugShip command
     */
    public DebugShip(String gameId, String playerId, int lv, String title, String token, int number) {
        super(gameId, playerId, lv, title, token,-1);
        this.gameId = gameId;
        this.playerId = playerId;
        this.title = title;
        this.number = number;
    }


    /**
     * Executes the DebugShip command for the specified player. This method modifies the player's
     * game state by manipulating their player board, making tile placements based on predefined configurations.
     *
     * @param player the Player instance whose game state is to be modified through this command execution
     * @throws IOException if an input or output operation fails during execution
     */
    @Override
    public void execute(Player player) throws IOException {

        PlayerBoard debugShip = player.getmyPlayerBoard();
        GAGen gag = new GAGen();

        int[][]valid = debugShip.getValidPlayerBoard();


        int[][]validCopy = new int[valid.length][valid[0].length];
        for (int i = 0; i < valid.length; i++) {
            validCopy[i] = Arrays.copyOf(valid[i], valid[i].length);
        }

        for(int i=0; i<10; i++) {
            for(int j=0; j<10; j++) {
                if (valid[i][j]== 1 && (i!=6 && j!=6)) debugShip.removeTile(i, j);
            }
        }

        ArrayList<Tile> tiles = gag.getTilesDeck();



        switch (number){  /// fai id-1
            case 0:{
                Tile tile = tiles.get(37); ///mod housing uinit
                debugShip.insertTile(tile,6,5,false);

                tile = tiles.get(86);//engine
                debugShip.insertTile(tile,7,6,false);

                tile = tiles.get(60); //specialstorage
                debugShip.insertTile(tile,7,5,false);

                tile = tiles.get(121);//double drill
                debugShip.insertTile(tile,5,6,false);

                tile = tiles.get(137);//brownalien
                tile.RotateDx();
                tile.setRotation(90);
                debugShip.insertTile(tile,6,4,false);

                tile = tiles.get(149); //shield
                debugShip.insertTile(tile,6,7,false);

                tile = tiles.get(144);//shield
                tile.RotateSx();
                tile.setRotation(270);
                debugShip.insertTile(tile,5,7,false);

                tile = tiles.get(11); //triplepowercenter
                debugShip.insertTile(tile,7,7,false);

                tile = tiles.get(15);//  powercenter
                tile.RotateDx();
                tile.RotateDx();
                tile.setRotation(180);
                debugShip.insertTile(tile,5,5,false);

                tile = tiles.get(104); //plasmadrill
                debugShip.insertTile(tile,4,5,false);

                tile = tiles.get(118); //plasmadrill
                tile.RotateDx();
                tile.RotateDx();
                tile.setRotation(180);
                debugShip.insertTile(tile,8,7,false);

                tile = tiles.get(62);//special storage
                tile.RotateDx();
                tile.RotateDx();
                tile.setRotation(180);
                debugShip.insertTile(tile,7,4,false);

                tile = tiles.get(33); //modular housing
                tile.RotateDx();
                tile.RotateDx();
                tile.setRotation(180);
                debugShip.insertTile(tile,6,8,false);

                tile = tiles.get(142);//purplealien
                tile.RotateSx();
                tile.setRotation(270);
                debugShip.insertTile(tile,7,8,false);

                tile = tiles.get(78); // engine
                debugShip.insertTile(tile,8,4,false);

                tile = tiles.get(94); // doubleengine
                debugShip.insertTile(tile,8,5,false);

                tile = tiles.get(54); // sewerpies
                debugShip.insertTile(tile,8,3,false);

                tile = tiles.get(120); // plasmadrill
                tile.RotateSx();
                tile.setRotation(270);
                debugShip.insertTile(tile,7,3,false);

                tile = tiles.get(29); //triple storage
                debugShip.insertTile(tile,6,3,false);

                tile = tiles.get(125); // doubleplasmadrill
                debugShip.insertTile(tile,5,4,false);


                tile = tiles.get(45); //housing
                debugShip.insertTile(tile,5,8,false);

                tile = tiles.get(17); /// storage
                tile.RotateSx();
                tile.setRotation(270);
                debugShip.insertTile(tile,8,8,false);

                tile = tiles.get(147); //shield
                tile.RotateDx();
                tile.setRotation(90);
                debugShip.insertTile(tile,8,9,false);

                tile = tiles.get(97);
                debugShip.insertTile(tile,4,7,false);

                tile = tiles.get(146);//shield
                debugShip.insertTile(tile,6,9,false);

                break;
            }
            case 1:{
                Tile tile = tiles.get(91);//engine
                debugShip.insertTile(tile,7,6,false);

                tile = tiles.get(3); // double battery
                debugShip.insertTile(tile,6,7,false);


                tile = tiles.get(48);//housing
                debugShip.insertTile(tile,7,7,false);

                tile = tiles.get(54); //sewer
                debugShip.insertTile(tile,6,5,false);

                tile = tiles.get(5); //Powercenter
                debugShip.insertTile(tile,7,5,false);

                tile = tiles.get(40); // housingunits
                debugShip.insertTile(tile,7,4,false);

                tile = tiles.get(133);
                debugShip.insertTile(tile,8,4,false);


                tile = tiles.get(145); //shield
                tile.RotateDx();
                tile.setRotation(90);
                debugShip.insertTile(tile,8,5,false);


                tile = tiles.get(10); // battery comp
                tile.RotateDx();
                tile.setRotation(90);
                debugShip.insertTile(tile,8,3,false);

                tile = tiles.get(75);//engine
                debugShip.insertTile(tile,8,7,false);

                tile = tiles.get(63); //special storage
                tile.RotateSx();
                tile.setRotation(270);
                debugShip.insertTile(tile,5,6,false);

                tile = tiles.get(19); //storage
                tile.RotateDx();
                tile.setRotation(90);
                debugShip.insertTile(tile,5,7,false);

                tile = tiles.get(131); // double plasma
                debugShip.insertTile(tile,4,7,false);

                tile = tiles.get(38); //housing
                tile.RotateDx();
                tile.setRotation(90);
                debugShip.insertTile(tile,5,5,false);

                tile = tiles.get(119); // plasmadrill
                debugShip.insertTile(tile,4,5,false);

                tile = tiles.get(47); //house
                tile.RotateSx();
                tile.setRotation(270);
                debugShip.insertTile(tile,7,8,false);

                tile = tiles.get(14); // triple power
                tile.RotateSx();
                tile.setRotation(270);
                debugShip.insertTile(tile,8,8,false);

                tile = tiles.get(87); //double hot water
                debugShip.insertTile(tile,8,9,false);

                tile = tiles.get(126);//double plasma
                tile.RotateDx();
                tile.setRotation(90);
                debugShip.insertTile(tile,7,9,false);

                tile = tiles.get(18); //storage
                tile.RotateDx();
                tile.setRotation(90);
                debugShip.insertTile(tile,6,9,false);

                tile = tiles.get(49);//sewerpipes
                debugShip.insertTile(tile,6,8,false);

                tile = tiles.get(150); //shield
                debugShip.insertTile(tile,5,8,false);

                tile = tiles.get(106); //plasmadrill
                debugShip.insertTile(tile,5,4,false);

                tile = tiles.get(0); //powercenter
                debugShip.insertTile(tile,6,4,false);

                tile = tiles.get(127); //double plasma
                tile.RotateSx();
                tile.setRotation(270);
                debugShip.insertTile(tile,6,3,false);

                tile = tiles.get(36);
                tile.RotateDx();
                tile.setRotation(90);
                debugShip.insertTile(tile,7,3,false);


                break;
            }

        }

    }

    /**
     * Sets the value of the number property for the instance.
     *
     * @param number the integer value to assign to the number property
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Determines if the current command is allowed in the given player's state.
     *
     * @param playerState the PlayerState instance to check whether this command is allowed
     * @return true if the command is allowed in the specified PlayerState; false otherwise
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

}

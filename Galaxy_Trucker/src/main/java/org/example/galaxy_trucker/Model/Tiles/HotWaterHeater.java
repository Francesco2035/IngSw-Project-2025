package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.RemoveTileEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

/**
 * The HotWaterHeater class represents a special type of component that
 * can be added to or removed from a player's board in a game. The component
 * has specific functionalities such as rotating in different directions
 * and interacting with the player's board and state.
 * It also controls its validity and can perform actions when visited.
 */
public class HotWaterHeater extends Component{


    /**
     * Represents the rotational direction state of the engine in the HotWaterHeater component.
     * EngineDirection is an integer value that is used to control and manage the orientation of the engine.
     *
     * This variable typically cycles through values representing discrete rotational states (e.g., 0 to 3),
     * influenced by the rotation methods within the HotWaterHeater class. It is utilized in determining
     * the component's validity in its current orientation as well as in updates triggered by rotations.
     */
    private int EngineDirection = 3;

    /**
     * Default constructor for the HotWaterHeater class.
     * This creates an instance of the HotWaterHeater component, which can be added to a player's game board.
     * The HotWaterHeater has specific functionalities including rotation, interaction with the board and state,
     * and the ability to validate its placement or perform actions as required.
     */
    public HotWaterHeater() {}



    /**
     * Rotates the engine direction of the component. The direction is updated
     * according to the provided parameter, where true represents a clock-wise
     * rotation and false represents a counter-clockwise rotation.
     * The engine direction is maintained within 0 to 3 (inclusive), representing
     * the four cardinal directions.
     *
     * @param direction a Boolean indicating the rotation direction. A value of
     *                  true rotates the engine clock-wise, while a value of false
     *                  rotates the engine counter-clockwise.
     */
    @Override
    public void rotate(Boolean direction) {
        if (direction){
            EngineDirection += 1;
        }
        else {
            EngineDirection -= 1;
        }
        EngineDirection = EngineDirection % 4;
    }



    /**
     * Checks the validity of the hot water heater placement on the player board
     * based on its current engine direction.
     *
     * @param pb the player board where the component is being validated
     * @param x the x-coordinate on the player board
     * @param y the y-coordinate on the player board
     * @return true if the engine direction is equal to 3, false otherwise
     */
    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y){
        return EngineDirection == 3;
    }




    /**
     * Processes an interaction with a HotWaterHeater component by using the visitor pattern.
     * This method allows a {@link ComponentAction} to perform specific operations on the
     * HotWaterHeater while considering the current {@link PlayerState}.
     *
     * @param visitor the {@link ComponentAction} that will interact with the HotWaterHeater component
     * @param state the current {@link PlayerState}, which may influence or validate the action being performed
     */
    @Override
    public void accept(ComponentAction visitor, PlayerState state) {
        visitor.visit(this, state);
    }


    /**
     * Inserts a HotWaterHeater component onto the specified player's board at the given coordinates.
     * Depending on the type of the HotWaterHeater, it may affect the engine power of the player's board.
     * Additionally, the inserted component is registered within the player's hot water heaters list,
     * and a tile update is triggered.
     *
     * @param playerBoard the player's game board where the component will be inserted
     * @param x the x-coordinate of the insertion location on the game board
     * @param y the y-coordinate of the insertion location on the game board
     */
    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
        if (type == 1) {
            playerBoard.setEnginePower(1);
        }

        playerBoard.getHotWaterHeaters().add(this);
        tile.sendUpdates(null,0, false, false, 0);
    }




    /**
     * Removes the current HotWaterHeater component from the provided player's board.
     * If the component type is 1, it decreases the player's engine power by 1.
     * Removes this component from the player's collection of hot water heaters
     * and sends an update for tile removal.
     *
     * @param playerBoard the player's board from which the HotWaterHeater is removed
     */
    @Override
    public void remove(PlayerBoard playerBoard)  {
        if (type == 1) {
            playerBoard.setEnginePower(-1);
        }

        playerBoard.getHotWaterHeaters().remove(this);
        tile.sendUpdates(new RemoveTileEvent());
    }


    /**
     * Retrieves the engine power associated with the current component.
     *
     * @return an integer representing the engine power.
     *         Returns 2 if the component type is 2; otherwise, returns 0.
     */
    public int getEnginePower(){
        if (type == 2){
            return 2;
        }
        else {
            return 0;
        }
    }

    /**
     * Creates a deep copy of the current HotWaterHeater instance.
     *
     * @param clonedPlayerBoard the PlayerBoard to associate with the cloned component
     * @return the cloned HotWaterHeater instance
     */
    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        HotWaterHeater clone = new HotWaterHeater();
        clone.type = this.type;
        return clone;
    }


}


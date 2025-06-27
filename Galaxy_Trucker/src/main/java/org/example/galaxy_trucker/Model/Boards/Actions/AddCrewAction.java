package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;

/**
 * AddCrewAction represents an action where crew members consisting of humans and/or aliens
 * are added to a housing unit. This action interacts with the player's board and state
 * to ensure valid additions are made while respecting game rules.
 *
 * This action checks for various constraints, such as whether a particular type of alien
 * is already present on the player's board, or if the player attempts to add both types of aliens
 * simultaneously. Any invalid operation results in an exception.
 */
public class AddCrewAction extends ComponentAction {

    /**
     * Represents the number of human crew members involved in the AddCrewAction.
     * This variable is used to track how many humans are being added to a housing unit
     * on the player's board as part of this action. The number must comply with in-game rules.
     */
    private int humans;
    /**
     * Represents a flag indicating whether a purple alien is being added to the player's board.
     * This variable is used in the context of the AddCrewAction to determine if an attempt
     * is being made to add a purple alien to the housing unit.
     *
     * Constraints:
     * - A purple alien cannot be added if one already exists in the player's board.
     * - It is not allowed to add both a purple alien and a brown alien simultaneously.
     *
     * This boolean value is validated during the execution of associated actions to ensure
     * compliance with game rules.
     */
    private boolean purpleAlien;
    /**
     * Represents a boolean value indicating whether a brown alien is present
     * in the context of the AddCrewAction. This variable is set during the
     * initialization of the action and used to ensure compliance with game
     * rules related to the addition of crew members.
     *
     * Brown aliens are a type of crew member that can be added to a housing unit.
     * Its value is checked to prevent duplication or invalid combinations of crew
     * members, such as attempting to add both brown and purple aliens simultaneously.
     *
     * If set to true, the AddCrewAction will attempt to add a brown alien to
     * the player's housing unit during the execution of the action.
     */
    private boolean brownAlien;
    /**
     * Represents the player's board, which maintains the current state of the player,
     * including the number of humans and the presence of any aliens.
     *
     * This variable is used to enforce game rules, check for constraints,
     * and update the state of the player's board during actions such as adding crew members.
     */
    private PlayerBoard playerBoard;


    /**
     * Constructs an AddCrewAction which represents the action of adding crew members,
     * including humans and/or aliens, to a housing unit within the player's board.
     *
     * @param humans       the number of human crew members to be added
     * @param purpleAlien  a boolean indicating whether a purple alien is being added
     * @param brownAlien   a boolean indicating whether a brown alien is being added
     * @param playerBoard  the player's board on which this action will be executed
     */
    public AddCrewAction(int humans, boolean purpleAlien, boolean brownAlien, PlayerBoard playerBoard) {
        this.humans = humans;
        this.purpleAlien = purpleAlien;
        this.brownAlien = brownAlien;
        this.playerBoard = playerBoard;
    }

    /**
     * Executes the addition of crew members (humans and/or aliens) to a specified housing unit.
     * The method ensures that the operation complies with the current player's state and game rules.
     * It validates if the action is permitted in the player's state and prevents invalid configurations,
     * such as adding multiple alien types or duplicate alien types already existing in the player's board.
     *
     * @param housing The housing unit where the crew members will be added.
     * @param playerState The current state of the player, which governs whether the action is permitted.
     * @throws IllegalStateException If the player's state does not allow this action.
     * @throws InvalidInput If the operation violates game rules, such as attempting to add multiple alien types
     *                      or a duplicate alien type already present on the player's board.
     */
    @Override
    public void visit(HousingUnit housing, PlayerState playerState) {
        if (!playerState.allows(this)){
            throw new IllegalStateException("You are not allowed to perform this action in this state");
        }
        if ((playerBoard.getPurpleAlien() && purpleAlien) || (playerBoard.getBrownAlien() && brownAlien)) {
            throw new InvalidInput("An alien of the same type is already present in the board");
        }

        if (purpleAlien && brownAlien){
            throw new InvalidInput("Is possible to add only one type of alien");
        }

        try{
            housing.addCrew(humans, purpleAlien, brownAlien);
            System.out.println("proseguo oltre");
            playerBoard.setNumHumans(humans);
            if (purpleAlien){
                playerBoard.setPurpleAlien(true);
            }
            if (brownAlien){
                playerBoard.setBrownAlien(true);
            }

        }catch (Exception e){
            throw new InvalidInput(e.getMessage());
        }

    }

}

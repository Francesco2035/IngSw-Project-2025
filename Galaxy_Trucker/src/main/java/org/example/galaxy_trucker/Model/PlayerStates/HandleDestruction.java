package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.SelectChunkCommand;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;
import org.example.galaxy_trucker.View.ClientModel.States.HandleDestructionClient;

import java.util.ArrayList;

/**
 * The HandleDestruction class represents a specific state of the player during the game,
 * where the player has to handle the aftermath of destruction events such as meteor strikes.
 * It extends the PlayerState class, which provides the base logic for different player states.
 *
 * This state involves actions such as selecting chunks of destroyed housing units
 * and determining the next steps. It also ensures that the player's actions are
 * appropriate based on the current state and handles the transition to a client-side representation of this state.
 */
public class HandleDestruction extends PlayerState {

//    @Override
//    public Command PlayerAction(String json, Player player) {
//        JsonNode root = JsonHelper.parseJson(json);
//
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"Choose chunk".equals(title)) {
//            throw new InvalidInput("Unexpected title: " + title);
//        }
//
//        int x = JsonHelper.readInt(root, "x");
//        int y = JsonHelper.readInt(root, "y");
//        IntegerPair chunk = new IntegerPair(x, y);
//
//        return new SelectChunkCommand(player, chunk);
//    }

    /**
     * Creates and returns a default command for the player based on the current state of their game board.
     *
     * @param gameId the unique identifier of the game.
     * @param player the player for whom the default command is being generated.
     * @return a command instance that represents the player's next possible action.
     */
    @Override ///  che cristo succede se Viene distrutta ogni singola tile?
    public Command createDefaultCommand(String gameId, Player player) {
        int lv= player.getCommonBoard().getLevel();
        /// prendi i tronconi e scegli il primo non ricordo come se fa
        PlayerBoard board=player.getmyPlayerBoard();
        ArrayList<HousingUnit> houses=board.getHousingUnits();

        int i=0;
        IntegerPair coord= null;

        if (houses.size()==0){ //edge case difficle da testare ma tecnicmente possibile se meteoriti colpisca la tua ultima casa
            for( int x=0; x<10;x++){
                for( int y=0; y<10; y++){

                    if( board.getValidPlayerBoard()[x][y]==1){
                        coord= new IntegerPair(x,y);
                        return new SelectChunkCommand(coord,gameId, player.GetID(),lv,"SelectChunkCommand","placeholder");
                    }
                }
            }
            ///  questo vuol dire che non ha trovato una singola tile nell'intera board Sei cucinato
        }

            for (HousingUnit housingUnit : houses) {
                if(housingUnit.getNumHumans()!=0||housingUnit.isPurpleAlien()||housingUnit.isBrownAlien()){

                    coord = new IntegerPair(housingUnit.getX(),housingUnit.getY());
                    break;
                }
            }
            if (coord==null){
                //devo mettere una return particolare :()()()
                /// non hai case con umani hai perso brutta merda
            }


            //board.choosePlayerBoard(coord);

        return new SelectChunkCommand(coord,gameId, player.GetID(),lv,"SelectChunkCommand","placeholder");
    }

    /**
     * Determines whether the provided SelectChunkCommand is allowed in
     * the current state of the player.
     *
     * @param command the SelectChunkCommand to be evaluated
     * @return true if the command is allowed, false otherwise
     */
    @Override
    public boolean allows(SelectChunkCommand command){
        return true;
    }


    /**
     * Converts the current state of the "HandleDestruction" to a client-facing phase state.
     * This method creates a new {@link PhaseEvent} instance encapsulating a client-side
     * representation of the "HandleDestruction" state for use in the game.
     *
     * @return a {@link PhaseEvent} object containing a {@link HandleDestructionClient}
     *         instance that represents the player's state when handling destruction in the game phase.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new HandleDestructionClient());
    }

}

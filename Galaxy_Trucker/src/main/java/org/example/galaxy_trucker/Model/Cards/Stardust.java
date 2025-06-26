package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.ClientServer.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;

import java.util.ArrayList;

/**
 * The Stardust class represents a specific type of card in the game.
 * This card implements a unique effect when activated, affecting the state
 * and position of players on the game board. The Stardust card interacts
 * with player boards to calculate its effect based on the number of
 * exposed connectors each player has.
 */
public class Stardust extends Card {

    /**
     * Represents a collection of players who are affected by the card's effect in such
     * a way that they are classified as "losers". This list is populated during the
     * execution of the card effect, based on specific conditions defined in the game logic.
     * It is used to track players that are impacted negatively and may need additional
     * processing or notification at the end of the card's activation.
     */
    private  ArrayList<Player> losers;

    /**
     * Constructs a new Stardust card with a specified level and assigned game board.
     * The Stardust card initiates with functionalities inherited from the Card class.
     *
     * @param level the level of the Stardust card, which determines its impact or behavior in the game.
     * @param board the game board on which the Stardust card will operate, influencing players and their states.
     */
    public Stardust(int level, GameBoard board){
        super(level, 0 ,board);
    }
    /**
     * Executes the effect of the Stardust card on the game board. This method impacts the
     * state and positions of all players based on the number of exposed connectors
     * on their respective player boards.
     *
     * The following actions are performed by this method:
     * 1. Retrieves the game board and the list of players currently in the game.
     * 2. Sets each player's state to "Waiting".
     * 3. Pauses execution for a brief delay to simulate effect timing.
     * 4. Iterates through the list of players in reverse order, calculates the
     *    movement derived from the number of exposed connectors on each player board,
     *    and updates their position accordingly.
     * 5. If no movement is required for a player, logs an appropriate message.
     * 6. Marks the card's effect as completed.
     *
     * @throws InterruptedException if the thread sleep operation is interrupted.
     */
    @Override
    public void CardEffect () throws InterruptedException {


        losers = new ArrayList<>();
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(new Waiting());
        }
        Thread.sleep(5000);
        PlayerBoard CurrentPlanche;
        int Order=PlayerList.size()-1;
        int StarpowderMovement=0;
        while(Order>=0){
            CurrentPlanche =PlayerList.get(Order).getmyPlayerBoard();
            System.out.println("numof exposed connectors of: "+PlayerList.get(Order).GetID()+" is: "+PlayerList.get(Order).getmyPlayerBoard().getExposedConnectors());
            StarpowderMovement=-CurrentPlanche.getExposedConnectors();
            if(StarpowderMovement!=0) {
                Board.movePlayer(PlayerList.get(Order).GetID(), StarpowderMovement);
            }
            else {
                System.out.println("No exposed connectors found for "+PlayerList.get(Order).GetID());
            }
            Order--;
        }
        this.finishCard();


        return;
    }

    /**
     * Sends a log event of type "Stardust" to each player in the game.
     * This method iterates over all current players retrieved from the game board
     * and triggers a random effect for each player, accompanied by a log event
     * describing the "Stardust" event.
     *
     * The log event includes placeholder values (-1) for parameters that are not
     * directly utilized in logging but might be extended for further functionality.
     *
     * This method is overridden from the base class to provide specific logging
     * functionality for the Stardust card.
     */
    @Override
    public void sendTypeLog(){
        this.getBoard().getPlayers();
        for (Player p : this.getBoard().getPlayers()){
            sendRandomEffect(p.GetID(), new LogEvent("Stardust",-1,-1,-1,-1));
        }
    }

    /**
     * Completes the execution of the Stardust card's effects and marks the card as finished.
     *
     * This implementation performs the following actions:
     * 1. Outputs a message indicating the card is finished.
     * 2. Evaluates the current state of the game to identify players who meet losing conditions
     *    and handles their removal through the `checkLosers` method.
     * 3. Updates the status of the card by invoking `this.setFinished(true)` to mark the card's
     *    process as completed.
     */
    @Override
    public void finishCard() {
        System.out.println("card finished");
        checkLosers();

        this.setFinished(true);
    }

    /**
     * Default constructor for the Stardust class.
     * This constructor initializes a Stardust card without specifying any parameters.
     * It serves as the base setup for creating a Stardust object in its default state.
     * Further functionalities can be applied through other class methods or by
     * utilizing the parametrized constructors.
     *
     * The Stardust class extends the Card class, inheriting its basic properties and
     * functionalities, while adding unique effects and behaviors specific to the Stardust card.
     *
     * This constructor requires JSON support for serialization or configuration.
     */
    //json required
    public Stardust(){}

}



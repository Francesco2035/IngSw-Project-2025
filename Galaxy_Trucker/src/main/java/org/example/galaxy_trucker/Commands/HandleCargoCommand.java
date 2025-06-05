package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.AddGoodAction;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class HandleCargoCommand extends Command implements Serializable {

    String title;
    int position;
    IntegerPair coordinate;
    IntegerPair coordinate2;
    int position2;

//TODO: super buggata
    public HandleCargoCommand(int position, IntegerPair coordinate, int position2,IntegerPair coordinate2, String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.title = title;
        this.position = position;
        this.coordinate = coordinate;
        this.coordinate2 = coordinate2;
        this.position2 = position2;

    }



    @Override
    public void execute(Player player) {


        PlayerBoard playerBoard = player.getmyPlayerBoard();
        Goods temp;
        ArrayList<Goods> rewards = new ArrayList<>(playerBoard.getRewards());
        try{
            switch (title) {
                //TODO:getfromorewards non vuole nessun index per aggiungere, si può togliere nel chill
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
                    player.getCurrentCard().keepGoing();

                    break;
                }
                case "Switch":{
                    GetGoodAction action = new GetGoodAction(position,playerBoard,coordinate.getFirst(),coordinate.getSecond());
                    playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent()
                            , action, player.getPlayerState());
                    Goods good1 = action.getGood();
                    GetGoodAction action2 = new GetGoodAction(position2,playerBoard,coordinate2.getFirst(),coordinate2.getSecond());
                    playerBoard.performAction(playerBoard.getTile(coordinate2.getFirst(), coordinate2.getSecond()).getComponent()
                            , action2, player.getPlayerState());
                    Goods good2 = action2.getGood();
                    playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent()
                            , new AddGoodAction(good2,playerBoard,coordinate.getFirst(), coordinate.getSecond()), player.getPlayerState());
                    playerBoard.performAction(playerBoard.getTile(coordinate2.getFirst(), coordinate2.getSecond()).getComponent()
                            , new AddGoodAction(good1,playerBoard,coordinate2.getFirst(), coordinate2.getSecond()), player.getPlayerState());
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
            System.out.println("---------------mi è arrivata eccezione");
            playerBoard.setRewards(rewards);
            throw new InvalidInput(e.getMessage());
        }

    }

    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

}

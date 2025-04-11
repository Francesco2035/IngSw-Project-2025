package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Exceptions.WrongNumofEnergyExeption;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;
import org.example.galaxy_trucker.Model.PlayerStates.ConsumingEnergy;
import org.example.galaxy_trucker.Model.PlayerStates.GiveSpeed;

import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.Waiting;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;

public class OpenSpace extends Card{
    private Player currentPlayer;
    private int order;
    private int currentmovement;
    private int energyUsage;

    public OpenSpace(int level, GameBoard board){

        super(level, 0 ,board);
        this.order = 0;
        this.currentPlayer = null;
        this.energyUsage = 0;
        this.currentmovement = 0;
    }

    @Override
    public void CardEffect(){

        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(new Waiting());
        }
        this.updateSates();
    }
    @Override
    public void updateSates(){
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        currentmovement=0;
        if(this.order<PlayerList.size()){
            currentPlayer = PlayerList.get(this.order);
            PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();

                this.currentPlayer.setState(new GiveSpeed());
                //this.currentPlayer.setInputHandler(new GiveSpeed(this));

            this.order++;
        }
        else{
            this.finishCard();
        }
    }

    @Override
    public void finishCard() {
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(int i=0; i<PlayerList.size(); i++){
            PlayerList.get(i).setState(new BaseState());
        }
    }

    @Override
    public void checkMovement(int enginePower, int numofDouble) {
            this.currentmovement=enginePower;
            this.energyUsage=numofDouble;


      getBoard().movePlayer(currentPlayer.GetID(),enginePower);
        this.currentPlayer.setState(new Waiting());
        this.updateSates();
    }


    @Override
    public void consumeEnergy(ArrayList<IntegerPair> coordinates) {
        if(coordinates.size()!=this.energyUsage){
            throw new WrongNumofEnergyExeption("wrong number of enrgy cells");
            ///  devo fare si che in caso di errore torni alla give attack
        }
        PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();
        Tile[][] tiles = CurrentPlanche.getPlayerBoard();
        /// opero sulla copia
        for(IntegerPair i:coordinates){
            CurrentPlanche.performAction(tiles[i.getFirst()][i.getSecond()].getComponent(),new UseEnergyAction(CurrentPlanche), new ConsumingEnergy());
        }
        this.moveplayer();
    }
    public void moveplayer(){
        getBoard().movePlayer(currentPlayer.GetID(),currentmovement);
        this.currentPlayer.setState(new Waiting());
        this.updateSates();
    }



    //json required
    public OpenSpace() {}
}


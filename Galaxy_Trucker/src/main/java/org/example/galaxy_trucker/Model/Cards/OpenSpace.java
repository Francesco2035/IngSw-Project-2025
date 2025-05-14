package org.example.galaxy_trucker.Model.Cards;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Exceptions.WrongNumofEnergyExeption;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.PlayerStates.*;

import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;

public class OpenSpace extends Card{
    private Player currentPlayer;
    private int order;
    private int currentmovement;
    private int energyUsage;



    ///  in caso di disconnessione non attiva motori doppi ma avanza lo stesso
    public OpenSpace(int level, GameBoard board){

        super(level, 0 ,board);
        this.order = 0;
        this.currentPlayer = new Player();
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
            if (currentPlayer != null) {currentPlayer.setState(new Waiting());}
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
        this.setFinished(true);
    }

    @Override
    public void checkMovement(int enginePower, int numofDouble) {
            this.currentmovement=enginePower;
            this.energyUsage=numofDouble;
            if(this.energyUsage==0){
                this.moveplayer();
            }
            else{
                this.currentPlayer.setState(new ConsumingEnergy());
            }

    }


    @Override
    public void consumeEnergy(ArrayList<IntegerPair> coordinates) {
        if (coordinates==null){
            throw new IllegalArgumentException("you must give coordinates to consumeEnergy");
        }
        if(coordinates.size()!=this.energyUsage){
            currentPlayer.setState(new GiveSpeed());
            throw new WrongNumofEnergyExeption("wrong number of enrgy cells");
            ///  devo fare si che in caso di errore torni alla movement
        }
        PlayerBoard CurrentPlanche =currentPlayer.getmyPlayerBoard();
        Tile[][] tiles = CurrentPlanche.getPlayerBoard();
        /// opero sulla copia
        for(IntegerPair i:coordinates){
            try {
                CurrentPlanche.performAction(tiles[i.getFirst()][i.getSecond()].getComponent(),
                        new UseEnergyAction(CurrentPlanche), new ConsumingEnergy());
            }
            catch (InvalidInput e){
                currentPlayer.setState(new GiveSpeed());
                throw new WrongNumofEnergyExeption("there was no energy to use in:"+i.getFirst()+" "+i.getSecond());
            }
        }
        this.moveplayer();
    }
    public void moveplayer(){
        getBoard().movePlayer(currentPlayer.GetID(),currentmovement);
        this.currentPlayer.setState(new Waiting());
        this.updateSates();
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    //json required
    public OpenSpace() {}
}


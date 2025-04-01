package org.example.galaxy_trucker.Model.Cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.InputHandlers.Accept;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;


//inserire le chiamate alle interfacce
public class Pirates extends Card{
    private int requirement;
    private int reward;
    private int PlayerOrder;
    private int ShotsOrder;
    private int ShotsLine;
    private IntegerPair hit;
    private  boolean defeated;
    private int order;
    private Player currentPlayer;
    private int[] lines;

    @JsonProperty("punishment")
    private ArrayList<Integer> Punishment;
    // conviene creare una classe che lista gli attacchi o in qualche modo chiama solo una volta
    //il player da attaccare cambia Attack
    public Pirates(int level, int time, GameBoard board, int Reward, int Requirement, ArrayList<Integer> Punsihment){
        super(level, time, board);
        this.requirement = Requirement;
        this.reward = Reward;
        this.Punishment = Punsihment;
        this.ShotsOrder = 0;
        this.ShotsLine = 0;

        this.defeated = false;
        this.currentPlayer = null;
        this.lines = new int[Punsihment.size()/2];
        for(int i=0;i< Punishment.size()/2;i++){
            lines[i] = this.getBoard().getPlayers().getFirst().RollDice()-1;
        }
        this.hit = null;

    }
 @Override
    public void CardEffect(){

        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(Player p : PlayerList){
            p.setState(PlayerStates.Waiting);
        }
        this.updateSates();
    }


    @Override
    public void updateSates(){
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        if(this.order<PlayerList.size() && !this.defeated){
            currentPlayer = PlayerList.get(this.order);
            PlayerBoard CurrentPlanche =currentPlayer.getMyPlance();

            this.currentPlayer.setState(PlayerStates.GiveAttack);
            this.currentPlayer.setInputHandler(new Accept(this));

            this.order++;
        }
        else{
            this.finishCard();
        }
    }
//
//    @Override
//    public void continueCard(ArrayList<IntegerPair> cannons) {
//        double power= currentPlayer.getMyPlance().getPower(cannons);
//        if(power>this.getRequirement()){
//            this.defeated=true;
//            this.currentPlayer.setState(PlayerStates.Accepting);
//            this.currentPlayer.setInputHandler(new Accept(this));
//        }
//        else if (power<this.getRequirement()){
//            this.continueCard();
//        }
//
//    }



    @Override
    public void continueCard() {
        int Movement;
        boolean shotsFlag= false;
            while (this.ShotsOrder < Punishment.size() && shotsFlag == false) {

            PlayerBoard CurrentPlanche = currentPlayer.getMyPlance(); //prendo plancia
            int[][] MeteoritesValidPlanche = CurrentPlanche.getValidPlayerBoard();//prende matrice validita
            if (Punishment.get(ShotsOrder) == 0) { //sinistra
                Movement = 0;
                while (Movement < 10 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[lines[ShotsOrder / 2]][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo

                            shotsFlag = true;
                            hit.setValue(Movement, lines[ShotsOrder / 2]);
                            currentPlayer.setState(PlayerStates.DefendingFromShots);
                        }


                    Movement++;
                }
            }
            if (Punishment.get(ShotsOrder) == 1) {//sopra
                Movement = 0;
                while (Movement < 10 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[Movement][lines[ShotsOrder / 2]] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo

                            shotsFlag = true;
                            hit.setValue(Movement, lines[ShotsOrder / 2]);
                            currentPlayer.setState(PlayerStates.DefendingFromShots);

                    }

                    Movement++;
                }
            }
            if (Punishment.get(ShotsOrder) == 2) {// destra
                Movement = 9;
                while (Movement >= 0 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[lines[ShotsOrder / 2]][Movement] > 0) {

                            shotsFlag = true;
                            hit.setValue(Movement, lines[ShotsOrder/2]);
                            currentPlayer.setState(PlayerStates.DefendingFromShots);

                    }
                    Movement--;
                }

            }
            else { //sotto
                Movement = 9;
                while (Movement >= 0 && shotsFlag == false) {
                    if (MeteoritesValidPlanche[Movement][lines[ShotsOrder / 2]] > 0) {
                            shotsFlag = true;
                            hit.setValue(Movement, lines[ShotsOrder / 2]);
                            currentPlayer.setState(PlayerStates.DefendingFromShots);
                        }


                    Movement--;
                }

            }

            this.ShotsOrder += 2;
        }
        if(this.ShotsOrder >=Punishment.size() ){
            this.ShotsOrder = 0;
            this.updateSates();
        }
    }

    @Override
    public void continueCard(boolean accepted){
        if(accepted){
            currentPlayer.IncreaseCredits(this.reward);
            this.getBoard().movePlayer(this.currentPlayer.GetID(), this.getTime());
        }

        this.finishCard();
    }

    @Override
    public void DefendFromShots(IntegerPair coordinates) {
        PlayerBoard currentBoard =this.currentPlayer.getMyPlance();
        Tile[][] tiles =currentBoard.getPlayerBoard();

        if(coordinates !=null) {
//            if (!(Punishment.get(ShotsOrder + 1) == 0 && (currentBoard.getTile(coordinates.getFirst(), coordinates.getSecond()).getComponent().getAbility(0).contains(Punishment.get(ShotsOrder))) || Punishment.get(ShotsOrder + 1) == 1)) {
//                // non dovrei attivare lo scudo o lo scudo è sbagliato
//            }
        }
        if(coordinates ==null) { // se sono entrambi nulli non mi son difeso quindi vengo colpito
            currentBoard.destroy(hit.getFirst(), hit.getSecond());
        }
        this.continueCard();
    }

    @Override
    public void finishCard() {
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(int i=0; i<PlayerList.size(); i++){
            PlayerList.get(i).setState(PlayerStates.BaseState);
        }
    }



    //json reuired
    public Pirates(){}
    public int getRequirement() {return requirement;}
    public void setRequirement(int requirement) {this.requirement = requirement;}
    public int getReward() {return reward;}
    public void setReward(int reward) {this.reward = reward;}
    public ArrayList<Integer> getPunisment() {return Punishment;}
    public void setPunisment(ArrayList<Integer> punisment) {Punishment = punisment;}
}

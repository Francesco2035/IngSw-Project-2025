package org.example.galaxy_trucker.Model.Cards;
//import javafx.util.Pair;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Connectors.*;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;

//GESTISCI GLI ATTACCHI COME UN ARRAY LIST DI INTEGER E NON INTEGER PAIR

// direzioni int sinistra 0 sopra 1...
//0 piccolo 1 grande
public class Meteorites extends Card {
    @JsonProperty ("attacks")// prima è la direzione, secondo il tipo di attacco
    private ArrayList<Integer> attacks;
    private Player currentPlayer;
    private boolean flag;
    private int PlayerOrder;
    private int MeteoritesOrder;
    private int MeteoritesLine;
    private IntegerPair hit;

    public Meteorites(int level, int time, GameBoard board, ArrayList<Integer> attacks) {
        super(level, 0, board);

        this.attacks = attacks;
        this.currentPlayer = null;
        this.flag = false;
        this.PlayerOrder = 0;
        this.MeteoritesOrder = 0;
        this.MeteoritesLine = 0;
        this.hit = new IntegerPair(0,0);



    }
//per adesso hit non fa nulla ma o semplicemente chiamerà posizione + direzione + tipo per sparare
    //o gestisce la cosa e poi nel caso di hit chiama solo la posizione coplita se accade ed è indifesa
    @Override
    public void CardEffect() {

        GameBoard MeteoritesBoard = super.getBoard();
        ArrayList<Player> MeteoritesPlayerList = MeteoritesBoard.getPlayers();

        if (this.MeteoritesOrder< this.attacks.size()) { //scorre i meteoriti e attacca i player 1 a 1
            this.MeteoritesLine = MeteoritesPlayerList.get(0).RollDice(); // tira numero
            MeteoritesOrder+=2;
            this.updateSates();
        }
        else {
            this.finishCard();
        }
    }

    @Override
    public void updateSates(){
        int Movement;
        boolean MeteoritesFlag=false;
        this.currentPlayer=this.getBoard().getPlayers().get(PlayerOrder);


        if (PlayerOrder==this.getBoard().getPlayers().size()){
            PlayerOrder=0;
            this.CardEffect();
        }
      PlayerBoard  CurrentPlanche=currentPlayer.getmyPlayerBoard(); //prendo plancia
        int [][]MeteoritesValidPlanche=CurrentPlanche.getValidPlayerBoard();//prende matrice validita
        if (attacks.get(MeteoritesOrder)==0) { //sinistra
            Movement=0;
            while(Movement<10 && MeteoritesFlag == false){
                if(MeteoritesValidPlanche[MeteoritesLine][Movement]>0) {//guardo se la casella è occupata (spero basti fare questo controllo
                    Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                    if(attacks.get(MeteoritesOrder+1)==0 && tiles[Movement][MeteoritesLine].getConnectors().get(0).equals(new NONE())) {
                    }
                    else {
                        MeteoritesFlag = true;
                        hit.setValue(Movement, MeteoritesLine);
                        currentPlayer.setState(PlayerStates.DefendingFromMeteorites);
                    }
                }

                Movement++;
            }
        }
        if (attacks.get(MeteoritesOrder)==1) {//sopra
            Movement=0;
            while(Movement<10 && MeteoritesFlag == false) {
                if (MeteoritesValidPlanche[Movement][MeteoritesLine] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                    Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                    if(attacks.get(MeteoritesOrder+1)==0 && tiles[Movement][MeteoritesLine].getConnectors().get(1).equals(new NONE())) {
                    }
                    else {
                        MeteoritesFlag = true;
                        hit.setValue(Movement, MeteoritesLine);
                        currentPlayer.setState(PlayerStates.DefendingFromMeteorites);
                    }
                }

                Movement++;
            }
        }
        if (attacks.get(MeteoritesOrder)==2) {// destra
            Movement=9;
            while(Movement>=0 && MeteoritesFlag == false) {
                if (MeteoritesValidPlanche[MeteoritesLine][Movement] > 0) {
                    Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                    if(attacks.get(MeteoritesOrder+1)==0 && tiles[Movement][MeteoritesLine].getConnectors().get(2).equals(new NONE())) {
                    }
                    else {
                        MeteoritesFlag = true;
                        hit.setValue(Movement, MeteoritesLine);
                        currentPlayer.setState(PlayerStates.DefendingFromMeteorites);
                    }
                }
                Movement--;
            }

        }
        else { //sotto
            Movement=9;
            while(Movement>=0 && MeteoritesFlag == false) {
                if (MeteoritesValidPlanche[Movement][MeteoritesLine] > 0) {
                    Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                    if(attacks.get(MeteoritesOrder+1)==0 && tiles[Movement][MeteoritesLine].getConnectors().get(3).equals(new NONE())) {
                    }
                    else {
                        MeteoritesFlag = true;
                        hit.setValue(Movement, MeteoritesLine);
                        currentPlayer.setState(PlayerStates.DefendingFromMeteorites);
                    }
                }

                Movement--;
            }

        }
        this.PlayerOrder++;
        if (!MeteoritesFlag){
            this.updateSates();
        }
    }
    public int schifo(){
        IntegerPair a = new IntegerPair(0,0);
        IntegerPair b = new IntegerPair(1,1);
        this.DefendFromMeteorites(a,b);
        return 1;
    }

    @Override
    public void DefendFromMeteorites(IntegerPair CannonCoord, IntegerPair ShieldCoord) {
        PlayerBoard currentBoard =this.currentPlayer.getmyPlayerBoard();
        Tile[][] tiles =currentBoard.getPlayerBoard();


        //se le coordinate date non son cannoni ne scudi ecxeption
        //o se segnalo cannoni diversi da quelli sensati

//        if(ShieldCoord !=null) {
//            if (!(attacks.get(MeteoritesOrder + 1) == 0 && (currentBoard.getTile(ShieldCoord.getFirst(), ShieldCoord.getSecond()).getComponent().get(new ShieldGetter(currentBoard.getTile(ShieldCoord.getFirst(), ShieldCoord.getSecond()).getComponent())) .contains(attacks.get(MeteoritesOrder))) || attacks.get(MeteoritesOrder + 1) == 1)) {
//                // non dovrei attivare lo scudo o lo scudo è sbagliato
//            }
//        }
        if(CannonCoord !=null) {
            if (attacks.get(MeteoritesOrder) == 0 || attacks.get(MeteoritesOrder) == 2) { // sinistra o destra
                if(!(attacks.get(MeteoritesOrder + 1) == 1 && (CannonCoord.getFirst() == hit.getFirst() && currentBoard.getTile(CannonCoord.getFirst(), CannonCoord.getSecond()).getConnectors().get(MeteoritesOrder).equals(new CANNON()))|| attacks.get(MeteoritesOrder + 1) == 0)){
                    // cannone errato o tipo dio attaco errato
                }
            } else {
                 if(!(attacks.get(MeteoritesOrder + 1) == 1 && (CannonCoord.getSecond() == hit.getSecond() && currentBoard.getTile(CannonCoord.getFirst(), CannonCoord.getSecond()).getConnectors().get(MeteoritesOrder).equals(new CANNON()))||attacks.get(MeteoritesOrder + 1) == 0 )){
                     // stessa cosa ma nelle atre due direzioni
                 }

            }
        }
        if(CannonCoord ==null && ShieldCoord ==null) { // se sono entrambi nulli non mi son difeso quindi vengo colpito
            currentBoard.destroy(hit.getFirst(), hit.getSecond());
        }
        this.updateSates();
    }
    @Override
    public void finishCard() {
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(int i=0; i<PlayerList.size(); i++){
            PlayerList.get(i).setState(PlayerStates.BaseState);
        }
    }


    //json required
    public Meteorites() {}
    public ArrayList<Integer> getAttacks() {return attacks;}
    public void setAttacks(ArrayList<Integer> attacks) {attacks = attacks;}
}
package org.example.galaxy_trucker.Model.Cards;
//import javafx.util.Pair;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.Connector;
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

        if (attacks.size() > this.MeteoritesOrder) { //scorre i meteoriti e attacca i player 1 a 1
            this.MeteoritesLine = MeteoritesPlayerList.get(0).RollDice(); // tira numero
            MeteoritesOrder+=2;
            this.updateSates();
        }
    }

    @Override
    public void updateSates(){
        int Movement;
        boolean MeteoritesFlag=false;
        this.currentPlayer=this.getBoard().getPlayers().get(PlayerOrder);



      PlayerBoard  CurrentPlanche=currentPlayer.getMyPlance(); //prendo plancia
        int [][]MeteoritesValidPlanche=CurrentPlanche.getValidPlayerBoard();//prende matrice validita
        if (attacks.get(MeteoritesOrder)==0) { //sinistra
            Movement=0;
            while(Movement<10 && MeteoritesFlag == false){
                if(MeteoritesValidPlanche[MeteoritesLine][Movement]>0) {//guardo se la casella è occupata (spero basti fare questo controllo
                    Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                    if(attacks.get(MeteoritesOrder+1)==0 && tiles[Movement][MeteoritesLine].getConnectors().get(0)== Connector.NONE) {
                    }
                    else {
                        MeteoritesFlag = true;
                        hit.setValue(Movement, MeteoritesLine);
                        currentPlayer.setState(PlayerStates.Defending);
                    }
                }

                Movement++;
            }
        }
        if (attacks.get(MeteoritesOrder)==1) {//sopra
            Movement=0;
            while(Movement<10 && MeteoritesFlag == false) {
                if (MeteoritesValidPlanche[Movement][MeteoritesLine] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo

                    MeteoritesFlag = true;
                }

                Movement++;
            }
        }
        if (attacks.get(MeteoritesOrder)==2) {// destra
            Movement=9;
            while(Movement>=0 && MeteoritesFlag == false) {
                if (MeteoritesValidPlanche[MeteoritesLine][Movement] > 0) {
                    MeteoritesFlag = true;
                }
                Movement--;
            }

        }
        else { //sotto
            Movement=9;
            while(Movement>=0 && MeteoritesFlag == false) {
                if (MeteoritesValidPlanche[Movement][MeteoritesLine] > 0) {
                    MeteoritesFlag = true;
                }

                Movement--;
            }

        }

    }
    @Override
    public void continueCard2(IntegerPair CannonCoord, IntegerPair ShieldCoord) {
        PlayerBoard currentBoard =this.currentPlayer.getMyPlance();
        Tile[][] tiles =currentBoard.getPlayerBoard();


        //se le coordinate date non son cannoni ne scudi ecxeption
        //o se segnalo cannoni diversi da quelli sensati

        if(ShieldCoord !=null) {
            if (!(attacks.get(MeteoritesOrder + 1) == 0 && (currentBoard.getTile(ShieldCoord.getFirst(), ShieldCoord.getSecond()).getComponent().getAbility(0).contains(attacks.get(MeteoritesOrder))) || attacks.get(MeteoritesOrder + 1) == 1)) {
                // non dovrei attivare lo scudo o lo scudo è sbagliato
            }
        }
        if(CannonCoord !=null) {
            if (attacks.get(MeteoritesOrder) == 0 || attacks.get(MeteoritesOrder) == 2) { // sinistra o destra
                if(!(attacks.get(MeteoritesOrder + 1) == 1 && (CannonCoord.getFirst() == hit.getFirst() && currentBoard.getTile(CannonCoord.getFirst(), CannonCoord.getSecond()).getConnectors().get(MeteoritesOrder) == Connector.CANNON)|| attacks.get(MeteoritesOrder + 1) == 0)){
                    // cannone errato o tipo dio attaco errato
                }
            } else {
                 if(!(attacks.get(MeteoritesOrder + 1) == 1 && (CannonCoord.getSecond() == hit.getSecond() && currentBoard.getTile(CannonCoord.getFirst(), CannonCoord.getSecond()).getConnectors().get(MeteoritesOrder) == Connector.CANNON)||attacks.get(MeteoritesOrder + 1) == 0 )){
                     // stessa cosa ma nelle atre due direzioni
                 }

            }
        }
        if(CannonCoord ==null && ShieldCoord ==null) { // se sono entrambi nulli non mi son difeso quindi vengo colpito
            currentBoard.destroy(hit.getFirst(), hit.getSecond());
        }

    }


    //json required
    public Meteorites() {}
    public ArrayList<Integer> getAttacks() {return attacks;}
    public void setAttacks(ArrayList<Integer> attacks) {attacks = attacks;}
}
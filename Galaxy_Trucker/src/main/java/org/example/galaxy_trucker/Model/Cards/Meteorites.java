package org.example.galaxy_trucker.Model.Cards;
//import javafx.util.Pair;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Exceptions.ImpossibleBoardChangeException;
import org.example.galaxy_trucker.Exceptions.InvalidDefenceEceptiopn;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.*;
import org.example.galaxy_trucker.Model.Connectors.*;
import org.example.galaxy_trucker.Model.Tiles.Tile;


import java.util.ArrayList;

//GESTISCI GLI ATTACCHI COME UN ARRAY LIST DI INTEGER E NON INTEGER PAIR

// direzioni int sinistra 0 sopra 1...
//0 piccolo 1 grande
public class Meteorites extends Card {

    private Player currentPlayer;
    private boolean flag;
    private int PlayerOrder;
    private int MeteoritesOrder;
    private int MeteoritesLine;
    private IntegerPair hit;



    @JsonProperty ("attacks")// prima è la direzione, secondo il tipo di attacco
    private ArrayList<Integer> attacks;



    public Meteorites(int level, int time, GameBoard board, ArrayList<Integer> attacks) {
        super(level, 0, board);

        this.attacks = attacks;
        this.currentPlayer = new Player();
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

        for (Player p: this.getBoard().getPlayers()) {
            p.setState(new Waiting());
        }

        System.out.println("SIZE OF ATTACK:"+attacks.size());

        GameBoard MeteoritesBoard = super.getBoard();
        ArrayList<Player> MeteoritesPlayerList = MeteoritesBoard.getPlayers();

        if (this.MeteoritesOrder< this.attacks.size()) { //scorre i meteoriti e attacca i player 1 a 1

            this.MeteoritesLine = MeteoritesPlayerList.get(0).RollDice()-1; // tira numero
//            if(this.attacks.get(this.MeteoritesOrder)==0){
//                this.MeteoritesLine=8;
//            }
            System.out.println("attacco da "+attacks.get(MeteoritesOrder) +"alla riga:"+this.MeteoritesLine);
            System.out.println("attacco numero: "+(this.MeteoritesOrder/2));
            this.updateSates();
        }
        else {
//            this.pog=true;
            this.finishCard();

        }
    }

//    public boolean isPog() {
//        return pog;
//    }

    @Override
    public void keepGoing(){
        updateSates();
    }

    @Override
    public void updateSates(){
        int Movement;
        boolean MeteoritesFlag=false;
        boolean DamageFlag=false;

        if (PlayerOrder>=this.getBoard().getPlayers().size()){
            PlayerOrder=0;
            MeteoritesOrder+=2;
            this.CardEffect();
        }
        else {
            if (currentPlayer != null) {currentPlayer.setState(new Waiting());}

            this.currentPlayer = this.getBoard().getPlayers().get(PlayerOrder);

            PlayerBoard CurrentPlanche = currentPlayer.getmyPlayerBoard(); //prendo plancia
            int[][] MeteoritesValidPlanche = CurrentPlanche.getValidPlayerBoard();//prende matrice validita
            System.out.println("attack number: "+this.MeteoritesOrder/2 +" on: "+currentPlayer.GetID());
            if (attacks.get(MeteoritesOrder) == 0) { //sinistra
                System.out.println("SINISTRA");
                Movement = 0;
                while (Movement < 10 && MeteoritesLine<10  && MeteoritesFlag == false) {
                    if (MeteoritesValidPlanche[MeteoritesLine][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                        Tile tiles[][] = CurrentPlanche.getPlayerBoard();
                        System.out.println("touch in:"+MeteoritesLine+" "+Movement);
                        if (attacks.get(MeteoritesOrder + 1) == 0 && tiles[MeteoritesLine][Movement].getConnectors().get(0) == NONE.INSTANCE) {
                            MeteoritesFlag = true;
                            System.out.println("lisciato");
                        }
                        else if (attacks.get(MeteoritesOrder + 1) == 0) {
                            MeteoritesFlag = true;
                            DamageFlag=true;
                            hit.setValue(MeteoritesLine,Movement);
                            System.out.println("Meteorites hit in: " + Movement + " " + MeteoritesLine);
                            currentPlayer.setState(new DefendingFromSmall());

                        }
                        else {
                            MeteoritesFlag = true;
                            DamageFlag = true;
                            hit.setValue(MeteoritesLine, Movement);
                            System.out.println("Meteorites hit in: " + MeteoritesLine + " " + Movement);
                            currentPlayer.setState(new DefendingFromLarge());
                        }
                    }

                    Movement++;
                }
            }
            else if (attacks.get(MeteoritesOrder) == 1) {//sopra
                System.out.println("SOPRA");
                Movement = 0;
                while (Movement < 10 && MeteoritesLine<10 && MeteoritesFlag == false) {
                    if (MeteoritesValidPlanche[Movement][MeteoritesLine] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                        Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                        if (attacks.get(MeteoritesOrder + 1) == 0 && tiles[Movement][MeteoritesLine].getConnectors().get(1).equals(NONE.INSTANCE)) {
                            MeteoritesFlag = true;

                            System.out.println("lisciato");
                        }
                        else if (attacks.get(MeteoritesOrder + 1) == 0) {
                            MeteoritesFlag = true;
                            DamageFlag=true;
                            hit.setValue( Movement, MeteoritesLine);
                            System.out.println("Meteorites hit in: " + Movement + " " + MeteoritesLine);
                            currentPlayer.setState(new DefendingFromSmall());

                        }
                        else {
                            MeteoritesFlag = true;
                            DamageFlag = true;
                            hit.setValue(Movement, MeteoritesLine);
                            System.out.println("Meteorites hit in: " + Movement + " " + MeteoritesLine);
                            currentPlayer.setState(new DefendingFromLarge());
                        }
                    }

                    Movement++;
                }
            }
            else if (attacks.get(MeteoritesOrder) == 2) {// destra
                System.out.println("DESTRA");
                Movement = 9;
                while (Movement >= 0  && MeteoritesLine<10 && MeteoritesFlag == false) {
                    if (MeteoritesValidPlanche[MeteoritesLine][Movement] > 0) {
                        Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                        if (attacks.get(MeteoritesOrder + 1) == 0 && tiles[MeteoritesLine][Movement].getConnectors().get(2).equals(NONE.INSTANCE)) {
                            MeteoritesFlag = true;
                            System.out.println("lisciato");
                        }
                        else if (attacks.get(MeteoritesOrder + 1) == 0) {
                            MeteoritesFlag = true;
                            DamageFlag=true;
                            hit.setValue(MeteoritesLine,Movement);
                            System.out.println("Meteorites hit in: " + MeteoritesLine+ " " + Movement);
                            currentPlayer.setState(new DefendingFromSmall());

                        }
                        else {
                            MeteoritesFlag = true;
                            DamageFlag = true;
                            hit.setValue(MeteoritesLine, Movement);
                            System.out.println("Meteorites hit in: " + MeteoritesLine + " " + Movement);
                            currentPlayer.setState(new DefendingFromLarge());
                        }
                    }
                    Movement--;
                }

            }
            else { //sotto
                System.out.println("SOTTO");
                Movement = 9;
                while (Movement >= 0 && MeteoritesLine<10  && MeteoritesFlag == false) {
                    if (MeteoritesValidPlanche[Movement][MeteoritesLine] > 0) {
                        Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                        if (attacks.get(MeteoritesOrder + 1) == 0 ){ //caso meteoriti piccoli
                            if(tiles[Movement][MeteoritesLine].getConnectors().get(3).equals(NONE.class)) {
                            MeteoritesFlag = true;
                            System.out.println("lisciato");
                        }
                            else if (attacks.get(MeteoritesOrder + 1) == 0) {
                                MeteoritesFlag = true;
                                DamageFlag = true;
                                hit.setValue( Movement, MeteoritesLine);
                                System.out.println("Meteorites hit in: " + Movement + " " + MeteoritesLine);
                                currentPlayer.setState(new DefendingFromSmall());

                            }

                        }
                        else {// caso meteoriti grandi
                            MeteoritesFlag = true;
                            DamageFlag = true;
                            hit.setValue(Movement, MeteoritesLine);
                            System.out.println("Meteorites hit in: " + Movement + " " + MeteoritesLine);
                            currentPlayer.setState(new DefendingFromLarge());
                        }
                    }

                    Movement--;
                }

            }
            this.PlayerOrder++;
            if (!DamageFlag) {
                this.updateSates();
            }
        }
    }

    /// //dividi la defend tu in shield e in cannone a seconda del tipo di meteorite così è piu facile il controllo di correttezza e il passaggio di input

        // DEVO NON ANDAR SUBITO IN UPDATE STATES MA IN SCELTA TRONCONI! UPSIE :)

    @Override
    public void DefendFromSmall(IntegerPair energy){
        PlayerBoard currentBoard =this.currentPlayer.getmyPlayerBoard();
        Tile[][] tiles =currentBoard.getPlayerBoard();
        if (energy!=null){
            if ((currentBoard.getShield()[attacks.get(MeteoritesOrder)]==0)){
                throw new InvalidDefenceEceptiopn("this shield defends the wrong side"+" the side was: "+attacks.get(MeteoritesOrder));
            }
            else {
                try {
                    currentBoard.performAction(tiles[energy.getFirst()][energy.getSecond()].getComponent(),new UseEnergyAction(currentBoard), new ConsumingEnergy());
                }
            catch (Exception e){
                throw new ImpossibleBoardChangeException("There was no energy to use here");
            }
                System.out.println("DefendFromSmall");
            }
        }
        else {
            currentBoard.destroy(hit.getFirst(), hit.getSecond());
            currentBoard.handleAttack(hit.getFirst(), hit.getSecond());
            if (currentBoard.getBroken()){
                System.out.println("rottura nave");
                this.currentPlayer.setState(new HandleDestruction());
                return;

            }
            System.out.println("destroyed: "+hit.getFirst()+" "+hit.getSecond());
        }
        this.updateSates();
    }

    @Override
    public void DefendFromLarge(IntegerPair CannonCoord,IntegerPair EnergyStorage) {
        PlayerBoard currentBoard =this.currentPlayer.getmyPlayerBoard();
        Tile[][] tiles =currentBoard.getPlayerBoard();
        if(CannonCoord !=null) {
            if (attacks.get(MeteoritesOrder) == 0 || attacks.get(MeteoritesOrder) == 2) { // sinistra o destra
                if(!((( CannonCoord.getFirst() == hit.getFirst() ||CannonCoord.getFirst() == hit.getFirst()+1 || CannonCoord.getFirst() == hit.getFirst()-1 ) && currentBoard.getTile(CannonCoord.getFirst(), CannonCoord.getSecond()).getConnectors().get(attacks.get(MeteoritesOrder))==(CANNON.INSTANCE)))){
                    throw new InvalidDefenceEceptiopn("this cannon isn't aiming at the meteorite");
                }
                else if (currentBoard.getTile(CannonCoord.getFirst(), CannonCoord.getSecond()).getComponent().getType() == 2) {
                    if(EnergyStorage==null){
                        throw new InvalidDefenceEceptiopn("you have to consume energy to use this cannon");
                    }
                    try {
                        currentBoard.performAction(tiles[EnergyStorage.getFirst()][EnergyStorage.getSecond()].getComponent(), new UseEnergyAction(currentBoard), new DefendingFromLarge());
                    }
                    catch (Exception e){ // potrei splittare la catch in no energia e coord sbagliata
                        throw new ImpossibleBoardChangeException("There was no energy to use");}
                }
                System.out.println("DefendFromLarge");
            }
            else if (attacks.get(MeteoritesOrder) == 1 || attacks.get(MeteoritesOrder) == 3){//sopra o sotto
                 if(!((CannonCoord.getSecond() == hit.getSecond() && currentBoard.getTile(CannonCoord.getFirst(), CannonCoord.getSecond()).getConnectors().get(attacks.get(MeteoritesOrder))==(CANNON.INSTANCE))) ){
                     throw new InvalidDefenceEceptiopn("this cannon isn't aiming at the meteorite");
                 }
                 else if (currentBoard.getTile(CannonCoord.getFirst(), CannonCoord.getSecond()).getComponent().getType() == 2) {
                     if(EnergyStorage==null){
                         throw new InvalidDefenceEceptiopn("you have to consume energy to use this cannon");
                     }
                     try {
                         currentBoard.performAction(tiles[EnergyStorage.getFirst()][EnergyStorage.getSecond()].getComponent(), new UseEnergyAction(currentBoard), new DefendingFromLarge());
                     }
                 catch (Exception e){ // potrei splittare la catch in no energia e coord sbagliata
                     throw new ImpossibleBoardChangeException("There was no energy to use");}
                 }
                System.out.println("DefendFromLarge");
            }
        }
        else  {
            currentBoard.destroy(hit.getFirst(), hit.getSecond());
            currentBoard.handleAttack(hit.getFirst(), hit.getSecond());
            System.out.println("destryoyed: "+hit.getFirst()+" "+hit.getSecond()+" of:"+currentPlayer.GetID());
            if (currentBoard.getBroken()){
                this.currentPlayer.setState(new HandleDestruction());
                return;

            }
        }
        this.updateSates();
    }
    @Override
    public void finishCard() {
        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(int i=0; i<PlayerList.size(); i++){
            PlayerList.get(i).setState(new BaseState());
        }
        System.out.println("FINE");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
    }

    public void setMeteoritesOrder(int meteoritesOrder) {
        MeteoritesOrder = meteoritesOrder;
    }

    public void setPlayerOrder(int playerOrder) {
        PlayerOrder = playerOrder;
    }

    public IntegerPair getHit() {
        return hit;
    }

    public void setHit(int x, int y) {
        this.hit.setValue(x,y);
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getMeteoritesOrder() {
        return MeteoritesOrder;
    }

    //json required
    public Meteorites() {}
    public ArrayList<Integer> getAttacks() {return attacks;}
    @JsonCreator
    public void setAttacks(ArrayList<Integer> attacks) {this.attacks = attacks;}
}
package org.example.galaxy_trucker.Model.Cards;
//import javafx.util.Pair;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Controller.Messages.ConcurrentCardListener;
import org.example.galaxy_trucker.Controller.Messages.TileSets.RandomCardEffectEvent;
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
import java.util.HashMap;

//GESTISCI GLI ATTACCHI COME UN ARRAY LIST DI INTEGER E NON INTEGER PAIR

// direzioni int sinistra 0 sopra 1...
//0 piccolo 1 grande
public class   Meteorites extends Card {

    private Player currentPlayer;
    private boolean flag;
    private int PlayerOrder;
    private int MeteoritesOrder;
    private int MeteoritesLine;
    private HashMap <String,IntegerPair> hits;
    private  int SuccessfulDefences;
    private  int NumofDefences;

    @JsonProperty ("attacks")// prima è la direzione, secondo il tipo di attacco
    private ArrayList<Integer> attacks;




    ///  in caso di disconnessione semplicemente non si difende da ncazzoz che lo colpisce
    public Meteorites(int level, int time, GameBoard board, ArrayList<Integer> attacks) {
        super(level, 0, board);

        this.attacks = attacks;
        this.currentPlayer = new Player();
        this.flag = false;
        this.PlayerOrder = 0;
        this.MeteoritesOrder = 0;
        this.MeteoritesLine = 0;
        this.hits = new HashMap<>();



    }
//per adesso hit non fa nulla ma o semplicemente chiamerà posizione + direzione + tipo per sparare
    //o gestisce la cosa e poi nel caso di hit chiama solo la posizione coplita se accade ed è indifesa
    @Override
    public void CardEffect() {
        hits = new HashMap<>();
        for (Player p: this.getBoard().getPlayers()) {
            p.setState(new Waiting());
            hits.put(p.GetID(),new IntegerPair(0,0));
        }

        System.out.println("SIZE OF ATTACK:"+attacks.size()/2);

        GameBoard MeteoritesBoard = super.getBoard();
        ArrayList<Player> MeteoritesPlayerList = MeteoritesBoard.getPlayers();
        this.SuccessfulDefences=0;
        this.NumofDefences=super.getBoard().getPlayers().size();

        if (this.MeteoritesOrder< this.attacks.size()) { //scorre i meteoriti e attacca i player 1 a 1

            this.MeteoritesLine = MeteoritesPlayerList.get(0).RollDice()-1; // tira numero
//            if(this.attacks.get(this.MeteoritesOrder)==0){
//                this.MeteoritesLine=8;
//            }
            System.out.println("attacco da "+attacks.get(MeteoritesOrder) +" alla riga:"+this.MeteoritesLine);
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

        this.SuccessfulDefences++;
        updateSates();
    }

    @Override
    public void updateSates(){
        int Movement;
        boolean MeteoritesFlag=false;
        boolean DamageFlag=false;
        System.out.println("Successfuldefences ="+SuccessfulDefences +" NumofDefences= " +NumofDefences);
        if(this.SuccessfulDefences==NumofDefences) {
       // if (PlayerOrder>=this.getBoard().getPlayers().size()){
            try{
                Thread.sleep(2500);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            PlayerOrder=0;
            MeteoritesOrder+=2;
            this.CardEffect();
        }
        else if (PlayerOrder<this.getBoard().getPlayers().size()){
            //if (currentPlayer != null) {currentPlayer.setState(new Waiting());}

            this.currentPlayer = this.getBoard().getPlayers().get(PlayerOrder);

            PlayerBoard CurrentPlanche = currentPlayer.getmyPlayerBoard(); //prendo plancia
            int[][] MeteoritesValidPlanche = CurrentPlanche.getValidPlayerBoard();//prende matrice validita
            System.out.println("attack number: "+this.MeteoritesOrder/2 +" on: "+currentPlayer.GetID());

            /// cose per stampare al client
            String dimensione= new String();
            String direction = new String();
            String Colpito = new String("missed you");
            String location = new String("");

            if(attacks.get(MeteoritesOrder+1)==0) {
                dimensione = new String("small");

            }
            else {
                dimensione = new String("large");
            }

            if (attacks.get(MeteoritesOrder) == 0) { //sinistra
                System.out.println("SINISTRA");
                direction = new String("the left on line "+MeteoritesLine);
                Movement = 0;
                while (Movement < 10 && MeteoritesLine<10  && MeteoritesFlag == false) {
                    if (MeteoritesValidPlanche[MeteoritesLine][Movement] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                        location= new String(MeteoritesLine+ " "+Movement);
                        Tile tiles[][] = CurrentPlanche.getPlayerBoard();
                        System.out.println("touch in:"+MeteoritesLine+" "+Movement);
                        if (attacks.get(MeteoritesOrder + 1) == 0 && tiles[MeteoritesLine][Movement].getConnectors().get(0) == NONE.INSTANCE) {
                            MeteoritesFlag = true;
                            System.out.println("lisciato");
                            Colpito =new String("bounced off");

                        }
                        else if (attacks.get(MeteoritesOrder + 1) == 0) {
                            MeteoritesFlag = true;
                            DamageFlag=true;
                            hits.get(currentPlayer.GetID()).setValue(MeteoritesLine,Movement);
                            System.out.println("Meteorites hit in: " + MeteoritesLine + " " + Movement);
                            Colpito = new String("hit you");
                            currentPlayer.setState(new DefendingFromSmall());

                        }
                        else {
                            MeteoritesFlag = true;
                            DamageFlag = true;
                            hits.get(currentPlayer.GetID()).setValue(MeteoritesLine, Movement);
                            System.out.println("Meteorites hit in: " + MeteoritesLine + " " + Movement);
                            Colpito = new String("hit you");
                            currentPlayer.setState(new DefendingFromLarge());
                        }
                    }

                    Movement++;
                }
            }
            else if (attacks.get(MeteoritesOrder) == 1) {//sopra
                System.out.println("SOPRA");
                direction = new String("above on column "+MeteoritesLine);
                Movement = 0;
                while (Movement < 10 && MeteoritesLine<10 && MeteoritesFlag == false) {
                    if (MeteoritesValidPlanche[Movement][MeteoritesLine] > 0) {//guardo se la casella è occupata (spero basti fare questo controllo
                        location= new String(Movement+ " "+MeteoritesLine);
                        Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                        if (attacks.get(MeteoritesOrder + 1) == 0 && tiles[Movement][MeteoritesLine].getConnectors().get(1).equals(NONE.INSTANCE)) {
                            MeteoritesFlag = true;

                            System.out.println("lisciato");
                            Colpito =new String("bounced off");

                        }
                        else if (attacks.get(MeteoritesOrder + 1) == 0) {
                            MeteoritesFlag = true;
                            DamageFlag=true;
                            hits.get(currentPlayer.GetID()).setValue( Movement, MeteoritesLine);
                            System.out.println("Meteorites hit in: " + Movement + " " + MeteoritesLine);
                            Colpito = new String("hit you");
                            currentPlayer.setState(new DefendingFromSmall());

                        }
                        else {
                            MeteoritesFlag = true;
                            DamageFlag = true;
                            hits.get(currentPlayer.GetID()).setValue(Movement, MeteoritesLine);
                            System.out.println("Meteorites hit in: " + Movement + " " + MeteoritesLine);
                            Colpito = new String("hit you");
                            currentPlayer.setState(new DefendingFromLarge());
                        }
                    }

                    Movement++;
                }
            }
            else if (attacks.get(MeteoritesOrder) == 2) {// destra
                System.out.println("DESTRA");
                direction = new String("the right on line "+MeteoritesLine);
                Movement = 9;
                while (Movement >= 0  && MeteoritesLine<10 && MeteoritesFlag == false) {
                    if (MeteoritesValidPlanche[MeteoritesLine][Movement] > 0) {
                        location= new String(MeteoritesLine+ " "+Movement);
                        Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                        if (attacks.get(MeteoritesOrder + 1) == 0 && tiles[MeteoritesLine][Movement].getConnectors().get(2).equals(NONE.INSTANCE)) {
                            MeteoritesFlag = true;
                            System.out.println("lisciato");
                            Colpito =new String("bounced off");

                        }
                        else if (attacks.get(MeteoritesOrder + 1) == 0) {
                            MeteoritesFlag = true;
                            DamageFlag=true;
                            hits.get(currentPlayer.GetID()).setValue(MeteoritesLine,Movement);
                            System.out.println("Meteorites hit in: " + MeteoritesLine+ " " + Movement);
                            Colpito = new String("hit you");
                            currentPlayer.setState(new DefendingFromSmall());

                        }
                        else {
                            MeteoritesFlag = true;
                            DamageFlag = true;
                            hits.get(currentPlayer.GetID()).setValue(MeteoritesLine, Movement);
                            System.out.println("Meteorites hit in: " + MeteoritesLine + " " + Movement);
                            Colpito = new String("hit you");
                            currentPlayer.setState(new DefendingFromLarge());
                        }
                    }
                    Movement--;
                }

            }
            else { //sotto
                System.out.println("SOTTO");
                direction = new String("below on column "+MeteoritesLine);
                Movement = 9;
                while (Movement >= 0 && MeteoritesLine<10  && MeteoritesFlag == false) {
                    if (MeteoritesValidPlanche[Movement][MeteoritesLine] > 0) {
                        location= new String(Movement+ " "+MeteoritesLine);
                        Tile tiles[][] = CurrentPlanche.getPlayerBoard();

                        if (attacks.get(MeteoritesOrder + 1) == 0 ){ //caso meteoriti piccoli
                            if(tiles[Movement][MeteoritesLine].getConnectors().get(3).equals(NONE.class)) {
                            MeteoritesFlag = true;
                            System.out.println("lisciato");
                                Colpito =new String("bounced off");

                        }
                            else if (attacks.get(MeteoritesOrder + 1) == 0) {
                                MeteoritesFlag = true;
                                DamageFlag = true;
                                hits.get(currentPlayer.GetID()).setValue( Movement, MeteoritesLine);
                                System.out.println("Meteorites hit in: " + Movement + " " + MeteoritesLine);
                                Colpito = new String("hit you");
                                currentPlayer.setState(new DefendingFromSmall());

                            }

                        }
                        else {// caso meteoriti grandi
                            MeteoritesFlag = true;
                            DamageFlag = true;
                            hits.get(currentPlayer.GetID()).setValue(Movement, MeteoritesLine);
                            System.out.println("Meteorites hit in: " + Movement + " " + MeteoritesLine);
                            Colpito = new String("hit you");
                            currentPlayer.setState(new DefendingFromLarge());
                        }
                    }

                    Movement--;
                }

            }

            System.out.println("a "+dimensione+" meteorite came from "+direction+" and it "+Colpito+" at "+location);
            this.sendRandomEffect(currentPlayer.GetID(),new RandomCardEffectEvent("a "+dimensione+" meteorite came from "+dimensione+" and it "+Colpito+" at "+location));

            if (!DamageFlag){this.SuccessfulDefences++;}
            this.PlayerOrder++;
            if (PlayerOrder<this.getBoard().getPlayers().size()) {
                this.updateSates();
            }
            else if(this.SuccessfulDefences==NumofDefences){
                this.updateSates();
            }
        }
    }

    /// //dividi la defend tu in shield e in cannone a seconda del tipo di meteorite così è piu facile il controllo di correttezza e il passaggio di input

        // DEVO NON ANDAR SUBITO IN UPDATE STATES MA IN SCELTA TRONCONI! UPSIE :)



    @Override
    public void DefendFromSmall(IntegerPair energy, Player player){
        System.out.println(player.GetID()+ "is defending from small");
        PlayerBoard currentBoard =player.getmyPlayerBoard();
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
            currentBoard.destroy(hits.get(player.GetID()).getFirst(), hits.get(player.GetID()).getSecond());
            currentBoard.handleAttack(hits.get(player.GetID()).getFirst(), hits.get(player.GetID()).getSecond());
            if (currentBoard.getBroken()){

                System.out.println("\nrottura nave\n");

                System.out.println("destroyed: "+hits.get(player.GetID()).getFirst()+" "+hits.get(player.GetID()).getSecond());

                player.setState(new HandleDestruction());
                System.out.println("Stato del player "+ player.getPlayerState().getClass().getName());
                return;

            }
            System.out.println("Stato del player "+ player.getPlayerState().getClass().getName());
            System.out.println("destroyed: "+hits.get(player.GetID()).getFirst()+" "+hits.get(player.GetID()).getSecond());
        }
        this.SuccessfulDefences++;
        this.updateSates();
    }

    @Override
    public void DefendFromLarge(IntegerPair CannonCoord,IntegerPair EnergyStorage, Player player) {
        PlayerBoard currentBoard =player.getmyPlayerBoard();
        Tile[][] tiles =currentBoard.getPlayerBoard();
        if(CannonCoord !=null) {
            if (attacks.get(MeteoritesOrder) == 0 || attacks.get(MeteoritesOrder) == 2) { // sinistra o destra
                if(!((( CannonCoord.getFirst() == hits.get(player.GetID()).getFirst() ||CannonCoord.getFirst() == hits.get(player.GetID()).getFirst()+1 || CannonCoord.getFirst() == hits.get(player.GetID()).getFirst()-1 ) && currentBoard.getTile(CannonCoord.getFirst(), CannonCoord.getSecond()).getConnectors().get(attacks.get(MeteoritesOrder))==(CANNON.INSTANCE)))){
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
                 if(!((CannonCoord.getSecond() == hits.get(player.GetID()).getSecond() && currentBoard.getTile(CannonCoord.getFirst(), CannonCoord.getSecond()).getConnectors().get(attacks.get(MeteoritesOrder))==(CANNON.INSTANCE))) ){
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
            currentBoard.destroy(hits.get(player.GetID()).getFirst(), hits.get(player.GetID()).getSecond());
            currentBoard.handleAttack(hits.get(player.GetID()).getFirst(), hits.get(player.GetID()).getSecond());
            System.out.println("destryoyed: "+hits.get(player.GetID()).getFirst()+" "+hits.get(player.GetID()).getSecond()+" of:"+player.GetID());
            if (currentBoard.getBroken()){
                System.out.println("\nrottura nave\n");

                System.out.println(" rottura in "+hits.get(player.GetID()).getFirst()+" "+hits.get(player.GetID()).getSecond());
                player.setState(new HandleDestruction());
                System.out.println("Stato del player "+ player.getPlayerState().getClass().getName());
                return;

            }
        }
        this.SuccessfulDefences++;
        this.updateSates();
    }
    @Override
    public void finishCard() {
        ConcurrentCardListener concurrentCardListener = this.getConcurrentCardListener();
        concurrentCardListener.onConcurrentCard(false);

        GameBoard Board=this.getBoard();
        ArrayList<Player> PlayerList = Board.getPlayers();
        for(int i=0; i<PlayerList.size(); i++){
            PlayerList.get(i).setState(new BaseState());

        }
        System.out.println("card finished\n");
        this.setFinished(true);
    }

    public void setMeteoritesOrder(int meteoritesOrder) {
        MeteoritesOrder = meteoritesOrder;
    }

    public void setPlayerOrder(int playerOrder) {
        PlayerOrder = playerOrder;
    }

    public IntegerPair getHit(Player p) {
        return hits.get(p.GetID());
    }

    public void setHit(int x, int y, Player p) {
        this.hits.get(p.GetID()).setValue(x,y);
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getMeteoritesOrder() {
        return MeteoritesOrder;
    }

    @Override
    public void setConcurrentCardListener(ConcurrentCardListener listener){
       // ConcurrentCardListener concurrentCardListener = this.getConcurrentCardListener();
//         ConcurrentCardListener concurrentCardListener1 =this.getConcurrentCardListener() ;
//               concurrentCardListener1  = listener;
        concurrentCardListener = listener;

        this.getConcurrentCardListener().onConcurrentCard(true);
    }



        //json required
    public Meteorites() {}
    public ArrayList<Integer> getAttacks() {return attacks;}
    @JsonCreator
    public void setAttacks(ArrayList<Integer> attacks) {this.attacks = attacks;}
}
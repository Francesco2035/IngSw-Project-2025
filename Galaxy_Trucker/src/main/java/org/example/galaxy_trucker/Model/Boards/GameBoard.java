package org.example.galaxy_trucker.Model.Boards;



import org.example.galaxy_trucker.Controller.Listeners.GameBoardListener;
import org.example.galaxy_trucker.Controller.Messages.GameBoardEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Cards.CardStacks;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BuildingShip;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.Tiles.TileSets;

import java.lang.*;
import java.util.ArrayList;
import java.util.Comparator;

//TODO: vincolo player non può scegliere posizione nella gameboard se pos > numPlayer
// dovrebbe essere stato fatto
public class GameBoard {

    // questo arrayList tiene conto della posizione effettiva nel Game
    private ArrayList<Player_IntegerPair> players;
    private ArrayList<Player_IntegerPair> scoreboard;
    private Player[] positions;
    private int nPositions;
    private int[] startPos;
    private TileSets tileSets;
    private Hourglass hourglass;
    private int GameLv;
    private int PlayersOnBoard;
    private CardStacks CardStack;
    private Card CurrentCard;


    private ArrayList<GameBoardListener> listeners = new ArrayList<>();



    public GameBoard(TileSets list, int lv, CardStacks stack) {
        this.players = new ArrayList<>();
        GameLv = lv;
        tileSets = list;
        startPos = new int[4];
        PlayersOnBoard = 0;


        if(lv == 2) {
            nPositions = 24;
            startPos[0] = 6;
            startPos[1] = 3;

        }
        else {
            nPositions = 18;
            startPos[0] = 4;
            startPos[1] = 2;
        }

        startPos[2] = 1;
        startPos[3] = 0;

        positions = new Player[nPositions];
        hourglass = new Hourglass(GameLv);
        this.CardStack = stack;
    }


    /**
     * adds a new player to the game
     * @param NewPlayer reference to the newborn player
     */
    public void addPlayer(Player NewPlayer){
        NewPlayer.setBoards(this);
        Player_IntegerPair NewPair = new Player_IntegerPair(NewPlayer, -1);
        this.players.add(NewPair);
//        NewPlayer.setState(new BuildingShip());
    }


    public void callHourglass(Player player) throws RuntimeException{
        if(hourglass.getUsages() == 1 && !player.GetReady())
            throw new RuntimeException("You need to finish your ship before using the hourglass");
        else try{
            StartHourglass();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    public void StartHourglass() throws RuntimeException{
            System.out.println("Gameboard attempting  to call start hourglass");
        if(this.GameLv == 1){
            throw new IllegalStateException("Cannot use Hourglass in a level 1 game!");
        }
        else if(!hourglass.isStartable())
            throw new  IllegalStateException("Hourglass is already running.");
        else if(hourglass.getUsages() <= 0)
            throw new RuntimeException("No Hourglass usages left.");
        else{
            hourglass.setLock();
            hourglass.startHourglass();
        }
    }


    public void removePlayer(Player DeadMan){

        Player_IntegerPair eliminated = players.stream()
                .filter(p -> DeadMan.equals( p.getKey()))
                .findFirst().orElseThrow();

        players.remove(eliminated);
    }


    /**
     * sets the starting position of a player on the common board:
     * the position players[0] corresponds to the starting position of the 1st player
     * the leader (1st player) will be in the first position of the arraylist
     * @param pl reference to the player
     */
    public void SetStartingPosition(Player pl){

        Player_IntegerPair cur = players.stream()
                .filter(p -> pl.equals( p.getKey()) )
                .findFirst().orElseThrow();

        SetNewPosition(cur, startPos[PlayersOnBoard], startPos[PlayersOnBoard]);

        PlayersOnBoard++;
    }


    public void SetStartingPosition (Player pl, int index) throws IllegalArgumentException{

        Player_IntegerPair cur = players.stream()
                .filter(p -> pl.equals( p.getKey()) )
                .findFirst().orElseThrow();

        if(index > players.size())
            throw new IllegalArgumentException("Cell not available!");

        if(positions[startPos[index-1]] == null) {
            SetNewPosition(cur, startPos[index-1], startPos[index-1]);
            sendUpdates(new GameBoardEvent(startPos[index-1], pl.GetID()));
            PlayersOnBoard++;
        }
        else throw new IllegalArgumentException("This cell is already taken!");
    }


    
    public void removePlayerAndShift(Player pl) throws  RuntimeException{
        sendUpdates(new GameBoardEvent(-1, pl.GetID()));
        int[] shiftedPositions = new int[players.size()];

        Player_IntegerPair cur = players.stream()
                .filter(p -> pl.equals(p.getKey()) )
                .findFirst().orElseThrow();



        if(cur.equals(players.getLast())) {
            positions[cur.getValue()] = null;
            cur.setValue(-1);
        }
        else{
            int i=0;
            for(Player_IntegerPair p : players){
                if(p.getValue() >=0){
                    shiftedPositions[i] = p.getValue();
                    positions[shiftedPositions[i]] = null;
                    i++;
                }
            }

            cur.setValue(-1);

            i=0;
            for(Player_IntegerPair p : players)
                if(p.getValue() >=0){
                    p.setValue(shiftedPositions[i]);
                    positions[shiftedPositions[i]] = p.getKey();
                    i++;
                }
            updateAllPosition();



            for(Player_IntegerPair p : players)
                if(p.getValue() >=0)
                    sendUpdates(new GameBoardEvent(p.getValue(), pl.GetID()));


        }



    }


    public void updateAllPosition(){
        for(Player_IntegerPair p : players)
            if(p.getValue() >=0)
                sendUpdates(new GameBoardEvent(p.getValue(), p.getKey().GetID()));
    }



    /**
     * moves the player forward or backwards on the board of a selected number of steps
     *
     * @param ID of the player to move
     * @param nSteps to run (negative if the player is moving backwards)
     * @throws IllegalArgumentException if the number of steps is 0
     */
    public void movePlayer(String ID, int nSteps) throws IllegalArgumentException{

        int NewIndex;

        Player_IntegerPair cur = players.stream()
                                        .filter(p -> ID.equals( p.getKey().GetID() ) )
                                        .findFirst()
                                        .orElseThrow();

        int NewPos = cur.getValue();
        int i = nSteps;
        if(NewPos < 0) NewIndex = nPositions - (-NewPos % nPositions);
        else NewIndex = NewPos;

        if(nSteps == 0) throw new IllegalArgumentException("Number of steps cannot be 0: must move forward or backwards");

        else if(nSteps > 0) {
            while (i > 0) {
                NewPos++;
                NewIndex++;
                if (positions[NewIndex % nPositions] == null) i--;
                else if (cur.getKey().equals(players.getFirst().getKey()) && players.getFirst().getValue() + nSteps - nPositions >= players.getLast().getValue()) {
                    abandonRace(players.getLast().getKey());
                }
            }

        }
        else while(i < 0){
            NewPos--;
            if(NewPos < 0) NewIndex = nPositions - (-NewPos % nPositions);
            else NewIndex = NewPos % nPositions;

            if(positions[NewIndex % nPositions] == null) i++;
            else if(cur.getKey().equals(players.getLast().getKey()) && players.getLast().getValue() + nSteps +nPositions <= players.getFirst().getValue()){
                abandonRace(players.getLast().getKey());
            }
        }

        if(NewPos < 0) NewIndex = nPositions + NewPos;
        else NewIndex = NewPos % nPositions;

        SetNewPosition(cur, NewPos, NewIndex);
    }


    /**
     * support method: brings a player on a specified position on the board and reorders the leadboard
     * @param cur pair of: player to move and relative score (number of steps taken so far)
     * @param newPosition new score that the player will have once moved
     * @param NewIndex target index of the array to move the player on
     */
    private void SetNewPosition(Player_IntegerPair cur, int newPosition, int NewIndex){

        int CurIndex = players.indexOf(cur);
        int OldIndex = cur.getValue();

        if(OldIndex < 0 ) OldIndex = nPositions - (-OldIndex % nPositions);
        else OldIndex = OldIndex % nPositions;

        Player_IntegerPair NewPair = new Player_IntegerPair(cur.getKey(), newPosition);

        positions[OldIndex] = null;
        positions[NewIndex] = NewPair.getKey();

        players.remove(CurIndex);
        players.add(CurIndex, NewPair);


        players.sort(Comparator.comparing(Player_IntegerPair::getValue));
        //revers the order of the arraylist to have the leader in the 1st position (index = 0)
        ArrayList<Player_IntegerPair> OrderedPlayers = new ArrayList<>();
        for(int i= players.size()-1; i>=0; i--){
            OrderedPlayers.add(players.get(i));
        }
        players = OrderedPlayers;


        //listener
        sendUpdates(new GameBoardEvent(NewIndex, cur.getKey().GetID()));

    }



    public Card NewCard() throws InterruptedException {
        CurrentCard = CardStack.PickNewCard();

        for(Player_IntegerPair p : players){
            p.getKey().setCard(CurrentCard);
        }

        CurrentCard.setBoard(this);
        CurrentCard.CardEffect();
        System.out.println("Id Card: " +CurrentCard.getId() + " "+ CurrentCard.getClass().getName());
        return CurrentCard;
    }



    public ArrayList<Player> getPlayers(){
        ArrayList<Player> PlayersCopy = new ArrayList<>();

        for (Player_IntegerPair player : players) {
            PlayersCopy.add(player.getKey());
        }

        return PlayersCopy;
    }


    public int getLevel(){return GameLv;}
    public Player[] getPositions(){return this.positions;}
    public TileSets getTilesSets(){return tileSets;}
    public CardStacks getCardStack(){return this.CardStack;}

    public Hourglass getHourglass() {return hourglass;}

    public void abandonRace(Player loser){

        Player_IntegerPair pair = players.stream()
                                         .filter(p -> p.getKey().equals(loser))
                                         .findFirst()
                                         .orElseThrow();

        positions[pair.getValue() % nPositions] = null;
        Player playah = pair.getKey();
        int finalScore = playah.finishRace(false);
        //questo mi ritorna l'intero direi che posso salvarmelo in una qualche classifioca i guess
        // todo aggiungere una classifica?


        //--> ho fatto metodo finishGame per mettere in classifica anche quelli che vincono alla fine
        //-palu
        scoreboard.add(new Player_IntegerPair(playah, finalScore));

        players.remove(pair);
    }

    public void finishGame(){
        int score;
        for(Player_IntegerPair p : players){
            score = p.getKey().finishRace(true);
            scoreboard.add(new Player_IntegerPair(p.getKey(), score));
        }
    }


    public void sendUpdates(GameBoardEvent event){
        if(listeners != null) {
            for (GameBoardListener listener :listeners) {
                listener.gameBoardChanged(event);

            }
        }
    }

    public void setListeners(GameBoardListener listener) {
        listeners.add(listener);
    }


}
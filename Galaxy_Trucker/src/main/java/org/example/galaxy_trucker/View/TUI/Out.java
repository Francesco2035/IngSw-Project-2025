package org.example.galaxy_trucker.View.TUI;

import org.example.galaxy_trucker.Commands.InputReader;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.View.ClientModel.PlayerClient;
import org.example.galaxy_trucker.View.ViewPhase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Out {

    private int CardId = -1;
    private PlayerClient playerClient;
    private Boolean show = true;


    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final AtomicBoolean updateScheduled = new AtomicBoolean(false);

    private String CacheCard = "";
    private ArrayList<String> players = new ArrayList<>();
    private ArrayList<Boolean> ready = new ArrayList<>();


    private final Object lock = new Object();
    private final HashMap<Integer, String> idToNameMap = new HashMap<>();
    private final int contentWidth = 33;
    private String[][][] cachedBoard;
    private String[] cacheHand = null;
    private final String border = "+---------------------------------+";
    private ArrayList<Integer> uncoveredTilesId = new ArrayList<>(); //ordine, quindi contiene gli ID in ordine di come arrivano
    private HashMap<Integer, String[]> uncoverdTileSetCache = new HashMap();
    private HashMap<Integer, String> CardsDescriptions = new HashMap<>();
    private String[][][] Gameboard;
    private int lv;
    private int setup = 102;
    private boolean fase = false;
    private int CoveredTileSet = -1;
    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private InputReader inputReader;
    private Thread inputThread;
    private Boolean connected = false;
    private HashMap<Integer, IntegerPair> positionToGameboard = new HashMap<>();
    private HashMap<String,Integer > PlayerToPosition = new HashMap<>();
    private HashMap<String, String[]> lobby = new HashMap<>();
    private ViewPhase phase;

    public Out(InputReader inputReader, PlayerClient playerClient) {
        this.inputReader = inputReader;
        this.lobby = new HashMap<>();
        cachedBoard = new String[10][10][7];
        for (int k = 0; k < 10; k++) {
            for (int l = 0; l < 10; l++) {
                cachedBoard[k][l] = new String[7];
                for (int m = 0; m < 7; m++) {
                    cachedBoard[k][l][m] = "";
                }
            }
        }
        cacheHand = new String[7];
        for (int i = 0; i < 7; i++) {
            cacheHand[i] = "";
        }
        this.playerClient = playerClient;
    }


    public Object getLock(){
        return lock;
    }

    public HashMap<String, Integer> getPlayerToPosition() {
        return PlayerToPosition;
    }

    public void setCardId(int cardId) {
        CardId = cardId;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public void setReady(ArrayList<Boolean> ready) {
        this.ready = ready;
    }

    public void setCachedBoard(int x, int y, String[] cell) {
        for (int i = 0; i < 7; i++) {
//            if (cell[i].equals("")){
//                System.out.println("oh no");
//            }
//            System.out.println("cell"+ cell[i]);
        }
        cachedBoard[x][y] = cell;
    }

    public void setCachedBoard(int x, int y,int k, String cell) {
        cachedBoard[x][y][k] = cell;
    }

    public void setCacheHand(String[] cacheHand) {
        this.cacheHand = cacheHand;
    }


    public void setUncoveredTilesId(int id) {
        if (uncoveredTilesId.contains(id)) {
            uncoveredTilesId.remove(id);
        }
        else{
            uncoveredTilesId.add(id);
        }
    }

    public void setUncoverdTileSetCache(int i, String[] cache) {
        //if (!uncoverdTileSetCache.containsKey(i)) {
            uncoverdTileSetCache.put(i, cache);
        //}
//        else{
//            this.uncoverdTileSetCache.remove(i);
//        }

    }

    public void setCardsDescriptions(HashMap<Integer, String> cardsDescriptions) {
        CardsDescriptions = cardsDescriptions;
    }

    public void setGameboard(int x, int y, String[] cell) {
        Gameboard[x][y] = cell;
    }

    public void setGameboard(int x, int y, int k, String cell) {
        Gameboard[x][y][k] = cell;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public void setSetup(int setup) {
        this.setup = setup;
    }

    public void setFase(boolean fase) {
        this.fase = fase;
    }

    public void setCoveredTileSet(int coveredTileSet) {
        CoveredTileSet = coveredTileSet;
    }

    public void setInputReader(InputReader inputReader) {
        this.inputReader = inputReader;
    }

    public void setInputThread(Thread inputThread) {
        this.inputThread = inputThread;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public void setPositionToGameboard(int i, IntegerPair pair) {
        positionToGameboard.put(i, pair);
    }

    public void setPlayerToPosition(HashMap<String, Integer> playerToPosition) {
        PlayerToPosition = playerToPosition;
    }

    public void setLobby(String gameid, String[]cell) {
        lobby.put(gameid,cell);
        //TODO: ricordarsi di rimuovere il primo se gameid è quello che è
    }

    public void setPhase(ViewPhase phase) {
        this.phase = phase;
    }

    public String[][][] getPlayerBoard() {
        return cachedBoard;
    }







    public StringBuilder showLobby(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (String[] game : lobby.values()) {
                sb.append(game[i] + "   ");
            }
            sb.append(String.valueOf(sb)+"\n");

        }
        sb.append("\n\n");
        return sb;

    }


    public StringBuilder printBoard() {
        StringBuilder toPrint = new StringBuilder();
        int rows = 10;
        int cols = 10;
        toPrint.append("\n############################ BOARD ############################\n");
        toPrint.append("\n\n");
        //System.out.println("############################ BOARD ############################\n");
        for (int y = 0; y < rows; y++) {


            String[][] formattedRow = new String[cols][];
            for (int x = 0; x < cols; x++) {
                formattedRow[x] = cachedBoard[y][x];
            }

            for (int i = 0; i < 7; i++) {
                for (int x = 0; x < cols; x++) {
                    if (y > 2 && x > 1){
                        //System.out.print(formattedRow[x][i]);
                        toPrint.append(formattedRow[x][i]);
                        //System.out.println(formattedRow[x][i]);
                    }
                }
                if (y > 2 ){
                    toPrint.append("\n");
                    //System.out.println();
                }

            }
        }

        //System.out.println();
        //toPrint.append("\n");

        //System.out.println("\n############################ ##### ############################");
        toPrint.append("\n############################ ##### ############################\n");
        return toPrint;
    }



    public StringBuilder showUncoveredTiles() {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("\n############################ UNCOVERED TILES ############################\n");

        StringBuilder line = new StringBuilder();
        StringBuilder topLine = new StringBuilder();
        for (int j = 0; j < uncoveredTilesId.size(); j++) {
            topLine.append("Position ").append(j).append("                          ");
        }
        line.append("\n");

        for (int i = 0; i < 7; i++){
            for (Integer id : uncoveredTilesId) {
                line.append(uncoverdTileSetCache.get(id)[i]).append(" ");
            }
            line.append("\n");

        }
        toPrint.append(String.valueOf(topLine));
        toPrint.append(String.valueOf(line));
        toPrint.append("\n############################ ############## ############################\n");
        return toPrint;

    }



    public StringBuilder printHand(){
        StringBuilder toPrint = new StringBuilder();
        //inputReader.clearScreen();
        
        toPrint.append("\n\n############################ HAND ############################\n\n");
        for (String l : cacheHand) toPrint.append("\n"+l);
        toPrint.append("\n"+border + "\n");
        toPrint.append("\n############################ #### ############################\n");
        return toPrint;
    }


    public  StringBuilder printGameboard(){
        //inputReader.clearScreen();
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("\n\n\n########################## GameBoard #########################\n\n");
        //toPrint.append("\n\n");
        //toPrint.append("########################## GameBoard #########################\n");
        if (lv == 2){
            for (int i = 0; i < 6; i++) {

                for (int k = 0; k < 7; k++) {
                    for (int j = 0; j < 12; j++) {
                        toPrint.append(Gameboard[i][j][k]);
                    }
                    toPrint.append("\n");
                }

            }

        }
        else{

        }
        //toPrint.append("\n");
        toPrint.append("\n\n########################## ######### #########################\n\n");
        return toPrint;


    }


    public StringBuilder showPlayers() {
        StringBuilder line = new StringBuilder();
                int i = 0;
                line.append("\n\n");
                while(i < players.size()){
                    line.append(players.get(i) + " : ");
                    if (ready.get(i)){
                        line.append("Ready");
                    }
                    else{
                        line.append("Not Ready");
                    }
                    i++;
                    line.append("          ");
                }
                //toPrint.append("##################   WELCOME   ##################\n\n");
        return line;
    }




    public void showGame(){
        show  = false;
        //inputReader.clearScreen();
        this.playerClient.showGame(this);
        show = true;

    }


    public HashMap<Integer, IntegerPair> getPositionToGameboard() {
        return positionToGameboard;
    }

    public void initGameBoard(int lv){
        this.lv = lv;
        if (lv == 2){
            Gameboard = new String[6][12][7];
            positionToGameboard.put(-1, new IntegerPair(-1,-1));
            positionToGameboard.put(0, new IntegerPair(0,2));
            positionToGameboard.put(1, new IntegerPair(0,3));
            positionToGameboard.put(2, new IntegerPair(0,4));
            positionToGameboard.put(3, new IntegerPair(0,5));
            positionToGameboard.put(4, new IntegerPair(0,6));
            positionToGameboard.put(5, new IntegerPair(0,7));
            positionToGameboard.put(6, new IntegerPair(0,8));
            positionToGameboard.put(7, new IntegerPair(0,9));
            positionToGameboard.put(8, new IntegerPair(1,10));
            positionToGameboard.put(9, new IntegerPair(2,11));
            positionToGameboard.put(10, new IntegerPair(3,11));
            positionToGameboard.put(11, new IntegerPair(4,10));
            positionToGameboard.put(12, new IntegerPair(5,9));
            positionToGameboard.put(13, new IntegerPair(5,8));
            positionToGameboard.put(14, new IntegerPair(5,7));
            positionToGameboard.put(15, new IntegerPair(5,6));
            positionToGameboard.put(16, new IntegerPair(5,5));
            positionToGameboard.put(17, new IntegerPair(5,4));
            positionToGameboard.put(18, new IntegerPair(5,3));
            positionToGameboard.put(19, new IntegerPair(5,2));
            positionToGameboard.put(20, new IntegerPair(4,1));
            positionToGameboard.put(21, new IntegerPair(3,0));
            positionToGameboard.put(22, new IntegerPair(2,0));
            positionToGameboard.put(23, new IntegerPair(1,1));
        }
        else if (lv == 1){
            Gameboard = new String[10][5][7];
        }
    }



    private void printTilesSet(){
        showUncoveredTiles();
        //toPrint.append("\n");
    }


    public void setCacheCard(String s){
        CacheCard = s;
        //printMessage(s);
    }

    public StringBuilder showCovered(){
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("\n\n############################ COVERED TILES SET ############################\n\n");
        toPrint.append("\n CoveredTileSet size: "+ CoveredTileSet);
        return toPrint;
    }

    public StringBuilder showCard(){
        StringBuilder toPrint = new StringBuilder();
        if (!CacheCard.equals("")){
            toPrint.append("\n\n"+ CacheCard+ "\n\n");
        }
        else{
            toPrint.append("empty bro");
        }
        
        return toPrint;
    }

    public void printMessage(String s){
        inputReader.printGraphicMessage(s);
    }


    public void render(StringBuilder sb){
        inputReader.renderScreen(sb);
    }





}

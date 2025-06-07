package org.example.galaxy_trucker.View.TUI;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.View.ClientModel.PlayerClient;
import org.example.galaxy_trucker.View.ViewPhase;
import org.jline.jansi.Ansi;

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

    String PBInfo = "";
    String exception = "";
    String effect = "";
    String titleCard = "";


    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final AtomicBoolean updateScheduled = new AtomicBoolean(false);

    private String CacheCard = "";
    private ArrayList<String> players = new ArrayList<>();
    private ArrayList<Boolean> ready = new ArrayList<>();


    private final Object lock = new Object();
    private final HashMap<Integer, String> idToNameMap = new HashMap<>();
    private final HashMap<String,  String[][][]> otherPlayersBoard  = new HashMap<>();
    private final int contentWidth = 33;
    private String[][][] cachedBoard;
    private String[] cacheHand = null;
    private final String border = "+━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━+";
    private ArrayList<Integer> uncoveredTilesId = new ArrayList<>(); //ordine, quindi contiene gli ID in ordine di come arrivano
    private HashMap<Integer, String[]> uncoverdTileSetCache = new HashMap();
    private HashMap<Integer, String> CardsDescriptions = new HashMap<>();
    private String[][][] Gameboard;
    private StringBuilder Rewards;
    private int lv;
    private int setup = 102;
    private boolean fase = false;
    private int CoveredTileSet = 152;
    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private InputReader inputReader;
    private Thread inputThread;
    private Boolean connected = false;
    private HashMap<Integer, IntegerPair> positionToGameboard = new HashMap<>();
    private HashMap<String,Integer > PlayerToPosition = new HashMap<>();
    private HashMap<String, String[]> lobby = new HashMap<>();
    private ViewPhase phase;



    public void setOthersPB(String playerId, int x, int y, String[] cell){
        if (!otherPlayersBoard.containsKey(playerId)){
            otherPlayersBoard.put(playerId, new String[10][10][7]);
            for (int i = 0; i < 10; i++){
                for (int j = 0; j < 10; j++){
                    otherPlayersBoard.get(playerId)[i][j] = new String[7];
                    for (int m = 0; m < 7; m++) {
                        otherPlayersBoard.get(playerId)[i][j][m] = "";
                    }
                }
            }
        }
        otherPlayersBoard.get(playerId)[x][y] = cell;
    }

    public void setOthersPB(String playerId, int x, int y,int k, String s){
        if (!otherPlayersBoard.containsKey(playerId)){
            otherPlayersBoard.put(playerId, new String[10][10][7]);
            for (int i = 0; i < 10; i++){
                for (int j = 0; j < 10; j++){
                    otherPlayersBoard.get(playerId)[i][j] = new String[7];
                    for (int m = 0; m < 7; m++) {
                        otherPlayersBoard.get(playerId)[i][j][m] = "";
                    }
                }
            }
        }
        otherPlayersBoard.get(playerId)[x][y][k] = s;
    }

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
        switch (cardId){
            case 1, 2: {
                titleCard = ASCII_ART.TitleSlavers;
                break;
            }
            case 3, 4: {
                titleCard = ASCII_ART.TitleSmugglers;
                break;
            }
            case 5, 6: {
                titleCard = ASCII_ART.TitlePirates;
                break;
            }
            case 7, 8,9,10: {
                titleCard = ASCII_ART.TitleAbandonedShip;
                break;
            }
            case 11, 12,13,14: {
                titleCard = ASCII_ART.TitleAbandonedStation;
                break;
            }
            case 15,16,17,18,19,20: {
                titleCard = ASCII_ART.TitleMeteorSwarm;
                break;
            }
            case 21,22,23,24,25,26,27,28: {
                titleCard = ASCII_ART.TitlePlanets;
                break;
            }

            case 29,30,31,32,33,34,35: {
                titleCard = ASCII_ART.TitleOpenSpace;
                break;
            }

            case 36, 37: {
                titleCard = ASCII_ART.TitleCombatZone;
                break;
            }
            case 38, 39: {
                titleCard = ASCII_ART.TitleStardust;
                break;
            }
            case 40: {
                titleCard = ASCII_ART.TitleEpidemic;
                break;
            }

        }
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public void setReady(ArrayList<Boolean> ready) {
        this.ready = ready;
    }

    public void setCachedBoard(int x, int y, String[] cell) {
        cachedBoard[x][y] = cell;
    }

    public void setCachedBoard(int x, int y,int k, String cell) {
        cachedBoard[x][y][k] = cell;
    }

    public void setCacheHand(String[] cacheHand) {
        this.cacheHand = cacheHand;
    }


    public void setUncoveredTilesId(int id) {
        if (uncoveredTilesId.contains((Integer)id)) {
            uncoveredTilesId.remove(Integer.valueOf(id));
        }
        else{
            uncoveredTilesId.add(id);
        }

    }

    public void setUncoverdTileSetCache(int i, String[] cache) {
        if (cache != null) {
            uncoverdTileSetCache.put((Integer)i, cache);
        }
        else{

            this.uncoverdTileSetCache.remove((Integer) i);
        }

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
    }

    public void setPhase(ViewPhase phase) {
        this.phase = phase;
    }

    public String[][][] getPlayerBoard() {
        return cachedBoard;
    }







    public StringBuilder showLobby(){
        StringBuilder sb = new StringBuilder();

        int last = 0;
        int begin = 0;

        sb.append("\n\n");
        if (lobby.isEmpty()){
            sb.append(ASCII_ART.noGame);
        }

        else{

            int index = 0;
            while (index < lobby.size()) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 8 && index + j < lobby.size(); j++) {
                        sb.append(lobby.get(lobby.keySet().stream().toList().get(index + j))[i]).append("   ");
                    }
                    sb.append("\n");
                }
                sb.append("\n\n\n");
                index += 8;
            }


        }

        sb.append("\n\n");


        return sb;

    }


    public StringBuilder printBoard() {
        StringBuilder toPrint = new StringBuilder();
        int rows = 10;
        int cols = 10;
        toPrint.append("\n"+ASCII_ART.Board+"\n");
        toPrint.append("\n\n");
        for (int y = 0; y < rows; y++) {


            String[][] formattedRow = new String[cols][];
            for (int x = 0; x < cols; x++) {
                formattedRow[x] = cachedBoard[y][x];
            }

            for (int i = 0; i < 7; i++) {
                for (int x = 0; x < cols; x++) {
                    if (y > 2 && x > 1){
                        toPrint.append(formattedRow[x][i]);
                    }
                }
                if (y > 2 ){
                    toPrint.append("\n");
                }

            }
        }

        toPrint.append(ASCII_ART.Border);
        return toPrint;
    }


    public StringBuilder printBoard(String[][][] board) {
        StringBuilder toPrint = new StringBuilder();
        int rows = 10;
        int cols = 10;

        toPrint.append("\n\n");
        for (int y = 0; y < rows; y++) {


            String[][] formattedRow = new String[cols][];
            for (int x = 0; x < cols; x++) {
                formattedRow[x] = board[y][x];
            }

            for (int i = 0; i < 7; i++) {
                for (int x = 0; x < cols; x++) {
                    if (y > 2 && x > 1){
                        toPrint.append(formattedRow[x][i]);
                    }
                }
                if (y > 2 ){
                    toPrint.append("\n");
                }

            }
        }

        toPrint.append(ASCII_ART.Border);
        return toPrint;
    }



    public StringBuilder showUncoveredTiles() {


        StringBuilder toPrint = new StringBuilder();
        toPrint.append(ASCII_ART.UncoveredTiles);

        int index = 0;
        while (index < uncoveredTilesId.size()) {
            StringBuilder topLine = new StringBuilder();

            for (int j = 0; j < 8 && index + j < uncoveredTilesId.size(); j++) {
                topLine.append("Position ").append(index + j).append("                          ");
            }
            toPrint.append(topLine).append("\n");


            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 8 && index + j < uncoveredTilesId.size(); j++) {
                    int id = uncoveredTilesId.get(index + j);
                    toPrint.append(uncoverdTileSetCache.get(id)[i]).append(" ");
                }
                toPrint.append("\n");
            }
            toPrint.append("\n\n\n");
            index += 8;
        }

        toPrint.append(ASCII_ART.Border);
        return toPrint;


    }



    public StringBuilder printHand(){
        StringBuilder toPrint = new StringBuilder();
        //inputReader.clearScreen();
        
        toPrint.append(ASCII_ART.Hand);
        for (String l : cacheHand) toPrint.append("\n"+l);
        toPrint.append("\n"+border + "\n");
        toPrint.append(ASCII_ART.Border);
        return toPrint;
    }


    public  StringBuilder printGameboard(){

        StringBuilder toPrint = new StringBuilder();
        toPrint.append(ASCII_ART.GameBoard);

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
            for (int i = 0; i < 5; i++) {

                for (int k = 0; k < 7; k++) {
                    for (int j = 0; j < 11; j++) {
                        toPrint.append(Gameboard[i][j][k]);
                    }
                    toPrint.append("\n");
                }

            }
        }

        toPrint.append(ASCII_ART.Border);
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
        return line;
    }




    public void showGame(){
        show  = false;
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
            Gameboard = new String[5][11][7];
            positionToGameboard.put(-1, new IntegerPair(-1,-1));
            positionToGameboard.put(0, new IntegerPair(0,3));
            positionToGameboard.put(1, new IntegerPair(0,4));
            positionToGameboard.put(2, new IntegerPair(0,5));
            positionToGameboard.put(3, new IntegerPair(0,6));
            positionToGameboard.put(4, new IntegerPair(0,7));
            positionToGameboard.put(5, new IntegerPair(0,8));
            positionToGameboard.put(6, new IntegerPair(1,9));
            positionToGameboard.put(7, new IntegerPair(2,10));
            positionToGameboard.put(8, new IntegerPair(3,9));
            positionToGameboard.put(9, new IntegerPair(4,8));
            positionToGameboard.put(10, new IntegerPair(4,7));
            positionToGameboard.put(11, new IntegerPair(4,6));
            positionToGameboard.put(12, new IntegerPair(4,5));
            positionToGameboard.put(13, new IntegerPair(4,4));
            positionToGameboard.put(14, new IntegerPair(4,3));
            positionToGameboard.put(15, new IntegerPair(3,2));
            positionToGameboard.put(16, new IntegerPair(2,1));
            positionToGameboard.put(17, new IntegerPair(1,0));

        }
    }



    private void printTilesSet(){
        showUncoveredTiles();
    }


    public void setCacheCard(String s){
        CacheCard = s;
    }

    public void setRewards(StringBuilder rewards){
        Rewards = rewards;
    }

    public StringBuilder showCovered(){
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(ASCII_ART.CoveredTiles);
        toPrint.append("\n CoveredTileSet size: "+ CoveredTileSet);
        return toPrint;
    }

    public StringBuilder showCard(){
        StringBuilder toPrint = new StringBuilder();

        if (!CacheCard.equals("")){
            toPrint.append("\n\n");
            toPrint.append(ASCII_ART.Card);
            toPrint.append("\n\n");
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


    public StringBuilder showRewards() {
        return Rewards;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public StringBuilder showException() {
        StringBuilder sb = new StringBuilder();
        if (!exception.equals("")){
            sb.append(Ansi.ansi().fgRed().a("[ " + exception + " ]").reset());
            sb.append("\n\n");
            exception = "";
        }
        return sb;
    }

    public StringBuilder printSystemException(String s){
        StringBuilder sb = new StringBuilder();


        return sb;
    }

    public void seeBoards() {
        StringBuilder sb = new StringBuilder();


        for (String player : otherPlayersBoard.keySet()){
            sb.append(ASCII_ART.Border);
            sb.append("\n\nPlayer: " +player);
            sb.append(printBoard(otherPlayersBoard.get(player)));
            sb.append(ASCII_ART.Border);
        }
        sb.append(printBoard());
        inputReader.renderScreen(sb);
    }

    public void setEffectCard(String message) {
        this.effect = message;
    }

    public StringBuilder showCardEffect(){
        StringBuilder sb = new StringBuilder();
        if (!effect.equals("")){
            sb.append(Ansi.ansi().fgYellow().a("[ " + effect + " ]").reset());
            sb.append("\n\n");
            //effect = "";
        }
        return sb;
    }

    public StringBuilder getTitleCard(){
        return new StringBuilder(titleCard);
    }

    public void setPBInfo(String s) {
        PBInfo = s;
    }

    public StringBuilder showPbInfo(){
        return new StringBuilder(PBInfo);
    }
}

package org.example.galaxy_trucker.View.TUI;
//si occupa di ricevere gli eventi, formattarli e inviarli ad out. Chiama la show di out, la show di out chiama il metodo di player che in base allo stato in cui è (ricevendo come parametro this) chiama i metodi giusti

//eccezioni gestite direttamente da TUI
//se disconnesso setta player a stato di disconnessione e chiama
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Commands.InputReader;
import org.example.galaxy_trucker.Controller.Messages.*;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.View.ClientModel.PlayerClient;
import org.example.galaxy_trucker.View.ClientModel.States.LobbyClient;
import org.example.galaxy_trucker.View.View;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.DeckEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.UncoverdTileSetEvent;
import org.example.galaxy_trucker.Model.Connectors.Connectors;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.View.ViewPhase;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;
//TODO: non è ancora gestita la print del lv 1
//TODO: mostrare che si è avviata la building phase
//TODO: salvare lo stesso in virtualview per il momento non sto gestendo f.a. del tutto
//TODO: impostare vincolo lunghezza nome e gameid (anche lato server)
//TODO: stampare games per righe e non in colonna perchè mi da fastidio, oppure farlo su più righe
//TODO: rimozione game se tutti i player quittano oppure se il game è partito
//TODO: mettere le fasi nella TUI in modo tale che venga chiamato solo showTUI (salvando i dati in cache prime) e in base alle varie fasi chiama i metodi giusti

public class TUI implements View {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledTask;
    private final int debounceDelayMs = 200;

    private int CardId = -1;
    private final TileEvent[][] board = new TileEvent[10][10];
    private final HashMap<Integer, String> idToNameMap = new HashMap<>();
    private final int contentWidth = 33;
    private String[][][] cachedBoard;
    private String[] cacheHand = null;
    private final String gamboardBorder = "+-----------------------+";
    private final String border = "+---------------------------------+";
    private ArrayList<Integer> uncoveredTilesId = new ArrayList<>(); //ordine, quindi contiene gli ID in ordine di come arrivano
    private HashMap<Integer, String[]> uncoverdTileSetCache = new HashMap();
    private HashMap<Integer, String> CardsDescriptions = new HashMap<>();
    private String[][][] Gameboard;
    private int lv;
    private int setup = 102;
    private HashMap<Integer, IntegerPair> positionToGameboard = new HashMap<>();
    private HashMap<String,Integer > PlayerToPosition = new HashMap<>();
    private HashMap<String, String[]> lobby = new HashMap<>();
    private ViewPhase phase;
    private Out out;


    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private Thread inputThread;
    private InputReader inputReader;
    private PlayerClient playerClient;



    @Override
    public void setGameboard(int lv) {
        out.initGameBoard(lv);
        this.lv = lv;
        int position = 4;
        if (lv == 2){
            for (int i = 0; i < 6; i++){
                for (int j = 0; j < 12; j++){
                    if ((i == 0 && (j >= 2 && j <= 9)||(i == 1 && (j == 1 || j == 10)))
                            || (i == 2 && (j == 0 || j == 11)||(i == 3 && (j == 0 || j == 11)))
                    || (i == 4 && (j == 1 || j == 10)) || (i == 5 && (j >= 2 && j <= 9))){

                        if (i == 0 && (j == 2 || j == 3 || j == 5 ||j == 8)) {
                            out.setGameboard(i,j,formatPosition(position));
                            position--;
                        }

                        else {
                            out.setGameboard(i,j,formatCell());
                        }
                    }

                    else {

                        out.setGameboard(i,j,emptyGbCell());
                    }
                }
            }
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
        else{
            Gameboard = new String[10][5][7];
        }

    }

    @Override
    public void showLobby(LobbyEvent event) {
        //System.out.println(event.getGameId());
        out.setLobby(event.getGameId(),formatCell(event)); //QUI
        onGameUpdate();
//        if (event.getGameId().equals("EMPTY CREATE NEW GAME")){
//            lobby.remove(event.getGameId());
//        }
    }

    @Override
    public void showLobbyGame(GameLobbyEvent event) {

        out.setPlayers(event.getPlayers());
        out.setReady(event.getReady());

        onGameUpdate();
    }

    @Override
    public void rewardsChanged(RewardsEvent event) {
        out.setRewards(formatRewards(event));
        onGameUpdate();
    }

    @Override
    public void phaseChanged(PhaseEvent event) {
        System.out.println("STATE CHANGED: "+ event.getStateClient().getClass());
        playerClient.setPlayerState(event.getStateClient());

        onGameUpdate();
    }


    public String[] formatCell(LobbyEvent event) {
        String[] cell = new String[8];
        cell[0] = "+"+centerTextAnsi(event.getGameId(),25, "-")+"+";
        cell[1] = "+                         +";
        cell[2] = "+                         +";
        cell[3] = "+                         +";
        cell[4] = "+                         +";
        cell[5] = "+                         +";
        cell[6] = "+                         +";
        cell[7] = "+-------------------------+";
        int k = 1;
        if (!event.getGameId().equals("EMPTY CREATE NEW GAME")){
            //TODO: chiama metodo speciale di out senza salvare la stringa su out se il titolo è questo
            ArrayList<String> players = event.getPlayers();
            cell[7] = "+"+centerTextAnsi("Game level: "+ event.getLv(),25, "-")+"+";
            for (String player : players) {
                cell[2+ k -1] = "+"+centerTextAnsi("p"+k+ ": "+player, 25)+"+";
                k++;
            }
        }

        return cell;
    }

    public TUI() throws IOException {
        playerClient = new PlayerClient();
        playerClient.setPlayerState(new LobbyClient());
        loadComponentNames();
        loadCardsDescriptions();
        cachedBoard = new String[10][10][7];
        cacheHand = new String[7];
        for (int i = 0; i < 7; i++) {
            cacheHand[i] = "";
        }
        inputReader = new InputReader(inputQueue);
        inputThread = new Thread(inputReader);
        inputThread.setDaemon(true); // opzionale, per terminare col processo principale
        inputThread.start();
        phase = ViewPhase.LOBBY;
        inputReader.renderScreen(new StringBuilder(ASCII_ART.Title));
        out = new Out(inputReader, playerClient);
        //inputReader.clearScreen();


    }

    private void loadCardsDescriptions() {
        ObjectMapper mapper = new ObjectMapper();
        //System.out.println( getClass().getClassLoader().getResource("ClientTiles.json"));
        try (InputStream cardsStream = getClass().getClassLoader().getResourceAsStream("ClientCards.json")) {
            if (cardsStream == null) {
                System.err.println("File ClientTiles.JSON non found!");
                return;
            }

            JsonNode root = mapper.readTree(cardsStream);

            root.fields().forEachRemaining(entry -> {
                String description = entry.getValue().get("description").asText();
                int id = Integer.parseInt(entry.getKey());
                CardsDescriptions.put(id, description);
            });

        } catch (IOException e) {
            System.err.println("Error loading names: " + e.getMessage());
        }
    }



    @Override
    public void updateBoard(TileEvent event) {
        if (setup!=0){
            setup--;
        }
        board[event.getX()][event.getY()] = event;
        if (event == null) {
            out.setCachedBoard(event.getX(), event.getY(), emptyCell());
        }
        else if (event.getId() == 158) {
            out.setCachedBoard(event.getX(), event.getY(), emptyCell());
            if (setup == 0 && event.getX() == 3 && event.getY() == 8){
                out.setCachedBoard(3,8,out.getPlayerBoard()[3][9]); //QUI
                out.setCachedBoard(3, 9, emptyCell());
            }
            else if (!(event.getX() == 3 && event.getY() == 8) && (event.getX() < 3 || event.getY() < 3)) {

                for (int k = 0; k < 7; k++) {
                    out.setCachedBoard(event.getX(), event.getY(),k, "");//QUI
                }
            }

        }

        else if (event.getId() == 159) {
            out.setCachedBoard(7, 8, emptyCell());
            out.setCachedBoard(7, 9, emptyCell());
        }


        else {
            out.setCachedBoard(event.getX(), event.getY(), formatCell(event)); //QUI
        }
        if (setup == 0){
            out.setCacheHand(emptyCell());
            onGameUpdate();
        }
    }

    private String[] emptyCell() {
        String[] cellLines = new String[7];

        for (int i = 0; i < 7; i++) {
            cellLines[i] = "                                   ";
        }
        return cellLines;
    }

    private String[] emptyGbCell(){
        String[] cellLines = new String[7];

        for (int i = 0; i < 7; i++) {
            cellLines[i] = "                         ";
        }
        return cellLines;
    }

    @Override
    public void showMessage(String message) {
        //System.out.println(message);
    }

    public String[] formatCell(){
        String[] cellLines = new String[7];

        for (int i = 0; i < 7; i++) {
            cellLines[i] = "|                       |";
        }
        cellLines[0] = gamboardBorder;
        cellLines[6] = gamboardBorder;
        return cellLines;
    }

    public String[] formatPosition(int k){
        String[] cellLines = new String[7];
        for (int i = 2; i < 6; i++) {
            cellLines[i] = "|                       |";
        }
        cellLines[1] = "|-----------"+ k + "-----------|";
        cellLines[0] = gamboardBorder;
        cellLines[6] = gamboardBorder;
        cellLines[5] = "|-----------"+ k + "-----------|";

        return cellLines;
    }

    public String[] formatCell(TileEvent event) {
        String[] cellLines = new String[7];


        if (event == null) {
            for (int i = 0; i < 7; i++) {
                cellLines[i] = "|" + " ".repeat(contentWidth) + "|";
            }
            return cellLines;
        }

        ArrayList<Connectors> conns = event.getConnectors();
        String top    = getConnectorSymbol(conns.get(1));
        String left   = getConnectorSymbol(conns.get(0));
        String right  = getConnectorSymbol(conns.get(2));
        String bottom = getConnectorSymbol(conns.get(3));


        String colored = idToNameMap.getOrDefault(event.getId(), "Unknown");
        String name = colored.replaceAll("\u001B\\[[;\\d]*m", "");
        if(event.getX() == 3 && (event.getY() == 8 || event.getY() == 9)){
            colored = "\u001B[35m" + colored + "\u001B[0m";
        }

        cellLines[2] = "|" + centerTextAnsi(colored, contentWidth) + "|";

        String extra = "";
        switch (name) {
            case "powerCenter", "TriplePowerCenter" -> extra = "B: "+event.getBatteries();
            case "modularHousingUnit", "MainCockpit" -> {
                extra += "H: " +event.getHumans() + " | ";
                extra += "B: "+event.isBrownAlien() + " | ";
                extra += "P: "+event.isPurpleAlien();
            }
            case "storageCompartment", "TripleStorageCompartment", "specialStorageCompartment" -> {
                if (event.getCargo() != null && !event.getCargo().isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (Goods g : event.getCargo()) {
                        switch (g.getValue()) {
                            case 4 -> sb.append("\u001B[31m[]\u001B[0m "); // Rosso
                            case 3 -> sb.append("\u001B[33m[]\u001B[0m "); // Giallo
                            case 2 -> sb.append("\u001B[32m[]\u001B[0m "); // Verde
                            case 1 -> sb.append("\u001B[34m[]\u001B[0m "); // Blu
                        }
                    }
                    extra = sb.toString().trim();
                }
            }

            case "shieldGenerator" -> {
                int rot = event.getRotation() % 360;
                if (rot == 0){
                    top ="\u001B[33m" + top + "\u001B[0m";
                    right ="\u001B[33m" + right + "\u001B[0m";
                }
                else if (rot == 1){
                    right ="\u001B[33m" + right + "\u001B[0m";
                    bottom ="\u001B[33m" + bottom + "\u001B[0m";
                } else if (rot == 2) {
                    bottom ="\u001B[33m" + bottom + "\u001B[0m";
                    left ="\u001B[33m" + left + "\u001B[0m";
                }
                else {
                    left ="\u001B[33m" + left + "\u001B[0m";
                    top ="\u001B[33m" + top + "\u001B[0m";
                }
            }
        }



        String leftPart = "" + event.getId();
        String centeredPart = centerText(leftPart, contentWidth - 8);
        cellLines[3] = "| < "+ left + centeredPart + right + " > |";

        cellLines[4] = "|" + centerText(extra, contentWidth) + "|";

        String position = " " + event.getX() + " : " + event.getY();

        String arrow = "v";

        int availableSpace = contentWidth - (position.length() + arrow.length() + bottom.length());

        int spaceBeforeArrow = availableSpace / 2;
        int spaceAfterArrow = availableSpace - spaceBeforeArrow;

        String finalRow = position + " ".repeat(spaceBeforeArrow -4) + arrow + " "+ bottom + " ".repeat(spaceAfterArrow );
        cellLines[1] = "|" + centerTextAnsi("^ " + top, contentWidth) + "|";

        cellLines[5] = "|" + centerTextAnsi(finalRow, contentWidth) + "|";

        cellLines[6] = border;;
        cellLines[0] = border;

        return cellLines;
    }

    private String centerText(String text, int width) {
        int padding = Math.max(0, (width - text.length()) / 2);
        return " ".repeat(padding) + text + " ".repeat(width - padding - text.length());
    }

    private String getConnectorSymbol(Connectors c) {
        if (c == null) return ".";
        return switch (c.getClass().getSimpleName()) {
            case "NONE"      -> "°";
            case "SINGLE"    -> "S";
            case "DOUBLE"    -> "D";
            case "UNIVERSAL" -> "U";
            case "CANNON"    -> "C";
            case "ENGINE"    -> "M";
            default           -> "?";
        };
    }

    public static String stripAnsi(String s) {
        return s.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    public static String centerTextAnsi(String s, int width) {
        String visible = stripAnsi(s);
        int padding = width - visible.length();
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        return " ".repeat(Math.max(0, padLeft)) + s + " ".repeat(Math.max(0, padRight));
    }

    public static String centerTextAnsi(String s, int width, String c) {
        String visible = stripAnsi(s);
        int padding = width - visible.length();
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        return c.repeat(Math.max(0, padLeft)) + s + c.repeat(Math.max(0, padRight));
    }

    private void loadComponentNames() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //System.out.println( getClass().getClassLoader().getResource("ClientTiles.json"));
        try (InputStream tilesStream = getClass().getClassLoader().getResourceAsStream("ClientTiles.json")) {
            if (tilesStream == null) {
                System.err.println("File ClientTiles.JSON non found!");
                return;
            }

            JsonNode root = mapper.readTree(tilesStream);

            root.fields().forEachRemaining(entry -> {
                String componentType = entry.getValue().get("componentType").asText();
                int id = Integer.parseInt(entry.getKey());
                idToNameMap.put(id, componentType);
            });

        } catch (IOException e) {
            System.err.println("Error loading names: " + e.getMessage());
        }
    }

    public void updateHand(HandEvent event) {
        inputReader.printServerMessage("\n" + border);
        if(event.getId() == 158){
            out.setCacheHand(emptyCell());
        }
        else {
            TileEvent temp = new TileEvent(event.getId(), 0, 0, null, 0, false, false, 0, 0, event.getConnectors());
            out.setCacheHand(formatCell(temp)); //QUI
        }
        onGameUpdate();
    }

    @Override
    public void updateGameboard(GameBoardEvent event) {

        int x = out.getPositionToGameboard().get(event.getPosition()).getFirst();
        int y = out.getPositionToGameboard().get(event.getPosition()).getSecond();

        if(out.getPlayerToPosition().containsKey(event.getPlayerID())){
            int pos = out.getPlayerToPosition().get(event.getPlayerID());
            int x1 = positionToGameboard.get(pos).getFirst();
            int y1 = positionToGameboard.get(pos).getSecond();
            out.setGameboard(x1,y1,3,"|                       |") ;
            out.getPlayerToPosition().remove(event.getPlayerID());
            if(x != -1){
                out.setGameboard(x,y,3,"|"+centerTextAnsi(event.getPlayerID(),23) + "|") ;
                out.getPlayerToPosition().put(event.getPlayerID(), event.getPosition());
            }
        }
        else{
            out.getPlayerToPosition().put(event.getPlayerID(), event.getPosition());
            out.setGameboard(x,y,3,"|"+centerTextAnsi(event.getPlayerID(),23) + "|") ;
        }


        onGameUpdate();

    }

    @Override
    public void updateCoveredTilesSet(CoveredTileSetEvent event) {

        out.setCoveredTileSet(event.getSize()); //QUI
        onGameUpdate();
    }

    @Override
    public void updateUncoveredTilesSet(UncoverdTileSetEvent event) {
        out.setUncoveredTilesId(event.getId()); //QUI
        out.setUncoverdTileSetCache(event.getId(), null);//QUI

        if(event.getConnectors() != null) {
            uncoveredTilesId.add(event.getId());

            String[] cache = formatCell(new TileEvent(event.getId(), 0, 0, null, 0, false, false, 0, 0, event.getConnectors()));

            out.setUncoverdTileSetCache(event.getId(), cache); //QUI
        }
        onGameUpdate();
    }




    //questo come anche qualche altro metodo sarà per gestire le cose eccezionali o comunque chiama un metodo speciale di out
    public void showCard(int id){
        inputReader.printServerMessage("\n");
        inputReader.printServerMessage(CardsDescriptions.get(id));
        //printBoard();
        //System.out.println(CardsDescriptions.get(id));
    }

    @Override
    public void showCard(CardEvent event){

        out.setCardId(event.getId());
        //inputReader.printServerMessage("\n");
        //inputReader.printServerMessage(CardsDescriptions.get(CardId));
        out.setCacheCard(CardsDescriptions.get(event.getId()));
        //printBoard();
        //System.out.println(CardsDescriptions.get(id));
        //onGameUpdate();
    }

    @Override
    public void disconnect() {
//        inputReader.stop();
//        inputThread.interrupt();
//        try {
//            inputThread.join();
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
        inputReader.stop();
        inputReader.printServerMessage("Disconnected cleanly.");
    }

    @Override
    public void connect() throws IOException {
//        inputReader = new InputReader(inputQueue);
//        inputThread = new Thread(inputReader);
//        inputThread.setDaemon(true);
//        inputThread.start();
    }

    @Override
    public String askInput(String message) {
        inputReader.printServerMessage(message);
        try {
            String toSend = inputQueue.take();
            //System.out.println("to send: " + toSend);
            return toSend;
        } catch (InterruptedException e) {
            //Thread.currentThread().interrupt();
            inputReader.printServerMessage("Input interrupted");
            return "";
        }
    }


    @Override
    public void showDeck(DeckEvent deck){
        for (Integer e : deck.getIds()) {
            showCard(e);
        }
    }




    public synchronized void onGameUpdate() {
        if (scheduledTask != null && !scheduledTask.isDone()) {
            scheduledTask.cancel(false);
        }

        scheduledTask = scheduler.schedule(() -> {
            //inputReader.clearScreen();
            out.showGame();
        }, debounceDelayMs, TimeUnit.MILLISECONDS);
    }

//quando termina tutto chiamo questo anche se non credo dovrebbe particolamente servirmi
    public void shutdown() {
        scheduler.shutdown();
    }

    public StringBuilder formatRewards(RewardsEvent event) {
        ArrayList<Goods> goodsList = event.getRewards();
        StringBuilder sb = new StringBuilder();

        int size = goodsList.size();

        sb.append("REWARDS\n");
        int k = 0;
        for (Goods goods : goodsList) {
            sb.append("| (pos: "+k+") ");
            switch (goods.getValue()){
                    case 4 -> sb.append("\u001B[31m[]\u001B[0m"); // Rosso
                    case 3 -> sb.append("\u001B[33m[]\u001B[0m"); // Giallo
                    case 2 -> sb.append("\u001B[32m[]\u001B[0m"); // Verde
                    case 1 -> sb.append("\u001B[34m[]\u001B[0m"); // Blu

            }
            sb.append(" |");
            k++;
        }
        sb.append("\n");

        return sb;
    }



}

//riceve eventi e formatta, aggiorna il "client"
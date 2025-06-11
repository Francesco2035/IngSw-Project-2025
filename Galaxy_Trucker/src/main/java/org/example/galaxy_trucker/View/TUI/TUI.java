package org.example.galaxy_trucker.View.TUI;
//si occupa di ricevere gli eventi, formattarli e inviarli ad out. Chiama la show di out, la show di out chiama il metodo di player che in base allo stato in cui è (ricevendo come parametro this) chiama i metodi giusti

//eccezioni gestite direttamente da TUI
//se disconnesso setta player a stato di disconnessione e chiama
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.Controller.Messages.*;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.*;
import org.example.galaxy_trucker.View.ClientModel.PlayerClient;
import org.example.galaxy_trucker.View.ClientModel.States.*;
import org.example.galaxy_trucker.View.View;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Model.Connectors.Connectors;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.View.ViewPhase;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;
//TODO: salvare lo stesso in virtualview per il momento non sto gestendo f.a. del tutto
//TODO: impostare vincolo lunghezza nome e gameid (anche lato server)
//TODO: rimozione game se tutti i player quittano oppure se il game è partito

public class TUI implements View {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledTask;
    private final int debounceDelayMs = 200;
    private PlayerStateClient lastState;
    private boolean firstUpdate = false;

    private int CardId = -1;
    private final TileEvent[][] board = new TileEvent[10][10];
    private final HashMap<Integer, String> idToNameMap = new HashMap<>();
    private final int contentWidth = 33;
    private String[][][] cachedBoard;
    private String[] cacheHand = null;
    private final String gamboardBorder = "+━━━━━━━━━━━━━━━━━━━━━━━+";
    private final String border = "+━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━+";
    private ArrayList<Integer> uncoveredTilesId = new ArrayList<>(); //ordine, quindi contiene gli ID in ordine di come arrivano
    private HashMap<Integer, String[]> uncoverdTileSetCache = new HashMap();
    private HashMap<Integer, String> CardsDescriptions = new HashMap<>();
    private String[][][] Gameboard;
    private int lv;
    private int setup = 102;

    private HashMap<String, String[]> lobby = new HashMap<>();
    private ViewPhase phase;
    private Out out;


    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private Thread inputThread;
    private InputReader inputReader;
    private PlayerClient playerClient;
    private Client client;
    private final SeeLog seeLog = new SeeLog();
    private final SeeBoardsClient seeBoardsClient = new SeeBoardsClient();
    private  LoginClient loginClient;



    public TUI(){

    }

    public TUI(LoginClient loginClient) throws IOException {

        this.loginClient = loginClient;
        loadComponentNames();
        loadCardsDescriptions();
        cachedBoard = new String[10][10][7];
        cacheHand = new String[7];
        for (int i = 0; i < 7; i++) {
            cacheHand[i] = "";
        }
        inputReader = new InputReader(inputQueue);
        inputThread = new Thread(inputReader);
        inputThread.setDaemon(true);
        inputThread.start();
        //phase = ViewPhase.LOBBY;
        //inputReader.renderScreen(new StringBuilder(ASCII_ART.Title));
        playerClient = new PlayerClient();
        playerClient.setPlayerState(new LoginClient());
        playerClient.setCompleter(inputReader.getCompleter());
        lastState = new LoginClient();
        out = new Out(inputReader, playerClient);
        onGameUpdate();
        //inputReader.clearScreen();

    }



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


        }
        else{

            for (int i = 0; i < 5; i++){
                for (int j = 0; j < 11; j++){
                    if ((i == 0 && (j >= 3 && j <= 8)||(i == 1 && (j == 2 || j == 9)))
                            || (i == 2 && (j == 1 || j == 10)||(i == 3 && (j == 2 || j == 9)))
                            ||  (i == 4 && (j >= 3 && j <= 8))){

                        if (i == 0 && (j == 3 || j == 4 || j == 5 ||j == 7)) {
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
        }

    }

    @Override
    public void showLobby(LobbyEvent event) {
        if (!firstUpdate){
            firstUpdate = true;
            playerClient.setPlayerState(new LobbyClient());
            lastState = new LobbyClient();
        }
        //System.out.println(event.getGameId());
        if (event.getLv() != -1){
            //System.out.println("put "+event.getGameId()+" "+event.getLv());
            try{
                out.setLobby(event.getGameId(),formatCell(event)); //QUI
                client.setGameIdToLV(event.getGameId(), event.getLv());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if (event.getGameId() != null){
            out.setLobby(event.getGameId(), null);
        }
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
        //System.out.println("STATE CHANGED: "+ event.getStateClient().getClass());
        //lastState = null;
        if (event.getStateClient().equals(loginClient)){
            firstUpdate = false;
            out.clearOut();
        }
        lastState = event.getStateClient();
        playerClient.setPlayerState(event.getStateClient());
        playerClient.getCompleter().setCommands(event.getStateClient().getCommands());
        onGameUpdate();
    }

    @Override
    public void exceptionOccurred(ExceptionEvent exceptionEvent) {
        out.setException(exceptionEvent.getException());
        onGameUpdate();
    }

    @Override
    public void updateOthersPB(PlayerTileEvent event) {

         if (event.getId() == 158) {
            out.setOthersPB(event.getPlayerName(),event.getX(), event.getY(), emptyCell());
            if (setup == 0 && event.getX() == 3 && event.getY() == 8){
                out.setOthersPB(event.getPlayerName(),3,8,emptyCell()); //QUI
            }
            else if (!(event.getX() == 3 && event.getY() == 8) && (event.getX() < 3 || event.getY() < 3) && (event.getY() != 2)) {

                for (int k = 0; k < 7; k++) {
                    out.setOthersPB(event.getPlayerName(),event.getX(), event.getY(),k, "");//QUI
                }
            }

        }

        else if (event.getId() == 159) {
             out.setOthersPB(event.getPlayerName(),7, 8, emptyCell());
             out.setOthersPB(event.getPlayerName(),7, 9, emptyCell());
        }


        else {
             out.setOthersPB(event.getPlayerName(),event.getX(), event.getY(), formatCell(event)); //QUI
        }

        if (lastState != null){
            onGameUpdate();
        }


    }

    @Override
    public void seeBoards() {
        if (!(playerClient.getPlayerState().equals(seeBoardsClient) || playerClient.getPlayerState().equals(seeLog))){
            lastState = playerClient.getPlayerState();
        }
        playerClient.setPlayerState(seeBoardsClient);
        onGameUpdate();
    }

    @Override
    public void refresh() {
        playerClient.setPlayerState(lastState);
        //lastState = null;
        onGameUpdate();
    }

    @Override
    public void effectCard(LogEvent event) {
        out.setEffectCard(event.message());
        onGameUpdate();
    }

    @Override
    public void updatePBInfo(PBInfoEvent event) {
        try{
            out.setPBInfo(formatPBInfo(event));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateHourglass(HourglassEvent event) {
        out.setHorglass(event.getStart(), event.message());
        onGameUpdate();
    }

    @Override
    public void seeLog() {
        if (!(playerClient.getPlayerState().equals(seeBoardsClient) || playerClient.getPlayerState().equals(seeLog))){
            lastState = playerClient.getPlayerState();
        }
        playerClient.setPlayerState(seeLog);
        onGameUpdate();
    }

    public String formatPBInfo(PBInfoEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(ASCII_ART.compose("            ",
                ASCII_ART.addNumber(ASCII_ART.credits,String.valueOf(event.getCredits())),
                ASCII_ART.addNumber(ASCII_ART.cargoValue,String.valueOf(event.getTotValue())),
                ASCII_ART.addNumber(ASCII_ART.exposedConnectors,String.valueOf(event.getExposedConnectors())),
                ASCII_ART.addNumber(ASCII_ART.damage,String.valueOf(event.getDamage()))
                        ));

        sb.append(ASCII_ART.compose("          ",
                ASCII_ART.addNumber(ASCII_ART.crew,String.valueOf(event.getNumHumans())),
                ASCII_ART.addNumber(ASCII_ART.enginePower,String.valueOf(event.getEnginePower())),
                ASCII_ART.addNumber(ASCII_ART.plasmDrillPower,String.valueOf(event.getPlasmaDrillsPower()))
        ));


        return sb.toString();
    }


    public String[] formatCell(LobbyEvent event) {
        String[] cell = new String[9];
        cell[0] = "+"+centerTextAnsi(event.getGameId(),25, "-")+"+";
        cell[1] = "+                         +";
        cell[2] = "+                         +";
        cell[3] = "+                         +";
        cell[4] = "+                         +";
        cell[5] = "+                         +";
        cell[6] = "+                         +";
        cell[7] = "+-------------------------+";
        cell[8] = "+-------------------------+";
        int k = 1;
        if (!event.getGameId().equals("EMPTY CREATE NEW GAME")){
            //TODO: chiama metodo speciale di out senza salvare la stringa su out se il titolo è questo
            ArrayList<String> players = event.getPlayers();
            cell[7] = "+"+centerTextAnsi("Game level: "+ event.getLv(),25, "-")+"+";
            cell[8] = "+"+centerTextAnsi("Max players: "+ event.getMaxPlayers(),25, "-")+"+";
            for (String player : players) {
                cell[2+ k -1] = "+"+centerTextAnsi("p"+k+ ": "+player, 25)+"+";
                k++;
            }
        }

        return cell;
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

        if (event == null) {
            out.setCachedBoard(event.getX(), event.getY(), emptyCell());
        }
        else if (event.getId() == 158) {
            out.setCachedBoard(event.getX(), event.getY(), emptyCell());
            if (setup == 0 && event.getX() == 3 && event.getY() == 8){
                out.setCachedBoard(3,8,out.getPlayerBoard()[3][9]); //QUI
                out.setCachedBoard(3, 9, emptyCell());
            }

            else if (!(event.getX() == 3 && event.getY() == 8) && (event.getX() < 3 || event.getY() < 3) && (event.getY() != 2)) {
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
                            case 4 -> sb.append("\u001B[31m██\u001B[0m "); // Rosso
                            case 3 -> sb.append("\u001B[33m██\u001B[0m "); // Giallo
                            case 2 -> sb.append("\u001B[32m██\u001B[0m "); // Verde
                            case 1 -> sb.append("\u001B[34m██\u001B[0m "); // Blu
                        }
                    }
                    extra = sb.toString();
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

        cellLines[4] = "|" + centerTextAnsi(extra, contentWidth) + "|";

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
            default          -> "?";
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

        if(event.getId() == 158){
            out.setCacheHand(emptyCell());
        }
        else {
            TileEvent temp = new TileEvent(event.getId(), 0, 0, null, 0, false, false, 0, 0, event.getConnectors());
            out.setCacheHand(formatCell(temp));
        }
        onGameUpdate();
    }

    @Override
    public void updateGameboard(GameBoardEvent event) {

        int x = out.getPositionToGameboard().get(event.getPosition()).getFirst();
        int y = out.getPositionToGameboard().get(event.getPosition()).getSecond();

        if(out.getPlayerToPosition().containsKey(event.getPlayerID())){
            int pos = out.getPlayerToPosition().get(event.getPlayerID());
            int x1 = out.getPositionToGameboard().get(pos).getFirst();
            int y1 = out.getPositionToGameboard().get(pos).getSecond();
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
        if(event.getConnectors() != null) {
            out.setUncoveredTilesId(event.getId());

            String[] cache = formatCell(new TileEvent(event.getId(), 0, 0, null, 0, false, false, 0, 0, event.getConnectors()));

            out.setUncoverdTileSetCache(event.getId(), cache); //QUI
        }
        else{

            out.setUncoveredTilesId(event.getId()); //QUI
            out.setUncoverdTileSetCache(event.getId(), null);//QUI
        }

        onGameUpdate();
    }




    //questo come anche qualche altro metodo sarà per gestire le cose eccezionali o comunque chiama un metodo speciale di out
    public String formatCard(int id){
        return CardsDescriptions.get(id);
    }

    @Override
    public void showCard(CardEvent event){

        out.setCardId(event.getId());

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
        ArrayList<String> toAdd = new ArrayList<>();
        for (Integer card : deck.getIds()){
            toAdd.add(formatCard(card));
        }
        out.setDeck(toAdd);
        this.onGameUpdate();
    }




    public synchronized void onGameUpdate() {
        //System.out.println("waiting all package");
        if (scheduledTask != null && !scheduledTask.isDone()) {
            scheduledTask.cancel(false);
        }

        scheduledTask = scheduler.schedule(() -> {
            //inputReader.clearScreen();
            try{
                out.showGame();
            }
            catch (Exception e){
                e.printStackTrace();
            }
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
                    case 4 -> sb.append("\u001B[31m██\u001B[0m"); // Rosso
                    case 3 -> sb.append("\u001B[33m██\u001B[0m"); // Giallo
                    case 2 -> sb.append("\u001B[32m██\u001B[0m"); // Verde
                    case 1 -> sb.append("\u001B[34m██\u001B[0m"); // Blu

            }
            sb.append(" |");
            k++;
        }
        sb.append("\n");

        return sb;
    }



    public Out getOut(){
        return out;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}

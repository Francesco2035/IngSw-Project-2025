package org.example.galaxy_trucker.Controller.ClientServer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Commands.InputReader;
import org.example.galaxy_trucker.Controller.Messages.GameBoardEvent;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.DeckEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.UncoverdTileSetEvent;
import org.example.galaxy_trucker.Model.Boards.GameBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Connectors.Connectors;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;


import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class TUI implements View {

    private final TileEvent[][] board = new TileEvent[10][10];
    private final HashMap<Integer, String> idToNameMap = new HashMap<>();
    private final int contentWidth = 33;
    private String[][][] cachedBoard;
    private String[] cacheHand;
    private final String gamboardBorder = "+-----------------------+";
    private final String border = "+---------------------------------+";
    private ArrayList<Integer> uncoveredTilesId = new ArrayList<>(); //ordine, quindi contiene gli ID in ordine di come arrivano
    private HashMap<Integer, String[]> uncoverdTileSetCache = new HashMap();
    private HashMap<Integer, String> CardsDescriptions = new HashMap<>();
    private String[][][] Gameboard;
    private int lv;
    private int setup = 101;
    private boolean fase = false;
    private int CoveredTileSet = 152;
    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private InputReader inputReader;
    private Thread inputThread;
    private Boolean connected = false;
    private HashMap<Integer, IntegerPair> positionToGameboard = new HashMap<>();
    private HashMap<String,Integer > PlayerToPosition = new HashMap<>();

    @Override
    public void setGameboard(int lv) {
        this.lv = lv;
        int position = 4;
        if (lv == 2){
            Gameboard = new String[6][12][7];
            for (int i = 0; i < 6; i++){
                for (int j = 0; j < 12; j++){
                    if ((i == 0 && (j >= 2 && j <= 9)||(i == 1 && (j == 1 || j == 10)))
                            || (i == 2 && (j == 0 || j == 11)||(i == 3 && (j == 0 || j == 11)))
                    || (i == 4 && (j == 1 || j == 10)) || (i == 5 && (j >= 2 && j <= 9))){

                        if (i == 0 && (j == 2 || j == 3 || j == 5 ||j == 8)) {
                            Gameboard[i][j] = formatPosition(position);
                            position--;
                        }

                        else {

                            Gameboard[i][j] = formatCell();
                        }
                    }

 else {
                        Gameboard[i][j] = emptyGbCell();
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

            for (int k = 0; k < 24; k++){
                //System.out.println( positionToGameboard.get(k).getFirst() + " " + positionToGameboard.get(k).getSecond());
                for (int l = 0; l < 7; l++){
                    //System.out.println(positionToGameboard.containsKey(k) + " " + k );

                    System.out.println(Gameboard[positionToGameboard.get(k).getFirst()][positionToGameboard.get(k).getSecond()][l]);
                }
            }
        }
        else{
            Gameboard = new String[10][5][7];
        }

    }

    public TUI() throws IOException {
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
            cachedBoard[event.getX()][event.getY()] = emptyCell();
        }
        else if (event.getId() == 158) {
            cachedBoard[event.getX()][event.getY()] = emptyCell();
            if (setup == 0 && event.getX() == 3 && event.getY() == 8){
                cachedBoard[3][8] = cachedBoard[3][9];
                cachedBoard[3][9] = emptyCell();
            }
            else if (!(event.getX() == 3 && event.getY() == 8) && (event.getX() < 3 || event.getY() < 3)) {

                for (int k = 0; k < 7; k++) {
                    cachedBoard[event.getX()][event.getY()][k] = "";
                }
            }

        }

        else if (event.getId() == 159) {
            cachedBoard[7][8] = emptyCell();
            cachedBoard[7][9] = emptyCell();
        }


        else {
            cachedBoard[event.getX()][event.getY()] = formatCell(event);
        }
        if (setup == 0){
            cacheHand = emptyCell();
            showTUI();
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
        System.out.println(message);
    }



    public void printBoard() {
        int rows = 10;
        int cols = 10;
        System.out.println("############################ BOARD ############################\n");
        for (int y = 0; y < rows; y++) {


            String[][] formattedRow = new String[cols][];
            for (int x = 0; x < cols; x++) {
                formattedRow[x] = cachedBoard[y][x];
            }

            for (int i = 0; i < 7; i++) {
                for (int x = 0; x < cols; x++) {
                    if (y > 2 && x > 1){
                        System.out.print(formattedRow[x][i]);
                    }
                }
                if (y > 2 ){
                    System.out.println();
                }

            }
        }

        System.out.println();
        System.out.println("\n############################ ##### ############################");
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
                            case 4 -> sb.append("\u001B[31mR\u001B[0m ");
                            case 3 -> sb.append("\u001B[33mY\u001B[0m ");
                            case 2 -> sb.append("\u001B[32mG\u001B[0m ");
                            case 1 -> sb.append("\u001B[34mB\u001B[0m ");
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
        System.out.println("\n" + border);
        if(event.getId() == 158){
            cacheHand = emptyCell();
        }
        else {
            TileEvent temp = new TileEvent(event.getId(), 0, 0, null, 0, false, false, 0, 0, event.getConnectors());
            cacheHand = formatCell(temp);
        }
        showTUI();
    }

    @Override
    public void updateGameboard(GameBoardEvent event) {

        int x = positionToGameboard.get(event.getPosition()).getFirst();
        int y = positionToGameboard.get(event.getPosition()).getSecond();
        if(PlayerToPosition.containsKey(event.getPlayerID())){
            int pos = PlayerToPosition.get(event.getPlayerID());
            int x1 = positionToGameboard.get(pos).getFirst();
            int y1 = positionToGameboard.get(pos).getSecond();
            Gameboard[x1][y1][3] = "|                       |";

            PlayerToPosition.remove(event.getPlayerID());
            if(x != -1){
                Gameboard[x][y][3] = "|"+centerTextAnsi(event.getPlayerID(),23) + "|";
                PlayerToPosition.put(event.getPlayerID(), event.getPosition());
            }
        }
        else{
            PlayerToPosition.put(event.getPlayerID(), event.getPosition());
            Gameboard[x][y][3] = "|"+centerTextAnsi(event.getPlayerID(),23) + "|";
        }



        printGameboard();

    }

    @Override
    public void updateCoveredTilesSet(CoveredTileSetEvent event) {
        CoveredTileSet = event.getSize();
        showTUI();
    }

    @Override
    public void updateUncoveredTilesSet(UncoverdTileSetEvent event) {
        uncoveredTilesId.remove(event.getId());
        uncoverdTileSetCache.remove(event.getId());
        if(event.getConnectors() != null) {
            uncoveredTilesId.add(event.getId());
            String[] cache = formatCell(new TileEvent(event.getId(), 0, 0, null, 0, false, false, 0, 0, event.getConnectors()));
            uncoverdTileSetCache.put(event.getId(), cache);
        }
        showTUI();
    }


    private void showUncoveredTiles() {
        System.out.println("############################ UNCOVERED TILES ############################\n");

        StringBuilder line = new StringBuilder();
        StringBuilder topLine = new StringBuilder();
        for (int j = 0; j < uncoveredTilesId.size(); j++) {
            topLine.append("Position ").append(j).append("                          ");
        }
        for (int i = 0; i < 7; i++){

            for (Integer id : uncoveredTilesId) {
                line.append(uncoverdTileSetCache.get(id)[i]).append(" ");
            }
            line.append("\n");

        }
        System.out.println(topLine);
        System.out.println(line);
        System.out.println("\n############################ ############## ############################");

    }

    private void printTilesSet(){
        showUncoveredTiles();
        System.out.println();
    }

    private void printHand(){
        System.out.println("############################ HAND ############################\n");
        for (String l : cacheHand) System.out.println(l);
        System.out.println(border + "\n");
        System.out.println("\n############################ #### ############################");
    }

    private void showTUI(){
        if (!fase){
            System.out.println("############################ COVERED TILES SET ############################\n");
            System.out.println("\n CoveredTileSet size: "+ CoveredTileSet);
            showUncoveredTiles();
            printHand();
            printBoard();
            System.out.println("\n############################ ################# ############################");
        }
        else {
            //
        }
    }

    @Override
    public void showCard(int id){
        System.out.println("\n");
        System.out.println(CardsDescriptions.get(id));
    }

    @Override
    public void disconnect() {
        inputReader.stop();
        inputThread.interrupt();
        try {
            inputThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Disconnected cleanly.");
    }

    @Override
    public void connect() {
        inputReader = new InputReader(inputQueue);
        inputThread = new Thread(inputReader);
        inputThread.setDaemon(true);
        inputThread.start();
    }

    @Override
    public String askInput(String message) {
        System.out.print(message);
        try {
            String toSend = inputQueue.take();
            //System.out.println("ask input: " + toSend);
            return toSend;
        } catch (InterruptedException e) {
            //Thread.currentThread().interrupt();
            System.out.println("Input interrupted");
            return "";
        }
    }


    @Override
    public void showDeck(DeckEvent deck){
        for (Integer e : deck.getIds()) {
            showCard(e);
        }
    }

    public void printGameboard(){
        System.out.println("\n\n");
        if (lv == 2){
            StringBuilder toPrint = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                for (int k = 0; k < 7; k++) {
                    for (int j = 0; j < 12; j++) {
                        toPrint.append(Gameboard[i][j][k]);
                    }
                    System.out.println(toPrint.toString());
                    toPrint = new StringBuilder();

                }


            }


        }

        else{

        }
    }




}
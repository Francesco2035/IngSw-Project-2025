package org.example.galaxy_trucker.Controller.ClientServer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Model.Connectors.Connectors;
import org.example.galaxy_trucker.Model.Goods.Goods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class TUI implements View {

    private final TileEvent[][] board = new TileEvent[10][10];
    private final HashMap<Integer, String> idToNameMap = new HashMap<>();
    private final int contentWidth = 33;
    private String[][][] cachedBoard;
    private final String border = "+---------------------------------+";

    public TUI() {
        loadComponentNames();
        cachedBoard = new String[10][10][7];

    }


    @Override
    public void updateBoard(TileEvent event) {
        board[event.getX()][event.getY()] = event;
        if (event == null) {
            cachedBoard[event.getX()][event.getY()] = emptyCell();
        }
        else if (event.getId() == 158) {
            cachedBoard[event.getX()][event.getY()] = emptyCell();
            if (event.getX() < 4 || event.getY() < 3) {
                for (int k = 0; k < 7; k++) {
                    cachedBoard[event.getX()][event.getY()][k] = "";
                }

            } else if (event.getId() == -1) {
                cachedBoard[event.getX()][event.getY()] = emptyCell();
            }
        }
        else {
            cachedBoard[event.getX()][event.getY()] = formatCell(event);
        }
        printBoard();
    }

    private String[] emptyCell() {
        String[] cellLines = new String[7];

        for (int i = 0; i < 7; i++) {
            cellLines[i] = "                                   ";
        }
        return cellLines;
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String askInput(String message) {
        System.out.print(message);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    public void printBoard() {
        int rows = 10;
        int cols = 10;

        for (int y = 0; y < rows; y++) {


            String[][] formattedRow = new String[cols][];
            for (int x = 0; x < cols; x++) {
                formattedRow[x] = cachedBoard[y][x];
            }

            for (int i = 0; i < 7; i++) {
                for (int x = 0; x < cols; x++) {
                    System.out.print(formattedRow[x][i]);
                }
                System.out.println();
            }
        }

        System.out.println();
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

        cellLines[1] = "|" + centerText("\u2191 " + top, contentWidth) + "|";

        String colored = idToNameMap.getOrDefault(event.getId(), "Unknown");
        String name = colored.replaceAll("\u001B\\[[;\\d]*m", "");

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
        }

        String leftPart = "" + event.getId();
        String centeredPart = centerText(leftPart, contentWidth - 8);
        cellLines[3] = "| \u2190 "+ left + centeredPart + right + " \u2192 |";
        cellLines[4] = "|" + centerText(extra, contentWidth) + "|";

        String position = " " + event.getX() + " : " + event.getY();

        String arrow = "\u2193";

        int availableSpace = contentWidth - (position.length() + arrow.length() + bottom.length());

        int spaceBeforeArrow = availableSpace / 2;
        int spaceAfterArrow = availableSpace - spaceBeforeArrow;

        String finalRow = position + " ".repeat(spaceBeforeArrow -4) + arrow + " "+ bottom + " ".repeat(spaceAfterArrow );

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
            case "NONE"      -> ".";
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

    private void loadComponentNames() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream tilesStream = getClass().getClassLoader().getResourceAsStream("ClientTiles.JSON")) {
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
        TileEvent temp = new TileEvent(event.getId(), 0, 0, null, 0, false, false, 0, 0, event.getConnectors());
        String[] lines = formatCell(temp);
        for (String l : lines) System.out.println(l);
        System.out.println(border + "\n");
    }
}
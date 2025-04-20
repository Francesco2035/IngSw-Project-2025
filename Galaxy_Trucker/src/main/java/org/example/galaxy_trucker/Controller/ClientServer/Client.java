package org.example.galaxy_trucker.Controller.ClientServer;



import org.example.galaxy_trucker.Controller.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Controller.ClientServer.TCP.TCPClient;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Model.Connectors.*;
import org.example.galaxy_trucker.Model.Goods.Goods;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Client {

    private RMIClient rmiClient;
    private TCPClient tcpClient;
    private TileEvent[][] board; // Board di gioco

    public Client() {
        board = new TileEvent[10][10];
    }

    public void startRMIClient() throws IOException, NotBoundException {
        rmiClient = new RMIClient(this);
        rmiClient.StartClient();
    }

//    public void startTCPClient() throws IOException {
//        this.tcpClient = new TCPClient(this);
//        tcpClient.StartClient();
//    }

    public void updateBoard(TileEvent event) {
        System.out.println("Received event: " + event);
        board[event.getX()][event.getY()] = event;
        printPlayerBoard();
    }

    public String[] formatCell(TileEvent event) {
        String[] cellLines = new String[5];
        int contentWidth = 27;

        if (event == null) {
            for (int i = 0; i < 5; i++) {
                cellLines[i] = "|" + " ".repeat(contentWidth) + "|";
            }
            return cellLines;
        }

        ArrayList<Connectors> conns = event.getConnectors();
        String top    = getConnectorSymbol(conns.get(1)); // UP
        String left   = getConnectorSymbol(conns.get(0)); // LEFT
        String right  = getConnectorSymbol(conns.get(2)); // RIGHT
        String bottom = getConnectorSymbol(conns.get(3)); // DOWN

        cellLines[0] = "|" + centerText("↑ " + top, contentWidth) + "|";

        cellLines[1] = "|" + centerText(event.getType(), contentWidth) + "|";

        String leftPart =  ""+event.getId();
        String centeredPart = centerText(leftPart, contentWidth - 8);
        cellLines[2] = "| ← "+ left + centeredPart + right + " → |";


        String line3 = "";
        if (event.getHumans() > 0) line3 += event.getHumans() + " H  ";
        if (event.getBatteries() > 0) line3 += event.getBatteries() + " B";
        cellLines[3] = "|" + centerText(line3.trim(), contentWidth) + "|";

        cellLines[4] = "|" + centerText("↓ " + bottom, contentWidth) + "|";

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
            default                   -> "?";
        };
    }

    private String centerSymbol(String s) {
        return String.format("%-1s", s);
    }

    public void printPlayerBoard() {
        int rows = 10;
        int cols = 10;

        String horizontalBorder = "+---------------------------+";

        for (int y = 0; y < rows; y++) {
            for (int c = 0; c < cols; c++) {
                System.out.print(horizontalBorder);
            }
            System.out.println();

            String[][] formattedRow = new String[cols][];
            for (int x = 0; x < cols; x++) {
                formattedRow[x] = formatCell(board[y][x]);
            }

            for (int i = 0; i < 5; i++) {
                for (int x = 0; x < cols; x++) {
                    System.out.print(formattedRow[x][i]);
                }
                System.out.println();
            }
        }

        for (int c = 0; c < cols; c++) {
            System.out.print(horizontalBorder);
        }
        System.out.println();
    }

    public void updateHand(HandEvent event) {
        System.out.println("\n");
        String horizontalBorder = "+---------------------------+";

        System.out.print(horizontalBorder);
        System.out.println("\n");
        TileEvent eventTemp = new TileEvent(event.getId(),0, 0,null, 0, false, false, 0, 0, "Hand", event.getConnectors());
        String[] formattedCell = formatCell(eventTemp);

        for (String line : formattedCell) {
            System.out.println(line);
        }
        System.out.print(horizontalBorder);
        System.out.println("\n");
    }

public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Please specify connection type: RMI or TCP");
            return;
        }

        String connectionType = args[0];
        Client client = new Client();

        if (connectionType.equalsIgnoreCase("RMI")) {
            client.startRMIClient();
        } else if (connectionType.equalsIgnoreCase("TCP")) {
            //client.startTCPClient();
        } else {
            throw new IllegalArgumentException("Invalid connection type. Use 'RMI' or 'TCP'.");
        }
    }
}

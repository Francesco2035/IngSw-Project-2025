package org.example.galaxy_trucker.Controller.ClientServer;

import org.example.galaxy_trucker.Commands.CommandInterpreter;
import org.example.galaxy_trucker.Controller.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Controller.ClientServer.TCP.TCPClient;
import org.example.galaxy_trucker.Controller.Messages.*;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.DeckEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.UncoverdTileSetEvent;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;

public class Client implements EventVisitor {

    private View view;
    private RMIClient rmiClient;
    private TCPClient tcpClient;
    private TileEvent[][] board;
    private UUID token;

    public Client() {
        board = new TileEvent[10][10];
    }

    public void startRMIClient() throws IOException, NotBoundException {
        rmiClient = new RMIClient(this);
        rmiClient.StartClient();
    }

    private void startTCPClient() throws IOException {
        tcpClient = new TCPClient(this);
        tcpClient.startClient();
    }



    public void updateBoard(TileEvent event) {
        //System.out.println(event.message());
        board[event.getX()][event.getY()] = event;
        view.updateBoard(event);
    }

    public void updateHand(HandEvent event) {
        view.updateHand(event);
    }

    public  void run() throws Exception {
        view = new TUI();
        Settings.setIp(view.askInput("Please enter server ip address: "));
        String Connection = "";
        while(!Connection.equals("TCP") && !Connection.equals("RMI")) {

            Connection = getView().askInput("Please enter type of connection <TCP|RMI> [exit]: ").toUpperCase();
            if (Connection.equals("EXIT")) {
                System.exit(1);
            }

        }
        String view1 = "";
        while(!view1.equals("GUI") && !view1.equals("TUI")) {
            view1 = getView().askInput("Choose GUI | TUI [exit]: ").toUpperCase();
            if (view1.equals("EXIT")) {
                System.exit(1);
            }
        }


        Client client = new Client();

        if (view1.equals("TUI")) {
            client.setView(new TUI());
        } else if (view1.equals("GUI")) {
            //client.setView(new GUI());
        }

        if (Connection.equals("RMI")) {
            client.startRMIClient();
        } else if (Connection.equals("TCP")) {
             client.startTCPClient();
        }
    }

//    public void disconnected(UUID token){
//        this.token = token;
//        String s = view.askInput("Choose connection type or EXIT");
//        if (s.equalsIgnoreCase("EXIT")) {
//            System.exit(0);
//        }
//        if (s.equals("RMI")) {
//            System.exit(0);
//        }
//        if (s.equals("TCP")) {
//            tcpClient = new TCPClient(this);
//            tcpClient.reconnect(token);
//        }
//    }



    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void updateCoveredTilesSet(CoveredTileSetEvent event) {
        this.view.updateCoveredTilesSet(event);
    }


    public void updateUncoveredTilesSet(UncoverdTileSetEvent event) {
        this.view.updateUncoveredTilesSet(event);
    }

    public void seeDeck(DeckEvent deck) {
        this.view.showDeck(deck);
    }

    public void receiveEvent(Event event) {
        event.accept(this);
    }

    @Override
    public void visit(DeckEvent event) {
        this.view.showDeck(event);
    }

    @Override
    public void visit(CardEvent event) {
        this.view.showCard(event.getId());
    }

    @Override
    public void visit(HandEvent event) {
        this.view.updateHand(event);
    }

    @Override
    public void visit(VoidEvent event) {
        System.out.println("non so a cosa serve, vediamo se arriva un void");
    }

    @Override
    public void visit(TileEvent event) {
        this.view.updateBoard(event);
    }
    
    @Override
    public void visit(UncoverdTileSetEvent event) {
        this.view.updateUncoveredTilesSet(event);
    }

    @Override
    public void visit(CoveredTileSetEvent event) {
        this.view.updateCoveredTilesSet(event);
    }

    @Override
    public void visit(GameBoardEvent gameBoardEvent) {
        this.view.updateGameboard(gameBoardEvent);
    }

    public void changeConnection(String connection, CommandInterpreter interpreter) throws IOException, NotBoundException, InterruptedException {
        if (connection.equals("RMI")) {
            RMIClient rmiClient = new RMIClient(this, interpreter);


        }
        if (connection.equals("TCP")) {
            TCPClient tcpClient = new TCPClient(this, interpreter);

        }

    }
}

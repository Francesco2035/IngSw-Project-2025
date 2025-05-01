package org.example.galaxy_trucker.Controller.ClientServer;

import org.example.galaxy_trucker.Commands.CommandInterpreter;
import org.example.galaxy_trucker.Controller.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Controller.ClientServer.TCP.TCPClient;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.EventVisitor;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.DeckEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.UncoverdTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.VoidEvent;

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

    public  void run(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("java Client <RMI|TCP> <TUI|GUI>");
            return;
        }

        String connectionType = args[0];
        String uiType = args[1];

        Client client = new Client();

        if (uiType.equalsIgnoreCase("TUI")) {
            client.setView(new TUI());
        } else if (uiType.equalsIgnoreCase("GUI")) {
            //client.setView(new GUI());
        }

        if (connectionType.equalsIgnoreCase("RMI")) {
            client.startRMIClient();
        } else if (connectionType.equalsIgnoreCase("TCP")) {
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

    public void changeConnection(String connection, CommandInterpreter interpreter) throws IOException, NotBoundException {
        if (connection.equals("RMI")) {
            RMIClient rmiClient = new RMIClient(this, interpreter);

            rmiClient.inputLoop(true);
        }
        if (connection.equals("TCP")) {
            TCPClient tcpClient = new TCPClient(this, interpreter);

        }

    }
}

package org.example.galaxy_trucker.Controller.ClientServer;

import org.example.galaxy_trucker.Controller.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.Controller.ClientServer.TCP.TCPClient;
import org.example.galaxy_trucker.Controller.ClientServer.TUI;
import org.example.galaxy_trucker.Controller.ClientServer.View;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.VoidEvent;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class Client {

    private View view;
    private RMIClient rmiClient;
    private TCPClient tcpClient;
    private TileEvent[][] board;

    public Client() {
        board = new TileEvent[10][10];
    }

    public void startRMIClient() throws IOException, NotBoundException {
        rmiClient = new RMIClient(this);
        rmiClient.StartClient();
    }




    public void updateBoard(TileEvent event) {
        System.out.println("Received event: " + event);
        board[event.getX()][event.getY()] = event;
        view.updateBoard(event); // Invia l'evento alla view (TUI o GUI)
    }

    public void updateHand(HandEvent event) {
        view.updateHand(event);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java Client <RMI|TCP> <TUI|GUI>");
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
            // client.startTCPClient();
        }
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}

package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Controller.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Controller.Listeners.HandListener;
import org.example.galaxy_trucker.Controller.Listeners.PlayerBoardListener;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.VoidEvent;

import java.rmi.RemoteException;

public class VirtualView implements PlayerBoardListener, HandListener {

    private Event[][] eventMatrix;
    private String playerName;
    private String idGame;
    private ClientInterface client;


    public VirtualView(String playerName, String idGame, ClientInterface client) {
        this.playerName = playerName;
        this.idGame = idGame;
        this.client = client;
        eventMatrix = new Event[10][10];
    }



    public void setEventMatrix(int lv) {
        if (lv == 2) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (x < 4 || y < 3 || (x == 4 && (y == 3 || y == 4 || y == 6 || y == 8 || y == 9)) ||(x == 5 && (y == 3 || y== 9)) || (x == 8 && y == 6) || x ==9) {
                        eventMatrix[x][y] = null;
                    }

                    else {
                        eventMatrix[x][y] = new VoidEvent();
                    }

                }
            }
        }
        else {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if(y <= 3 ||x == 9 || y == 9|| x <4 || ( x == 4 && (y != 6)) || (x== 5 &&(y <5 || y>7)) || (x==8 && y == 6))  {
                        eventMatrix[x][y] = null;
                    }

                    else {
                        eventMatrix[x][y] = new VoidEvent();
                    }
                }
            }

        }
        //sendEvent(new VoidEvent());
    }





    public void sendEvent(Event event) {

        try {
            client.receiveMessage(event);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    public void sendEvent(HandEvent event) {
        try {
            client.receiveMessage(event);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendEvent(TileEvent event) {

        try {
            client.receiveMessage(event);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void playerBoardChanged(TileEvent event) {
        eventMatrix[event.getX()][event.getY()] = event;
        sendEvent(event);
    }

    @Override
    public void handChanged(HandEvent event) {
        sendEvent(event);
    }
}

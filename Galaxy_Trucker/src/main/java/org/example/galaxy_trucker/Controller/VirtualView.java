package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Controller.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Controller.Listeners.HandListener;
import org.example.galaxy_trucker.Controller.Listeners.PlayerBoardListener;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.VoidEvent;
import org.example.galaxy_trucker.Model.Connectors.Connectors;
import org.example.galaxy_trucker.Model.Connectors.NONE;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class VirtualView implements PlayerBoardListener, HandListener {

    private TileEvent[][] eventMatrix;
    private String playerName;
    private String idGame;
    private ClientInterface client;


    public VirtualView(String playerName, String idGame, ClientInterface client) {
        this.playerName = playerName;
        this.idGame = idGame;
        this.client = client;
        eventMatrix = new TileEvent[10][10];
    }



    public void setEventMatrix(int lv) {
        ArrayList<Connectors> noneConnectors = new ArrayList<>();
        noneConnectors.add(NONE.INSTANCE);
        noneConnectors.add(NONE.INSTANCE);
        noneConnectors.add(NONE.INSTANCE);
        noneConnectors.add(NONE.INSTANCE);
        if (lv == 2) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (x < 4 || y < 3 || (x == 4 && (y == 3 || y == 4 || y == 6 || y == 8 || y == 9)) ||(x == 5 && (y == 3 || y== 9)) || (x == 8 && y == 6) || x ==9) {

                        eventMatrix[x][y] = new TileEvent(158,x,y,null,0,false,false,0,0,noneConnectors);

                    }

                    else {
                        eventMatrix[x][y] = new TileEvent(157,x,y,null,0,false,false,0,0,noneConnectors);


                    }

                }
            }
        }
        else {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if(y <= 3 ||x == 9 || y == 9|| x <4 || ( x == 4 && (y != 6)) || (x== 5 &&(y <5 || y>7)) || (x==8 && y == 6))  {
                        eventMatrix[x][y] = new TileEvent(158,x,y,null,0,false,false,0,0,noneConnectors);

                    }

                    else {
                        eventMatrix[x][y] = new TileEvent(157,x,y,null,0,false,false,0,0,noneConnectors);

                    }

                }
            }

        }

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {

                sendEvent(eventMatrix[x][y]);

            }
        }
    }




//
//    public void sendEvent(VoidEvent event) {
//
//        try {
//            client.receiveMessage(event);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//
//    }


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

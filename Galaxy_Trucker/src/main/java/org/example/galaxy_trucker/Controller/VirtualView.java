package org.example.galaxy_trucker.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Controller.Listeners.*;
import org.example.galaxy_trucker.Controller.Messages.*;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.DeckEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.UncoverdTileSetEvent;
import org.example.galaxy_trucker.Model.Connectors.Connectors;
import org.example.galaxy_trucker.Model.Connectors.NONE;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

//TODO: fare un solo listener, sono scemo non c'era bisogno di crearne 20000 tanto il dispatch viene fatto lato client col pattern, prima finiamo un game e poi cambiamo
public class VirtualView implements PlayerBoardListener, HandListener, TileSestListener, CardListner, GameBoardListener, GameLobbyListener, PhaseListener, RewardsListener{

    private boolean Disconnected = false;
    private TileEvent[][] eventMatrix;
    private String playerName;
    private String idGame;
    private ClientInterface client;
    private PrintWriter out ;
    private int coveredTiles= 0;
    private HashMap<Integer, ArrayList<Connectors>> uncoveredTilesMap = new HashMap<>();
    private HandEvent hand ;
    private UUID token;
    private CardEvent card = null;
    private GameLobbyEvent lobby = null;
    private GameBoardEvent board = null;
    private PhaseEvent phase = null;
    private RewardsEvent rewardsEvent = null;


    public VirtualView(String playerName, String idGame, ClientInterface client, PrintWriter echoSocket) {
        this.playerName = playerName;
        this.idGame = idGame;
        this.client = client;
        eventMatrix = new TileEvent[10][10];
        this.out = echoSocket;
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



    public void sendEvent(HandEvent event)  {
        if (!Disconnected) {
            if (out != null ) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {
                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public void sendEvent(TileEvent event) {
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {
                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @Override
    public void playerBoardChanged(TileEvent event) {
        eventMatrix[event.getX()][event.getY()] = event;
        sendEvent(event);
    }

    @Override
    public void handChanged(HandEvent event)  {
        hand = event;
        sendEvent(event);
    }

    @Override
    public void tilesSetChanged(CoveredTileSetEvent event)  {
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {
                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void tilesSetChanged(UncoverdTileSetEvent event) throws RemoteException {
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {
                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void seeDeck(DeckEvent event) {
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {
                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void newCard(CardEvent event) {
        card = event;
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {

                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @Override
    public void gameBoardChanged(GameBoardEvent event)  {
        board = event;
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {

                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



    public void setDisconnected(boolean disconnected) {
        Disconnected = disconnected;
    }


    public boolean getDisconnected(){
        return Disconnected;
    }


    public void reconnect() {
        for (int i = 0; i < 10; i ++){
            for(int j = 0; j < 10; j ++){
                sendEvent(eventMatrix[i][j]);
            }
        }
        if(hand != null) {
            sendEvent(hand);

        }
        if (card != null){
            newCard(card);
        }
        if (phase != null){
            sendEvent(phase);
        }
        if (lobby != null){
            sendEvent(lobby);
        }
        if (board!= null){
            sendEvent(board);
        }
        if (rewardsEvent != null){
            sendEvent(rewardsEvent);
        }

    }

    public void setToken(UUID token) {
        this.token = token;
    }
    public UUID getToken() {
        return token;
    }

    public void setPrintWriter(PrintWriter printWriter) {
        this.out = printWriter;
    }

    public void setClient(ClientInterface client){
        this.client = client;
    }

    @Override
    public void GameLobbyChanged(GameLobbyEvent event) {
        lobby = event;
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {

                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void PhaseChanged(PhaseEvent event) {
        phase = event;
        sendEvent(event);
    }

    public void sendEvent(Event event) {
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {

                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @Override
    public void rewardsChanged(RewardsEvent e) {
        rewardsEvent = e;
        sendEvent(e);
    }
}

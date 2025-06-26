package org.example.galaxy_trucker.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Controller.Listeners.*;
import org.example.galaxy_trucker.Controller.Messages.*;
import org.example.galaxy_trucker.Controller.Messages.TileSets.*;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Model.Connectors.Connectors;
import org.example.galaxy_trucker.Model.Connectors.NONE;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class VirtualView implements PlayerBoardListener, HandListener, TileSestListener, CardListner, GameBoardListener, GameLobbyListener, PhaseListener, RewardsListener, ExceptionListener, PlayersPBListener, RandomCardEffectListener{

    private int lv;
    private boolean Disconnected = false;
    private TileEvent[][] eventMatrix;
    private String playerName;
    private String idGame;
    private ClientInterface client;
    private PrintWriter out ;
    private int coveredTiles= 0;
    private HashMap<Integer, ArrayList<Connectors>> uncoveredTilesMap = new HashMap<>();
    private HandEvent hand ;
    private String token;
    private CardEvent card = null;
    private GameLobbyEvent lobby = null;
    //TODO: forse è meglio fare che board sia un singolo evento che contenga tutti i player
    private ArrayList<GameBoardEvent> board = new ArrayList<>();
    private PhaseEvent phase = null;
    private RewardsEvent rewardsEvent = null;
    private ArrayList<PlayersPBListener> playersPBListeners = new ArrayList<>();
    private PBInfoEvent pbInfoEvent = null;
    private ArrayList<LogEvent> logEvents = new ArrayList<>();
    private ArrayList<PlayerTileEvent> otherPlayerTileEvents = new ArrayList<>();
    private HourglassEvent hourglassEvent = null;

    public void setLv(int lv){
        this.lv = lv;
    }


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
                        //updateOtherPlayers(eventMatrix[x][y]);

                    }

                    else {
                        eventMatrix[x][y] = new TileEvent(157,x,y,null,0,false,false,0,0,noneConnectors);
                        //updateOtherPlayers(eventMatrix[x][y]);

                    }

                }
            }
        }
        else {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if(y <= 3 ||x == 9 || y == 9|| x <4 || ( x == 4 && (y != 6)) || (x== 5 &&(y <5 || y>7)) || (x==8 && y == 6))  {
                        eventMatrix[x][y] = new TileEvent(158,x,y,null,0,false,false,0,0,noneConnectors);
                        //updateOtherPlayers(eventMatrix[x][y]);
                    }

                    else {
                        eventMatrix[x][y] = new TileEvent(157,x,y,null,0,false,false,0,0,noneConnectors);
                        //updateOtherPlayers(eventMatrix[x][y]);
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
        hand = event;
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


    public void sendEvent(PlayerTileEvent event){  

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
        updateOtherPlayers(event);
        sendEvent(event);
    }


    @Override
    public void PBInfoChanged(PBInfoEvent event) {
        this.pbInfoEvent = event;
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
        board.add(event);
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

        sendEvent(new ReconnectedEvent(token,idGame,playerName,lv));

        if (card != null){
            newCard(card);
        }

        for (LogEvent log : logEvents){
            sendEvent(log);
        }

        for (PlayerTileEvent playerTileEvent : otherPlayerTileEvents){
            sendEvent(playerTileEvent);
        }

        for (int i = 0; i < 10; i ++){
            for(int j = 0; j < 10; j ++){
                sendEvent(eventMatrix[i][j]);
            }
        }

        if (pbInfoEvent != null){
            sendEvent(pbInfoEvent);
        }

        if(hand != null) {
            sendEvent(hand);
        }

        if (lobby != null){
            sendEvent(lobby);
        }

        for (GameBoardEvent gbEvent : board){
            sendEvent(gbEvent);
        }

        if (rewardsEvent != null){
            sendEvent(rewardsEvent);
        }
        if (hourglassEvent != null){
            sendEvent(hourglassEvent);
        }

        if (phase != null){
            sendEvent(phase);
        }

    }


    public void setToken(String token) {
        this.token = token;
    }  


    public String getToken() {
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

    public void sendHourglass(HourglassEvent event){
        hourglassEvent = event;
        sendEvent(event);
    }

    public void sendEvent(Event event) {  
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                    //System.out.println("Send: "+objectMapper.writeValueAsString(event));
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
    public void rewardsChanged(RewardsEvent event) {  
        rewardsEvent = event;
        sendEvent(event);
    }


    @Override
    public void exceptionOccured(ExceptionEvent event) {
        sendEvent(event);
    }  


    public PlayersPBListener getPBlistener(){
        return this;
    }  


    public void setPlayersPBListeners(PlayersPBListener listener){  
        this.playersPBListeners.add(listener);
        for (int i = 0; i < 10; i ++){
            for (int j = 0; j < 10; j ++){
                PlayerTileEvent newEvent = new PlayerTileEvent(playerName,eventMatrix[i][j].getId(),eventMatrix[i][j].getX(), eventMatrix[i][j].getY(),eventMatrix[i][j].getCargo(),eventMatrix[i][j].getHumans()
                        ,eventMatrix[i][j].isPurpleAlien(),eventMatrix[i][j].isBrownAlien(), eventMatrix[i][j].getBatteries(),eventMatrix[i][j].getRotation(),eventMatrix[i][j].getConnectors());
                listener.receivePBupdate(newEvent);
            }
        }
    }


    public void updateOtherPlayers(TileEvent event){
        PlayerTileEvent newEvent = new PlayerTileEvent(playerName,event.getId(),event.getX(), event.getY(),event.getCargo(),event.getHumans()
        ,event.isPurpleAlien(),event.isBrownAlien(), event.getBatteries(),event.getRotation(),event.getConnectors());
        for (PlayersPBListener listener : playersPBListeners){
            listener.receivePBupdate(newEvent);
        }
    }


    @Override
    public void receivePBupdate(PlayerTileEvent event){  
        //System.out.println("adding player tile event "+ event.getPlayerName()+ " "+ event.getId());
        otherPlayerTileEvents.add(event);
        sendEvent(event);
    }


    public String getPlayerName(){
        return playerName;
    }  


    @Override
    public void Effect(LogEvent event) {
        logEvents.add(event);
        sendEvent(event);
    }


    public void removeListeners(){
        this.playersPBListeners.clear();
    }


    public void removeListener(PlayersPBListener listener){
        this.playersPBListeners.remove(listener);
    }  

    public void sendLogEvent(LogEvent event) {
        logEvents.add(event);
        sendEvent(event);
    }


//    public void sendEvent(LogEvent event){
//        logEvents.add(event);
//        sendEvent(event);
//    }

}

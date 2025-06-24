package org.example.galaxy_trucker.ClientServer;

import org.example.galaxy_trucker.Commands.CommandInterpreter;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.ClientServer.TCP.TCPClient;
import org.example.galaxy_trucker.Messages.*;
import org.example.galaxy_trucker.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Messages.TileSets.*;
import org.example.galaxy_trucker.View.ClientModel.States.LoginClient;
import org.example.galaxy_trucker.View.GUI.GuiRoot;
import org.example.galaxy_trucker.View.TUI.TUI;
import org.example.galaxy_trucker.View.View;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.HashMap;

public class Client implements EventVisitor {

    private View view;
    private RMIClient rmiClient;
    private TCPClient tcpClient;
    private TileEvent[][] board;
    private String token;
    private boolean login = false;
    private boolean lobby = false;
    private final LoginClient loginClient = new LoginClient();
    HashMap<String, Integer> gameidToLV = new HashMap<>();
    CommandInterpreter commandInterpreter;


    public boolean getLogin(){
        return login;
    }

    public void setLogin(boolean login){
        this.login = login;
    }

    public boolean getLobby(){
        return lobby;
    }

    public void setLobby(boolean lobby){
        this.lobby = lobby;
    }


    public synchronized boolean containsGameId(String gameId) {
        //System.out.println(this);

        return gameidToLV.containsKey(gameId);
    }

    public synchronized int getLevel(String gameId) {
        //System.out.println(this);

        return gameidToLV.get(gameId);
    }

    public synchronized void setGameIdToLV(String gameid, int lv) {
        //System.out.println(this);

        //System.out.println(gameid + " " + lv);
        this.gameidToLV.putIfAbsent(gameid, lv);
        //System.out.println(gameid + " " + gameidToLV.get(gameid)+" size: "+gameidToLV.size());
    }

    public synchronized HashMap<String, Integer> getGameidToLV() {
        return gameidToLV;
    }

    public Client() {
        board = new TileEvent[10][10];
    }

    public void startRMIClient() throws IOException, NotBoundException, InterruptedException {
        //this.commandInterpreter = new CommandInterpreter();

        //String ip = NetworkUtils.getLocalIPAddress();

        //TODO: da fare in modo dinamico, non so se la classe networkutils lo trova quello di zerotier
        //System.setProperty("java.rmi.server.hostname", ip);

        //System.out.println("RMI hostname set to: " + ip);
        rmiClient = new RMIClient(this);
        rmiClient.StartClient();
    }

    private void startTCPClient() throws IOException {
        //this.commandInterpreter = new CommandInterpreter();
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
        Terminal terminal = TerminalBuilder.builder().build();
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
        //view = new TUI();
        Settings.setIp(reader.readLine("Please enter server ip address: "));
        String Connection = "";
        while(!Connection.equals("TCP") && !Connection.equals("RMI")) {

            Connection = reader.readLine("Please enter type of connection <TCP|RMI> [exit]: ").toUpperCase();
            if (Connection.equals("EXIT")) {
                System.exit(1);
            }

        }
        String view1 = "";
        while(!view1.equals("GUI") && !view1.equals("TUI")) {
            view1 = reader.readLine("Choose GUI | TUI [exit]: ").toUpperCase();
            if (view1.equals("EXIT")) {
                System.exit(1);
            }
        }
        terminal.close();


        Client client = this;

        if (view1.equals("TUI")) {
            TUI tui = new TUI(loginClient);
            tui.setClient(client);
            client.setView(tui);
        } else if (view1.equals("GUI")){
            GuiRoot gui = new GuiRoot(loginClient);
            client.setView(gui);
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
    public void visit(LobbyEvent event){this.view.showLobby(event);}

    @Override
    public void visit(PhaseEvent event) {
        //System.out.println("------------------------------------------------------------------------------------nuove phase "+event.getStateClient().getClass());
        this.view.phaseChanged(event);
    }

    @Override
    public void visit(RewardsEvent rewardsEvent) {this.view.rewardsChanged(rewardsEvent);}

    @Override
    public void visit(ExceptionEvent exceptionEvent) {
        this.view.exceptionOccurred(exceptionEvent);
    }

    @Override
    public void visit(PlayerTileEvent playerTileEvent) {
        this.view.updateOthersPB(playerTileEvent);
    }

    @Override
    public void visit(LogEvent event) {
        this.view.effectCard(event);
    }

    @Override
    public void visit(ConnectionRefusedEvent event) {
        login = false;
        this.view.exceptionOccurred(new ExceptionEvent(event.message()));
    }

    @Override
    public void visit(PBInfoEvent event) {
        this.view.updatePBInfo(event);
    }

    @Override
    public void visit(QuitEvent quitEvent) {
        this.login = false;
        this.lobby = false;
        this.view.phaseChanged(new PhaseEvent(loginClient));
    }

    @Override
    public void visit(HourglassEvent event) {
        this.view.updateHourglass(event);
    }

    @Override
    public void visit(FinishGameEvent event) {
        this.login = false;
        this.lobby = false;
        this.view.showOutcome(event);
        try{
            Thread.sleep(3000);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        this.view.phaseChanged(new PhaseEvent(loginClient));
    }

    @Override
    public void visit(ReconnectedEvent event) {
        this.token = event.getToken();
        this.lobby = true;
        this.login = true;
        commandInterpreter = new CommandInterpreter(event.getPlayerId(), event.getGameId());
        commandInterpreter.setlv(event.getLv());
        commandInterpreter.setToken(token);
        this.view.setGameboard(event.getLv());
        this.view.reconnect(event);
        if (rmiClient != null){
            rmiClient.setCommandInterpreter(commandInterpreter);
        }
        if (tcpClient != null){
            tcpClient.setCommandInterpreter(commandInterpreter);
        }
    }

    @Override
    public void visit(TokenEvent tokenEvent) {
        this.view.Token(tokenEvent);
    }

    @Override
    public void visit(DeckEvent event) {
        this.view.showDeck(event);
    }

    @Override
    public void visit(CardEvent event) {
        this.view.showCard(event);
    }

    @Override
    public void visit(GameLobbyEvent event){
        this.view.showLobbyGame(event);
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
//            String ip = NetworkUtils.getLocalIPAddress();
//            System.setProperty("java.rmi.server.hostname", ip);
//            System.out.println("RMI hostname set to: " + ip);

            RMIClient rmiClient = new RMIClient(this, interpreter);


        }
        if (connection.equals("TCP")) {
            TCPClient tcpClient = new TCPClient(this, interpreter);

        }

    }
}

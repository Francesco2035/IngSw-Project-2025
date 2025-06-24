package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Controller.Listeners.GameLobbyListener;
import org.example.galaxy_trucker.Controller.Listeners.LobbyListener;
import org.example.galaxy_trucker.Controller.Messages.*;
import org.example.galaxy_trucker.Controller.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Exceptions.ImpossibleActionException;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import javax.swing.text.StyledEditorKit;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
//TODO: rimozione dei player e notifica con -1 al posto del nome del player
//TODO: aggiungere listener dei ready per il momento vedo se me la cavo senza listener: fare GameController un listener dei ready e semplicemente quando c'è un nuovo ready chiamare updatePlayers
public class GameController  implements ConcurrentCardListener , ReadyListener, FinishListener{
    String idGame;
    private final HashMap<String,Controller> ControllerMap;
    private final HashMap<String, Boolean> connectedPlayers = new HashMap<>();
    private final HashMap<String, BlockingQueue<Command>> commandQueues = new HashMap<>();
    private final HashMap<String, Thread> threads = new HashMap<>();
    private final HashMap<String, VirtualView> VirtualViewMap = new HashMap<>();
    private final HashMap<String, String> tokenToPlayerId = new HashMap<>();
    final Game game;
    private GamesHandler gh;
    //private BlockingQueue<Command> prepQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<Command> flightQueue = new LinkedBlockingQueue<>();
    private Thread flightThread;
    private boolean flightMode = false;
    int flightCount = 0;
    int buildingCount = 0;
    boolean GameOver = false;
    private boolean started = false;
    private boolean firtflight = true;
    private boolean concurrent = false;
    private int color = 153;
    int lv = 0;
    private int maxPlayer = 4;
    private final Object lock = new Object();
    private Thread prepThread;

    private LobbyListener lobbyListener;
    private ArrayList<GameLobbyListener> gameLobbyListeners = new ArrayList<>();

    public void setLobbyListener(LobbyListener lobbyListener) {
        this.lobbyListener = lobbyListener;
    }

    public boolean isStarted() {
        return started;
    }

    public synchronized HashMap<String, VirtualView> getVirtualViewMap() {
        return VirtualViewMap;
    }

    public GameController(String idGame, Game game, GamesHandler gh, int lv, int maxPlayer) {
        System.out.println(this);
        this.idGame = idGame;
        ControllerMap = new HashMap<>();
        this.game = game;
        this.gh = gh;
        this.flightQueue = new LinkedBlockingQueue<>();
        this.lv = lv;
        this.maxPlayer = maxPlayer;
//        this.prepThread = new Thread(() -> {
//            while(true){
//                Command cmd = prepQueue.poll();
//            }
//        });

    }



    public HashMap<String, Controller> getControllerMap() {
        return ControllerMap;
    }


    public void NewPlayer(Player p, VirtualView vv, String token){
        if (ControllerMap.keySet().contains(p.GetID())) {
            vv.sendEvent(new ConnectionRefusedEvent("Player ID " + p.GetID() + " already exists in game "+idGame));
            //throw new IllegalArgumentException("Player ID " + p.GetID() + " already exists in game "+idGame);
        }
        else if (maxPlayer == ControllerMap.size()){
            vv.sendEvent(new ConnectionRefusedEvent(idGame + " is full!"));
        }
         else {
            vv.setLv(lv);
            String playerId = p.GetID();
            System.out.println("Player ID: " + playerId);
            System.out.println("Token: " + token.toString());
            Controller controller = new LoginController(p, idGame);
            controller.setExceptionListener(vv);
            ControllerMap.put(playerId, controller);
            sendMessage(new LogEvent("New player: " + playerId,-1,-1,-1,-1));

            System.out.println("New player " + playerId+" in "+ this);
            tokenToPlayerId.put(token, playerId);
            BlockingQueue<Command> queue = new LinkedBlockingQueue<>();
            commandQueues.put(playerId, queue);
            game.NewPlayer(p);
            VirtualViewMap.put(playerId,vv);

            p.getmyPlayerBoard().setRewardsListener(vv);
            p.getmyPlayerBoard().setListener(vv);
            p.getCommonBoard().getCardStack().addListener(p.GetID(),vv);
            Tile mainCockpitTile = new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE);
            mainCockpitTile.setId(color);
            color++;
            p.setReadyListener(this);
            p.setFinishListener(this);
            p.setHandListener(vv);
            p.getCommonBoard().setListeners(vv);
            p.getCommonBoard().getTilesSets().setListeners(vv);
            p.setCardListner(vv);
            gameLobbyListeners.add(vv);
            vv.setEventMatrix(game.getGameBoard().getLevel());
            for (VirtualView v : VirtualViewMap.values()) {

                if(!vv.getPlayerName().equals(v.getPlayerName())){
                    v.setPlayersPBListeners(vv);
                    //System.out.println("vv "+vv.getPlayerName()+" v "+v.getPlayerName());
                    vv.setPlayersPBListeners(v);
                }
            }
            p.getmyPlayerBoard().insertTile(mainCockpitTile,6,6, false);
            updatePlayers();
            synchronized (connectedPlayers){
                connectedPlayers.put(playerId, true);
            }

            Thread t = new Thread(() -> {
                while (getConnection(playerId)) {
                    synchronized (ControllerMap) {
                        Controller current = ControllerMap.get(playerId);
//                        if(current != null && !getConnection(playerId)){
//                            System.out.println("Building command "+ current.getClass()+ " "+ current.getDisconnected());
//                            current.DefaultAction(this);
//                        }
//                        else{
                            Command cmd = queue.poll();
                            if (cmd != null){
                                System.out.println("Bulding command connected" );
                                current.action(cmd, this);
                            }

                    }
                }
            });
            t.start();
            threads.put(playerId, t);
            ArrayList<String> players = new ArrayList<>(VirtualViewMap.keySet());
            if (lobbyListener != null)
                lobbyListener.sendEvent(new LobbyEvent(game.getGameID(),game.getLv() ,players, maxPlayer));
        }


    }


    public synchronized void changeState() {
        System.out.println("change state called");
        //TODO: questo potrebbe essere molto pericoloso
        //updatePlayers();

        long readyCount = game.getPlayers().values().stream()
                .filter(Player::GetReady)
                .count();

        if (readyCount == game.getPlayers().size()) {
            System.out.println("change state successful");
            synchronized (game) {
                for (Player p : game.getPlayers().values()) {
                    String playerId = p.GetID();
                    ControllerMap.get(playerId).nextState(this);
                }
                started = true;
                game.getPlayers().values().forEach(p -> p.SetReady(false));
            }
        }
    }

    public void addCommand(Command command) {
        if(!flightMode) {
            BlockingQueue<Command> queue = commandQueues.get(command.getPlayerId());
            if (queue != null && !VirtualViewMap.get(command.getPlayerId()).getDisconnected()) {
                System.out.println("adding command for player "+command.getPlayerId()+ " "+command.getClass());
                queue.offer(command);
            } else {
                System.out.println("Empty queue for: " + command.getPlayerId());
            }
        }
        else {
            if (!VirtualViewMap.get(command.getPlayerId()).getDisconnected()){
                this.flightQueue.offer(command);
            }
        }
    }

    public void removePlayer(String token, Command command) {
        String playerId = tokenToPlayerId.get(token);
        if (playerId == null || !ControllerMap.containsKey(playerId)) {
            throw new IllegalArgumentException("Player ID " + playerId + " non found");
        }
        if (!command.allowedIn(game.getPlayers().get(playerId).getPlayerState())){
            getVirtualViewMap().get(playerId).sendEvent(new ExceptionEvent("You can't quit in this state!"));
        }
        else{
            try{
                System.out.println("Player removed: " + playerId);

                game.getGameBoard().abandonRace(game.getPlayers().get(playerId), "Abandoned race",started);


        }
            catch (Exception e){
            e.printStackTrace();
        }

        }

    }

    public void stopGame() {

        //threads.values().forEach(Thread::interrupt);
        threads.clear();
        commandQueues.clear();
        ControllerMap.clear();
        game.getPlayers().clear();
        gh.removeGame(idGame);
    }

    public void setControllerMap(Player player, Controller controller) {
        synchronized (ControllerMap) {
            System.out.println(player.GetID() + " : "+ controller.getClass());
            ControllerMap.remove(player.GetID());
            ControllerMap.put(player.GetID(), controller);

            if(buildingCount == ControllerMap.size()){
                sendMessage(new LogEvent("Building started",-1,-1,-1,-1));
                try {
                    game.getGameBoard().StartHourglass();
                }catch(RuntimeException e){
                    System.out.println(e.getMessage());
                }

                buildingCount = -1;
            }

            if (flightCount == ControllerMap.size()) {
                for (Player p : game.getPlayers().values()) {
                    p.SetReady(false);
                }
//
//            if (!flightMode){
//                stopAllPlayerThreads();
//
//            }
                flightMode = true;
                startFlightMode();
                flightCount = 0;
            }
            for (Controller controller1 : ControllerMap.values()) {
                System.out.println(controller1.getClass());
            }
        }

    }

    public void startFlightMode() {  ///  per aggiornare il

//client si riconnete ma non può inviare input fino a che non si ricambia il controller
        if (firtflight) {
            System.out.println("MERGE");
            game.getGameBoard().getCardStack().mergeDecks();
        }
        firtflight = false;
        ArrayList<Player> players = game.getGameBoard().getPlayers();
        sendMessage(new LogEvent("Flight started",-1,-1,-1,-1));
        flightThread = new Thread(() -> {
            System.out.println("PESCO CARTA!");

            Card card= game.getGameBoard().NewCard();
            sendMessage(new LogEvent("New card drawn",-1,-1,-1,-1));


            card.setConcurrentCardListener(this);
            for (String player: VirtualViewMap.keySet()) {
                card.setRandomCardEffectListeners(player, VirtualViewMap.get(player));
            }
            card.sendTypeLog();
            try{
                card.CardEffect();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }

            int index = 0;

            while (!card.isFinished()) {
            System.out.println(players.size());
                index = 0;
                int k = 0;
                Player currentPlayer = players.get(index);
                while (index < players.size() && !card.isFinished()) {
                    if (k >= 50001){
                        System.out.println("CURRENT: "+currentPlayer.GetID()+ " "+currentPlayer.getPlayerState().getClass().getSimpleName());
                        k = 0;
                    }
                    k++;
                    for( int j=0; j<players.size();j++){
                        if(!players.get(j).GetHasActed()){
                            currentPlayer = players.get(j);
                            break;
                        }
                    }

                    //Player currentPlayer = players.get(index);

                    Controller cur = ControllerMap.get(currentPlayer.GetID());

                    /// probabilmente da errore con meteoriti per la concorrenzialità e perché current non è molto deterministiFFco, potrebbe essere che vada spostato dentro al controllo di current
                    if (cur != null && !getConnection(currentPlayer.GetID())){ // se è disconnesso chiamo il comando di default
                        System.out.println("Player disconnected "+cur.getClass());
                        try {
                            cur.DefaultAction(this);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage()+ "cristo de dio");
                            //System.exit(-1);
                            //throw new ImpossibleActionException("errore nell'azione di default, che dio ci aiuti");
                        }
                        ///  credo ci vada una thìry ctch ma non la stava lanciando :)

                            ///  ready ce lomette la carta quando sa che il player deve smettere di dar input
                            if (currentPlayer.GetHasActed()) {
                                System.out.println("aggiorno index");
                                index++;
                            }

                    }

                    else { // se il player  non è disconneso prendo icommand dalla queue

                        try {
                            Command cmd = flightQueue.poll();
                            //TODO: notify della carta se è fase concorrenziale
                            //game.getPlayers().get(cmd.getPlayerId()).getmyPlayerBoard().setRewardsListener(VirtualViewMap.get(currentPlayer.GetID()));
                            if (cmd != null){
                                if(concurrent){
                                    Controller controller = ControllerMap.get(cmd.getPlayerId());
                                    controller.action(cmd, this);
                                }
                                else if (cmd.getPlayerId().equals(currentPlayer.GetID())) {
                                    Controller controller = ControllerMap.get(cmd.getPlayerId());
                                    controller.action(cmd, this);

                                    ///  ready ce lomette la carta quando sa che il player deve smettere di dar input

                                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>index "+ index);
                                    if (currentPlayer.GetHasActed()) {
                                        System.out.println("aggiornmo index");
                                        index++;
                                    }
                                }
                                else {
                                    //TODO: non ho capito che minchia fa la offer
                                    flightQueue.offer(cmd);
                                }
                            }



                        } catch (Exception e) {
                            System.out.println(e.getMessage()+ "cristo de dio");
                            break;
                        }
                    }
                }


                System.out.println("PRIMO WHILE FINITO");


            }

            System.out.println("USCITO DAL SECONDO WHILE");
            Controller ReadySetter;
            System.out.println("players "+ game.getPlayers().size());
            for (Player p : game.getPlayers().values()) {
                System.out.println("-------------------------------------------------FORCED");
                System.out.println(p.GetID()+ " is in this state: "+ p.getPlayerState().getClass());
                ReadySetter = ControllerMap.get(p.GetID());
                ReadyCommand readyCommand = new ReadyCommand(game.getID(),p.GetID(),game.getLv(),"Ready",true,"placeholder");
                ReadySetter.action(readyCommand, this);
                p.SetHasActed(false);
                ///  senno invece che mettere tutti a ready posso frli direttamente andare nell'altro contrpoller??

//                p.SetReady(true);
            }
            sendMessage(new LogEvent("Flight finished",-1,-1,-1,-1));
            flightMode = false;
        });

        flightThread.start();
        //THREAD NON FINISCE SEMPLCIMENTE STO CREANDO OGNI VOLTA UNO NUOVO NEL CASO DI CARTA NON SPECIALE

//        flightMode = false;
//        changeState();

    }

    public boolean checkGameOver() {
        return GameOver;
    }

    public void setGameOver(){
        getGame().getGameBoard().finishGame();
        GameOver = true;
        //System.out.println("Game over the winner is: " + game.getGameBoard().getPlayers().getFirst().GetID());
    }

    private void stopAllPlayerThreads() {

        for (Thread t : threads.values()) {
            t.interrupt();
        }
        threads.clear();
    }

    public void setFlightCount(int count) {
        flightCount += count;
    }

    public void setBuildingCount(int count) {
        buildingCount += count;
    }


    public Game getGame() {
        return game;
    }

    public void stopPlayer(String token) {
        String playerId = tokenToPlayerId.get(token);
        Controller curr = ControllerMap.get(playerId);
        curr.setDisconnected(true);
        synchronized (connectedPlayers){
            connectedPlayers.put(playerId, false);
        }
        //setto booleano del controller

        //if (!flightMode) {
            System.out.println("Player ID " + playerId + " not in flight mode, interrupting thread");
            threads.get(playerId).interrupt();
            threads.remove(playerId);
            Thread t = new Thread(()->{
                while (!GameOver && !getConnection(playerId)) {
                    if (!GameOver && !game.getPlayers().get(playerId).GetHasActed() && !getConnection(playerId) && !flightMode){
                        System.out.println("disconnection Thread");
                        Controller current = ControllerMap.get(playerId);
                        synchronized (lock){
                            current.DefaultAction(this);
                        }
                    }
                    if (!GameOver && game.getPlayers().get(playerId).GetHasActed() && !flightMode){
                        //System.out.println("disconnection Thread: has acted");
                    }
                }
                System.out.println("disconnection Thread finished");
            });            ;
            t.start();
            threads.put(playerId, t);
        //}

    }

    public void startPlayer(String token) {
        String playerId = tokenToPlayerId.get(token);
        System.out.println("STARTING "+playerId);
        //VirtualViewMap.get(playerId).sendEvent(new ReconnectedEvent(token,game.getGameID(),playerId, lv));
        synchronized (lock){
            threads.get(playerId).interrupt();
        }
        Controller curr = ControllerMap.get(playerId);
        curr.setDisconnected(false);
        synchronized (connectedPlayers){
            connectedPlayers.put(playerId, true);
        }
        //setto booleano del controler
        threads.remove(playerId);

        //if (!flightMode){
            System.out.println("Player ID " + playerId + " not in flight mode, starting thread");

            BlockingQueue<Command> queue = commandQueues.get(playerId);
            Thread t = new Thread(() -> {
                while (getConnection(playerId)) {
                    synchronized (ControllerMap) {
                        Controller current = ControllerMap.get(playerId);

                            Command cmd = queue.poll();
                            if (cmd != null){
                                System.out.println("Bulding command connected" );
                                current.action(cmd, this);
                            }

                    }
                }
            });
            t.start();
            threads.put(playerId, t);

        //}

    }

    public void updatePlayers(){
        ArrayList<Boolean> ready = new ArrayList<>();
        ArrayList<String> players = new ArrayList<>(VirtualViewMap.keySet());
        for (String player : players) {
            ready.add(game.getPlayers().get(player).GetReady());
        }
        sendGameLobbyUpdate(new GameLobbyEvent(players,ready));
    }

    public void sendGameLobbyUpdate(GameLobbyEvent event){
        for (GameLobbyListener listener : gameLobbyListeners) {
            listener.GameLobbyChanged(event);
        }
    }

    @Override
    public void onConcurrentCard(boolean phase) {
        this.concurrent = phase;
    }

    public int getlv(){
        return this.lv;
    }


    public String check(Command command) {
        if(isStarted()){
            return "Game already stated!";
        }
        if (command.getLv() != lv){
            return "Game level doesn't match!";
        }
        if (ControllerMap.containsKey(command.getPlayerId())) {
            return "Player "+command.getPlayerId()+" already exists in game "+game.getID();
        }
        if (ControllerMap.size() == maxPlayer){
            return "This game is full";
        }
        return "";
    }


    public void sendMessage(LogEvent event){
        System.out.println("sending message " + event.message());
        for (VirtualView vv : VirtualViewMap.values()){
            vv.sendLogEvent(event);
        }
    }

    @Override
    public void onReady() {
        updatePlayers();
    }

    @Override
    public void onEndGame(boolean success, String playerId, String message, ScoreboardEvent event) {
            try{
                System.out.println("Player removed: " + playerId);


                //REMOVING LISTENERS

                Player p = game.getPlayers().remove(playerId);
                p.getmyPlayerBoard().removeListener();
                p.removeCardListener();
                p.removeHandListener();
                p.removeReadyListener();
                p.removeFinishListener();

                VirtualView vv = getVirtualViewMap().remove(playerId);
                p.getCommonBoard().getCardStack().removeListener(p.GetID());
                p.getCommonBoard().removeListener(vv);
                p.getCommonBoard().getTilesSets().removeListeners(vv);
                gameLobbyListeners.remove(vv);

                for (VirtualView vv2 : getVirtualViewMap().values()){
                    if (vv2 != vv){
                        vv2.removeListener(vv);
                    }
                }
                Thread t = threads.remove(playerId);
                if (t != null) {
                    synchronized (lock){
                        t.interrupt();
                    }
                }
                commandQueues.remove(playerId);
                ControllerMap.remove(playerId);
                game.RemovePlayer(playerId);
                if (game.getPlayers().isEmpty()) {
                    System.out.println("Stop game");
                    lobbyListener.sendEvent(new LobbyEvent(game.getGameID(), -1 ,null, maxPlayer));
                    stopGame();
                }
                else {
                    ArrayList<String> players = new ArrayList<>(ControllerMap.keySet());
                    if (lobbyListener != null)
                        lobbyListener.sendEvent(new LobbyEvent(game.getGameID(),game.getLv() ,players, maxPlayer));
                }
                //VirtualView vv2 = VirtualViewMap.remove(playerId);
                sendMessage(new LogEvent(playerId + "quit",-1,-1,-1,-1));
                if (event != null){
                    vv.sendEvent(event);
                }
                else {
                    vv.sendEvent(new FinishGameEvent(success, message));
                }
                vv.removeListeners();
                vv.setDisconnected(true);
                updatePlayers();
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }


        public  boolean getConnection(String playerId){
            synchronized (connectedPlayers){
                return connectedPlayers.get(playerId);
            }
        }

    public HashMap<String, String> getTokenToPlayerId() {
        return tokenToPlayerId;
    }
}

package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Controller.Listeners.GameLobbyListener;
import org.example.galaxy_trucker.Controller.Listeners.LobbyListener;
import org.example.galaxy_trucker.Controller.Messages.ConcurrentCardListener;
import org.example.galaxy_trucker.Controller.Messages.GameLobbyEvent;
import org.example.galaxy_trucker.Controller.Messages.LobbyEvent;
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
public class GameController  implements ConcurrentCardListener {
    String idGame;
    private final HashMap<String,Controller> ControllerMap;
    private final HashMap<String, BlockingQueue<Command>> commandQueues = new HashMap<>();
    private final HashMap<String, Thread> threads = new HashMap<>();
    private final HashMap<String, VirtualView> VirtualViewMap = new HashMap<>();
    private final HashMap<UUID, String> tokenToPlayerId = new HashMap<>();
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

    public GameController(String idGame, Game game, GamesHandler gh) {
        this.idGame = idGame;
        ControllerMap = new HashMap<>();
        this.game = game;
        this.gh = gh;
        this.flightQueue = new LinkedBlockingQueue<>();
//        this.prepThread = new Thread(() -> {
//            while(true){
//                Command cmd = prepQueue.poll();
//            }
//        });

    }

    public void NewPlayer(Player p, VirtualView vv, UUID token){
        if (ControllerMap.keySet().contains(p.GetID())) {
            throw new IllegalArgumentException("Player ID " + p.GetID() + " already exists");
        }
        String playerId = p.GetID();
        System.out.println("Player ID: " + playerId);
        System.out.println("Token: " + token.toString());
        Controller controller = new LoginController(p, idGame);
        ControllerMap.put(playerId, controller);
        System.out.println("New player " + playerId+" in "+ this);
        tokenToPlayerId.put(token, playerId);
        BlockingQueue<Command> queue = new LinkedBlockingQueue<>();
        commandQueues.put(playerId, queue);
        game.NewPlayer(p);
        VirtualViewMap.put(playerId,vv);
        vv.setEventMatrix(game.getGameBoard().getLevel());
        p.getmyPlayerBoard().setListener(vv);
        p.getCommonBoard().getCardStack().addListener(p.GetID(),vv);
        Tile mainCockpitTile = new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE,UNIVERSAL.INSTANCE);
        mainCockpitTile.setId(color);
        color++;
        p.getmyPlayerBoard().insertTile(mainCockpitTile,6,6, false);
        p.setHandListener(vv);
        p.getmyPlayerBoard().setRewardsListener(vv);
        //p.setPhaseListener(vv);
        p.getCommonBoard().setListeners(vv);
        p.getCommonBoard().getTilesSets().setListeners(vv);
        p.setCardListner(vv);
        gameLobbyListeners.add(vv);
        updatePlayers();



        //p.getGmaebord.setVrtualview(vv);
//        p.getCommonBoard().addListener(p.GetID(),vv);

        Thread t = new Thread(() -> {
            while (true) {
                synchronized (ControllerMap) {
                    Controller current = ControllerMap.get(playerId);
                    if(current.disconnected){ //questo è il thread  dei command fuori dalla flight mode giusto?
                        //current.DefaultAction(this);
                    }
                    else{
                        Command cmd = queue.poll(); // se questa è esclusiva del player si potrebbe svuotare in caso di disconnessione
                        if (cmd != null){
                            current.action(cmd, this);
                        }
                    }
                }
                //vedi se è connesso
                //se è connesso prendi dalla coda e chiami il metodo


                //se non è connesso chiami defaultaction
            }
        });
        t.start();
        threads.put(playerId, t);
        ArrayList<String> players = new ArrayList<>(VirtualViewMap.keySet());
        lobbyListener.sendEvent(new LobbyEvent(game.getGameID(),game.getLv() ,players));
    }


    public synchronized void changeState() {

        //TODO: questo potrebbe essere molto pericoloso
        updatePlayers();

        long readyCount = game.getPlayers().values().stream()
                .filter(Player::GetReady)
                .count();

        if (readyCount == game.getPlayers().size()) {
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

    public void removePlayer(UUID token) {
        String playerId = tokenToPlayerId.get(token);
        if (playerId == null || !ControllerMap.keySet().contains(playerId)) {
            throw new IllegalArgumentException("Player ID " + playerId + " non found");
        }
        System.out.println("Player removed: " + playerId);

        Thread t = threads.remove(playerId);
        if (t != null) {
            t.interrupt();
        }
        commandQueues.remove(playerId);
        ControllerMap.remove(playerId);
        game.RemovePlayer(playerId);
        if (game.getPlayers().isEmpty()) {
            System.out.println("Stop game");
            stopGame();
        }
        ArrayList<String> players = new ArrayList<>(ControllerMap.keySet());
        lobbyListener.sendEvent(new LobbyEvent(game.getGameID(),game.getLv() ,players));

    }

    public void stopGame() {
        threads.values().forEach(Thread::interrupt);
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
        flightThread = new Thread(() -> {
            System.out.println("PESCO CARTA!");
            Card card= game.getGameBoard().NewCard();
            card.setConcurrentCardListener(this);
            //SET

                    //game.getGameBoard().getPlayers().getFirst()
            while (!card.isFinished()) {

                int index = 0;
                /// il game pesca la carta automaticamente
                //game.getGameBoard().NewCard();

                while (index < players.size()) {
                    Player currentPlayer = players.get(index);
                    //prendi controller
                    //se è disconnesso chiami def action
                    //altrimenti esegui il blocco try
                    Controller cur = ControllerMap.get(currentPlayer.GetID());
                    if (cur.disconnected==true){ // se è disconnesso chiamo il comando di default
                        try {
                            cur.DefaultAction(this);
                        } catch (Exception e) {
                            throw new ImpossibleActionException("errore nell'azione di default, che dio ci aiuti");
                        }
                        ///  credo ci vada una thìry ctch ma non la stava lanciando :)

                            ///  ready ce lomette la carta quando sa che il player deve smettere di dar input
                            if (currentPlayer.GetHasActed()) {
                                index++;
                            }

                    }

                    else { // se il player  non è disconneso prendo icommand dalla queue
                        //TODO : aggiungere se detto da cugola timout

                        try {
                            Command cmd = flightQueue.take();
                            //TODO: notify della carta se è fase concorrenziale
                            if(concurrent){
                                Controller controller = ControllerMap.get(cmd.getPlayerId());
                                controller.action(cmd, this);
                            }
                            else if (cmd.getPlayerId().equals(currentPlayer.GetID())) {
                                Controller controller = ControllerMap.get(cmd.getPlayerId());
                                controller.action(cmd, this);

                                ///  ready ce lomette la carta quando sa che il player deve smettere di dar input
                                if (currentPlayer.GetHasActed()) {
                                    index++;
                                }
                            } else {

                                flightQueue.offer(cmd);
                            }

                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }


                //System.out.println("Flight phase complete");

            }
            for (Player p : game.getPlayers().values()) {
                p.SetReady(true);
            }
        });

        flightThread.start();

        System.out.println("Thread volo finito");
        flightMode = false;
        changeState();

    }

    public boolean checkGameOver() {
        return GameOver;
    }

    public void setGameOver(){
        GameOver = true;
        System.out.println("Game over the winner is: " + game.getGameBoard().getPlayers().getFirst().GetID());
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

    public void stopPlayer(UUID token) {
        String playerId = tokenToPlayerId.get(token);
        Controller curr = ControllerMap.get(playerId);
        curr.setDisconnected(true);
        //setto booleano del controller

        //if (!flightMode) {
            System.out.println("Player ID " + playerId + " not in flight mode, interrupting thread");
            threads.get(playerId).interrupt();
            threads.remove(playerId);
            Thread t = new Thread(()->{
                while (true) {
                    if (!game.getPlayers().get(playerId).GetHasActed()){
                        Controller current = ControllerMap.get(playerId);
                        current.DefaultAction(this);
                    }

                }
            });            ;
            t.start();
            threads.put(playerId, t);
        //}

    }

    public void startPlayer(UUID token) {
        String playerId = tokenToPlayerId.get(token);
        threads.get(playerId).interrupt();
        //setto booleano del controler
        threads.remove(playerId);

        //if (!flightMode){
            System.out.println("Player ID " + playerId + " not in flight mode, starting thread");

            BlockingQueue<Command> queue = commandQueues.get(playerId);
            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        Command cmd = queue.take();
                        Controller current = ControllerMap.get(playerId);
                        current.action(cmd, this);
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted: " + playerId);
                        break;
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
}

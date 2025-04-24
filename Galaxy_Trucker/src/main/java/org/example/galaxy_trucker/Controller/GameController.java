package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class GameController {
    String idGame;
    private final HashMap<String,Controller> ControllerMap;
    private final HashMap<String, BlockingQueue<Command>> commandQueues = new HashMap<>();
    private final HashMap<String, Thread> threads = new HashMap<>();
    private final HashMap<String, VirtualView> VirtualViewMap = new HashMap<>();
    final Game game;
    private GamesHandler gh;
    private BlockingQueue<Command> flightQueue = new LinkedBlockingQueue<>();
    private Thread flightThread;
    private boolean flightMode = false;
    int flightCount = 0;
    int buildingCount = 0;
    boolean GameOver = false;
    private boolean started = false;
    private int color = 153;

    public boolean isStarted() {
        return started;
    }


    public GameController(String idGame, Game game, GamesHandler gh) {
        this.idGame = idGame;
        ControllerMap = new HashMap<>();
        this.game = game;
        this.gh = gh;
        this.flightQueue = new LinkedBlockingQueue<>();

    }

    public void NewPlayer(Player p, VirtualView vv) throws IOException {
        if (ControllerMap.keySet().contains(p.GetID())) {
            throw new IllegalArgumentException("Player ID " + p.GetID() + " already exists");
        }
        String playerId = p.GetID();
        Controller controller = new LoginController(p, idGame);
        ControllerMap.put(playerId, controller);
        System.out.println("New player " + playerId+" in "+ this);

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
        p.getmyPlayerBoard().insertTile(mainCockpitTile,6,6);
        p.setHandListener(vv);
        p.getCommonBoard().getTilesSets().setListeners(vv);

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

    }


    public synchronized void changeState() {
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
            if (queue != null) {
                queue.offer(command);
            } else {
                System.out.println("Empty queue for: " + command.getPlayerId());
            }
        }
        else {
            this.flightQueue.offer(command);
        }
    }

    public void removePlayer(String playerId) {
        if (!ControllerMap.keySet().contains(playerId)) {
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
            stopGame();
        }
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
        ControllerMap.put(player.GetID(), controller);

        if(buildingCount == ControllerMap.size()){
            game.getGameBoard().StartHourglass();
            buildingCount = -1;
        }

        if (flightCount == ControllerMap.size()) {
            for (Player p : game.getPlayers().values()) {
                p.SetReady(false);
            }
            game.getGameBoard().getCardStack().mergeDecks();

            flightMode = true;
            startFlightMode();
            flightCount = -1;
        }
    }

    public void startFlightMode() {


        ArrayList<Player> players = game.getGameBoard().getPlayers();
        stopAllPlayerThreads();
        flightThread = new Thread(() -> {

            while (!checkGameOver()) {
                int index = 0;
                game.getGameBoard().NewCard();

                while (index < players.size()) {
                    Player currentPlayer = players.get(index);
                    try {
                        Command cmd = flightQueue.take();

                        if (cmd.getPlayerId().equals(currentPlayer.GetID())) {
                            Controller controller = ControllerMap.get(cmd.getPlayerId());
                            controller.action(cmd, this);

                            if (currentPlayer.GetReady()) {
                                index++;
                            }
                        } else {

                            flightQueue.offer(cmd);
                        }

                    } catch (InterruptedException e) {
                        break;
                    }
                }


                System.out.println("Flight phase complete");

            }
            stopGame();
        });
        flightThread.start();

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



}

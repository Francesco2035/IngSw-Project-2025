package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class GameController {
    String idGame;
    private final HashMap<String,Controller> ControllerMap;
    private final HashMap<String, BlockingQueue<Command>> commandQueues = new HashMap<>();
    private final HashMap<String, Thread> threads = new HashMap<>();
    final Game game;
    private GamesHandler gh;


    public GameController(String idGame, Game game, GamesHandler gh) {
        this.idGame = idGame;
        ControllerMap = new HashMap<>();
        this.game = game;
        this.gh = gh;
    }

    public void NewPlayer(Player p){
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
                game.getPlayers().values().forEach(p -> p.SetReady(false));
            }
        }
    }

    public void addCommand(Command command) {
        BlockingQueue<Command> queue = commandQueues.get(command.getPlayerId());
        if (queue != null) {
            queue.offer(command);
        } else {
            System.out.println("Empty queue for: " + command.getPlayerId());
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
        game.getPlayers().remove(playerId);
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
    }

}

package org.example.galaxy_trucker.Commands;

import java.util.HashMap;
import java.util.Map;

public class CommandInterpreter {

    private String playerId;
    private String gameId;
    private int lv;

    // Mappa per associare i comandi alla loro rispettiva lambda
    private Map<String, CommandCreator> commandMap;

    public CommandInterpreter(String playerId, String gameId) {
        this.playerId = playerId;
        this.gameId = gameId;
        initializeCommandMap();
    }

    public void setlv(int lv){
        this.lv = lv;
    }

    // Inizializza la mappa dei comandi con il loro interprete
    private void initializeCommandMap() {
        commandMap = new HashMap<>();
        commandMap.put("Login", this::createLoginCommand);
        commandMap.put("InsertTile", this::createInsertTileCommand);
        commandMap.put("PickTile", this::createInsertTileCommand);
        commandMap.put("Accept", this::createAcceptCommand);
        commandMap.put("AddCrew", this::createAddCrewCommand);
        commandMap.put("ChoosingPlanet", this::createChoosingPlanetCommand);
        commandMap.put("ConsumeEnergy", this::createConsumeEnergyCommand);
        commandMap.put("Quit", this::createQuitCommand);
        commandMap.put("Ready", this::createReadyCommand);
        // Aggiungi altri comandi qui...
    }

    private Command createReadyCommand(String[] strings) {

        boolean accepted = Boolean.parseBoolean(strings[1]);
        return new ReadyCommand(gameId, playerId,lv, "Ready", accepted);
    }

    private Command createQuitCommand(String[] strings) {
        return new ReadyCommand(gameId,playerId,lv,"Quit",false);
    }

    private Command createConsumeEnergyCommand(String[] strings) {
        return null;
    }

    private Command createChoosingPlanetCommand(String[] strings) {
        return null;
    }

    private Command createAddCrewCommand(String[] strings) {
        return null;
    }

    private Command createAcceptCommand(String[] strings) {
        return null;
    }

    public Command interpret(String commandString) {
        String[] parts = commandString.split(" ");
        String commandTitle = parts[0];
        CommandCreator creator = commandMap.get(commandTitle);

        if (creator == null) {
            throw new IllegalArgumentException("Comando non riconosciuto: " + commandTitle);
        }

        return creator.createCommand(parts);
    }

    private Command createLoginCommand(String[] parts) {
        if (parts.length != 4) {
            throw new IllegalArgumentException("Comando Login richiede 3 argomenti: nome giocatore, nome gioco, livello");
        }

        String playerName = parts[1];
        String gameName = parts[2];
        String level = parts[3];
        int levelInt  = 0;
        try {
            levelInt = Integer.parseInt(level);
        } catch (NumberFormatException e) {
            System.out.println("Errore: la stringa non è un numero valido.");
        }
        setlv(levelInt);

        return new LoginCommand(gameId,playerName, levelInt, "Login");
    }

    // Funzione che crea un InsertTileCommand
    private Command createInsertTileCommand(String[] parts) {
        if (parts.length != 5) {
            throw new IllegalArgumentException("Comando InsertTile richiede 4 argomenti: x, y, rotazione, posizione");
        }
        String title = parts[0];
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        int rotation = Integer.parseInt(parts[3]);
        int position = Integer.parseInt(parts[4]);

        return new InsertTileCommand(x, y, rotation,position, gameId,playerId, lv, title);
    }


    @FunctionalInterface
    public interface CommandCreator {
        Command createCommand(String[] parts);
    }
}

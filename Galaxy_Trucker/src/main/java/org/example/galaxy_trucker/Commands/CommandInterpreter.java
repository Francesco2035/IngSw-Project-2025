package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.HashMap;
import java.util.Map;

public class CommandInterpreter {

    private String playerId;
    private String gameId;
    private int lv;

    private Map<String, CommandCreator> commandMap;

    public CommandInterpreter(String playerId, String gameId) {
        this.playerId = playerId;
        this.gameId = gameId;
        initializeCommandMap();
    }

    public void setlv(int lv){
        this.lv = lv;
    }

    private void initializeCommandMap() {
        commandMap = new HashMap<>();
        commandMap.put("Login", this::createLoginCommand);
        commandMap.put("InsertTile", this::createInsertTileCommand);
        commandMap.put("PickTile", this::createInsertTileCommand);
        commandMap.put("Hourglass", this::createInsertTileCommand);
        commandMap.put("Accept", this::createAcceptCommand);
        commandMap.put("Discard", this::createInsertTileCommand);
        commandMap.put("FromBuffer", this::createAcceptCommand);
        commandMap.put("ToBuffer", this::createAcceptCommand);
        commandMap.put("AddCrew", this::createAddCrewCommand);
        commandMap.put("ChoosingPlanet", this::createChoosingPlanetCommand);
        commandMap.put("ConsumeEnergy", this::createConsumeEnergyCommand);
        commandMap.put("Quit", this::createQuitCommand);
        commandMap.put("Ready", this::createReadyCommand);
        //  altri comandi
        commandMap.put("DebugShip", this::createDebugShip);
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

        String level = parts[3];
        int levelInt  = 0;
        try {
            levelInt = Integer.parseInt(level);
        } catch (NumberFormatException e) {
            System.out.println("Errore: la stringa non è un numero valido.");
        }
        setlv(levelInt);

        return new LoginCommand(gameId,playerId, levelInt, "Login");
    }

    private Command createInsertTileCommand(String[] parts) {

        String title = parts[0];
        int x = -1;
        int y = -1;
        int rotation = 0;
        int position =-1;
        switch (title){
            case "InsertTile":{
                if (parts.length != 4) {
                    throw new IllegalArgumentException("Comando InsertTile richiede 4 argomenti: x, y, rotazione");
                }
                x = Integer.parseInt(parts[1]);
                y = Integer.parseInt(parts[2]);
                rotation = Integer.parseInt(parts[3]);
                break;
            }
            case "PickTile":{
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Comando InsertTile richiede 1 argomento: posizione");
                }
                position = Integer.parseInt(parts[1]);
                break;

            }
            case "Hourglass", "Discard":{
                if (parts.length != 1) {
                    throw new IllegalArgumentException("Comando Hourglass e Discard non richiedono nessun argomento");
                }
                break;
            }
            case "ToBuffer" :{
                if (parts.length != 3) {
                    throw new IllegalArgumentException("Comando Hourglass richiede 2 argomenti: posizione e rotazione");
                }
                rotation = Integer.parseInt(parts[1]);
                position = Integer.parseInt(parts[2]);
                break;
            }
            case "FromBuffer" :{
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Comando FromBuffer richiede 1 argomento: posizione");
                }
                position = Integer.parseInt(parts[1]);
                break;
            }
        }

        return new InsertTileCommand(x, y, rotation,position, gameId,playerId, lv, title);
    }


    @FunctionalInterface
    public interface CommandCreator {
        Command createCommand(String[] parts);
    }




    private Command createDebugShip(String[] strings) {

        String title = strings[0];
        return new DebugShip(gameId,playerId, lv, title);
    }
}

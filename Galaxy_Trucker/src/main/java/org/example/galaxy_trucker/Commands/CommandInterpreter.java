package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.HashMap;
import java.util.Map;

public class CommandInterpreter {

    private String playerId;
    private String gameId;
    private int lv;
    private String token;

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
        commandMap.put("InsertTile", this::createBuildingCommand);
        commandMap.put("PickTile", this::createBuildingCommand);
        commandMap.put("Hourglass", this::createBuildingCommand);
        commandMap.put("SeeDeck", this::createBuildingCommand);
        commandMap.put("Accept", this::createAcceptCommand);
        commandMap.put("Discard", this::createBuildingCommand);
        commandMap.put("FromBuffer", this::createBuildingCommand);
        commandMap.put("ToBuffer", this::createBuildingCommand);
        commandMap.put("AddCrew", this::createAddCrewCommand);
        commandMap.put("AddBrownAlien", this::createAddCrewCommand);
        commandMap.put("AddPurpleAlien", this::createAddCrewCommand);
        commandMap.put("ChoosingPlanet", this::createChoosingPlanetCommand);
        commandMap.put("ConsumeEnergy", this::createConsumeEnergyCommand);
        commandMap.put("Quit", this::createQuitCommand);
        commandMap.put("Ready", this::createReadyCommand);
        commandMap.put("RemoveTile", this::createRemoveTileCommand);
        //  altri comandi
        commandMap.put("DebugShip", this::createDebugShip);

        commandMap.put("Reconnect", this::createReconnectCommand);
    }

    private Command createReconnectCommand(String[] strings) {
        ReconnectCommand CMD = new ReconnectCommand(token, gameId, playerId,lv, "Reconnect");
        return CMD;
    }


    private Command createReadyCommand(String[] strings) {

        boolean accepted = Boolean.parseBoolean(strings[1]);
        return new ReadyCommand(gameId, playerId,lv, "Ready", accepted, token);
    }

    private Command createQuitCommand(String[] strings) {
        return new ReadyCommand(gameId,playerId,lv,"Quit",false, token);
    }

    private Command createConsumeEnergyCommand(String[] strings) {
        return null;
    }

    private Command createChoosingPlanetCommand(String[] strings) {
        return null;
    }

    private Command createAddCrewCommand(String[] strings) {

        if (strings.length != 3) {
            throw new IllegalArgumentException("Comando AddCrew richiede 3 argomenti: titolo, x e y");
        }
        String title = strings[0];
        int x = Integer.parseInt(strings[1]);
        int y = Integer.parseInt(strings[2]);

        return switch (title) {
            case "AddCrew" ->
                    new AddCrewCommand(2, false, false, new IntegerPair(x, y), gameId, playerId, lv, "AddCrew", token);
            case "AddPurpleAlien" ->
                    new AddCrewCommand(0, true, false, new IntegerPair(x, y), gameId, playerId, lv, "AddPurpleAlien", token);
            case "AddBrownAlien" ->
                    new AddCrewCommand(0, false, true, new IntegerPair(x, y), gameId, playerId, lv, "AddBrownAlien", token);
            default -> throw new InvalidInput("invalid input");
        };

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

    private Command createBuildingCommand(String[] parts) {

        String title = parts[0];
        int x = -1;
        int y = -1;
        int rotation = 0;
        int position =-1;
        int index = -1;
        switch (title){

            case "SeeDeck":{
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Comando SeeDeck richiede 1 argomento: numero deck scelto");
                }
                x = Integer.parseInt(parts[1]);
                break;
            }

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
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Comando ToBuffer richiede 1 argomento: rotazione");
                }
                rotation = Integer.parseInt(parts[1]);
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

        return new BuildingCommand(x, y, rotation,position, gameId,playerId, lv, title, token);
    }

    private Command createRemoveTileCommand(String[] parts) {
        if (parts.length != 3) {
            throw new IllegalArgumentException("Comando Hourglass richiede 2 argomenti: x e y");
        }
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        return new RemoveTileCommand(x,y,gameId,playerId,lv, "RemoveTileCommand", token);
    }


    @FunctionalInterface
    public interface CommandCreator {
        Command createCommand(String[] parts);
    }




    private Command createDebugShip(String[] strings) {

        String title = strings[0];
        return new DebugShip(gameId,playerId, lv, title, token);
    }

    public void setToken(String token) {
        this.token = token;
    }
}

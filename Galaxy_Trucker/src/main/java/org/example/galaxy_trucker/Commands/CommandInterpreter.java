package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandInterpreter {
//TODO: creare i comandi di rifiuto senza dover passare -1 -1
    private String playerId;
    private String gameId;
    private int lv;
    private String token;
    private ClientInterface client;

    public int getLv(){
        return lv;
    }

    public void setClient(ClientInterface client) {
        this.client = client;
    }

    public ClientInterface getClient() {
        return client;
    }

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
        commandMap.put("LOGIN", this::createLoginCommand);
        commandMap.put("INSERTTILE", this::createBuildingCommand);
        commandMap.put("PICKTILE", this::createBuildingCommand);
        commandMap.put("HOURGLASS", this::createBuildingCommand);
        commandMap.put("SEEDECK", this::createBuildingCommand);
        commandMap.put("DISCARD", this::createBuildingCommand);
        commandMap.put("FROMBUFFER", this::createBuildingCommand);
        commandMap.put("TOBUFFER", this::createBuildingCommand);
        commandMap.put("FINISHBUILDING", this::createFinishBuildingCommand);
        commandMap.put("ADDCREW", this::createAddCrewCommand);
        commandMap.put("ADDBROWNALIEN", this::createAddCrewCommand);
        commandMap.put("ADDPURPLEALIEN", this::createAddCrewCommand);
        commandMap.put("QUIT", this::createQuitCommand);
        commandMap.put("READY", this::createReadyCommand);
        commandMap.put("NOTREADY", this::createReadyCommand);
        commandMap.put("REMOVETILE", this::createRemoveTileCommand);
        //  altri comandi
        commandMap.put("DEBUGSHIP", this::createDebugShip);

        commandMap.put("RECONNECT", this::createReconnectCommand);

        commandMap.put("CONSUMEENERGY", this::createConsumeEnergyCommand);
        commandMap.put("ACCEPT", this::createAcceptCommand);
        commandMap.put("DECLINE", this::createAcceptCommand);
        commandMap.put("CHOOSEPLANET", this::createChoosingPlanetsCommand); // il command usa planets con la s ma tu più volte lo hai scritto senza che faccio?
        commandMap.put("DEFENDLARGE",this::createDefendFromLargeCommand);
        commandMap.put("DEFENDSMALL",this::createDefendFromSmallCommand);
        commandMap.put("KILL", this::createKillCommand);
        commandMap.put("GIVEATTACK", this::createGiveAttackCommand);
        commandMap.put("GIVESPEED", this::createGiveSpeedCommand);

        commandMap.put("FINISHCARGO", this::createHandleCargoCommand);
        //commandMap.put("PUTINSTORAGE", this::createHandleCargoCommand);
        commandMap.put("GETREWARD", this::createHandleCargoCommand);
        //commandMap.put("GETFROMSTORAGE", this::createHandleCargoCommand);
        commandMap.put("DISCARDCARGO", this::createHandleCargoCommand);
        commandMap.put("SWITCH", this::createHandleCargoCommand);
        commandMap.put("THEFT", this::createHandleCargoCommand);
        commandMap.put("SELECTCHUNK", this::creatselectchunkCommand);

    }

    private Command creatselectchunkCommand(String[] strings) {
        int x;
        int y;
        ArrayList<IntegerPair> coordinates = new ArrayList<>();
        if (strings.length != 3) {
            throw new IllegalArgumentException("Comando selectChunk richiede 2 argomenti: le coordinate");
        }
        x = Integer.parseInt(strings[1]);
        y = Integer.parseInt(strings[2]);

        return new SelectChunkCommand(new IntegerPair(x,y),gameId,playerId,lv, "SelectChunkCommand",token);
    }

    private Command createHandleCargoCommand(String[] strings) {
        int x1 = -1;
        int y1 = -1;
        int x2 = -1;
        int y2 = -1;
        int position1 = -1;
        int position2 = -1;
        String title = strings[0];
        switch (title){
            case "FINISHCARGO":{
                if (strings.length != 1) {
                    throw new IllegalArgumentException("Comando FinishCargo non richiede argomenti");
                }
                title = "FinishCargo";
                break;
            }
            case "SWITCH":{
                if (strings.length != 7) {
                    throw new IllegalArgumentException("Comando Switch richiede 6 argomenti: x1, y1, pos1, x2, y2, pos2");
                }
                title = "Switch";
                x1 = Integer.parseInt(strings[1]);
                y1 = Integer.parseInt(strings[2]);
                x2= Integer.parseInt(strings[4]);
                y2= Integer.parseInt(strings[5]);
                position1= Integer.parseInt(strings[3]);
                position2= Integer.parseInt(strings[6]);

                break;
            }
            case "DISCARDCARGO":{
                if (strings.length != 4) {
                    throw new IllegalArgumentException("Comando DiscardCargo richiede 3 argomenti: x1, y1, pos1");
                }
                title = "Discard";
                x1 = Integer.parseInt(strings[1]);
                y1 = Integer.parseInt(strings[2]);
                position1= Integer.parseInt(strings[3]);

                break;
            }

            case "THEFT":{
                if (strings.length != 4) {
                    throw new IllegalArgumentException("Comando Theft richiede 3 argomenti: x1, y1, pos1");
                }
                x1 = Integer.parseInt(strings[1]);
                y1 = Integer.parseInt(strings[2]);
                position1= Integer.parseInt(strings[3]);
                return new TheftCommand(position1,new IntegerPair(x1,y1));
            }
            case "GETREWARD":{
                if (strings.length != 5) {
                    throw new IllegalArgumentException("Comando Switch richiede 4 argomenti: posRewards, x1, y1, pos1");
                }
                title = "GetFromRewards";
                position2= Integer.parseInt(strings[1]);
                x1 = Integer.parseInt(strings[2]);
                y1 = Integer.parseInt(strings[3]);
                position1= Integer.parseInt(strings[4]);
                break;
            }
        }
        return new HandleCargoCommand(position1, new IntegerPair(x1,y1),position2, new IntegerPair(x2,y2), gameId, playerId, lv, title, token);
    }


    private Command createReconnectCommand(String[] strings) {
        ReconnectCommand CMD = new ReconnectCommand(token, gameId, playerId,lv, "Reconnect");
        CMD.setClient(client);
        return CMD;
    }


    private Command createReadyCommand(String[] strings) {

        boolean accepted = Boolean.parseBoolean(strings[1]);
        return new ReadyCommand(gameId, playerId,lv, "Ready", accepted, token);
    }

    private Command createQuitCommand(String[] strings) {
        return new ReadyCommand(gameId,playerId,lv,"Quit",false, token);
    }




    private Command createAddCrewCommand(String[] strings) {

        if (strings.length != 3) {
            throw new IllegalArgumentException("Comando AddCrew richiede 3 argomenti: titolo, x e y");
        }
        String title = strings[0];
        int x = Integer.parseInt(strings[1]);
        int y = Integer.parseInt(strings[2]);

        return switch (title) {
            case "ADDCREW" ->
                    new AddCrewCommand(2, false, false, new IntegerPair(x, y), gameId, playerId, lv, "AddCrew", token);
            case "ADDPURPLEALIEN" ->
                    new AddCrewCommand(0, true, false, new IntegerPair(x, y), gameId, playerId, lv, "AddPurpleAlien", token);
            case "ADDBROWNALIEN" ->
                    new AddCrewCommand(0, false, true, new IntegerPair(x, y), gameId, playerId, lv, "AddBrownAlien", token);
            default -> throw new InvalidInput("invalid input");
        };

    }


    public Command interpret(String commandString) {
        String[] parts = commandString.split(" ");
        String commandTitle = parts[0];
        commandTitle = commandTitle.toUpperCase();
        parts[0] = commandTitle;
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
        switch (title){

            case "SEEDECK":{
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Comando SeeDeck richiede 1 argomento: numero deck scelto");
                }
                x = Integer.parseInt(parts[1]);
                break;
            }

            case "INSERTTILE":{
                if (parts.length != 4) {
                    throw new IllegalArgumentException("Comando InsertTile richiede 4 argomenti: x, y, rotazione");
                }
                x = Integer.parseInt(parts[1]);
                y = Integer.parseInt(parts[2]);
                rotation = Integer.parseInt(parts[3]);
                break;
            }
            case "PICKTILE":{
                if (parts.length != 2 && parts.length != 1) {
                    throw new IllegalArgumentException("picktile -> covered, picktile i -> uncovered ");
                }
                if (parts.length == 2) {
                    position = Integer.parseInt(parts[1]);
                }
                break;

            }
            case "HORGLASS", "DISCARD":{
                if (parts.length != 1) {
                    throw new IllegalArgumentException("Comando Hourglass e Discard non richiedono nessun argomento");
                }
                break;
            }
            case "TOBUFFER" :{
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Comando ToBuffer richiede 1 argomento: rotazione");
                }
                rotation = Integer.parseInt(parts[1]);
                break;
            }
            case "FROMBUFFER" :{
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Comando FromBuffer richiede 1 argomento: posizione");
                }
                position = Integer.parseInt(parts[1]);
                break;
            }
        }

        return new BuildingCommand(x, y, rotation,position, gameId,playerId, lv, title, token);
    }


    private Command createFinishBuildingCommand(String[] parts) {
        int index = -1;

        if (lv == 2) {
            if(parts.length != 2 )
                throw new IllegalArgumentException("Comando FinishBuilding richiede 1 argomento: Posizione iniziale scelta");
            else
                index = Integer.parseInt(parts[1]);
        }
        else if (parts.length != 1 && lv == 1)
            throw new IllegalArgumentException("Comando FinishBuilding non richiede argomenti");

        return new FinishBuildingCommand(index, gameId,playerId, lv, "FinishBuilding", token);
    }


    private Command createRemoveTileCommand(String[] parts) {
        if (parts.length != 3) {
            throw new IllegalArgumentException("Comando Hourglass richiede 2 argomenti: x e y");
        }
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        return new RemoveTileCommand(x,y,gameId,playerId,lv, "RemoveTileCommand", token);
    }


    private Command createAcceptCommand(String[] parts){
        boolean accept;
        if (parts.length != 1) {
            throw new IllegalArgumentException("Comando Accept richiede 1 argomento: se si accetta o meno");
        }
        if(parts[0].equals("ACCEPT")){
            accept = true;
        } else if (parts[0].equals("DECLINE")) {
            accept = false;
        }
        else{
            throw new IllegalArgumentException("Accept | Decline");
        }
        return new AcceptCommand(gameId,playerId,lv,"AcceptCommand",accept,token);
    }

    private Command createChoosingPlanetsCommand(String[] parts) {
        if (parts.length != 2) {
            throw new IllegalArgumentException("Comando ChoosingPlanets richiede 1 argomento: numero del pianeta");
        }
        int x = Integer.parseInt(parts[1]);
        return new ChoosingPlanetsCommand(x,gameId,playerId,lv, "ChoosingPlanetsCommand",token);
    }
    /// per gli integer pair pretendo un numero pari di stringhe + il title o altre cose da includere, quindi ogni coordinata è due stringhe separate di parts
    /// si può assolutyamente cambiare se non piace :)
    /// sempre arboitrariamente ho deciso che per leggere null possiamo passare semplicemente -1 come coordinata perché sicuro insensata altrimenti
    /// questo lo faccio solo se ha senso che il player dia null come coordinata, altrimenti la considero -1 -1 e lancerà poi errore
    private Command createConsumeEnergyCommand(String[] parts) {
        int x;
        int y;
        ArrayList<IntegerPair> coordinates = new ArrayList<>();
        if ((parts.length-1)%2 != 0) {
            throw new IllegalArgumentException("Comando ConsumeEnergy richiede un numero pari di argomenti: le coordinate"); // anche se dubito possa essere colpa del player se succedono casini qui ma vabbé
        }
        for (int i = 1; i < parts.length; i+=2) {
            x = Integer.parseInt(parts[i]);
            y = Integer.parseInt(parts[i+1]);
            coordinates.add(new IntegerPair(x,y));
        }
        return new ConsumeEnergyCommand(coordinates,gameId,playerId,lv, "ConsumeEnergyCommand",token);
    }

    private Command createDefendFromLargeCommand(String[] parts) {
        int x;
        int y;
        IntegerPair plasmaDrill;
        IntegerPair energyStorage;
        if (parts.length != 5) {
            throw new IllegalArgumentException("Comando DefendFromLarge richiede 4 argomenti: le due coordinate del cannone e dell'eventuale energia da consumare"); // anche se dubito possa essere colpa del player se succedono casini qui ma vabbé
        }
        x= Integer.parseInt(parts[1]);
        y= Integer.parseInt(parts[2]);
        if(x==-1 && y==-1){
            plasmaDrill = null;
        }
        else {
            plasmaDrill = new IntegerPair(x,y);
        }
        x= Integer.parseInt(parts[3]);
        y= Integer.parseInt(parts[4]);
        if(x==-1 && y==-1){
             energyStorage = null;
        }
        else {
            energyStorage = new IntegerPair(x,y);
        }
        return new DefendFromLargeCommand(plasmaDrill,energyStorage,gameId,playerId,lv, "DefendFromLargeCommand",token);
    }

    private Command createDefendFromSmallCommand(String[] parts) {
        int x;
        int y;
        IntegerPair energyStorage;
        if (parts.length != 3) {
            throw new IllegalArgumentException("Comando DefendFromSmall richiede 2 argomenti: le due coordinate dell'energia da consumare"); // anche se dubito possa essere colpa del player se succedono casini qui ma vabbé
        }
        x=Integer.parseInt(parts[1]);
        y=Integer.parseInt(parts[2]);
        if(x==-1 && y==-1){
            energyStorage = null;
        }
        else {
            energyStorage = new IntegerPair(x,y);
        }
        return new DefendFromSmallCommand(energyStorage,gameId,playerId,lv, "DefendFromSmallCommand",token);
    }

    private Command createGiveAttackCommand(String[] parts) {
        int x;
        int y;
        ArrayList<IntegerPair> coordinates = new ArrayList<>();
        if ((parts.length-1)%2 != 0) {
            throw new IllegalArgumentException("Comando GiveAttack richiede un numero pari di argomenti: le coordinate"); // anche se dubito possa essere colpa del player se succedono casini qui ma vabbé
        }
        for (int i = 1; i < parts.length; i+=2) {
            x = Integer.parseInt(parts[i]);
            y = Integer.parseInt(parts[i+1]);
            coordinates.add(new IntegerPair(x,y));
        }
        return new GiveAttackCommand(coordinates,gameId,playerId,lv,"GiveAttackCommand",token);
    }

    private Command createGiveSpeedCommand(String[] parts) {
        int x;
        int y;
        ArrayList<IntegerPair> coordinates = new ArrayList<>();
        if ((parts.length-1)%2 != 0) {
            throw new IllegalArgumentException("Comando GiveSpeed richiede un numero pari di argomenti: le coordinate"); // anche se dubito possa essere colpa del player se succedono casini qui ma vabbé
        }
        for (int i = 1; i < parts.length; i+=2) {
            x = Integer.parseInt(parts[i]);
            y = Integer.parseInt(parts[i+1]);
            coordinates.add(new IntegerPair(x,y));
        }
        return new GiveSpeedCommand(coordinates,gameId,playerId,lv,"GiveSpeedCommand",token);
    }

    private Command createKillCommand(String[] parts) {
        int x;
        int y;
        ArrayList<IntegerPair> coordinates = new ArrayList<>();
        if ((parts.length-1)%2 != 0) {
            throw new IllegalArgumentException("Comando Kill richiede un numero pari di argomenti: le coordinate"); // anche se dubito possa essere colpa del player se succedono casini qui ma vabbé
        }

        for (int i = 1; i < parts.length; i+=2) {
            x = Integer.parseInt(parts[i]);
            y = Integer.parseInt(parts[i+1]);
            coordinates.add(new IntegerPair(x,y));
        }
        return new KillCommand(coordinates,gameId,playerId,lv,"KillCommand",token);
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

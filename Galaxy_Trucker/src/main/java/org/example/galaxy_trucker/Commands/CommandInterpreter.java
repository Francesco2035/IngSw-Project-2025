package org.example.galaxy_trucker.Commands;

import org.example.galaxy_trucker.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The CommandInterpreter class is responsible for parsing and interpreting command strings,
 * converting them into appropriate Command objects for execution. The commands are used
 * in the context of a multiplayer game system.
 */
public class CommandInterpreter {
    /**
     * A unique identifier representing a specific player.
     * This value is used to distinguish between different players
     * in the system or application.
     */
    private String playerId;
    /**
     * A unique identifier representing a specific game instance.
     * This identifier is used to distinguish one game from another
     * and is typically assigned when the game is created.
     */
    private String gameId;
    /**
     * Represents the level or value associated with a specific context.
     * This integer variable may be used to indicate depth, rank,
     * or any other measurable quantity defined in the program.
     */
    private int lv;
    /**
     * A string variable used to store a token, which may represent
     * a unique identifier, authentication credential, or other
     * encoded information for specific application purposes.
     */
    private String token;
    /**
     * Represents the client interface instance used to interact with the underlying client implementation.
     * This variable provides access to client-specific operations and functionalities.
     */
    private ClientInterface client;

    /**
     * Retrieves the value of the level (lv) field.
     *
     * @return the current value of the lv field
     */
    public int getLv(){
        return lv;
    }

    /**
     * Sets the client instance for use in the current context.
     *
     * @param client the client instance to set, implementing the ClientInterface
     */
    public void setClient(ClientInterface client) {
        this.client = client;
    }

    /**
     * Retrieves the client instance.
     *
     * @return the client instance implementing ClientInterface
     */
    public ClientInterface getClient() {
        return client;
    }

    /**
     * A mapping of command names to their corresponding CommandCreator instances.
     * This map is used to associate specific command names with the logic
     * required to create or handle those commands.
     */
    private Map<String, CommandCreator> commandMap;

    /**
     * Constructs a CommandInterpreter object with a specific player ID and game ID.
     *
     * @param playerId the unique identifier of the player
     * @param gameId the unique identifier of the game
     */
    public CommandInterpreter(String playerId, String gameId) {
        this.playerId = playerId;
        this.gameId = gameId;
        initializeCommandMap();
    }

    /**
     * Sets the value of the level field.
     *
     * @param lv the value to be set for the level field
     */
    public void setlv(int lv){
        this.lv = lv;
    }

    /**
     * Initializes the command map with command identifiers mapping to their respective command creation methods.
     * Each command identifier string is associated with the functional method that creates the corresponding command object.
     * This mapping is used to dynamically handle and execute commands throughout the application.
     *
     * The `commandMap` is implemented as a {@link HashMap}, allowing efficient storage and retrieval of commands
     * based on their identifiers. The commands are stored in a key-value pair format where the key is the command name
     * (e.g., "LOGIN", "QUIT") and the value is a reference to the function that creates the associated command.
     *
     * Commands include a variety of actions such as login, quit, creating and managing game objects, handling player actions,
     * managing resources, and interacting with game mechanics like defending or attacking.
     *
     * This method encapsulates the initialization logic for the map, ensuring all available commands are added consistently.
     */
    private void initializeCommandMap() {
        commandMap = new HashMap<>();
        commandMap.put("LOGIN", this::createLoginCommand);
        commandMap.put("QUIT", this::createQuitCommand);
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


    /**
     * Creates a command to select a chunk based on the provided coordinates.
     *
     * @param strings An array of strings where the first element is the command identifier,
     *                and the second and third elements are the x and y coordinates respectively.
     *                The array must have a length of 3.
     * @return A new instance of {@link SelectChunkCommand} configured with the provided coordinates
     *         and other required parameters.
     * @throws IllegalArgumentException If the number of arguments in the strings array is not equal to 3.
     */
    private Command creatselectchunkCommand(String[] strings) {
        int x;
        int y;
        ArrayList<IntegerPair> coordinates = new ArrayList<>();
        if (strings.length != 3) {
            throw new IllegalArgumentException("Command selectChunk requires 2 arguments: the coordinates");
        }
        x = Integer.parseInt(strings[1]);
        y = Integer.parseInt(strings[2]);

        return new SelectChunkCommand(new IntegerPair(x,y),gameId,playerId,lv, "SelectChunkCommand",token);
    }

    /**
     * Creates and returns a {@link Command} object based on the input string array.
     * The method parses the strings to identify the command type and its associated arguments.
     * Supported commands include "FINISHCARGO", "SWITCH", "DISCARDCARGO", "THEFT", and "GETREWARD".
     *
     * @param strings an array of strings representing the command and its arguments. The first element
     *                specifies the command type, and subsequent elements provide required arguments
     *                depending on the command type.
     *                - "FINISHCARGO": Requires only the command name with no additional arguments.
     *                - "SWITCH": Requires 6 arguments: x1, y1, position1, x2, y2, position2.
     *                - "DISCARDCARGO": Requires 3 arguments: x1, y1, position1.
     *                - "THEFT": Requires 3 arguments: x1, y1, position1.
     *                - "GETREWARD": Requires 3 arguments: x1, y1, position1.
     *
     * @return a {@link Command} instance representing the parsed command. This could be an instance of
     *         {@link HandleCargoCommand}, {@link TheftCommand}, or other specific implementations based
     *         on the command type.
     *
     * @throws IllegalArgumentException if the command type is unrecognized or if the number of arguments
     *                                  provided is incorrect for the specified command type.
     */
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
                    throw new IllegalArgumentException("Command FinishCargo doesn't require arguments");
                }
                title = "FinishCargo";
                break;
            }
            case "SWITCH":{
                if (strings.length != 7) {
                    throw new IllegalArgumentException("Command Switch requires 6 arguments: x1, y1, pos1, x2, y2, pos2");
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
                    throw new IllegalArgumentException("Command DiscardCargo richiede 3 argomenti: x1, y1, pos1");
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
                title = "TheftCommand";
                x1 = Integer.parseInt(strings[1]);
                y1 = Integer.parseInt(strings[2]);
                position1= Integer.parseInt(strings[3]);
                return new TheftCommand(position1,new IntegerPair(x1,y1), gameId, playerId,lv,title,token);
            }
            case "GETREWARD":{
                if (strings.length != 4) {
                    throw new IllegalArgumentException("Comando GetReward richiede 3 argomenti: x1, y1, posRewards");
                }
                title = "GetFromRewards";
                //position2= Integer.parseInt(strings[1]);
                x1 = Integer.parseInt(strings[1]);
                y1 = Integer.parseInt(strings[2]);
                position1 = Integer.parseInt(strings[3]);
                break;
            }
        }
        return new HandleCargoCommand(position1, new IntegerPair(x1,y1),position2, new IntegerPair(x2,y2), gameId, playerId, lv, title, token);
    }


    /**
     * Creates and returns a ReconnectCommand instance configured with the provided parameters.
     *
     * @param strings an array of strings potentially used for additional configurations or parameters for the command.
     * @return a Command object representing the ReconnectCommand with pre-configured values.
     */
    private Command createReconnectCommand(String[] strings) {
        ReconnectCommand CMD = new ReconnectCommand(token, gameId, playerId,lv, "Reconnect");
        CMD.setClient(client);
        return CMD;
    }


    /**
     * Creates a ReadyCommand object based on the provided input parameters.
     * The method determines if the command is accepted based on the first string element
     * in the input array and initializes a ReadyCommand with the appropriate values.
     *
     * @param strings an array of strings where the first element is checked to determine
     *                if the command is "READY".
     * @return a new instance of ReadyCommand initialized with the appropriate values.
     */
    private Command createReadyCommand(String[] strings) {
        boolean accepted;
        if (strings[0].equals("READY")){
            accepted = true;
        }
        else {
            accepted = false;
        }

        return new ReadyCommand(gameId, playerId,lv, "Ready", accepted, token);
    }


    /**
     * Creates and returns a Command object that represents a quit command
     * for the game.
     *
     * @param strings An array of strings used as input parameters necessary
     *                for creating the quit command.
     * @return A Command object configured as a quit command with the
     *         specified parameters.
     */
    private Command createQuitCommand(String[] strings) {
        return new QuitCommand(gameId,  playerId,  lv, "Quit",  token);
    }


    /**
     * Creates a command to add crew members or aliens to the game.
     *
     * @param strings an array of strings where:
     *                - the first element specifies the command type ("ADDCREW", "ADDPURPLEALIEN", or "ADDBROWNALIEN"),
     *                - the second element specifies the x-coordinate as a string,
     *                - the third element specifies the y-coordinate as a string.
     * @return an instance of {@code AddCrewCommand} representing the action to be executed.
     * @throws IllegalArgumentException if the input does not contain exactly three elements.
     * @throws NumberFormatException if the second or third element cannot be parsed into an integer.
     * @throws InvalidInput if the command type is invalid.
     */
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


    /**
     * Interprets a given command string, identifying the corresponding command creator
     * and using it to construct and return a Command object.
     *
     * @param commandString the string representation of the command to interpret
     * @return an instance of {@link Command} created based on the given command string
     * @throws IllegalArgumentException if the command is not recognized
     */
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


    /**
     * Creates a new LoginCommand based on the provided input parameters.
     * Validates and processes input to set appropriate game level and maximum number of players.
     *
     * @param parts an array of strings containing command arguments. Must include exactly 5 elements:
     *              game ID, player ID, player name, game level (integer), and maximum number of players (integer).
     * @return a new LoginCommand instance with the specified parameters.
     * @throws IllegalArgumentException if the input array does not contain exactly 5 elements.
     */
    private Command createLoginCommand(String[] parts) {
        if (parts.length != 5) {
            throw new IllegalArgumentException("Comando Login richiede 3 argomenti: nome giocatore, nome gioco, livello, max num di players");
        }

        String level = parts[3];
        int levelInt  = 0;
        try {
            levelInt = Integer.parseInt(level);
        } catch (NumberFormatException e) {
            System.out.println("Errore: la stringa non è un numero valido.");
        }
        setlv(levelInt);
        int maxPlayers = Integer.parseInt(parts[4]);
        if (maxPlayers < 1){
            maxPlayers = 1;
        }
        if (maxPlayers > 4){
            maxPlayers = 4;
        }
        return new LoginCommand(gameId,playerId, levelInt, "Login", maxPlayers);
    }

    /**
     * Creates a BuildingCommand instance based on the provided command arguments.
     * The specific behavior and parameters depend on the command type indicated
     * by the first element of the parts array.
     *
     * @param parts an array of strings representing the command type and its arguments.
     *              The first element denotes the command type, followed by required
     *             */
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
                if (parts.length != 1) {
                    throw new IllegalArgumentException("Comando ToBuffer non richiede argomenti");
                }
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


    /**
     * Creates a `FinishBuildingCommand` from the provided input parts.
     * The command arguments are validated based on the current level (`lv`):
     * - If `lv` equals 2, one additional argument is required (the index).
     * - If `lv` equals*/
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


    /**
     * Creates a `RemoveTileCommand` using the provided input parameters.
     *
     * This method parses the input arguments to extract the coordinates
     * (x and y) required for the command. If the arguments are not valid,
     * an exception is thrown.
     *
     * @param parts an array of strings representing the command and its arguments.
     *              The first element is the command keyword, while the second
     *              and third elements are expected to be integers representing
     *              the x and y coordinates, respectively.
     * @return*/
    private Command createRemoveTileCommand(String[] parts) {
        if (parts.length != 3) {
            throw new IllegalArgumentException("Comando Hourglass richiede 2 argomenti: x e y");
        }
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        return new RemoveTileCommand(x,y,gameId,playerId,lv, "RemoveTileCommand", token);
    }


    /**
     * Creates an AcceptCommand based on the provided input parameters.
     *
     * This method processes the input string array to determine whether the command
     * represents an "ACCEPT" or "DECLINE" action. If the input is invalid or does not
     * meet the expected criteria, an IllegalArgumentException is thrown.
     *
     * @param parts an array of strings representing the command*/
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

    /**
     * Creates a `ChoosingPlanetsCommand` based on the provided input parameters.
     *
     * The method processes the array of input strings to determine the command configuration.
     * If the command only has one argument or the second argument is `DoNothing`, a default
     * `DefendFromSmallCommand` is returned with a placeholder value. Otherwise, the method
     * parses the planet number from the provided arguments. An exception is thrown if the
     * command does not match the expected format.
     *
     * @param parts an array of*/
    private Command createChoosingPlanetsCommand(String[] parts) {

        if(parts.length==1 || parts[1].equals("DoNothing")){
            return new ChoosingPlanetsCommand(-1,gameId,playerId,lv, "DefendFromSmallCommand",token);
        }

        if (parts.length != 2) {
            throw new IllegalArgumentException("Comando ChoosingPlanets richiede 1 argomento: numero del pianeta");
        }
        int x = Integer.parseInt(parts[1]);
        return new ChoosingPlanetsCommand(x,gameId,playerId,lv, "ChoosingPlanetsCommand",token);
    }

    /**
     * Creates a `ConsumeEnergyCommand` using the provided input parts. The input must contain
     * an even number of coordinates following the command keyword. Coordinates are parsed
     * from the input and added to a list.
     *
     * @param parts an array of strings representing the command and its arguments.
     *              The first element should be the command keyword, and the subsequent elements
     *              should form pairs of integers representing coordinates.
     * @return a new instance of `ConsumeEnergyCommand` initialized with the parsed coordinates
     *         and the current state of the CommandInterpreter instance.
     * @throws IllegalArgumentException if the number of coordinate arguments is not even.
     */
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

    /**
     * Creates a DefendFromLargeCommand based on the provided input parameters.
     * The command is used to defend a location from a large-scale attack, utilizing
     * a plasma drill and/or energy storage coordinates if specified.
     *
     * @param parts an array of strings where:
     *              - parts[0] is the command type (e.g., "DefendFromLargeCommand").
     *              - parts[1] and parts[2] represent the x and y coordinates of the plasma drill.
     *                If both values are -1, the plasma drill is considered null.
     *              - parts[3] and parts[4] represent the x and y coordinates of energy storage.
     *                If both values are -1, the energy storage is considered null.
     * @return a new instance of the DefendFromLarge*/
    private Command createDefendFromLargeCommand(String[] parts) {
        int x;
        int y;
        IntegerPair plasmaDrill;
        IntegerPair energyStorage;
        if(parts.length==1 || parts[1].equals("DoNothing")|| parts.length==1){
            return new DefendFromLargeCommand(null,null,gameId,playerId,lv, "DefendFromLargeCommand",token);
        }
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

    /**
     * Creates a `DefendFromSmallCommand` based on the input parameters.
     *
     * The method processes the given array of command parts to determine
     * the command configuration. If no additional arguments are provided,
     * it defaults to a `DoNothing` configuration. Otherwise, it parses
     * the coordinates of the energy source to be consumed. If the coordinates
     * are -1, -1, it denotes no energy source.
     *
     * @param parts an array of strings representing the command arguments;
     *              the first value is the command name and the subsequent
     *              values represent optional parameters (e.g., energy source
     *              coordinates to consume).
     * @return a `DefendFromSmallCommand` instance initialized with the
     *         parsed parameters*/
    private Command createDefendFromSmallCommand(String[] parts) {
        int x;
        int y;
        IntegerPair energyStorage;
        if(parts.length==1 || parts[1].equals("DoNothing")){
            return new DefendFromSmallCommand(null,gameId,playerId,lv, "DefendFromSmallCommand",token);
        }
        if (parts.length != 3) {
            throw new IllegalArgumentException("Comando DefendFromSmall richiede 2 argomenti: le due coordinate dell'energia da consumare " + parts[0] + " " + parts[1]); // anche se dubito possa essere colpa del player se succedono casini qui ma vabbé
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

    /**
     * Creates a GiveAttackCommand based on the provided input parameters.
     * The command represents an attack action, potentially with coordinates of targets.
     * If no coordinates or "DoNothing" is specified, the command will be created without any specified targets.
     * An exception is thrown if the provided arguments are not formatted correctly, specifically when an odd number
     * of arguments is given for coordinates.
     *
     * @param parts an array of strings where the first element is the command keyword,
     *              followed by an even number of elements representing x and y coordinates of targets,
     *              or a single element of "DoNothing".
     * @return a new instance of the GiveAttackCommand initialized with the appropriate parameters.
     * @throws IllegalArgumentException if the input parts array contains an odd number of elements after the command keyword.
     */
    private Command createGiveAttackCommand(String[] parts) {
        int x;
        int y;
        ArrayList<IntegerPair> coordinates = new ArrayList<>();
        if(parts.length==1 || parts[1].equals("DoNothing")){
            return new GiveAttackCommand(coordinates,gameId,playerId,lv,"GiveAttackCommand",token);
        }
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

    /**
     * Creates a GiveSpeedCommand based on the provided input parts.
     *
     * @param parts An array of strings where the first element signifies the command type
     *              and subsequent elements represent the coordinates as pairs of integers.
     *              If the array has only one element or the second element is "DoNothing",
     *              a command with an empty list of coordinates is created.
     * @return A Command object configured with the specified coordinates and necessary game details.
     * @throws IllegalArgumentException If the number of additional elements in the parts array
     *                                  (after the first element) is not even, indicating an invalid
     *                                  pairing of coordinates.
     */
    private Command createGiveSpeedCommand(String[] parts) {
        int x;
        int y;
        ArrayList<IntegerPair> coordinates = new ArrayList<>();
        if(parts.length==1 || parts[1].equals("DoNothing")){
            return new GiveSpeedCommand(coordinates,gameId,playerId,lv,"GiveAttackCommand",token);
        }
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

    /**
     * Creates a KillCommand using the provided array of parameters.
     *
     * The method expects an array of strings where the coordinates
     * for the command are provided in pairs starting from the second element.
     * Each pair represents an (x, y) coordinate. If the number of elements
     * (excluding the first element) is not even, an IllegalArgumentException
     * is thrown.
     *
     * @param parts an array of strings where the first element is ignored,
     *              and the subsequent elements, in pairs, represent the
     *              (x, y) coordinates for the command.
     * @return a new instance of KillCommand initialized with the specified
     *         coordinates and other internal state parameters.
     * @throws IllegalArgumentException if the number of coordinates provided
     *                                  is not even.
     */
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

    /**
     * Retrieves the game ID associated with this CommandInterpreter instance.
     *
     * @return The game ID as a String.
     */
    public String getGame() {
        return gameId;
    }

    /**
     * Retrieves the unique identifier associated with the player.
     *
     * @return a string representing the player's ID.
     */
    public String getPlayerId(){
        return playerId;
    }


    /**
     * Creates a new DebugShip command with the specified parameters.
     *
     * The first element in the provided strings array is used as the title of the ship.
     * The second element, if present, is parsed as an integer and used to determine
     * the number field for the DebugShip command. If the parsed integer is greater
     * than 0, the number is set to 1; otherwise, it defaults to 0.
     *
     * @param strings an array of strings where the first element represents the title
     *                of the ship, and the second (optional) element is used to
     *                determine the number field of the command.
     * @return a new instance of the DebugShip command initialized with the provided
     *         parameters.
     */
    private Command createDebugShip(String[] strings) {

        String title = strings[0];
        int number = 0;
        if(strings.length > 1){
            number = Integer.parseInt(strings[1]);
            if (number > 0){
                number = 1;
            }
        }
        return new DebugShip(gameId,playerId, lv, title, token, number);
    }

    /**
     * Sets the token value for this instance.
     *
     * @param token the token to set, representing a unique identifier or access token
     */
    public void setToken(String token) {
        this.token = token;
    }

    @FunctionalInterface
    public interface CommandCreator {
        /**
         * Creates a Command object based on the input array of parameters.
         *
         * @param parts an array of Strings containing the parameters required to create the Command.
         *              The specific contents and expected order depend on the implementation of
         *              the createCommand method and the type of Command being created.
         * @return a Command instance initialized with the provided parameters.
         */
        Command createCommand(String[] parts);
    }

}

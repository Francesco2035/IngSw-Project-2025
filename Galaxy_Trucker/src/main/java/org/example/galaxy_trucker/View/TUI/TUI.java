package org.example.galaxy_trucker.View.TUI;
//si occupa di ricevere gli eventi, formattarli e inviarli ad out. Chiama la show di out, la show di out chiama il metodo di player che in base allo stato in cui è (ricevendo come parametro this) chiama i metodi giusti

//eccezioni gestite direttamente da TUI
//se disconnesso setta player a stato di disconnessione e chiama
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.Controller.Messages.*;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.*;
import org.example.galaxy_trucker.View.ClientModel.PlayerClient;
import org.example.galaxy_trucker.View.ClientModel.States.*;
import org.example.galaxy_trucker.View.View;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Model.Connectors.Connectors;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.View.ViewPhase;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;


/**
 * The TUI class represents a text-based user interface for interacting with the game system.
 * It handles various in-game events, manages game state representations, and provides methods
 * for formatting and displaying information to the player during the game.
 * This class implements the necessary methods to respond to events and user interactions.
 * It extends the View class and provides a concrete implementation for text-based interactions.
 */
public class TUI implements View {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledTask;
    private final int debounceDelayMs = 250;
    private PlayerStateClient lastState;
    private boolean firstUpdate = false;

    private int CardId = -1;
    private final TileEvent[][] board = new TileEvent[10][10];
    private final HashMap<Integer, String> idToNameMap = new HashMap<>();
    private final int contentWidth = 33;
    private String[][][] cachedBoard;
    private String[] cacheHand = null;
    private final String gamboardBorder = "+━━━━━━━━━━━━━━━━━━━━━━━+";
    private final String border = "+━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━+";
    private ArrayList<Integer> uncoveredTilesId = new ArrayList<>(); //ordine, quindi contiene gli ID in ordine di come arrivano
    private HashMap<Integer, String[]> uncoverdTileSetCache = new HashMap<>();
    private HashMap<Integer, String> CardsDescriptions = new HashMap<>();
    private String[][][] Gameboard;
    private int lv;
    private int setup = 102;

    private HashMap<String, String[]> lobby = new HashMap<>();
    private ViewPhase phase;
    private Out out;


    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private Thread inputThread;
    private InputReader inputReader;
    private PlayerClient playerClient;
    private Client client;
    private final SeeLog seeLog = new SeeLog();
    private final SeeBoardsClient seeBoardsClient = new SeeBoardsClient();
    private  LoginClient loginClient;



    /**
     * A constructor for the TUI (Text User Interface) class.
     *
     * This method initializes a new instance of the TUI class. It is used
     * to set up the necessary fields or configurations required for the
     * proper functioning of the text-based user interface. This constructor
     * does not accept any parameters.
     */
    public TUI(){

    }

    /**
     * Constructs a TUI (Textual User Interface) instance for the game, facilitating interactions
     * between the user and the game state. Initializes various components such as input reader,
     * game board, cache structures, and client handlers.
     *
     * @param loginClient The initial login client which manages the login state and primary actions
     * such as joining a lobby, creating a game, or reconnecting.
     * @throws IOException If an error occurs while loading component names or initializing other settings.
     */
    public TUI(LoginClient loginClient) throws IOException {

        this.loginClient = loginClient;

        loadComponentNames();
        loadCardsDescriptions();
        cachedBoard = new String[10][10][7];
        cacheHand = new String[7];
        for (int i = 0; i < 7; i++) {
            cacheHand[i] = "";
        }
        inputReader = new InputReader(inputQueue);
        inputThread = new Thread(inputReader);
        inputThread.setDaemon(true);
        inputThread.start();
        //phase = ViewPhase.LOBBY;
        //inputReader.renderScreen(new StringBuilder(ASCII_ART.Title));
        playerClient = new PlayerClient();
        playerClient.setPlayerState(new LoginClient());
        playerClient.setCompleter(inputReader.getCompleter());
        lastState = new LoginClient();
        out = new Out(inputReader, playerClient);
        onGameUpdate();
        //inputReader.clearScreen();

    }



    /**
     * Sets up the gameboard according to the specified level. Initializes the gameboard
     * layout and populates it with formatted cells or positions based on the level.
     *
     * @param lv the level of the gameboard to set up; determines the gameboard structure and positions.
     */
    @Override
    public void setGameboard(int lv) {
        out.initGameBoard(lv);
        this.lv = lv;
        int position = 4;
        if (lv == 2){
            for (int i = 0; i < 6; i++){
                for (int j = 0; j < 12; j++){
                    if ((i == 0 && (j >= 2 && j <= 9)||(i == 1 && (j == 1 || j == 10)))
                            || (i == 2 && (j == 0 || j == 11)||(i == 3 && (j == 0 || j == 11)))
                    || (i == 4 && (j == 1 || j == 10)) || (i == 5 && (j >= 2 && j <= 9))){

                        if (i == 0 && (j == 2 || j == 3 || j == 5 ||j == 8)) {
                            out.setGameboard(i,j,formatPosition(position));
                            position--;
                        }

                        else {
                            out.setGameboard(i,j,formatCell());
                        }
                    }

                    else {
                        out.setGameboard(i,j,emptyGbCell());
                    }
                }
            }


        }
        else{

            for (int i = 0; i < 5; i++){
                for (int j = 0; j < 11; j++){
                    if ((i == 0 && (j >= 3 && j <= 8)||(i == 1 && (j == 2 || j == 9)))
                            || (i == 2 && (j == 1 || j == 10)||(i == 3 && (j == 2 || j == 9)))
                            ||  (i == 4 && (j >= 3 && j <= 8))){

                        if (i == 0 && (j == 3 || j == 4 || j == 5 ||j == 7)) {
                            out.setGameboard(i,j,formatPosition(position));
                            position--;
                        }

                        else {
                            out.setGameboard(i,j,formatCell());
                        }
                    }

                    else {
                        out.setGameboard(i,j,emptyGbCell());
                    }
                }
            }
        }

    }

    /**
     * Updates and displays the game lobby based on the provided lobby event.
     *
     * This method processes the given {@code event} to update the lobby state, such as
     * adding a new lobby entry or removing an existing one. It initializes the lobby
     * client state if this is the first update and triggers any required game state updates.
     *
     * @param event the {@link LobbyEvent} containing information about the lobby to be displayed.
     *              The event holds details such as the game ID, level (lv), and associated players.
     */
    @Override
    public void showLobby(LobbyEvent event) {
        if (!firstUpdate){
            firstUpdate = true;
            playerClient.setPlayerState(new LobbyClient());
            lastState = new LobbyClient();
        }
        //System.out.println(event.getGameId());
        if (event.getLv() != -1){
            //System.out.println("put "+event.getGameId()+" "+event.getLv());
            try{
                out.setLobby(event.getGameId(),formatCell(event)); //QUI
                client.setGameIdToLV(event.getGameId(), event.getLv());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if (event.getGameId() != null){
            out.setLobby(event.getGameId(), null);
        }
        onGameUpdate();
//        if (event.getGameId().equals("EMPTY CREATE NEW GAME")){
//            lobby.remove(event.getGameId());
//        }
    }

    /**
     * Updates the lobby game state by setting the players and their readiness status,
     * and then triggers a game update.
     *
     * @param event the {@link GameLobbyEvent} containing the list of players and their
     *              readiness statuses to be used for updating the lobby game state.
     */
    @Override
    public void showLobbyGame(GameLobbyEvent event) {

        out.setPlayers(event.getPlayers());
        out.setReady(event.getReady());

        onGameUpdate();
    }

    /**
     * Notifies the system that the rewards have changed and triggers corresponding updates.
     *
     * @param event the event containing the updated rewards information
     */
    @Override
    public void rewardsChanged(RewardsEvent event) {
        out.setRewards(formatRewards(event));
        onGameUpdate();
    }

    /**
     * Handles phase change events in the game.
     *
     * This method is triggered whenever a phase change occurs. It updates the player's state,
     * clears output if specific conditions are met, configures the auto-completion commands
     * based on the new phase, and triggers a game update process.
     *
     * @param event the PhaseEvent containing details about the new phase and player state.
     */
    @Override
    public void phaseChanged(PhaseEvent event) {
            if (!firstUpdate){
                firstUpdate = true;
            }

            if (event.getStateClient() == loginClient){
                firstUpdate = false;
                out.clearOut();
            }
            lastState = event.getStateClient();
            playerClient.setPlayerState(event.getStateClient());
            playerClient.getCompleter().setCommands(event.getStateClient().getCommands());
            onGameUpdate();
        //lastState = null;

    }

    /**
     * Handles an exception event by setting the exception message in the output object
     * and scheduling an update for the game state.
     *
     * @param exceptionEvent the event containing the exception message
     */
    @Override
    public void exceptionOccurred(ExceptionEvent exceptionEvent) {
        out.setException(exceptionEvent.getException());
        onGameUpdate();
    }

    /**
     * Updates the player board for other players based on the event details provided.
     * This method processes the event and makes updates to the game state associated with other players' boards.
     *
     * @param event the PlayerTileEvent object containing information about the player's action, including ID,
     *              player name, coordinates, and some contextual data.
     */
    @Override
    public void updateOthersPB(PlayerTileEvent event) {

         if (event.getId() == 158) {
            out.setOthersPB(event.getPlayerName(),event.getX(), event.getY(), emptyCell());
            if (setup == 0 && event.getX() == 3 && event.getY() == 8){
                out.setOthersPB(event.getPlayerName(),3,8,emptyCell()); //QUI
            }
            else if (!(event.getX() == 3 && event.getY() == 8) && (event.getX() < 3 || event.getY() < 3) && (event.getY() != 2)) {

                for (int k = 0; k < 7; k++) {
                    out.setOthersPB(event.getPlayerName(),event.getX(), event.getY(),k, "");//QUI
                }
            }

        }

        else if (event.getId() == 159) {
             out.setOthersPB(event.getPlayerName(),7, 8, emptyCell());
             out.setOthersPB(event.getPlayerName(),7, 9, emptyCell());
        }


        else {
             out.setOthersPB(event.getPlayerName(),event.getX(), event.getY(), formatCell(event)); //QUI
        }

        if (lastState != null){
            onGameUpdate();
        }


    }

    /**
     * Updates the player's state to allow viewing the game boards.
     *
     * This method transitions the player to the "see boards" state if the player's current
     * state is not already set to {@code seeBoardsClient} or {@code seeLog}. If the state
     * transition occurs, the previous state is stored in the {@code lastState} field for
     * potential future reference. After setting the new state to {@code seeBoardsClient},
     * the method triggers a game update using {@code onGameUpdate()}.
     *
     * The behavior of the game boards view is determined by the specific implementation
     * of the {@code seeBoardsClient} state and any subsequent updates to the game state.
     */
    @Override
    public void seeBoards() {
        if (!(playerClient.getPlayerState().equals(seeBoardsClient) || playerClient.getPlayerState().equals(seeLog))){
            lastState = playerClient.getPlayerState();
        }
        playerClient.setPlayerState(seeBoardsClient);
        onGameUpdate();
    }

    /**
     * Refreshes the game state by restoring the player's previous state and triggering a game update.
     *
     * This method is used to restore the player's state to the previously stored {@code lastState}.
     * Once the state is restored, a game update is triggered by invoking {@code onGameUpdate()}.
     * This ensures that the game reflects the updated player state and any associated changes in the
     * game display or internal logic.
     *
     * The restoration logic is dependent on {@code playerClient.setPlayerState()}, which sets the
     * state of the player based on the saved {@code lastState}. After the state is updated,
     * {@code onGameUpdate()} schedules and handles a refreshed view or processing of game details.
     */
    @Override
    public void refresh() {
        playerClient.setPlayerState(lastState);
        //lastState = null;
        onGameUpdate();
    }

    /**
     * Executes the card effect based on the provided log event.
     * This method processes the log event to extract its message,
     * updates the game log with the extracted message, and triggers
     * the game update functionality.
     *
     * @param event the log event carrying the message that needs to be processed
     */
    @Override
    public void effectCard(LogEvent event) {
        out.setLog(event.message());
        onGameUpdate();
    }

    /**
     * Updates the player's board information based on the provided event details.
     *
     * This method processes the {@link PBInfoEvent} to update the player's board state.
     * It utilizes the `formatPBInfo` helper method to format the event data appropriately
     * and sets the output using the `out` object. Any exceptions encountered during
     * processing are caught and logged.
     *
     * @param event the {@link PBInfoEvent} containing the updated player's board information
     *              to be processed and displayed.
     */
    @Override
    public void updatePBInfo(PBInfoEvent event) {
        try{
            out.setPBInfo(formatPBInfo(event));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Updates the hourglass state based on the provided event and triggers a game update.
     *
     * This method processes the specified {@code HourglassEvent} by checking its message
     * and start status. It updates the hourglass state in the output and invokes a game
     * update to reflect the changes.
     *
     * @param event the {@link HourglassEvent} containing the start status and associated message
     *              to update the hourglass state.
     */
    @Override
    public void updateHourglass(HourglassEvent event) {
        System.out.println(event.getClass());
        if (event.message() == null){
            System.out.println("oioioi");
        }
        out.setHourglass(event.getStart(), event.message());
        onGameUpdate();
    }

    /**
     * Updates the current state of the player's interface to allow viewing the game log.
     *
     * This method transitions the player's state to `seeLog` if the current state is not
     * already `seeBoardsClient` or `seeLog`. If a state transition occurs, the previous
     * state is stored in the `lastState` field to allow returning to the earlier context if needed.
     * After changing the state, the method triggers an update of the game view by invoking `onGameUpdate()`.
     */
    @Override
    public void seeLog() {
        if (!(playerClient.getPlayerState().equals(seeBoardsClient) || playerClient.getPlayerState().equals(seeLog))){
            lastState = playerClient.getPlayerState();
        }
        playerClient.setPlayerState(seeLog);
        onGameUpdate();
    }

    /**
     * Displays the outcome of the game based on the provided game event.
     *
     * This method updates the output to reflect the result of the game,
     * sets the player's state to the finish state, and triggers a game update.
     *
     * @param event the {@link FinishGameEvent} containing the result and message
     *              details of the finished game. The result indicates whether
     *              the player has won or lost, and the message provides additional
     *              context or feedback about the outcome.
     */
    @Override
    public void showOutcome(FinishGameEvent event) {
        out.setOutcome(event.message(), event.isWin());
        playerClient.setPlayerState(new FinishStateClient());
        onGameUpdate();
    }

    /**
     * Handles the reconnection event for the user interface.
     *
     * This method is invoked when a reconnection occurs. It clears the output
     * related to the lobby and log views to reset the state, ensuring a clean
     * interface state for the user.
     *
     * @param event the ReconnectedEvent containing the details of the reconnection,
     *              such as token, game ID, player ID, and level.
     */
    @Override
    public void reconnect(ReconnectedEvent event) {
        out.clearOut();
    }

    /**
     * Processes a token event and updates the log with the token string.
     *
     * @param tokenEvent the {@link TokenEvent} object containing the token string
     *                   to be processed and logged.
     */
    @Override
    public void Token(TokenEvent tokenEvent) {
        out.setLog(tokenEvent.getToken());
    }


    /**
     * Displays the score based on the provided scoreboard event.
     *
     * @param event the ScoreboardEvent that contains the details for displaying the score
     */
    @Override
    public void showScore(ScoreboardEvent event) {

    }

    /**
     * Formats the PBInfoEvent data into a structured string using ASCII art components.
     * This method converts various attributes of the given PBInfoEvent object, such as credits,
     * cargo value, connectors, damage*/
    public String formatPBInfo(PBInfoEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(ASCII_ART.compose("            ",
                ASCII_ART.addNumber(ASCII_ART.credits,String.valueOf(event.getCredits())),
                ASCII_ART.addNumber(ASCII_ART.cargoValue,String.valueOf(event.getTotValue())),
                ASCII_ART.addNumber(ASCII_ART.exposedConnectors,String.valueOf(event.getExposedConnectors())),
                ASCII_ART.addNumber(ASCII_ART.damage,String.valueOf(event.getDamage()))
                        ));

        sb.append(ASCII_ART.compose("          ",
                ASCII_ART.addNumber(ASCII_ART.crew,String.valueOf(event.getNumHumans())),
                ASCII_ART.addNumber(ASCII_ART.enginePower,String.valueOf(event.getEnginePower())),
                ASCII_ART.addNumber(ASCII_ART.plasmDrillPower,String.valueOf(event.getPlasmaDrillsPower()))
        ));


        return sb.toString();
    }


    /**
     * Formats a cell representation for displaying game information in a lobby.
     *
     * @param event the {@code LobbyEvent} object containing information about the game,
     *              such as game ID, level, players, and maximum players.
     **/
    public String[] formatCell(LobbyEvent event) {
        String[] cell = new String[9];
        cell[0] = "+"+centerTextAnsi(event.getGameId(),25, "-")+"+";
        cell[1] = "+                         +";
        cell[2] = "+                         +";
        cell[3] = "+                         +";
        cell[4] = "+                         +";
        cell[5] = "+                         +";
        cell[6] = "+                         +";
        cell[7] = "+-------------------------+";
        cell[8] = "+-------------------------+";
        int k = 1;
        if (!event.getGameId().equals("EMPTY CREATE NEW GAME")){
            //TODO: chiama metodo speciale di out senza salvare la stringa su out se il titolo è questo
            ArrayList<String> players = event.getPlayers();
            cell[7] = "+"+centerTextAnsi("Game level: "+ event.getLv(),25, "-")+"+";
            cell[8] = "+"+centerTextAnsi("Max players: "+ event.getMaxPlayers(),25, "-")+"+";
            for (String player : players) {
                cell[2+ k -1] = "+"+centerTextAnsi("p"+k+ ": "+player, 25)+"+";
                k++;
            }
        }

        return cell;
    }


    /**
     * Loads card descriptions from a JSON file and populates the CardsDescriptions map with the card ID
     * as the key and the description as the value. The JSON file is expected to be located in the
     * classpath and named "*/
    private void loadCardsDescriptions() {
        ObjectMapper mapper = new ObjectMapper();
        //System.out.println( getClass().getClassLoader().getResource("ClientTiles.json"));
        try (InputStream cardsStream = getClass().getClassLoader().getResourceAsStream("ClientCards.json")) {
            if (cardsStream == null) {
                System.err.println("File ClientTiles.JSON non found!");
                return;
            }

            JsonNode root = mapper.readTree(cardsStream);

            root.fields().forEachRemaining(entry -> {
                String description = entry.getValue().get("description").asText();
                int id = Integer.parseInt(entry.getKey());
                CardsDescriptions.put(id, description);
            });

        } catch (IOException e) {
            System.err.println("Error loading names: " + e.getMessage());
        }
    }



    /**
     * Updates the board state based on the specified tile event. This method manages
     * modifications to the cached board and player board, as well as handles specific
     * scenarios based on the event ID and coordinates. Additionally, it processes
     * certain actions when the setup phase is completed.
     *
     * @param event the TileEvent object containing details about the action to be applied to
     *              the board. It includes the coordinates of the action, the event's ID, and
     *              other relevant properties. If the event is null, an empty cell is set at the
     *              specified location.
     */
    @Override
    public void updateBoard(TileEvent event) {

        if (setup!=0){
            setup--;
        }

        if (event == null) {
            out.setCachedBoard(event.getX(), event.getY(), emptyCell());
        }
        else if (event.getId() == 158) {
            out.setCachedBoard(event.getX(), event.getY(), emptyCell());
            if (setup == 0 && event.getX() == 3 && event.getY() == 8){
                out.setCachedBoard(3,8,out.getPlayerBoard()[3][9]); //QUI
                out.setCachedBoard(3, 9, emptyCell());
            }

            else if (!(event.getX() == 3 && event.getY() == 8) && (event.getX() < 3 || event.getY() < 3) && (event.getY() != 2)) {
                for (int k = 0; k < 7; k++) {
                    out.setCachedBoard(event.getX(), event.getY(),k, "");//QUI
                }
            }

        }

        else if (event.getId() == 159) {
            out.setCachedBoard(7, 8, emptyCell());
            out.setCachedBoard(7, 9, emptyCell());
        }


        else {
            out.setCachedBoard(event.getX(), event.getY(), formatCell(event)); //QUI
        }
        if (setup == 0){
            out.setCacheHand(emptyCell());
            onGameUpdate();
        }
    }

    /**
     * Creates and returns an array of empty strings, each representing a blank cell line.
     *
     * @return an array of 7 blank strings, each containing 35 spaces
     */
    private String[] emptyCell() {
        String[] cellLines = new String[7];

        for (int i = 0; i < 7; i++) {
            cellLines[i] = "                                   ";
        }
        return cellLines;
    }

    /**
     * Creates and returns an array of strings representing an empty grid or cell.
     * Each element of the array is a string containing only spaces.
     *
     * @return an array of 7 strings, each consisting of 25 spaces.
     */
    private String[] emptyGbCell(){
        String[] cellLines = new String[7];

        for (int i = 0; i < 7; i++) {
            cellLines[i] = "                         ";
        }
        return cellLines;
    }

    /**
     * Displays the given message.
     *
     * @param message the message to be displayed
     */
    @Override
    public void showMessage(String message) {
        //System.out.println(message);
    }

    /**
     * Formats a cell in a game board by generating an array of strings
     * representing each line of the cell. The first and last lines of the array
     * consist of the game board border, while the middle lines contain uniform
     * spacing within borders.
     *
     * @return an array of strings where each string represents a line of a formatted cell
     */
    public String[] formatCell(){
        String[] cellLines = new String[7];

        for (int i = 0; i < 7; i++) {
            cellLines[i] = "|                       |";
        }
        cellLines[0] = gamboardBorder;
        cellLines[6] = gamboardBorder;
        return cellLines;
    }

    /**
     * Formats the position of the game board with a specified value and returns the formatted lines.
     *
     * @param k the value to be inserted in the formatted position lines
     * @return an array of strings representing the formatted lines of the game board
     */
    public String[] formatPosition(int k){
        String[] cellLines = new String[7];
        for (int i = 2; i < 6; i++) {
            cellLines[i] = "|                       |";
        }
        cellLines[1] = "|-----------"+ k + "-----------|";
        cellLines[0] = gamboardBorder;
        cellLines[6] = gamboardBorder;
        cellLines[5] = "|-----------"+ k + "-----------|";

        return cellLines;
    }

    /**
     * Formats a TileEvent into a visual representation of a cell consisting of an array of strings.
     * Each element of the array represents a line of text illustrating the tile's visual details such
     * as connectors, name, extra information, and coordinates.
     *
     * @param event The TileEvent containing information about the tile to be formatted. If the event is null,
     *              a default empty representation of the cell will be created.
     * @return A string array where each element represents a line of the formatted cell.
     */
    public String[] formatCell(TileEvent event) {
        String[] cellLines = new String[7];


        if (event == null) {
            for (int i = 0; i < 7; i++) {
                cellLines[i] = "|" + " ".repeat(contentWidth) + "|";
            }
            return cellLines;
        }

        ArrayList<Connectors> conns = event.getConnectors();
        String top    = getConnectorSymbol(conns.get(1));
        String left   = getConnectorSymbol(conns.get(0));
        String right  = getConnectorSymbol(conns.get(2));
        String bottom = getConnectorSymbol(conns.get(3));


        String colored = idToNameMap.getOrDefault(event.getId(), "Unknown");
        String name = colored.replaceAll("\u001B\\[[;\\d]*m", "");
        if(event.getX() == 3 && (event.getY() == 8 || event.getY() == 9)){
            colored = "\u001B[35m" + colored + "\u001B[0m";
        }

        cellLines[2] = "|" + centerTextAnsi(colored, contentWidth) + "|";

        String extra = "";
        switch (name) {
            case "powerCenter", "TriplePowerCenter" -> extra = "B: "+event.getBatteries();
            case "modularHousingUnit", "MainCockpit" -> {
                int brown = 0;
                int purple = 0;
                if (event.isBrownAlien()){
                    brown = 1;
                }
                if (event.isPurpleAlien()){
                    purple = 1;
                }
                extra += "H: " +event.getHumans() + " | ";
                extra += "B: "+ brown + " | ";
                extra += "P: "+purple;
            }
            case "storage", "TripleStorage", "specialStorage", "doubleSpecialStorage" -> {
                if (event.getCargo() != null && !event.getCargo().isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (Goods g : event.getCargo()) {
                        switch (g.getValue()) {
                            case 4 -> sb.append("\u001B[31m██\u001B[0m "); // Rosso
                            case 3 -> sb.append("\u001B[33m██\u001B[0m "); // Giallo
                            case 2 -> sb.append("\u001B[32m██\u001B[0m "); // Verde
                            case 1 -> sb.append("\u001B[34m██\u001B[0m "); // Blu
                        }
                    }
                    extra = sb.toString();
                }
            }

            case "shieldGenerator" -> {
                int rot = (event.getRotation() % 360) / 90;
                if (rot == 0){
                    top ="\u001B[33m" + top + "\u001B[0m";
                    right ="\u001B[33m" + right + "\u001B[0m";
                }
                else if (rot == 1){
                    right ="\u001B[33m" + right + "\u001B[0m";
                    bottom ="\u001B[33m" + bottom + "\u001B[0m";
                } else if (rot == 2) {
                    bottom ="\u001B[33m" + bottom + "\u001B[0m";
                    left ="\u001B[33m" + left + "\u001B[0m";
                }
                else {
                    left ="\u001B[33m" + left + "\u001B[0m";
                    top ="\u001B[33m" + top + "\u001B[0m";
                }
            }
        }



        String leftPart = "";
        String centeredPart = centerText(leftPart, contentWidth - 8);
        cellLines[3] = "| < "+ left + centeredPart + right + " > |";

        cellLines[4] = "|" + centerTextAnsi(extra, contentWidth) + "|";

        String position = " " + event.getX() + " : " + event.getY();

        String arrow = "v";

        int availableSpace = contentWidth - (position.length() + arrow.length() + bottom.length());

        int spaceBeforeArrow = availableSpace / 2;
        int spaceAfterArrow = availableSpace - spaceBeforeArrow;

        String finalRow = position + " ".repeat(spaceBeforeArrow -4) + arrow + " "+ bottom + " ".repeat(spaceAfterArrow );
        cellLines[1] = "|" + centerTextAnsi("^ " + top, contentWidth) + "|";

        cellLines[5] = "|" + centerTextAnsi(finalRow, contentWidth) + "|";

        cellLines[6] = border;;
        cellLines[0] = border;

        return cellLines;
    }




    /**
     * Centers the given text within a specified width by adding padding spaces on both sides.
     *
     * @param text the text to be centered
     * @param width the total width within which the text should be centered
     * @return a string with the original text centered within the specified width
     */
    private String centerText(String text, int width) {
        int padding = Math.max(0, (width - text.length()) / 2);
        return " ".repeat(padding) + text + " ".repeat(width - padding - text.length());
    }

    /**
     * Returns the symbol representing the given connector type. If the connector is null, a dot (".") is returned.
     * The symbol is determined based on the class name of the connector object.
     *
     * @param c the connector object whose symbol is to be determined
     * @return the symbol corresponding to the connector type, or "." if the connector is null
     */
    private String getConnectorSymbol(Connectors c) {
        if (c == null) return ".";
        return switch (c.getClass().getSimpleName()) {
            case "NONE"      -> "°";
            case "SINGLE"    -> "S";
            case "DOUBLE"    -> "D";
            case "UNIVERSAL" -> "U";
            case "CANNON"    -> "C";
            case "ENGINE"    -> "M";
            default          -> "?";
        };
    }

    /**
     * Removes ANSI escape codes from the given string. ANSI escape codes are used
     * for formatting text with colors or other styles in terminal output.
     *
     * @param s the input string that may contain ANSI escape codes
     * @return a new string with all ANSI escape codes removed
     */
    public static String stripAnsi(String s) {
        return s.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    /**
     * Centers the given text within the specified width, including ANSI escape sequences.
     * Accounts for the visible length of the text excluding ANSI escape codes when centering.
     *
     * @param s the input string, which may contain ANSI escape sequences
     * @param width the total width within which the text should be centered
     * @return a new string with the text centered, including padding spaces
     */
    public static String centerTextAnsi(String s, int width) {
        String visible = stripAnsi(s);
        int padding = width - visible.length();
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        return " ".repeat(Math.max(0, padLeft)) + s + " ".repeat(Math.max(0, padRight));
    }

    /**
     * Centers the given text within a specified width while supporting ANSI escape codes.
     * The padding is done with the specified character.
     *
     * @param s the text to be centered, which may include ANSI escape codes
     * @param width the total width within which the text should be centered
     * @param c the character to use for padding on both sides of the text
     * @return the text centered in the specified width, padded with the given character
     */
    public static String centerTextAnsi(String s, int width, String c) {
        String visible = stripAnsi(s);
        int padding = width - visible.length();
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        return c.repeat(Math.max(0, padLeft)) + s + c.repeat(Math.max(0, padRight));
    }

    /**
     * Loads component names from a JSON file and populates a mapping of IDs to component types.
     * The method reads the "ClientTiles.json" file from the classpath and processes its
     * contents using the Jackson library.
     *
     * @throws IOException if there is an error during reading the file or processing input stream.
     */
    private void loadComponentNames() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //System.out.println( getClass().getClassLoader().getResource("ClientTiles.json"));
        try (InputStream tilesStream = getClass().getClassLoader().getResourceAsStream("ClientTiles.json")) {
            if (tilesStream == null) {
                System.err.println("File ClientTiles.JSON non found!");
                return;
            }

            JsonNode root = mapper.readTree(tilesStream);

            root.fields().forEachRemaining(entry -> {
                String componentType = entry.getValue().get("componentType").asText();
                int id = Integer.parseInt(entry.getKey());
                idToNameMap.put(id, componentType);
            });

        } catch (IOException e) {
            System.err.println("Error loading names: " + e.getMessage());
        }
    }

    /**
     * Updates the hand based on the provided HandEvent. If the event ID matches
     * a specific value, the hand is cleared. Otherwise, a TileEvent is created
     * and formatted before updating the hand. Finally, a game update is triggered.
     *
     * @param event the HandEvent that contains the details to update the hand,
     *              including event ID and connectors.
     */
    public void updateHand(HandEvent event) {

        if(event.getId() == 158){
            out.setCacheHand(emptyCell());
        }
        else {
            TileEvent temp = new TileEvent(event.getId(), 0, 0, null, 0, false, false, 0, 0, event.getConnectors());
            out.setCacheHand(formatCell(temp));
        }
        onGameUpdate();
    }

    /**
     * Updates the gameboard based on the provided game event. This method adjusts the gameboard representation
     * and player positions based on the given event. If a player is already positioned, it clears their previous
     * position before updating to the new one.
     *
     * @param event the GameBoardEvent containing the position and player information required to update
     *              the gameboard.
     */
    @Override
    public void updateGameboard(GameBoardEvent event) {

        int x = out.getPositionToGameboard().get(event.getPosition()).getFirst();
        int y = out.getPositionToGameboard().get(event.getPosition()).getSecond();

        if(out.getPlayerToPosition().containsKey(event.getPlayerID())){
            int pos = out.getPlayerToPosition().get(event.getPlayerID());
            int x1 = out.getPositionToGameboard().get(pos).getFirst();
            int y1 = out.getPositionToGameboard().get(pos).getSecond();
            out.setGameboard(x1,y1,3,"|                       |") ;
            out.getPlayerToPosition().remove(event.getPlayerID());
            if(x != -1){
                out.setGameboard(x,y,3,"|"+centerTextAnsi(event.getPlayerID(),23) + "|") ;
                out.getPlayerToPosition().put(event.getPlayerID(), event.getPosition());
            }
        }
        else{
            out.getPlayerToPosition().put(event.getPlayerID(), event.getPosition());
            out.setGameboard(x,y,3,"|"+centerTextAnsi(event.getPlayerID(),23) + "|") ;
        }

        onGameUpdate();

    }

    /**
     * Updates the set of covered tiles based on the provided event and triggers a game update.
     *
     * @param event the event containing details about the changes in the covered tile set, including its new size
     */
    @Override
    public void updateCoveredTilesSet(CoveredTileSetEvent event) {

        out.setCoveredTileSet(event.getSize()); //QUI
        onGameUpdate();
    }

    /**
     * Updates the set of uncovered tiles based on the provided event information.
     *
     * @param event an instance of UncoverdTileSetEvent containing details about
     *              the uncovered tiles, including their identifiers and connectors.
     */
    @Override
    public void updateUncoveredTilesSet(UncoverdTileSetEvent event) {
        if(event.getConnectors() != null) {
            out.setUncoveredTilesId(event.getId());

            String[] cache = formatCell(new TileEvent(event.getId(), 0, 0, null, 0, false, false, 0, 0, event.getConnectors()));

            out.setUncoverdTileSetCache(event.getId(), cache); //QUI
        }
        else{

            out.setUncoveredTilesId(event.getId()); //QUI
            out.setUncoverdTileSetCache(event.getId(), null);//QUI
        }

        onGameUpdate();
    }




    /**
     * Retrieves the description of a card based on its unique identifier.
     *
     * @param id the unique identifier of the card
     * @return the description of the card associated with the given id
     */
    //questo come anche qualche altro metodo sarà per gestire le cose eccezionali o comunque chiama un metodo speciale di out
    public String formatCard(int id){
        return CardsDescriptions.get(id);
    }

    /**
     * Displays the card based on the provided CardEvent.
     * Updates the output with the card ID and its description.
     *
     * @param event the CardEvent that contains information about the card to be displayed
     */
    @Override
    public void showCard(CardEvent event){

        out.setCardId(event.getId());

        out.setCacheCard(CardsDescriptions.get(event.getId()));
        //printBoard();
        //System.out.println(CardsDescriptions.get(id));
        //onGameUpdate();
    }

    /**
     * Terminates the connection and performs necessary cleanup operations.
     * This method stops the input reader and displays a server message
     * indicating a clean disconnection.
     *
     * Overrides the base class implementation to ensure custom
     * disconnect behavior.
     */
    @Override
    public void disconnect() {
//        inputReader.stop();
//        inputThread.interrupt();
//        try {
//            inputThread.join();
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
        inputReader.stop();
        inputReader.printServerMessage("Disconnected cleanly.");
    }

    /**
     * Establishes a connection and initializes the required resources or threads
     * to facilitate further communication or operations. This method should be
     * called to set up the necessary infrastructure before interaction begins.
     *
     * @throws IOException if an I/O error occurs during the connection process.
     */
    @Override
    public void connect() throws IOException {
//        inputReader = new InputReader(inputQueue);
//        inputThread = new Thread(inputReader);
//        inputThread.setDaemon(true);
//        inputThread.start();
    }

    /**
     * Prompts the user with a message and waits for an input response from the input queue.
     *
     * @param message The message to be displayed to the user.
     * @return The input provided by the user as a String, or an empty string if interrupted.
     */
    @Override
    public String askInput(String message) {
        inputReader.printServerMessage(message);
        try {
            String toSend = inputQueue.take();
            //System.out.println("to send: " + toSend);
            return toSend;
        } catch (InterruptedException e) {
            //Thread.currentThread().interrupt();
            inputReader.printServerMessage("Input interrupted");
            return "";
        }
    }


    /**
     * Displays the deck by processing card IDs and updating the output deck list.
     *
     * @param deck the DeckEvent object containing the card IDs to be formatted and displayed
     */
    @Override
    public void showDeck(DeckEvent deck){
        ArrayList<String> toAdd = new ArrayList<>();
        for (Integer card : deck.getIds()){
            toAdd.add(formatCard(card));
        }
        out.setDeck(toAdd);
        this.onGameUpdate();
    }




    /**
     * Handles game update events. This method ensures that any previously scheduled
     * update tasks are canceled and a new task is scheduled with a specified debounce delay.
     * It updates the game state and triggers the display of the game's current status.
     *
     * The method is synchronized to ensure thread safety when dealing with the scheduling
     * of tasks and concurrent updates.
     *
     * Key Behavior:
     * - Cancels any existing scheduled task if it is not already completed.
     * - Schedules a new task after the configured debounce delay.
     * - Attempts to display the current game state when the scheduled task runs.
     *
     * Exceptions:
     * - Any exceptions thrown while showing the game (e.g., by the `out.showGame()`
     *   method) are caught and logged via stack trace output.
     */
    public synchronized void onGameUpdate() {
        //System.out.println("waiting all package");
        if (scheduledTask != null && !scheduledTask.isDone()) {
            scheduledTask.cancel(false);
        }

        scheduledTask = scheduler.schedule(() -> {
            //inputReader.clearScreen();
            try{
                out.showGame();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }, debounceDelayMs, TimeUnit.MILLISECONDS);
    }

/**
 * Terminates the scheduler and releases any resources associated with it.
 * This method is typically called when the application is shutting down
 * and the scheduler is no longer needed. It ensures that all tasks
 * currently managed by the scheduler are halted and any remaining resources
 * are properly cleaned up.
 */
//quando termina tutto chiamo questo anche se non credo dovrebbe particolamente servirmi
    public void shutdown() {
        scheduler.shutdown();
    }

    /**
     * Formats a list of rewards from a given RewardsEvent into a StringBuilder
     * representation with colored blocks based on the value of the goods.
     *
     * @param event the RewardsEvent containing the list of rewards to format
     * @return a StringBuilder containing the formatted rewards output
     */
    public StringBuilder formatRewards(RewardsEvent event) {
        ArrayList<Goods> goodsList = event.getRewards();
        StringBuilder sb = new StringBuilder();

        int size = goodsList.size();

        sb.append("REWARDS\n");
        int k = 0;
        for (Goods goods : goodsList) {
            sb.append("| (pos: "+k+") ");
            switch (goods.getValue()){
                    case 4 -> sb.append("\u001B[31m██\u001B[0m"); // Rosso
                    case 3 -> sb.append("\u001B[33m██\u001B[0m"); // Giallo
                    case 2 -> sb.append("\u001B[32m██\u001B[0m"); // Verde
                    case 1 -> sb.append("\u001B[34m██\u001B[0m"); // Blu

            }
            sb.append(" |");
            k++;
        }
        sb.append("\n");

        return sb;
    }



    /**
     * Retrieves the current value of the 'out' field.
     *
     * @return the current instance of the Out object.
     */
    public Out getOut(){
        return out;
    }

    /**
     * Sets the client instance to be used.
     *
     * @param client the Client object to set
     */
    public void setClient(Client client) {
        this.client = client;
    }
}

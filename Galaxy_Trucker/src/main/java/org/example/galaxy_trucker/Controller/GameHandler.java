package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.GameLists;
import org.example.galaxy_trucker.Model.State;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameHandler {
    private Controller CurController;
    private GameLists GameLists;
    private Game model;
    private File json;
    private State state;

    private ArrayList<LoginController> loginControllers;
    //altri arraylists


    private int index;
    private String gameName;
    private String CreatorName;
    private int lv;

    public GameHandler() {
        this.GameLists = new GameLists();
    }

    // poi si vedrà
    public void receive(File json){
        this.json = json;
        //aggiorna i vari cazzi index

    }

    //gamehandler legge il json per vedere l'indice del paketo
    //crea o inoltra al sottocontroller specifico

    public void handleGame() throws IOException {
        if (GameLists.getGames().isEmpty() || index >= GameLists.getGames().size()){
            GameLists.CreateNewGame(gameName,CreatorName,lv);
            Game curGame = GameLists.getGames().get(index);
            curGame.setState(State.LOGIN);
            loginControllers.add(new LoginController(curGame));
            loginControllers.get(index).action(json);
            //crea i controller del game e aggiunge agli arraylists
        }
        else{
            state  = model.getCurrentState();
            switch (state){
                case LOGIN -> {CurController = loginControllers.get(index); CurController.action(json);}
                case BUILDING -> {;}
                case SHIP_CHECK -> {;}
                case BUILDING_END -> {;}
                case FLIGHT -> {;}
                case FLIGHT_END -> {;}
                default -> {System.out.println("Invalid state");}
            }
        }

    }




    //riceve
    //gestisce eccezioni
    //curController.action()

}

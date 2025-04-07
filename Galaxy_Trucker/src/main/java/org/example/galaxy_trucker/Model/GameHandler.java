package org.example.galaxy_trucker.Model;

import java.io.IOException;
import java.util.ArrayList;

public class GameHandler {
    ArrayList<Game> Games;

    public GameHandler(){
        Games = new ArrayList<>();
    }

    public Game CreateNewGame(String gameId, Player creator, int level) throws IOException {
        for(Game g: Games){
            if(g.getID().equals(gameId)){
                throw new IllegalArgumentException("Game already exists");
            }
        }
        Game NewGame = new Game(level, gameId);
        NewGame.NewPlayer(creator);
        Games.add(NewGame);

        return NewGame;

    }

    public void JoinGame(Game g, Player p){

        try{
            Games.get(Games.indexOf(g)).NewPlayer(p);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Game> getGames(){
        return Games;
    }
}

package org.example.galaxy_trucker.Model;

import java.io.IOException;
import java.util.ArrayList;

public class GameHandler {
    ArrayList<Game> Games;

    public GameHandler(){
        Games = new ArrayList<>();
    }

    public void CreateNewGame(String gameId, String CreatorId, int level) throws IOException {
        for(Game g: Games){
            if(g.getID().equals(gameId)){
                throw new IllegalArgumentException("Game already exists");
            }
        }
        Game NewGame = new Game(level, gameId);
        NewGame.NewPlayer(CreatorId);
        Games.add(NewGame);

    }

    public void JoinGame(int index, String id){
        Games.get(index).NewPlayer(id);
    }

}

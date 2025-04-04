package org.example.galaxy_trucker.Model;

import java.io.IOException;
import java.util.ArrayList;

public class GameLists {
    ArrayList<Game> Games;

    public GameLists(){
        Games = new ArrayList<>();
    }

    public void CreateNewGame(String gameId, String CreatorId, int level) throws IOException {
        Game NewGame = new Game(level, gameId);
        NewGame.NewPlayer(CreatorId);
        Games.add(NewGame);

    }

    public void JoinGame(int index, String id){
        Games.get(index).NewPlayer(id);
    }

    public ArrayList<Game> getGames(){
        return Games;
    }

}

//package org.example.galaxy_trucker.Model;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class GameLists {
//    ArrayList<Game> Games;
//
//    public GameLists(){
//        Games = new ArrayList<>();
//    }
//
////    public synchronized Game CreateNewGame(String gameId, Player creator, int level) throws IOException {
////        for(Game g: Games){
////            if(g.getGameID().equals(gameId)){
////                throw new IllegalArgumentException("Game already exists");
////            }
////        }
////        Game NewGame = new Game(level, gameId);
////        NewGame.NewPlayer(creator);
////        Games.add(NewGame);
////        System.out.println(NewGame + " " + gameId + " created");
////
////        return NewGame;
////
////    }
////
////    public synchronized void JoinGame(Game g, Player p){
////
////        try{
////            Games.get(Games.indexOf(g)).NewPlayer(p);
////        }
////        catch (Exception e){
//////            e.printStackTrace();
////            System.out.println(e);
////        }
////    }
//
//    public synchronized ArrayList<Game> getGames(){
//        return Games;
//    }
//
//}

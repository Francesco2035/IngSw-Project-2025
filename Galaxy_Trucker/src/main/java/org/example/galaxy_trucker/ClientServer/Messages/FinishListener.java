package org.example.galaxy_trucker.ClientServer.Messages;

public interface FinishListener {
    public void onEndGame(boolean success, String id, String message, ScoreboardEvent event);

}

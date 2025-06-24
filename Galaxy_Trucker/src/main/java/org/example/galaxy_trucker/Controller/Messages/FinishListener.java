package org.example.galaxy_trucker.Controller.Messages;

public interface FinishListener {
    public void onEndGame(boolean success, String id, String message);
}

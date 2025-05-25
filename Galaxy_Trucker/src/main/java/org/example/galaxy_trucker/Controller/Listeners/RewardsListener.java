package org.example.galaxy_trucker.Controller.Listeners;


import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RewardsEvent;

public interface RewardsListener {

    public void sendEvent(RewardsEvent e);
}

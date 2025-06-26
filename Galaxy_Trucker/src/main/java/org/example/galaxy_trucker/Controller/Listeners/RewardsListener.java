package org.example.galaxy_trucker.Controller.Listeners;


import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.RewardsEvent;

/**
 * The RewardsListener interface should be implemented by any class
 * that intends to receive notifications when rewards-related events occur.
 *
 * Implementing classes must define the behavior for handling rewards
 * changes by providing an implementation for the rewardsChanged method.
 */
public interface RewardsListener {

    /**
     * Handles changes related to a rewards event. This method is invoked
     * when a rewards-related event occurs and provides the details of the change
     * through the supplied RewardsEvent object.
     *
     * @param e the RewardsEvent containing information about the rewards change,
     *          such as the updated list of rewards.
     */
    void rewardsChanged(RewardsEvent e);
}

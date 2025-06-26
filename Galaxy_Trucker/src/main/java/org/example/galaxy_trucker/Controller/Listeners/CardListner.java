package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.ClientServer.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.DeckEvent;

import java.io.Serializable;

/**
 * CardListner is an interface that defines methods to handle events related to card operations.
 * It extends the Serializable interface to allow implementations to be serialized.
 */
public interface CardListner extends Serializable {

    /**
     * Notifies the implementing listener about the current state of a deck
     * by providing a DeckEvent object that contains the relevant information.
     *
     * @param deck the DeckEvent containing the list of card IDs in the deck.
     */
    void seeDeck(DeckEvent deck);

    /**
     * Handles the action of a new card event. This method is triggered when a new card is added or introduced.
     *
     * @param cardEvent The CardEvent object containing details of the new card.
     */
    void newCard(CardEvent cardEvent);


}

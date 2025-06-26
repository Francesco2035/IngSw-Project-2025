package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.ClientServer.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.DeckEvent;

import java.io.Serializable;

public interface CardListner extends Serializable {

    public void seeDeck(DeckEvent deck) ;

    public void newCard(CardEvent cardEvent);


}

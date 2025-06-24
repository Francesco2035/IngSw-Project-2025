package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.DeckEvent;

import java.io.Serializable;

public interface CardListner extends Serializable {

    public void seeDeck(DeckEvent deck) ;

    public void newCard(CardEvent cardEvent);


}

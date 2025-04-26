package org.example.galaxy_trucker.Controller.Listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.DeckEvent;
import org.example.galaxy_trucker.Model.Tiles.TileSets;

import java.io.Serializable;
import java.util.ArrayList;

public interface CardListner extends Serializable {

    public void seeDeck(DeckEvent deck) ;

    public void newCard(CardEvent cardEvent);


}

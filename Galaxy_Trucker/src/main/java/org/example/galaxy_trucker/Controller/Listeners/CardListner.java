package org.example.galaxy_trucker.Controller.Listeners;

import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.Model.Tiles.TileSets;

import java.io.Serializable;
import java.util.ArrayList;

public interface CardListner extends Serializable {

    public void seeDeck(ArrayList<CardEvent > deck);

    public void newCard(CardEvent cardEvent);


}
